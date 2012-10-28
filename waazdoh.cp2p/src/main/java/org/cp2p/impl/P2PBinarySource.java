package org.cp2p.impl;

import java.io.File;

import org.cp2p.impl.handlers.ByteArraySource;
import org.cutils.JBeanResponse;
import org.cutils.MID;
import org.cutils.MLogger;
import org.cutils.MPreferences;

import waazdoh.common.model.Binary;
import waazdoh.common.model.BinaryListener;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.MBinaryStorage;
import waazdoh.service.CMService;
import waazdoh.service.ReportingService;


public class P2PBinarySource implements MBinarySource {
	P2PServer server;
	//
	MLogger log = MLogger.getLogger(this);
	private MPreferences preferences;
	private MBeanStorage beanstorage;
	private ReportingService reporting;
	private MBinaryStorage storage;
	private CMService service;

	public P2PBinarySource(MPreferences p, Object reportingservice) {
		this(p, true);
	}

	public P2PBinarySource(MPreferences p, boolean bind2) {
		MLoggerMessage w = new MLoggerMessage();
		MLogger.addObjectHandler(MMessage.class, w);
		//
		this.server = new P2PServer(p, bind2, new ByteArraySource() {
			@Override
			public byte[] get(MID streamid) {
				Binary fs = P2PBinarySource.this.get(streamid);
				if (fs == null || !fs.isReady()) {
					return null;
				} else {
					return fs.asByteBuffer();
				}
			}

			@Override
			public void addDownload(MID streamid) {
				getOrDownload(streamid);
			}
		});

		this.preferences = p;
	}

	@Override
	public File getBinaryFile(MID id) {
		return this.storage.getBinaryFile(id);
	}
	
	@Override
	public String getMemoryUsageInfo() {
		String info = "";
		info += "storage:[" + storage.getMemoryUserInfo() + "]";
		info += "server:[" + server.getMemoryUserInfo() + "]";
		return info;
	}

	@Override
	public boolean reload(Binary binary) {
		return this.storage.reload(binary)!=null;
	}
	
	@Override
	public String getInfoText() {
		String info = "server:" + server.getInfoText();
		return info;
	}

	@Override
	public String toString() {
		return "P2PWaveSource[" + server + "]";
	}

	@Override
	public boolean isRunning() {
		return server.isRunning();
	}

	@Override
	public CMService getService() {
		return service;
	}

	@Override
	public synchronized void addBean(MID id, JBeanResponse response) {
		beanstorage.addBean(id, response);
	}

	@Override
	public void setService(CMService service) {
		this.service = service;
		storage = new MBinaryStorage(preferences, service);
		beanstorage = new MBeanStorage(preferences);
		server.start();
	}

	@Override
	public JBeanResponse getBean(MID id) {
		return beanstorage.getBean(id);
	}

	@Override
	public synchronized Binary get(MID fsid) {
		Binary fs = storage.getFloatStream(fsid);
		return fs;
	}

	@Override
	public void clearFromMemory(int time, MID binaryid) {
		log.info("clear from memory " + binaryid + " time:" + time);
		storage.clearFromMemory(time, binaryid);
	}

	@Override
	public void clearMemory(int suggestedmemorytreshold) {
		storage.clearMemory(suggestedmemorytreshold);
		server.clearMemory(suggestedmemorytreshold);
	}

	public synchronized Binary getOrDownload(MID fsid) {
		Binary fs = get(fsid);
		if (fs == null) {
			if (server.canDownload()) {
				log.info("new Binary " + fsid);
				fs = new Binary(fsid, service);
				if (fs.isOK()) {
					addBinary(fs);
					addDownload(fs);
				} else {
					fs = null;
				}
			} else {
				log.info("Cannot download " + fsid
						+ ". Download queue is full ");
			}
			//
		}
		return fs;
	}

	private synchronized void addDownload(final Binary bin) {
		bin.addListener(new BinaryListener() {
			@Override
			public void ready(Binary binary) {
				saveWaves();
			}
		});
		server.addDownload(bin);
	}

	@Override
	public Binary newBinary(String string) {
		return storage.newBinary(string);
	}

	private synchronized void addBinary(Binary stream) {
		storage.addNewWave(stream);
	}

	@Override
	public void saveWaves() {
		storage.saveWaves();
	}

	@Override
	public void close() {
		server.close();
		if (storage != null) {
			storage.close();
		}
	}

	public void setDownloadEverything(boolean b) {
		server.setDownloadEverything(b);
	}

	public void setReportingService(ReportingService reporting) {
		server.setReportingService(reporting);
	}
}
