package waazdoh.common.model;

import waazdoh.cutils.xml.JBean;

public interface MObjectFactory {

	MWave newWave(int start, MEnvironment env);

	MWave newWave(JBean b, MEnvironment env);

}
