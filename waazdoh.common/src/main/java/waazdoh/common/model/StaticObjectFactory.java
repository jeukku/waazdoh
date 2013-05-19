/*******************************************************************************
 * Copyright (c) 2013 Juuso Vilmunen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Juuso Vilmunen - initial API and implementation
 ******************************************************************************/
package waazdoh.common.model;

import java.util.HashMap;
import java.util.Map;

import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.xml.JBean;

public class StaticObjectFactory implements MObjectFactory {
	private Map<MID, MWave> waves = new HashMap<MID, MWave>();
	private Map<MID, Instrument> instruments = new HashMap<MID, Instrument>();

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

	@Override
	public Instrument getInstrument(MID id, MEnvironment env) {
		Instrument i = instruments.get(id);
		if (i == null) {
			i = new Instrument(env);
			i.load(id);
			instruments.put(id, i);
		}
		return i;
	}

}
