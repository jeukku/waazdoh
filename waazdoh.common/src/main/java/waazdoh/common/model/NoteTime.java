package waazdoh.common.model;

public class NoteTime {

	private float time;

	public NoteTime(float ntime) {
		this.time = ntime;
	}

	public boolean isGreaterThan(NoteTime time2) {
		return time > time2.time;
	}

	public float getValue() {
		return time;
	}

	public float notetimeInRealtime(float tempo) {
		float rtime = time / tempo * 60.0f;
		return rtime;
	}

	public static NoteTime timeInNoteTime(float truetime, float tempo) {
		float time = truetime * tempo / 60.0f;
		return new NoteTime(time);
	}

	@Override
	public String toString() {
		return "time[" + time + "]";
	}
}
