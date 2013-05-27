/*******************************************************************************
 * Copyright (c) 2013 Juuso Vilmunen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Juuso Vilmunen - initial API and implementation
 ******************************************************************************/
package waazdoh.common.model;

import java.util.LinkedList;
import java.util.List;

import waazdoh.WaazdohInfo;
import waazdoh.cutils.JBeanResponse;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;

public class TrackGroup {
	private static final String BEANNAME = "trackgroup";
	private MID id;
	private List<Track> tracks = new LinkedList<Track>();
	// private Song song;
	private static int counter = 1;
	private String name = "TrackGroup" + (counter++);
	private long created;
	//
	private MLogger log = MLogger.getLogger(this);
	private boolean muted;
	//
	private MOutput currentoutput;
	private long modified;
	private JBean storedbean;
	private List<TrackGroupListener> listeners;
	private UserID creatorid;
	private String version;
	private MEnvironment env;
	private MID copyof;
	private AudioInfo audioinfo = new AudioInfo(0,
			WaazdohInfo.DEFAULT_SAMPLERATE);
	private int tempo = 120;

	public TrackGroup(UserID user, MEnvironment env) {
		this.env = env;
		this.creatorid = user;
		this.version = WaazdohInfo.version;
		id = new MID();
		created = System.currentTimeMillis();
	}

	public boolean load(MID trackid) {
		this.id = trackid;
		JBeanResponse response = env.getService().read(getID());
		if (response.isSuccess()) {
			return parseBean(response.getBean());
		} else {
			log.info("loading trackgroup bean failed " + trackid);
			return false;
		}
	}

	private boolean parseBean(JBean btrack) {
		storedbean = btrack;
		//
		if (btrack.get("object") != null) {
			btrack = btrack.get("object").get(BEANNAME);
		}
		//
		id = new MID(btrack.get("id").getValue());
		copyof = btrack.getIDAttribute("copyof");
		name = btrack.getAttribute("name");
		created = Long.parseLong(btrack.getAttribute("timestamp"));
		muted = btrack.getAttributeBoolean("muted");
		modified = btrack.getAttributeLong("modified");
		creatorid = btrack.getUserAttribute("creator");
		version = btrack.getAttribute("version");
		tempo = btrack.getAttributeInt("tempo");
		//
		boolean ret = true;
		List<JBean> btracks = btrack.get("tracks").get("list").getChildren();
		for (JBean jBean : btracks) {
			WaveTrack track = new WaveTrack(creatorid, env);
			if (track.load(new MID(jBean.getValue()))) {
				if (!tracks.add(track)) {
					ret = false;
				}
			} else {
				ret = false;
			}
		}
		//
		return ret;
	}

	public void addTo(JBean b) {
		JBean bt = getBean();
		b.add(bt);
	}

	public JBean getBean() {
		JBean bt = new JBean(BEANNAME);
		bt.add("id").setValue(id.toString());
		bt.addAttribute("muted", muted);
		bt.addAttribute("name", name);
		bt.addAttribute("modified", modified);
		bt.addAttribute("timestamp", "" + created);
		bt.addAttribute("creator", creatorid.toString());
		bt.addAttribute("version", version);
		bt.addAttribute("copyof", "" + copyof);
		bt.addAttribute("tempo", tempo);
		//
		List<Track> ts = this.tracks;
		JBean btracks = bt.add("tracks").add("list");
		for (Track track : ts) {
			btracks.add("id").setValue(track.getID().toString());
		}

		return bt;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TrackGroup) {
			TrackGroup tg = (TrackGroup) obj;
			if (!tg.id.equals(id) || !name.equals(tg.name)) {
				return false;
			}
			int index = 0;
			for (Track track : tracks) {
				if (tg.tracks.get(index++).equals(track)) {
					return false;
				}
			}
			//
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean save() {
		updateAudioInfo();
		//
		if (!env.getUserID().equals(creatorid)) {
			copyof = getID().copy();
			id = new MID();
			creatorid = env.getUserID();
		}

		for (Track track : tracks) {
			track.save();
		}

		JBean bt = getBean();
		if (!bt.equals(storedbean)) {
			id.updateVersion();
			//
			bt = getBean();
			storedbean = bt;
			//
			JBeanResponse response = env.getService().write(getID(), bt);
			return response.isSuccess();
		} else {
			return false;
		}
	}

	public MID getID() {
		return id;
	}

	public WaveTrack newTrack() {
		WaveTrack t = new WaveTrack(creatorid, env);
		addTrack(t);
		return t;
	}

	private WaveTrack addTrack(WaveTrack t) {
		modified();
		synchronized (tracks) {
			tracks.add(t);
		}
		//
		for (TrackGroupListener l : getListeners()) {
			l.trackAdded(t);
		}
		return t;
	}

	private void modified() {
		id.updateVersion();
		modified = System.currentTimeMillis();
	}

	// public Track addTrack(ETrack et) {
	// et.save();
	// //
	// Track t = new Track(song, service, streamsource);
	// t.load(et.getID());
	// addTrack(t);
	// return t;
	// }
	public List<Track> getTracks() {
		return tracks;
	}

	public void publish() {
		for (Track track : tracks) {
			track.save();
			track.publish();
		}
		env.getService().publish(getID());
	}

	public Float getViewSample(int isample) {
		float f = 0;
		for (Track t : tracks) {
			f += t.getViewSample(isample);
		}
		return f;
	}

	public void setName(String string) {
		modified();
		this.name = string;
	}

	public String getName() {
		return name;
	}

	public void setMute(boolean b) {
		muted = b;
	}

	public void checkTracks(MProgress p) {
		for (Track t : tracks) {
			t.checkProgress(p);
		}
	}

	private MOutput getOutputWave() {
		if (currentoutput == null
				|| isSomeTrackModifiedAfter(currentoutput.getTimestamp())) {
			updateAudioInfo();
			//
			MOutput output = new MOutput(env);
			for (Track t : tracks) {
				output.add(t.getStream());
			}

			log.info("outputwave " + output);
			currentoutput = output;
		}
		return currentoutput;
	}

	private boolean isSomeTrackModifiedAfter(long timestamp) {
		for (Track t : tracks) {
			if (t.getModifytime() > timestamp) {
				return true;
			}
		}

		return false;
	}

	public AudioSampleStream getStream() {
		updateAudioInfo();
		return new AudioSampleStream() {
			MOutput currentoutput = getOutputWave();

			@Override
			public AudioInfo getInfo() {
				return audioinfo;
			}

			@Override
			public AudioSample read(int index) {
				return currentoutput.getSample(index);
			}
		};
	}

	private synchronized void updateAudioInfo() {
		audioinfo = new AudioInfo(0, WaazdohInfo.DEFAULT_SAMPLERATE);
		List<Track> ts = tracks;
		for (Track track : ts) {
			audioinfo.setIfLonger(track.getAudioInfo());
		}

	}

	public void addListener(TrackGroupListener trackGroupListener) {
		getListeners().add(trackGroupListener);
	}

	private List<TrackGroupListener> getListeners() {
		if (listeners == null) {
			listeners = new LinkedList<TrackGroupListener>();
		}
		return listeners;
	}

	public void clearMemory(int time) {
		for (Track t : tracks) {
			t.clearMemory(time);
		}
	}

	public String getDetailInfo() {
		String s = "";
		s += getBean();

		s += "TRACKS[";
		for (Track t : tracks) {
			s += t.getDetailInfo();
			s += ",";
		}
		s += "]";
		return s;
	}

	public InstrumentTrack newInstrumentTrack() {
		synchronized (tracks) {
			InstrumentTrack i = new InstrumentTrack(this, env, creatorid);
			tracks.add(i);
			return i;
		}
	}

	public int getTempo() {
		return tempo;
	}
}
