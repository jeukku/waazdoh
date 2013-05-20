package waazdoh.common.model;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import waazdoh.cutils.MID;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;

public class InstrumentTrack implements ServiceObjectData {
	private String name;
	private ServiceObject o;
	private TrackGroup group;
	private Instrument instrument;
	private SortedSet<WNote> notes = new TreeSet<WNote>();

	public InstrumentTrack(TrackGroup ngroup, MEnvironment env, UserID creatorid) {
		group = ngroup;
		o = new ServiceObject("itrack", env, creatorid, this);
	}

	@Override
	public JBean getBean() {
		JBean b = o.getBean();
		b.addAttribute("name", name);
		b.addAttribute("groupid", group.getID());
		b.addAttribute("instrument", instrument.getID());

		JBean n = b.add("notes");
		for (WNote note : notes) {
			n.add(note.getBean());
		}
		return b;
	}

	@Override
	public boolean parseBean(JBean bean) {
		name = bean.getAttribute("name");
		instrument = o
				.getEnvironment()
				.getObjectFactory()
				.getInstrument(bean.getIDAttribute("instrument"),
						o.getEnvironment());

		List<JBean> bnotes = bean.get("bean").getChildren();
		for (JBean bnote : bnotes) {
			notes.add(new WNote(bnote));
		}

		return true;
	}

	public void checkProgress(MProgress p) {
		instrument.checkProgress(p);
	}

	public Instrument newInstrument() {
		this.instrument = new Instrument(o.getEnvironment());
		return instrument;
	}

	public void add(WNote note) {
		notes.add(note);
	}

	public void setName(String text) {
		this.name = text;
		o.modified();
	}

	public MID getID() {
		return o.getID();
	}

	public FloatStream getStream() {
		// TODO Auto-generated method stub
		return null;
	}
}
