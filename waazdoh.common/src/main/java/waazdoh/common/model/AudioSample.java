package waazdoh.common.model;


public class AudioSample {
	public final Float fs[] = new Float[] { 0.0f, 0.0f };

	public void add(AudioSample sample) {
		fs[0] += sample.fs[0];
		fs[1] += sample.fs[1];
	}

	public float mix() {
		return fs[0] + fs[1];
	}

	@Override
	public String toString() {
		return "AS[" + fs[0] + "," + fs[1] + "]";
	}

	public void reset() {
		fs[0] = 0.0f;
		fs[1] = 0.0f;
	}
}
