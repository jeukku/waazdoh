package waazdoh.client;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import waazdoh.CMJobs;
import waazdoh.client.rest.RestClient;
import waazdoh.client.test.ServiceMock;
import waazdoh.common.model.Binary;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.MEnvironment;
import waazdoh.common.model.MObjectFactory;
import waazdoh.common.model.Song;
import waazdoh.common.model.StaticObjectFactory;
import waazdoh.cutils.JBeanResponse;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.MPreferences;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;
import waazdoh.service.CMService;
import waazdoh.service.ReportingService;

public class MClient implements ReportingService, MEnvironment {
	protected static final int CLEARMEMORYTIME = 30000;
	//
	private CMService service;
	private Set<ClientListener> listeners = new HashSet<ClientListener>();
	private Map<MID, Song> songs = new HashMap<MID, Song>();
	private Map<UserID, WUser> users = new HashMap<UserID, WUser>();

	private boolean running = true;
	private MPreferences preferences;
	private MBinarySource source;
	private CMJobs jobs = new CMJobs();
	private Thread memoryt;
	private MLogger log = MLogger.getLogger(this);
	private MObjectFactory objectfactory;
	private WBookmarks bookmarks;

	public MClient(MPreferences p, MBinarySource wavesource)
			throws MalformedURLException {
		System.setProperty("java.net.preferIPv4Stack", "true");
		//
		this.preferences = p;
		this.source = wavesource;
		if ("true".equals(p.get(MPreferences.SERVICE_MOCK))) {
			service = new ServiceMock();
		} else {
			service = new RestClient(getServiceURL(), source);
		}
	}

	public MClient(MPreferences p, MBinarySource wavesource, CMService nservice)
			throws MalformedURLException {
		System.setProperty("java.net.preferIPv4Stack", "true");
		//
		this.preferences = p;
		this.source = wavesource;
		this.service = nservice;
	}

	@Override
	public MBinarySource getBinarySource() {
		return source;
	}

	@Override
	public CMJobs getJobs() {
		return jobs;
	}

	@Override
	public CMService getService() {
		return service;
	}

	@Override
	public void reportDownload(MID id, boolean success) {
		service.reportDownload(id, success);
	}

	@Override
	public MObjectFactory getObjectFactory() {
		if (objectfactory == null) {
			objectfactory = new StaticObjectFactory();
		}
		return objectfactory;
	}

	protected String memoryUsageInfo() {
		String info = "memoryusage ";
		info += "songs[";
		for (Song s : songs.values()) {
			info += s.getMemoryUsageInfo();
			info += ",";
		}
		info += "]";
		info += "source[" + source.getMemoryUsageInfo() + "]";
		info += "source[" + source.getInfoText() + "]";

		return info;
	}

	private String getServiceURL() {
		MPreferences p = getPreferences();
		return p.get(MPreferences.SERVICE_URL);
	}

	public MPreferences getPreferences() {
		return preferences;
	}

	public Song newSong(Song s) {
		if (songs.get(s.getID()) == null) {
			songs.put(s.getID(), s);
			fireNewSong(s);
		}
		//
		return s;
	}

	private void startMemoryHandlingThread() {
		if (memoryt == null) {
			log.info("starting memory cleanup thread");
			memoryt = new Thread(new Runnable() {
				private int currentclearmemorytime = MClient.CLEARMEMORYTIME;

				@Override
				public void run() {
					try {
						while (isRunning()) {
							try {
								doWait(500);

								long totalmem = Runtime.getRuntime()
										.totalMemory() / 1024;
								long freemem = Runtime.getRuntime()
										.freeMemory() / 1024;
								long maxmem = Runtime.getRuntime().maxMemory() / 1024;

								long used = totalmem - freemem;

								if (used > getPreferences().getInteger(
										MPreferences.MEMORY_MAX_USAGE, 300) * 1000) {
									currentclearmemorytime /= 2;
									if (currentclearmemorytime < 100) {
										currentclearmemorytime = 100;
									}
								} else {
									currentclearmemorytime = (int) (currentclearmemorytime * 1.2);
									if (currentclearmemorytime < 1000) {
										currentclearmemorytime = 1000;
									}
									doWait(5000);
								}

								log.info("memory (MB) : total:" + totalmem
										+ " free:" + freemem + " max:" + maxmem
										+ " used:" + used + " treshold:"
										+ currentclearmemorytime);

								for (MID songid : songs.keySet()) {
									Song song = songs.get(songid);
									if (song != null) {
										song.clearMemory(currentclearmemorytime);
									}
								}

								source.clearMemory(currentclearmemorytime);
								//
								log.info(memoryUsageInfo());
							} catch (Exception e) {
								log.error(e);
							}
						}
					} finally {
						memoryt = null;
					}
					log.info("memory cleanup thread - run out");
				}
			});
			memoryt.start();
		}
	}

	private void fireNewSong(Song s) {
		Set<ClientListener> ls = listeners;
		for (ClientListener clientListener : ls) {
			clientListener.newSong(s);
		}
	}

	private void loggedIn() {
		bookmarks = new WBookmarks(service);

		for (ClientListener clientListener : listeners) {
			clientListener.loggedIn();
		}
	}

	public String getLocalPath() {
		return getPreferences().get(
				MPreferences.LOCAL_PATH,
				System.getProperty("user.home") + File.separatorChar
						+ "/.waazdoh");
	}

	public void flushWaves() {
		getStreamSource().saveWaves();
	}

	private MBinarySource getStreamSource() {
		return source;
	}

	public void addClientListener(ClientListener clientListener) {
		listeners.add(clientListener);
	}

	public Song newSong() {
		startMemoryHandlingThread();
		return newSong(new Song(this));
	}

	public UserID getUserID() {
		return service.getUserID();
	}

	public boolean isRunning() {
		return running && source != null && source.isRunning()
				&& service != null && service.isLoggedIn();
	}

	public void stop() {
		running = false;
		jobs.stop();
		source.close();
	}

	public Song loadSong(MID id) {
		startMemoryHandlingThread();

		Song song = songs.get(id);
		if (song == null) {
			song = new Song(id, this);
			songs.put(id, song);
			if (!song.isOK()) {
				song = null;
			}
		}
		
		Set<ClientListener> ls = listeners;
		for (ClientListener clientListener : ls) {
			clientListener.songLoaded(song);
		}
		
		return song;
	}

	public boolean save(Song s) {
		if (s.save()) {
			flushWaves();
			return true;
		} else {
			return false;
		}
	}

	public boolean setUsernameAndSession(String username, String session) {
		if (!service.isLoggedIn()) {
			if (service.setSession(username, session)) {
				source.setService(service);
				startMemoryHandlingThread();
				loggedIn();
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public String requestAppLogin(String email, String appname, MID appid) {
		if (service.getSessionID() == null) {
			String sessionid = service.requestAppLogin(email, appname, appid);
			startMemoryHandlingThread();
			return sessionid;
		} else {
			return service.getSessionID();
		}
	}

	public boolean publish(Song s) {
		flushWaves();
		return s.publish();
	}

	public List<MID> searchSongs(String string, int index, int i) {
		startMemoryHandlingThread();

		if (string.equals("")) {
			string = "song";
		}
		JBeanResponse response = service.search(string, index, i);
		return response.getIDList();
	}

	public boolean isStoredBinary(MID mid) {
		Binary fs = source.get(mid);
		if (fs != null && fs.isReady()) {
			return true;
		} else {
			return false;
		}
	}

	public String getUsername() {
		return service.getUsername();
	}

	public String getObjectType(MID id) {
		JBeanResponse response = service.read(id);
		JBean bobj = response.getBean().get("object");
		return bobj.getChildren().get(0).getName();
	}

	public String getInfoText() {
		String info = "service:" + service.getInfoText();
		info += " network:" + this.source.getInfoText();
		return info;
	}

	public void close(Song song) {
		songs.remove(song);
	}

	private void doWait(int time) {
		synchronized (this) {
			try {
				this.wait(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public WBookmarks getBookmarks() {
		return bookmarks;
	}

	public WUser getUser(UserID userid) {
		if (users.get(userid) != null) {
			return users.get(userid);
		} else {
			JBeanResponse b = getService().getUser(userid);
			WUser u = new WUser(b.getBean());
			return u;
		}
	}

}
