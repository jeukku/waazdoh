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
package waazdoh.common.waves;

import sun.awt.SunHints.Value;
import waazdoh.WaazdohInfo;
import waazdoh.common.model.NoteTime;
import waazdoh.common.model.WNote;
import waazdoh.emodel.ETrack;

public class WaveGenerator {
	public void generate(ETrack t, int start, int length,
			SampleStream waveGeneratorSample) {
		float sample = start;
		float[] samples = new float[1000];
		int isamples = 0;
		DefInstrumentValues values = new DefInstrumentValues();		//
		WNote note = new WNote(0, new NoteTime(0), new NoteTime(1));
		
		values.setTempo(120);
		while (sample <= start + length + 0.001) {
			values.setSample((int) sample);
			values.setTime(1.0f* sample / WaazdohInfo.DEFAULT_SAMPLERATE);
			//
			float fsample = (float) waveGeneratorSample.getSample(note, values);
			samples[isamples++] = fsample;
			if (isamples >= samples.length) {
				t.addSamples(samples, isamples);
				isamples = 0;
			}
			sample++;
		}
		//
		t.setReady();
	}
}
