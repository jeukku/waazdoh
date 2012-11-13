package waazdoh.swt.updater;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import waazdoh.app.App;
import waazdoh.client.URLCaller;
import waazdoh.client.rest.ClientProxySettings;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.MPreferences;
import waazdoh.cutils.MURL;

public class Updater {
	public static final String NOTIFICATIONSOURCE = "UPDATER";
	public static final String STARTED_UPDATE = "started_update";
	private static final String UPDATE_DONE = "Restart application to complete update.";
	private static final String UPDATE_AVAILABLE = "Update available";

	private MPreferences preferences;
	private String url;
	private MLogger log = MLogger.getLogger(this);
	private String packagename;
	private App app;

	public Updater(App app, MPreferences mPreferences) {
		this.app = app;
		this.preferences = mPreferences;
		//
		this.url = mPreferences
				.get("update.url", "http://waazdoh.com/download");
		this.packagename = mPreferences.get("update.package", "waazdohapp");
		//
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				doUpdate();
				synchronized (this) {
					try {
						this.wait(1000 * 60 * 5);
					} catch (InterruptedException e) {
						log.error(e);
					}
				}
			}
		});
		t.start();
	}

	private void doUpdate() {
		byte[] data = call(url + "/version.txt");
		if (data != null) {
			String sversion = new String(data);
			log.info("got version " + sversion + " comparing to " + App.VERSION);
			if (App.VERSION.compareTo(sversion.trim()) < 0) {
				try {

					String osarch = System.getProperties().getProperty(
							"os.arch");
					String osname = System.getProperties().getProperty(
							"os.name");
					if (osname.toLowerCase().indexOf("xp") >= 0) {
						app.notification(Updater.NOTIFICATIONSOURCE, Updater.UPDATE_AVAILABLE);
					} else {
						String zipurl = this.url + "/" + packagename + "_"
								+ osname + "_" + osarch + ".zip";
						zipurl = zipurl.toLowerCase();
						zipurl = zipurl.replace(' ', '_');

						log.info("updating from url " + zipurl);

						//
						int filecount = 0;

						byte[] zippackage = call(zipurl);
						ZipInputStream dec = new ZipInputStream(
								new ByteArrayInputStream(zippackage));

						while (true) {
							ZipEntry e = dec.getNextEntry();
							if (e == null) {
								break;
							}
							filecount++;
							//
							String name = e.getName();
							log.info("zipfile entry " + name);
							name = ".." + File.separator + name;

							if (e.isDirectory()) {
								File d = new File(name);
								d.mkdirs();
							} else {
								String output = name;//
								// BufferedInputStream bis = new
								// BufferedInputStream(dec);
								FileOutputStream fos = new FileOutputStream(
										output);
								BufferedOutputStream bos = new BufferedOutputStream(
										fos);
								while (true) {
									int b = dec.read();
									if (b < 0) {
										break;
									}
									bos.write(b);
								}
								bos.close();
								dec.closeEntry();
							}
						}

						if (filecount > 0) {
							app.notification(Updater.NOTIFICATIONSOURCE,
									Updater.UPDATE_DONE);
						}
					}
				} catch (IOException e) {
					log.error(e);
					app.notification(Updater.NOTIFICATIONSOURCE, "" + e);
				}
			}
		}
	}

	private byte[] call(String string) {
		MURL murl = new MURL(string);
		//
		URLCaller urlcaller = new URLCaller(murl, new ClientProxySettings());
		byte[] sbody = urlcaller.getResponseBody();
		log.info("got zip bytes " + sbody.length);
		return sbody;
	}
}
