package waazdoh.common.model;

public class WNote implements Comparable<WNote> {
	private static final int TICKS_PER_BEAT = 1024;
	public static final int BASE = 0;
	private int time;
	private int note;
	private int length;

	public WNote(int note, int i, int j) {
		this.note = note;
		this.time = i;
		this.length = j;
	}

	@Override
	public int compareTo(WNote o) {
		if (o.time < time) {
			return -1;
		} else {
			return 1;
		}
	}

	public static int getLengthInBeats(int i) {
		return i * TICKS_PER_BEAT;
	}
}
