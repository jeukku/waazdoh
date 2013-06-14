package waazdoh.common.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import waazdoh.WaazdohInfo;
import waazdoh.common.waves.InstrumentValues;
import waazdoh.common.waves.WaveOutput;
import waazdoh.cutils.MID;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;

public class InstrumentTrack implements ServiceObjectData, Track {
	private String name = "track";
	private ServiceObject o;
	private TrackGroup group;
	private Instrument instrument;
	private List<WNote> notes = new LinkedList<WNote>();
	private MOutput output;
	private AudioInfo info;
	private Curve volume = new Curve();
	private boolean muted;

	public InstrumentTrack(TrackGroup ngroup, MEnvironment env, UserID creatorid) {
		group = ngroup;
		o = new ServiceObject("itrack", env, creatorid, this);
		volume.setLevel(0.5f);
		instrument = newInstrument();
		instrument.setSource(new WaveOutput());
	}

	@Override
	public JBean getBean() {
		JBean b = o.getBean();
		b.addAttribute("name", name);
		b.addAttribute("groupid", group.getID());
		if (instrument != null) {
			b.addAttribute("instrument", instrument.getID());
		}
		//
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
		//
		List<JBean> bnotes = bean.get("bean").getChildren();
		for (JBean bnote : bnotes) {
			notes.add(new WNote(bnote));
		}

		return true;
	}

	public void checkProgress(MProgress p) {
		if (instrument != null) {
			instrument.checkProgress(p);
		}
	}

	public Instrument newInstrument() {
		this.instrument = new Instrument(o.getEnvironment());
		return instrument;
	}

	public void add(WNote note) {
		notes.add(note);
		o.modified();
		updateInfo();
	}

	private void updateInfo() {
		WNote lastnote = notes.get(notes.size() - 1);
		float time = lastnote.getLength().notetimeInRealtime(group.getTempo())
				+ lastnote.getTime().notetimeInRealtime(group.getTempo());
		int length = timeToSampleCount(time);
		info = new AudioInfo(length, WaazdohInfo.DEFAULT_SAMPLERATE);
	}

	private int timeToSampleCount(float time) {
		time *= WaazdohInfo.DEFAULT_SAMPLERATE;
		return (int) time;
	}

	public void setName(String text) {
		this.name = text;
		o.modified();
	}

	public MID getID() {
		return o.getID();
	}

	@Override
	public Float getViewSample(int isample) {
		return getOutputWave().getSample(isample).mix();
	}

	public class ITValues implements InstrumentValues {

		private float time;
		private NoteTime notetime;
		private int sample;

		public void setTime(float truetime) {
			this.time = truetime;

		}

		@Override
		public float getTempo() {
			return group.getTempo();
		}

		@Override
		public float getTime() {
			return time;
		}

		@Override
		public NoteTime getNotetime() {
			return notetime;
		}

		public void setNoteTime(NoteTime notetime) {
			this.notetime = notetime;
		}

		public int getSample() {
			return sample;
		}

		public void setSample(int sample) {
			this.sample = sample;
		}

	}

	public List<WNote> getNotes() {
		List<WNote> ns = new LinkedList(notes);
		Collections.sort(ns);
		return ns;
	}

	private MOutput getOutputWave() {
		if (output == null) {
			// TODO I bet this is going to be buggy, ugly and slow
			Collections.sort(notes);
			output = new MOutput(o.getEnvironment());
			output.add(new AudioSampleStream() {
				private int lastnoteindex = 0;
				private ITValues values = new ITValues();

				@Override
				public AudioSample read(int sampleindex) {

					float truetime = 1.0f * sampleindex
							/ WaazdohInfo.DEFAULT_SAMPLERATE;
					values.setTime(truetime);

					NoteTime time = NoteTime.timeInNoteTime(truetime,
							group.getTempo());

					float sample = 0;
					int noteindex = lastnoteindex;
					if (lastnoteindex < notes.size()) {
						while (noteindex < notes.size()) {
							WNote note = notes.get(noteindex);
							if (time.isGreaterThan(note.getTime())) {
								NoteTime notetime = new NoteTime(time
										.getValue() - note.getTime().getValue());
								values.setNoteTime(notetime);
								//
								Float isample = instrument.getSample(note,
										values);
								if (isample != null) {
									sample += isample;
								} else {
									lastnoteindex++;
								}
								noteindex++;
							} else {
								break;
							}
						}
						sample *= volume.getLevel();
						AudioSample as = new AudioSample();
						as.fs[0] = sample;
						as.fs[1] = sample;
						return as;
					} else {
						return null;
					}
				}

				@Override
				public AudioInfo getInfo() {
					return info;
				}
			});
		}
		return output;
	}

	public AudioSampleStream getStream() {
		return new AudioSampleStream() {
			MOutput currentoutput = getOutputWave();

			@Override
			public AudioInfo getInfo() {
				return currentoutput.getAudioInfo();
			}

			@Override
			public AudioSample read(int index) {
				return currentoutput.getSample(index);
			}
		};
	}

	@Override
	public void publish() {
		o.publish();
	}

	@Override
	public void save() {
		o.save();
	}

	@Override
	public String getDetailInfo() {
		return "IntrumentTrack with " + notes;
	}

	@Override
	public void clearMemory(int time) {
		if (output != null) {
			output.clearMemory(time);
		}
	}

	public AudioInfo getAudioInfo() {
		return info;
	}

	public long getModifytime() {
		return o.getModifytime();
	}

	public ServiceObject getServiceObject() {
		return o;
	}

	public String getName() {
		return name;
	}

	public Curve getVolume() {
		return volume;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMute(boolean selection) {
		this.muted = selection;
	}

	public float getTempo() {
		return group.getTempo();
	}
}
