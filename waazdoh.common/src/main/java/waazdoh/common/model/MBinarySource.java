package waazdoh.common.model;

import java.io.File;

import org.cutils.JBeanResponse;
import org.cutils.MID;

import waazdoh.service.CMService;
import waazdoh.service.ReportingService;


public interface MBinarySource {
	Binary get(MID fsid);

	void saveWaves();

	void close();

	boolean isRunning();

	void setService(CMService service);

	CMService getService();

	Binary getOrDownload(MID samplesid);

	void clearFromMemory(int time, MID binaryid);

	void clearMemory(int suggestedmemorytreshold);

	JBeanResponse getBean(MID id);

	void addBean(MID id, JBeanResponse response);

	String getInfoText();

	String getMemoryUsageInfo();

	void setDownloadEverything(boolean b);

	void setReportingService(ReportingService rservice);

	Binary newBinary(String string);

	File getBinaryFile(MID id);

	boolean reload(Binary binary);

}
