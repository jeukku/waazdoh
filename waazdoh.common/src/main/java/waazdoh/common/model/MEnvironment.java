package waazdoh.common.model;

import org.cutils.UserID;

import waazdoh.CMJobs;
import waazdoh.service.CMService;


public interface MEnvironment {

	CMService getService();

	MBinarySource getBinarySource();

	CMJobs getJobs();

	UserID getUserID();

	MObjectFactory getObjectFactory();
}
