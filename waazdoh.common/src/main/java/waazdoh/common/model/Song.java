package waazdoh.common.model;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.cutils.JBeanResponse;
import org.cutils.MID;
import org.cutils.MLogger;
import org.cutils.UserID;
import org.utils.xml.JBean;
import org.utils.xml.XML;

import waazdoh.CMusic;
import waazdoh.emodel.ETrack;

public class Song {
	private MID id;
	private List<TrackGroup> trackgroups = new LinkedList<TrackGroup>();
	private MLogger log = MLogger.getLogger(this);
	private String name;
	private UserID creatorid;
	private String version;
	//
	private Set<SongListener> listeners = new HashSet<SongListener>();
	private JBean storedbean;
	private MEnvironment env;
	private MOutput output;
	private MID copyof;

	// new song
	public Song(MEnvironment env) {
		this.creatorid = env.getUserID();
		id = new MID();
		version = CMusic.version;
		this.env = env;
	}

	/*
	 * public Song(JBean b, MFloatStreamSource source, CMService service) {
	 * this.streamsource = source; this.service = service; load(b); }
	 */
	public Song(MID id, MEnvironment env) {
		this.env = env;
		this.id = id;
		load(id);
	}

	private String getDetails() {
		String d = "";
		d += "MEMORY:" + getMemoryUsageInfo() + "\n";
		d += getBean();
		for (TrackGroup tg : trackgroups) {
			d += tg.getDetailInfo();
			d += ",";
		}

		return d;
	}

	public String getMemoryUsageInfo() {
		String info = "";

		info += "tgs[" + trackgroups.size() + "]";

		return info;
	}

	public boolean isOK() {
		return id != null;
	}

	private boolean load(MID id2) {
		this.id = id2;
		JBeanResponse response = env.getService().read(getID());
		log.info("got response " + response);
		if (response.isSuccess()) {
			JBean songbean = response.getBean().find("song");
			return load(songbean);
		} else {
			reset();
			return false;
		}
	}

	private void reset() {
		this.id = null;
		this.name = null;
		this.creatorid = null;
		this.storedbean = null;
		this.env = null;
		listeners = null;
		trackgroups = null;
	}

	@Override
	public String toString() {
		return "Song[" + id + "][tgs:" + trackgroups.size() + "]";
	}

	private XML getAsXML() {
		JBean b = getBean();
		XML beanxml = b.toXML();
		return beanxml;
	}

	public JBean getBean() {
		JBean b = new JBean("song");
		if (id != null) {
			b.add("id").setValue(id.toString());
			b.addAttribute("name", getName());
			b.addAttribute("version", version);
			b.addAttribute("copyof", "" + copyof);
			//
			b.addAttribute("creator", creatorid.toString());
			//
			JBean btrackgroups = b.add("trackgroups").add("list");
			List<TrackGroup> ntracks = trackgroups;
			for (TrackGroup tg : ntracks) {
				btrackgroups.add("id").setValue(tg.getID().toString());
			}
		}
		return b;
	}

	public boolean load(Reader stringReader) {
		XML xml;
		try {
			xml = new XML(stringReader);
			return load(xml);
		} catch (IOException e) {
			log.error(e);
			return false;
		}
	}

	private boolean load(XML xml) {
		JBean bean = new JBean(xml);
		return load(bean);
	}

	private boolean load(JBean bean) {
		log.info("loading " + bean);
		//
		storedbean = bean.find("song");
		if (storedbean != null) {
			id = new MID(storedbean.get("id").getValue());
			copyof = storedbean.getIDAttribute("copyof");
			creatorid = new UserID(storedbean.getAttribute("creator"));
			version = storedbean.getAttribute("version");
			this.name = bean.getAttribute("name");
			//
			return parseGroupsTracks(bean);
		} else {
			return false;
		}
	}

	private boolean parseGroupsTracks(JBean bean) {
		List<JBean> btrackgroups = bean.get("trackgroups").get("list")
				.getChildren();
		for (JBean jBean : btrackgroups) {
			TrackGroup g = new TrackGroup(creatorid, env);
			if (g.load(new MID(jBean.getValue()))) {
				addTrackGroup(g);
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Song) {
			Song bsong = (Song) obj;
			XML asXML = getAsXML();
			XML basXML = bsong.getAsXML();
			boolean eq = asXML.equals(basXML);
			if (!eq) {
				log.info("song xml representations aren't equal");
			}
			return eq;
		} else {
			return false;
		}
	}

	private void updateVersion() {
		output = null;
		id.updateVersion();
	}

	// calls checkWave for every track
	// returning false, if some track returned false.
	public MProgress checkTracks() {
		MProgress p = new MProgress();
		//
		List<TrackGroup> ts = this.trackgroups;
		boolean ret = true;
		for (TrackGroup trackgroup : ts) {
			trackgroup.checkTracks(p);
		}
		//
		return p;
	}

	public TrackGroup addTrackGroup() {
		TrackGroup tg = new TrackGroup(creatorid, env);
		return addTrackGroup(tg);
	}

	private TrackGroup addTrackGroup(TrackGroup tg) {
		trackgroups.add(tg);
		//
		Set<SongListener> ls = listeners;
		for (SongListener songListener : ls) {
			songListener.trackGroupAdded(tg);
		}
		//
		return tg;
	}

	public MOutput getOutputWave() {
		if (output == null || hasChanged()) {
			while (env.getService().isLoggedIn()) {
				MProgress p = checkTracks();
				if (p.ready()) {
					break;
				}
			}
			//

			output = new MOutput(env);

			for (TrackGroup t : this.trackgroups) {
				FloatStream groupstream = t.getStream();
				output.add(groupstream);
			}
		}

		return output;
	}

	public String getName() {
		if (this.name == null) {
			name = "Song";
		}
		return this.name;
	}

	public MID getID() {
		return id;
	}

	public void addSongListener(SongListener songListener) {
		listeners.add(songListener);
	}

	public boolean publish() {
		save();
		//
		List<TrackGroup> ts = trackgroups;
		for (TrackGroup track : ts) {
			track.save();
			track.publish();
		}
		//
		return env.getService().publish(getID());
	}

	public synchronized boolean save() {
		if (!env.getUserID().equals(creatorid)) {
			copyof = getID().copy();
			id = new MID();
			creatorid = env.getUserID();
		}

		List<TrackGroup> ts = trackgroups;
		for (TrackGroup track : ts) {
			track.save();
		}
		//
		boolean changed = hasChanged();
		if (!changed) {
			log.info("song has changed. Saving.");

			updateVersion();
			//
			MID sid = getID();
			storedbean = getBean();
			JBeanResponse response = env.getService().write(sid, storedbean);
			log.info("save song got response " + response);
			//
			//
			return response.isSuccess();
		} else {
			return false;
		}
	}

	private boolean hasChanged() {
		JBean bean = getBean();
		boolean changed = bean.equals(storedbean);
		return changed;
	}

	public List<TrackGroup> getTrackGroups() {
		return new LinkedList<TrackGroup>(trackgroups);
	}

	public void clearMemory(int time) {
		for (TrackGroup t : this.trackgroups) {
			t.clearMemory(time);
		}
		//
		if (output != null) {
			output.clearMemory(time);
		}
	}

	public ETrack newTrack() {
		return new ETrack(env);
	}

	public void setName(String string) {
		this.name = string;
		this.updateVersion();
	}

}
