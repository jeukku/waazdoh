package waazdoh.common.waves;

import waazdoh.common.model.WNote;
import waazdoh.cutils.xml.JBean;

public class WaveOutput implements SampleStream {
	public static final String TYPE_SIN = "sin";
	//
	private String type;

	public WaveOutput() {
	}

	public WaveOutput(JBean b) {
		type = b.getAttribute("type");
	}

	@Override
	public Float getSample(WNote note, InstrumentValues values) {
		float notestartintruetime = note.getTime().notetimeInRealtime(
				values.getTempo());
		float instrumenttime = values.getTime() - notestartintruetime;
		boolean released = values.getNotetime().isGreaterThan(note.getLength());

		float reqhz = note.getHz();

		if (!released) {
			return (float) Math.sin(reqhz * instrumenttime * 2
					* Math.PI);
		} else {
			return null;
		}
	}

	public void setType(String ntype) {
		this.type = ntype;
	}

	public JBean getBean() {
		JBean b = new JBean("wave");
		b.addAttribute("type", type);
		return b;
	}

	public String getType() {
		return type;
	}
}
