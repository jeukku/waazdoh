package cmusic.swt.updater;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.cutils.MLogger;
import org.cutils.MPreferences;
import org.cutils.MURL;

import cmusic.app.App;
import cmusic.client.URLCaller;
import cmusic.client.rest.ClientProxySettings;

public class Updater {
	public static final String NOTIFICATIONSOURCE = "UPDATER";
	public static final String STARTED_UPDATE = "started_update";
	private static final String UPDATE_DONE = "update_done";

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
					String zipurl = this.url + "/" + packagename + "_" + osname
							+ "_" + osarch + ".zip";
					zipurl = zipurl.toLowerCase();
					zipurl = zipurl.replace(' ', '_');

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
						if (e.isDirectory()) {
							File d = new File(name);
							d.mkdirs();
						} else {
							String output = name;
							BufferedInputStream bis = new BufferedInputStream(
									dec);
							FileOutputStream fos = new FileOutputStream(output);
							while (true) {
								int b = bis.read();
								if (b < 0) {
									break;
								}
								fos.write(b);
							}
							fos.close();
							bis.close();
						}
					}

					if (filecount > 0) {
						app.notification(Updater.NOTIFICATIONSOURCE,
								Updater.UPDATE_DONE);
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
		return sbody;
	}
}
