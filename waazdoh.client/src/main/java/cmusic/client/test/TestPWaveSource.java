package cmusic.client.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.cutils.JBeanResponse;
import org.cutils.MID;
import org.cutils.MPreferences;

import waazdoh.common.model.Binary;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.MBinaryStorage;
import waazdoh.common.model.MEnvironment;
import waazdoh.service.CMService;
import waazdoh.service.ReportingService;


public class TestPWaveSource implements MBinarySource {
	private MPreferences preferences;
	private MBinaryStorage storage;
	private CMService service;
	private Map<MID, JBeanResponse> beans = new HashMap<MID, JBeanResponse>();

	public TestPWaveSource(MPreferences p) {
		this.preferences = p;
	}

	@Override
	public void clearFromMemory(int treshhold, MID binaryid) {
		storage.clearFromMemory(treshhold, binaryid);
	}

	@Override
	public boolean reload(Binary binary) {
		return storage.reload(binary) != null;
	}

	public void addBinary(Binary stream) {
		storage.addNewWave(stream);
	}

	@Override
	public File getBinaryFile(MID id) {
		return storage.getBinaryFile(id);
	}

	@Override
	public Binary newBinary(String string) {
		return storage.newBinary(string);
	}

	@Override
	public String getMemoryUsageInfo() {
		// TODO Auto-generated method stub
		return "info";
	}

	@Override
	public void setDownloadEverything(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReportingService(ReportingService rservice) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearMemory(int suggestedmemorytreshold) {
		storage.clearMemory(suggestedmemorytreshold);
	}

	@Override
	public String getInfoText() {
		return "TEST";
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addBean(MID id, JBeanResponse response) {
		beans.put(id, response);
	}

	@Override
	public JBeanResponse getBean(MID id) {
		return beans.get(id);
	}

	@Override
	public Binary get(MID fsid) {
		return storage.getFloatStream(fsid);
	}

	@Override
	public Binary getOrDownload(MID samplesid) {
		Binary b = storage.getFloatStream(samplesid);
		if (b == null) {
			b = new Binary(samplesid, service);
			storage.addNewWave(b);
		}
		return b;
	}

	@Override
	public CMService getService() {
		return service;
	}

	@Override
	public void setService(CMService service) {
		this.service = service;
		this.storage = new MBinaryStorage(preferences, service);
	}

	@Override
	public void saveWaves() {
		storage.saveWaves();
	}

	@Override
	public boolean isRunning() {
		return true;
	}
}
