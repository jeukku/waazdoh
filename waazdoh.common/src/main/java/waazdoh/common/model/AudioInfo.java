package waazdoh.common.model;

import waazdoh.cutils.xml.JBean;

public class AudioInfo {
	private int samples;
	private int samplespersecond;

	public AudioInfo(int nsamples, int spers) {
		this.samples = nsamples;
		this.samplespersecond = spers;
	}

	public AudioInfo(JBean e) {
		samples = e.getAttributeInt("samples");
		samplespersecond = e.getAttributeInt("persec");
	}

	public float inSeconds() {
		return 1.0f * samples / samplespersecond;
	}

	public int getSampleCount() {
		return samples;
	}

	public void add(AudioInfo length) {
		this.samples += length.samples;
	}

	public boolean isEmpty() {
		return samples == 0;
	}

	public boolean isSame(FloatArray fs) {
		return fs.length() == samples;
	}

	public JBean getBean() {
		JBean b = new JBean("info");
		b.addAttribute("samples", samples);
		b.addAttribute("persec", samplespersecond);
		return b;
	}

	public void setIfLonger(AudioInfo length) {
		if (length.samples > samples) {
			samples = length.samples;
		}
	}

	public int getSamplesPerSecond() {
		return samplespersecond;
	}

	@Override
	public String toString() {
		return "AudioInfo[" + samples + "]";
	}
}
