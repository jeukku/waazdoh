package cmusic.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import waazdoh.client.MClient;
import waazdoh.client.rest.RestClient;
import waazdoh.client.test.ServiceMock;
import waazdoh.client.test.TestPWaveSource;
import waazdoh.common.model.MBinarySource;
import waazdoh.cp2p.impl.P2PBinarySource;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.MPreferences;
import waazdoh.service.CMService;

public class CMusicTestCase extends TestCase {
	private static final String LOCALURL = "http://localhost:8080/cmusic";
	private Set<MClient> clients = new HashSet<MClient>();
	MLogger log = MLogger.getLogger(this);
	private int usercounter = 0;

	@Override
	protected synchronized void setUp() throws Exception {
		log.info("**************************************** [[[   SETTING UP TEST   ]]] *********************************");
		//
		createCommonSource();
		//
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		for (MClient c : clients) {
			c.stop();
		}
		clients = new HashSet<MClient>();
	}

	public void sleep(int i) {
		synchronized (this) {
			try {
				this.wait(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public MClient getNewDumbClient() throws MalformedURLException {
		TestPreferences p = new TestPreferences("test");
		MClient c = new MClient(p, getDumbWaveSource(p));
		return c;
	}

	private MBinarySource getDumbWaveSource(TestPreferences p) {
		return new TestPWaveSource(p);
	}

	protected MClient getNewClient() throws IOException {
		boolean bind = usercounter > 0 ? true : false;
		String username = "test_username_" + (usercounter++) + "@localhost";
		return getNewClient(username, bind);
	}

	public MClient getNewClient(String email, boolean bind)
			throws MalformedURLException {
		//
		TestPreferences p = new TestPreferences(email);
		MBinarySource waveSource = getWaveSource(p, bind);
		MClient c = new MClient(p, waveSource, getTestService(p, waveSource));
		waveSource.setReportingService(c);

		boolean setsession = c.setUsernameAndSession(email, getSession(p));
		if (setsession) {
			clients.add(c);
			return c;
		} else {
			String appidparam = "appid_" + email;
			String sid = p.get(appidparam);
			MID id;
			if (sid != null && sid.length() > 0) {
				id = new MID(sid);
			} else {
				id = new MID();
				p.set(appidparam, id.toString());
			}
			String session = c.requestAppLogin(email, "testapp", id);
			if (session != null) {
				p.set("session", session);
			}
			return null;
		}
	}

	private CMService getTestService(TestPreferences p, MBinarySource source) {
		String osname = System.getProperty("os.name").toLowerCase();
		if (osname.indexOf("linux") >= 0) {
			String url = "http://localhost:8080/cmusic";
			try {
				return new RestClient(url, source);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return new ServiceMock(source);
		}
	}

	private String getSession(TestPreferences p) {
		return p.get("session");
	}

	private void copyInstallationTo(String path) throws IOException {
		String inspath = "./target/cmclient-distribution/linux/";
		File file = new File(inspath);
		copyFilesTo(file, new File(path));
	}

	private void copyFilesTo(File file, File dstpath) throws IOException {
		if (file.isDirectory()) {
			file.mkdirs();
			dstpath.mkdirs();
			//
			String[] list = file.list();
			for (String string : list) {
				File src = new File(file.getAbsolutePath() + File.separator
						+ string);
				File dst = new File(dstpath + File.separator + string);
				if (src.isDirectory()) {
					dst.mkdirs();
					copyFilesTo(src, dst);
				} else {
					FileChannel inchannel = new FileInputStream(src)
							.getChannel();
					FileChannel outchannel = new FileOutputStream(dst)
							.getChannel();
					inchannel.transferTo(0, inchannel.size(), outchannel);
					if (dst.getName().equals("utserver")) {
						dst.setExecutable(true);
					}
					inchannel.close();
					outchannel.close();
				}
			}
		}
	}

	// private MFloatStreamSource getWaveSource(MPreferences p) throws
	// IOException {
	// boolean bind = false;
	// // String path = p.get(UTorrentSource.PREF_UTORRENT_PATH);
	// // copyInstallationTo(path);
	// // UTorrentSource testsource = new UTorrentSource(p);
	// // MFloatStreamSource testsource = new CommonTorrentStreamSource(p);
	// P2PWaveSource testsource = getWaveSource(p, bind);
	// return testsource;
	// }
	public MBinarySource getWaveSource(MPreferences p, boolean bind) {
		P2PBinarySource testsource = new P2PBinarySource(p, bind);
		if (bind) {
			testsource.setDownloadEverything(true);
		}
		// JXSEStreamSource testsource = new JXSEStreamSource(p);
		return testsource;
	}

	public void createCommonSource() throws IOException {
		// getNewClient("testdownloaderuser", true);
	}

	public void testTrue() {
		assertTrue(true);
	}
}
