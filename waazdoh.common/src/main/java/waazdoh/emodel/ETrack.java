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
package waazdoh.emodel;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import waazdoh.WaazdohInfo;
import waazdoh.common.model.MEnvironment;
import waazdoh.common.model.MWave;
import waazdoh.common.model.WaveList;
import waazdoh.cutils.MLogger;


public class ETrack {
	private List<ETrackListener> listeners = new LinkedList<ETrack.ETrackListener>();
	private WaveList waves;
	private MEnvironment env;
	//
	private MLogger log = MLogger.getLogger(this);

	public ETrack(MEnvironment env) {
		this.env = env;
		waves = new WaveList(env);
	}

	public void addETrackListener(ETrackListener l) {
		this.listeners.add(l);
	}

	public static interface ETrackListener {
	}

	@Override
	public String toString() {
		return "ETrack[l:" + getLength() + "["
				+ (getLength() / WaazdohInfo.DEFAULT_SAMPLERATE) + "]][" + waves
				+ "]";

	}

	public int getLength() {
		return getLastWave().getStart() + getLastWave().getLength();
	}

	public void addSamples(float[] samples, int count) {
		waves.addSamples(samples, count);
	}

	private MWave getLastWave() {
		return waves.getLastWave();
	}

	public void setReady() {
		waves.setReady();
	}

	public Collection<MWave> getWaves() {
		return waves.getWaves();
	}

	public void importFile(String sfile) throws UnsupportedAudioFileException,
			IOException {
		AudioFormat targetformat = new AudioFormat(WaazdohInfo.DEFAULT_SAMPLERATE,
				16, 1, true, false);
		AudioInputStream is = AudioSystem.getAudioInputStream(new File(sfile));
		is = AudioSystem.getAudioInputStream(targetformat, is);

		byte[] bytes = new byte[32 * 1000];
		while (true) {
			int count = is.read(bytes);
			if (count <= 0) {
				break;
			}
			float[] floats = new float[count / 2];
			int ifloat = 0;
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					bytes, 0, count));
			while (dis.available() > 0) {
				short i = dis.readShort();
				i = Short.reverseBytes(i);
				floats[ifloat++] = ((float) i) / Short.MAX_VALUE;
			}
			//
			addSamples(floats, ifloat);
		}
	}
}
