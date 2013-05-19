package waazdoh.common.waves;

public class WaveOutput implements SampleStream {

	private float hz;

	public WaveOutput(float hz) {
		this.hz = hz;
	}

	@Override
	public float getSample(float sample) {
		return (float) Math.sin(sample * hz * 2 * Math.PI/1000.0f);
	}
}
