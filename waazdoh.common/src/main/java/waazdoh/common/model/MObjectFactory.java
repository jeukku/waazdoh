package waazdoh.common.model;

import org.utils.xml.JBean;

public interface MObjectFactory {

	MWave newWave(int start, MEnvironment env);

	MWave newWave(JBean b, MEnvironment env);

}
