package waazdoh.common.model;

import waazdoh.CMJobs;
import waazdoh.cutils.UserID;
import waazdoh.service.CMService;


public interface MEnvironment {

	CMService getService();

	MBinarySource getBinarySource();

	CMJobs getJobs();

	UserID getUserID();

	MObjectFactory getObjectFactory();
}
