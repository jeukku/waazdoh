package waazdoh.common.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.cutils.MCRC;
import org.cutils.MLogger;
import org.utils.xml.JBean;

import waazdoh.CMusic;
import waazdoh.MJob;
import waazdoh.emodel.ETrack;


public class WaveList {
	private static final int MAX_WAVE_LENGTH = CMusic.DEFAULT_SAMPLERATE * 10;

	private List<MWave> waves = new LinkedList<MWave>();
	private MLogger log = MLogger.getLogger(this);
	private MEnvironment env;

	public WaveList(MEnvironment env) {
		this.env = env;
		log.info("new wavelist with " + env.toString());
	}

	public synchronized void add(MWave wave) {
		waves.add(wave);
	}

	public synchronized int getLength() {
		int length = 0;
		for (MWave wave : waves) {
			length += wave.getLength();
		}
		return length;
	}

	public synchronized void check(MProgress p) {
		List<MWave> ws = this.waves;
		for (MWave wave : ws) {
			wave.isReady(p);
		}
	}

	@Override
	public boolean equals(Object obj) {
		WaveList list = (WaveList) obj;
		for (int i = 0; i < waves.size(); i++) {
			if (!waves.get(i).equals(list.waves.get(i))) {
				return false;
			}
		}
		return true;
	}

	public synchronized MWave getWave(int sample) {

		List<MWave> ws = this.waves;
		for (MWave wave : ws) {
			int wavelastindex = wave.getStart() + wave.getLength();
			if (wavelastindex > sample && sample >= wave.getStart()) {
				if (sample == wave.getStart()) {
					log.info("ODBG getWave(index) sample :" + sample);
					log.info("ODBG getWave(index) returning wave " + wave);
					log.info("ODBG getWave(index) returning waves " + waves);
					log.info("ODBG getWave(index) returning wave " + wave
							+ " with index " + sample);
				}
				return wave;
			}
		}
		return null;
	}

	public synchronized Float getSample(int time) {
		MWave wave = getWave(time);
		if (wave != null) {
			return wave.getSample(time);
		} else {
			if (waves.size() > 2) {// TODO
				log.info("waves " + waves);
				log.info("returning null at time:" + time + " sec:"
						+ (time / CMusic.DEFAULT_SAMPLERATE));
			}
			return null;
		}
	}

	public void getBean(JBean bt) {
		JBean bwavelist = bt.add("wavelist").add("list");
		//
		for (MWave wave : waves) {
			bwavelist.add(wave.getBean());
		}
	}

	public synchronized void publish() {
		List<MWave> lwaves = this.waves;
		for (MWave wave : lwaves) {
			wave.publish();
		}
	}

	public synchronized void clear() {
		waves.clear();
	}

	public synchronized MCRC getCRC() {
		MCRC crc = new MCRC();

		for (MWave wave : waves) {
			MCRC tcrc = wave.getCRC();
			long value = tcrc.getValue();
			for (int i = 0; i < 8; i++) {
				byte b = (byte) ((value >> (i * 8)) & 0xff);
				crc.update(b);
			}
		}

		return crc;
	}

	public String getDetailInfo() {
		String s = "";
		s += "WAVES[";
		for (MWave w : waves) {
			s += w.getDetailInfo();
		}
		s += "]";
		return s;
	}

	public void replace(ETrack recordedTrack) {
		if (recordedTrack != null) {
			clear();
			Collection<MWave> nwaves = recordedTrack.getWaves();
			waves = new LinkedList<MWave>(nwaves);
		}
	}

	public synchronized void clearMemory(int time) {
		for (MWave wave : waves) {
			wave.clearMemory(time);
		}
	}

	public synchronized MWave getLastWave() {
		if (waves.size() == 0) {
			newWave();
		}
		return waves.get(waves.size() - 1);
	}

	private void newWave() {
		int start = 0;
		if (waves.size() > 0) {
			final MWave readywave = getLastWave();

			env.getJobs().addJob(new MJob() {
				public boolean run() {
					readywave.setReady();
					return true;
				}
			});

			// if not stored in service, we have to calculate the starting point
			// base on current length
			start = getLength();
		}
		//
		MWave wave = env.getObjectFactory().newWave(start, env);

		waves.add(wave);

		log.info("added new wave " + this);
	}

	public synchronized Collection<MWave> getWaves() {
		for (MWave wave : this.waves) {
			wave.setReady();
			wave.save();
		}
		return waves;
	}

	public synchronized void setReady() {
		List<MWave> ws = this.waves;
		for (MWave wave : ws) {
			wave.setReady();
		}
	}

	public synchronized void addSamples(float[] samples, int count) {
		getLastWave().addSamples(samples, count);
		if (getLastWave().getLength() > MAX_WAVE_LENGTH) {
			newWave();
		}
	}
}
