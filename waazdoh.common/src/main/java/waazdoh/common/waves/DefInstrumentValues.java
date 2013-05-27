package waazdoh.common.waves;

import waazdoh.common.model.NoteTime;

public class DefInstrumentValues implements InstrumentValues {

	private float tempo;
	private float time;
	private NoteTime notetime;
	private int sample;

	@Override
	public float getTempo() {
		return tempo;
	}

	@Override
	public float getTime() {
		return time;
	}

	@Override
	public NoteTime getNotetime() {
		return notetime;
	}

	@Override
	public int getSample() {
		return sample;
	}

	public void setSample(int sample) {
		this.sample = sample;
	}

	public void setNotetime(NoteTime notetime) {
		this.notetime = notetime;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public void setTempo(float tempo) {
		this.tempo = tempo;
	}

}
