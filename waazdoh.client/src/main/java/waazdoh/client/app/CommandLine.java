package waazdoh.client.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import waazdoh.client.MClient;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.MOutput;
import waazdoh.common.model.MProgress;
import waazdoh.common.model.MWave;
import waazdoh.common.model.Song;
import waazdoh.cp2p.impl.P2PBinarySource;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.MPreferences;


public class CommandLine {

	private static final String OUTPUT_PATH = "output.path";
	private static final int MAX_DOWNLOADS = 20;

	/**
	 * @param args
	 * @throws MalformedURLException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws MalformedURLException,
			InterruptedException {
		CommandLine cl = new CommandLine();
		cl.start();
	}

	private String outputpath;
	private List<Song> downloads = new LinkedList<Song>();
	private MClient client;
	private MLogger log = MLogger.getLogger(this);
	private String oggenc;

	private void start() throws MalformedURLException, InterruptedException {
		MPreferences p = new CommandLinePreferences();

		this.oggenc = p.get("oggenc", "oggenc");

		String email = p.get("email", "downloader@waazdoh.com");
		String savedsession = p.get("saved_session", null);
		String appname = "downloaderapp";
		MID appid = new MID();
		//
		p.get(MPreferences.SERVICE_URL, "http://waazdoh.localhost/service");
		p.get(MPreferences.SERVERLIST, "waazdoh.localhost,a.waazdoh.localhost");
		p.get(MPreferences.LOCAL_PATH, "waazdohfiles");
		p.get(CommandLine.OUTPUT_PATH, "waazdohoutput");
		p.get(MPreferences.NETWORK_MAX_DOWNLOADS, "" + 2);
		
		this.outputpath = p.get(CommandLine.OUTPUT_PATH, "waazdohoutput");
		File foutputpath = new File(outputpath);
		foutputpath.mkdirs();

		MBinarySource wavesource = new P2PBinarySource(p, true);
		wavesource.setDownloadEverything(true);

		client = new MClient(p, wavesource);
		wavesource.setReportingService(client);

		while (!client.isRunning()) {
			//
			if (savedsession != null) {
				boolean setsuccess = client.setUsernameAndSession(email,
						savedsession);
				if (!setsuccess) {
					savedsession = null;
				}
			} else {
				savedsession = client.requestAppLogin(email, appname, appid);
				p.set("saved_session", savedsession);
			}
			if (!client.isRunning()) {
				synchronized (client) {
					client.wait(10000);
				}
			}
		}
		//
		startDownloadChecker();
		//
		while (client.isRunning()) {
			try {
				int index = 0;
				List<MID> songs = client.searchSongs("song", index, 1000);
				if (songs.size() > 0) {
					index += songs.size();
					for (MID mid : songs) {
						log.info("addownload? " + mid);
						addDownload(mid);
					}
				} else {
					index = 0;
				}
			} catch (Exception e) {
				log.error(e);
			}
			//
			synchronized (client) {
				client.wait(30000);
			}
		}
	}

	private void startDownloadChecker() {
		clearLockFiles();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (client.isRunning()) {
					try {
						doRun();
					} catch (Exception e) {
						log.error(e);
					}
				}
			}

			public void doRun() {
				try {
					synchronized (downloads) {
						downloads.wait(30000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.error(e);
				}
				//
				try {
					List<Song> ndownloads;
					synchronized (downloads) {
						ndownloads = new LinkedList(downloads);
					}

					Map<Song, Float> progress = new HashMap<Song, Float>();
					for (Song song : ndownloads) {
						MProgress checkTracks = song.checkTracks();
						if (checkTracks.ready()) {
							MOutput wave = song.getOutputWave();
							synchronized (downloads) {
								downloads.remove(song);
								log.info("song checktracks ok");
								writeOutput(song);
								client.close(song);
							}
						}
					}

					while (downloads.size() > MAX_DOWNLOADS) {
						List<Song> songs = new LinkedList(downloads);
						Collections.sort(songs, new Comparator<Song>() {

							@Override
							public int compare(Song o1, Song o2) {
								return o1.checkTracks().getPersentage()
										- o2.checkTracks().getPersentage();
							}

						});

						for (Song song : songs) {
							log.info("sorted songs " + song.checkTracks());
						}

						Song toberemoved = songs.get(songs.size() - 1);
						log.info("removing song " + toberemoved.checkTracks()
								+ " song:" + toberemoved);
						downloads.remove(toberemoved);
						client.close(toberemoved);
					}
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
			}
		});
		t.start();
	}

	private void writeOutput(Song song) throws IOException {
		File output = getOutputFile(song.getID());
		OutputStream fos = new FileOutputStream(output);
		//
		MOutput outputwave = song.getOutputWave();
		outputwave.writeWAV(fos);
		fos.close();
		//
		try {
			Runtime.getRuntime().exec(oggenc + " " + output.getAbsolutePath());
		} catch (Exception e) {
			log.debug("running oggenc " + e);
		}
	}

	private void clearLockFiles() {
		File f = new File(outputpath);
		String[] list = f.list();
		for (String string : list) {
			if (string.indexOf(".lck") > 0) {
				File lockfile = new File(outputpath + File.separator + string);
				log.info("deleting lck " + lockfile);
				lockfile.delete();
			}
		}
	}

	private File getOutputFile(MID mid) {
		File f = new File(outputpath + File.separator + ""
				+ mid.toString().replace('.', '_'));
		return f;
	}

	private File getLockFile(MID id) {
		File f = new File(outputpath + File.separator + "" + id.toString()
				+ ".lck");
		return f;
	}

	private void addDownload(MID id) {
		try {
			if (!checkLock(id)) {
				Song song;
				song = client.loadSong(id);
				log.info("adding download " + song);
				createLock(song);
				synchronized (downloads) {
					downloads.add(song);
				}
			} else {
				log.info("not adding download " + id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkLock(MID id) {
		File f = getLockFile(id);
		if (f.exists()) {
			log.info("lock file exists " + f);
			return true;
		} else {
			f = getOutputFile(id);
			log.info("does output file exist " + f.exists());
			return f.exists();
		}
	}

	private void createLock(Song song) throws IOException {
		File f = getLockFile(song.getID());
		log.debug("output file " + f.getAbsolutePath());
		f.createNewFile();
	}

}
