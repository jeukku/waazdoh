package waazdoh.common.model;

import java.util.HashMap;
import java.util.Map;

import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.xml.JBean;

public class StaticObjectFactory implements MObjectFactory {
	private Map<MID, MWave> waves = new HashMap<MID, MWave>();
	private MLogger log = MLogger.getLogger(this);
	
	@Override
	public MWave newWave(int start, MEnvironment env) {
		MWave wave = new MWave(start, env);
		synchronized (waves) {
			waves.put(wave.getID(), wave);
		}
		return wave;
	}

	@Override
	public MWave newWave(JBean b, MEnvironment env) {
		MID id = b.getIDAttribute("id");
		MWave wave = waves.get(id);
		if (wave == null) {
			wave = new MWave(b, env);
			synchronized (wave) {
				waves.put(wave.getID(), wave);
			}
			log.info("created new wave " + wave);
		} else {
			log.info("wave already created " + wave);
		}
		return wave;
	}

}
