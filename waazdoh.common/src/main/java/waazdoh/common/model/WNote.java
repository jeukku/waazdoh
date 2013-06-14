package waazdoh.common.model;

import waazdoh.cutils.xml.JBean;

public class WNote implements Comparable<WNote> {
	public static final int BASE = 0;
	private NoteTime time;
	private NoteTime length;
	private int note;

	public static final double BASE_FREQ_MULTIPLIER = Math.pow(2.0, 1.0 / 12.0);

	public WNote(int nnote, NoteTime ntime, NoteTime nlength) {
		this.note = nnote;
		this.time = ntime;
		this.length = nlength;
	}

	public WNote(JBean bnote) {
		note = bnote.getAttributeInt("note");
		time = new NoteTime(bnote.getAttributeFloat("time"));
		length = new NoteTime(bnote.getAttributeFloat("length"));
	}

	@Override
	public int compareTo(WNote o) {
		if (o == this) {
			return 0;
		} else if (o.time.isGreaterThan(time)) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WNote) {
			WNote o = (WNote) obj;
			if (o.getBean().equals(getBean())) {
				return true;
			}
		}
		return false;
	}

	public JBean getBean() {
		JBean b = new JBean("hit");
		b.addAttribute("time", time.getValue());
		b.addAttribute("note", note);
		b.addAttribute("length", length.getValue());
		return b;
	}

	public NoteTime getTime() {
		return time;
	}

	public NoteTime getLength() {
		return length;
	}

	public float getHz() {
		float hz = (float) (440.0 * Math.pow(BASE_FREQ_MULTIPLIER, note));
		return hz;
	}

	public int getNote() {
		return note;
	}

	@Override
	public String toString() {
		return "WNote[" + note + "," + getHz() + ", " + getTime() + ", "
				+ getLength() + "]";
	}

}
