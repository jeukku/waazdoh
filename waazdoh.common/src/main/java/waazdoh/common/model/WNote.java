package waazdoh.common.model;

import waazdoh.cutils.xml.JBean;

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

	public WNote(JBean bnote) {
		note = bnote.getAttributeInt("note");
		time = bnote.getAttributeInt("time");
		length = bnote.getAttributeInt("length");
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

	public JBean getBean() {
		JBean b = new JBean("hit");
		b.addAttribute("time", time);
		b.addAttribute("note", note);
		b.addAttribute("length", length);
		return b;
	}
}
