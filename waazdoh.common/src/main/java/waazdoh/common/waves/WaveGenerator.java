package waazdoh.common.waves;

import waazdoh.emodel.ETrack;

public class WaveGenerator {
	public void generate(ETrack t, int start, int length,
			WaveGeneratorSample waveGeneratorSample) {
		float sample = start;
		float[] samples = new float[1000];
		int isamples = 0;
		while (sample <= start + length + 0.001) {
			float fsample = (float) waveGeneratorSample.getSample(sample);
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
