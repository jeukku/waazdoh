package waazdoh.common.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.cutils.JBeanResponse;
import org.cutils.MCRC;
import org.cutils.MID;
import org.cutils.MLogger;
import org.cutils.UserID;
import org.utils.xml.JBean;

import waazdoh.CMusic;
import waazdoh.emodel.ETrack;


public class Track {
	private Curve volume = new Curve();
	private WaveList waves;

	private String name = "Track";
	//
	private MLogger log = MLogger.getLogger(this);
	//
	private UserID creatorid;

	private String comment;
	private MID id = new MID();
	private long created = System.currentTimeMillis();
	private long modifytime;
	private boolean muted;
	private String version;
	private MEnvironment env;

	private JBean storedbean = new JBean("temp");
	private float framerate = CMusic.DEFAULT_SAMPLERATE;

	private List<TrackListener> listeners = new LinkedList<TrackListener>();

	public Track(UserID creatorid, MEnvironment env) {
		this.env = env;
		waves = new WaveList(env);
		this.creatorid = creatorid;

		this.version = CMusic.version;
		this.created = System.currentTimeMillis();
		this.comment = "Created at " + new Date();
	}

	public boolean load(MID trackid) {
		JBeanResponse response = env.getService().read(trackid);
		if (response.isSuccess()) {
			return parseBean(response.getBean());
		} else {
			log.info("loading track bean failed " + trackid);
			return false;
		}
	}

	private synchronized boolean parseBean(JBean btrack) {
		if (btrack.get("object") != null) {
			btrack = btrack.get("object").get("track");
		}

		comment = btrack.getAttribute("comment");
		version = btrack.getAttribute("version");
		volume = new Curve(btrack.get("volume"));
		name = btrack.getAttribute("name");
		created = btrack.getAttributeLong("created");
		creatorid = btrack.getUserAttribute("creator");
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
		MWave wave = env.getObjectFactory().newWave(b, env);

		log.info("adding wave " + wave);
		waves.add(wave);
		return true;
	}

	int getLength() {
		return waves.getLength();
	}

	public synchronized void checkWave(MProgress p) {
		waves.check(p);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Track) {
			Track track = (Track) obj;
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
		return id;
	}

	public Float getSample(int time) {
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

	public class TrackStream implements FloatStream {
		public TrackStream() {
		}

		@Override
		public String toString() {
			return "trackstream[" + getID() + "]";
		}

		@Override
		public AudioSample read(int index) {
			// TODO pan?
			Float f = getSample(index);
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
		public float getSamplesPerSecond() {
			return framerate;
		}

		@Override
		public int getLength() {
			return waves.getLength();
		}
	}

	public JBean getTrackBean() {
		JBean bt = new JBean("track");
		bt.add("id").setValue(id.toString());
		bt.addAttribute("comment", "" + comment);
		bt.addAttribute("created", created);
		bt.addAttribute("modified", modifytime);
		bt.addAttribute("muted", muted);
		bt.addAttribute("version", version);
		bt.addAttribute("creator", creatorid.toString());

		JBean bvol = bt.add("volume");
		volume.addTo(bvol);
		//
		waves.getBean(bt);
		return bt;
	}

	public void updateVersion() {
		getID().updateVersion();
	}

	public long getModifytime() {
		return modifytime;
	}

	public void clearMemory(int time) {
		waves.clearMemory(time);
	}

	public synchronized void publish() {
		save();
		env.getService().publish(id);
		waves.publish();
	}

	public void save() {
		if (!storedbean.equals(getTrackBean())) {
			modified();
			JBean trackBean = getTrackBean();
			storedbean = trackBean;
			env.getService().write(getID(), trackBean);
		}
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMute(boolean selection) {
		muted = selection;
	}

	public void replaceWave(ETrack recordedTrack) {
		waves.replace(recordedTrack);
		modified();
	}

	private void modified() {
		updateVersion();
		modifytime = System.currentTimeMillis();
		//
		LinkedList<TrackListener> ls = new LinkedList<TrackListener>(listeners);
		for (TrackListener trackListener : ls) {
			trackListener.modified();
		}
	}

	private void resetWaves() {
		updateVersion();
		waves.clear();
		modified();
	}

	public Curve getVolume() {
		return volume;
	}

	public String getDetailInfo() {
		String s = "";
		s += getTrackBean();
		s += waves.getDetailInfo();
		return s;
	}

	public boolean areSame(Track btrack) {
		if (getLength() != btrack.getLength()) {
			return false;
		}
		//
		for (int i = 0; i < getLength(); i++) {
			float ab = btrack.getSample(i) - getSample(i);
			if (ab < 0) {
				ab = -ab;
			}
			if (ab > CMusic.MAX_RESOLUTION) {
				log.info("ODBG comparing waves ab: " + ab);
				return false;
			}
		}
		return true;
	}

	public int getSamplesPerSecond() {
		return CMusic.DEFAULT_SAMPLERATE;
	}

	public MCRC getCRC() {
		return waves.getCRC();
	}

	public void addListener(TrackListener trackListener) {
		listeners.add(trackListener);
	}
}
