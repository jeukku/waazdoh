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

import java.util.Date;
import java.util.List;

import waazdoh.WaazdohInfo;
import waazdoh.cutils.MCRC;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;
import waazdoh.emodel.ETrack;

public class WaveTrack implements ServiceObjectData, Track {
	private Curve volume = new Curve();
	private WaveList waves;

	private ServiceObject so;

	private String name = "Track";
	//
	private MLogger log = MLogger.getLogger(this);
	//

	private String comment;
	private boolean muted;
	private String version;

	public WaveTrack(UserID creatorid, MEnvironment env) {
		so = new ServiceObject("track", env, creatorid, this);

		waves = new WaveList(env);

		this.version = WaazdohInfo.version;
		this.comment = "Created at " + new Date();
	}

	public synchronized boolean parseBean(JBean btrack) {
		if (btrack.get("object") != null) {
			btrack = btrack.get("object").get("track");
		}

		comment = btrack.getAttribute("comment");
		version = btrack.getAttribute("version");
		volume = new Curve(btrack.get("volume"));
		name = btrack.getAttribute("name");
		muted = btrack.getAttributeBoolean("muted");

		JBean bwaves = btrack.get("wavelist").get("list");
		List<JBean> waves = bwaves.getChildren();
		for (JBean bwave : waves) {
			boolean waveload = loadWave(bwave);
			if (!waveload) {
				return false;
			}
		}
		return true;
	}

	private boolean loadWave(JBean b) {
		MWave wave = getServiceObject().getEnvironment().getObjectFactory()
				.newWave(b, getServiceObject().getEnvironment());

		log.info("adding wave " + wave);
		waves.add(wave);
		return true;
	}

	public AudioInfo getAudioInfo() {
		return waves.getAudioInfo();
	}

	public synchronized void checkProgress(MProgress p) {
		waves.check(p);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WaveTrack) {
			WaveTrack track = (WaveTrack) obj;
			if (!track.getID().equals(getID()) || !name.equals(track.name)) {
				return false;
			}
			if (!waves.equals(track.waves)) {
				return false;
			}
			//
			return true;
		} else {
			return false;
		}
	}

	public MID getID() {
		return getServiceObject().getID();
	}

	public Float getViewSample(int time) {
		Float sample = waves.getSample(time);
		if (sample != null) {
			return sample * volume.getLevel();
		} else {
			return null;
		}
	}

	private MWave getWave(int sample) {
		return waves.getWave(sample);
	}

	public void setName(String string) {
		this.name = string;
	}

	public String getName() {
		return name;
	}

	public TrackStream getStream() {
		return new TrackStream();
	}

	public class TrackStream implements AudioSampleStream {
		public TrackStream() {
		}

		@Override
		public String toString() {
			return "trackstream[" + getID() + "]";
		}

		@Override
		public AudioSample read(int index) {
			// TODO pan?
			Float f = getViewSample(index);
			if ((index % 10000) == 0) {
				log.info("reading sample " + index + " == " + f);
			}
			if (f != null) {
				AudioSample s = new AudioSample();
				s.fs[0] = f;
				s.fs[1] = f;

				return s;
			} else {
				return null;
			}
		}

		@Override
		public AudioInfo getInfo() {
			return waves.getAudioInfo();
		}
	}

	public JBean getBean() {
		JBean bt = getServiceObject().getBean();
		bt.addAttribute("comment", "" + comment);
		bt.addAttribute("muted", muted);
		bt.addAttribute("name", name);
		bt.addAttribute("version", version);

		JBean bvol = bt.add("volume");
		volume.addTo(bvol);
		//
		waves.getBean(bt);
		return bt;
	}

	public void clearMemory(int time) {
		waves.clearMemory(time);
	}

	public ServiceObject getServiceObject() {
		return so;
	}

	public synchronized void publish() {
		getServiceObject().publish();
		waves.publish();
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMute(boolean selection) {
		muted = selection;
	}

	public void replaceWave(ETrack recordedTrack) {
		waves.replace(recordedTrack);
		getServiceObject().modified();
	}

	private void resetWaves() {
		getServiceObject().updateVersion();
		waves.clear();
		getServiceObject().modified();
	}

	public Curve getVolume() {
		return volume;
	}

	public String getDetailInfo() {
		String s = "";
		s += getBean();
		s += waves.getDetailInfo();
		return s;
	}

	public boolean areSame(WaveTrack btrack) {
		if (getAudioInfo() != btrack.getAudioInfo()) {
			return false;
		}
		//
		for (int i = 0; i < getAudioInfo().getSampleCount(); i++) {
			float ab = btrack.getViewSample(i) - getViewSample(i);
			if (ab < 0) {
				ab = -ab;
			}
			if (ab > WaazdohInfo.MAX_RESOLUTION) {
				log.info("ODBG comparing waves ab: " + ab);
				return false;
			}
		}
		return true;
	}

	public int getSamplesPerSecond() {
		return WaazdohInfo.DEFAULT_SAMPLERATE;
	}

	public MCRC getCRC() {
		return waves.getCRC();
	}

	public void clear() {
		waves.clear();
	}

	public long getModifytime() {
		return getServiceObject().getModifytime();
	}

	public void save() {
		getServiceObject().save();
	}

	public boolean load(MID mid) {
		return getServiceObject().load(mid);
	}
}
