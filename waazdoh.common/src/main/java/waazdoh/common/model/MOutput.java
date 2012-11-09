package waazdoh.common.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import waazdoh.cutils.MLogger;

public class MOutput {
	private List<WaveList> channels;
	private List<FloatStream> floatstreams = new LinkedList<FloatStream>();

	private long timestamp = System.currentTimeMillis();
	private int length;
	//
	private MLogger log = MLogger.getLogger(this);
	private float samplespersecond;
	private final MEnvironment env;

	public MOutput(MEnvironment env) {
		this.env = env;
		channels = new LinkedList<WaveList>();
		channels.add(new WaveList(env));
		channels.add(new WaveList(env));
		//
		log.info("new MOutput");
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void add(FloatStream fs) {
		floatstreams.add(fs);
		update();
	}

	@Override
	public String toString() {
		return "Output[ts:" + floatstreams.size() + "][l:" + length
				/ samplespersecond + " sec][" + floatstreams + "]";
	}

	private void update() {
		int length = 0;
		for (FloatStream t : floatstreams) {
			if (t.getLength() > length) {
				length = t.getLength();
			}
			samplespersecond = t.getSamplesPerSecond();
		}
		this.length = length;
		//
		timestamp = System.currentTimeMillis();
	}

	public AudioSample getSample(int i) {
		// TODO i might now be next sample in floatstream.
		// if all floatstreams are drained, return from stored channels.
		List<AudioSample> list = new ArrayList<AudioSample>();
		int addindex = 0;
		while (hasntGotEnoughData(i) && hasGotStreams()) {
			AudioSample result = null;

			int nindex = i + addindex;

			for (FloatStream floatstream : floatstreams) {
				if (floatstream != null) {
					AudioSample sample = floatstream.read(nindex);
					if (result == null && sample != null) {
						result = new AudioSample();
					}

					if (sample != null) {
						result.add(sample);
					} else {
						log.info("sample null with " + floatstream
								+ " at index " + nindex);
						floatstreams.set(floatstreams.indexOf(floatstream),
								null);
					}
				}
			}

			addindex++;

			if (result != null) {
				list.add(result);
			}

			if (list.size() > 1000 || result == null) {
				if (list.size() > 0) {
					addSamplesToChannel(list, 0);
					addSamplesToChannel(list, 1);
					list.clear();
				}
			}

			if (result == null) {
				log.info("Output DONE " + getChannel(0).getLength());
				getChannel(0).setReady();
				getChannel(1).setReady();
				floatstreams.clear();

				break;
			}
		}

		Float left = getChannel(0).getSample(i);
		Float right = getChannel(1).getSample(i);
		if (left == null && right == null) {
			return null;
		} else {
			AudioSample sample = new AudioSample();
			sample.fs[0] = left;
			sample.fs[1] = right;
			return sample;
		}
	}

	private boolean hasGotStreams() {
		for (FloatStream floatstream : floatstreams) {
			if (floatstream != null) {
				return true;
			}
		}
		return false;
	}

	private boolean hasntGotEnoughData(int i) {
		return channels.get(0).getLength() <= i;
	}

	private void addSamplesToChannel(List<AudioSample> list, int ichannel) {
		float[] fs = new float[list.size()];
		int index = 0;
		float total = 0;
		for (AudioSample sample : list) {
			Float float1 = sample.fs[ichannel];
			fs[index++] = float1;
			total += float1;
		}
		getChannel(ichannel).addSamples(fs, fs.length);
	}

	private WaveList getChannel(int i) {
		return channels.get(i);
	}

	public synchronized void writeWAV(OutputStream fos) throws IOException {
		log.info("writing wav " + getLength() + " " + this);
		AudioFormat format = new AudioFormat(samplespersecond, 16, 2, true,
				true);
		
		log.info("writing wav " + format );
		
		byte[] data = new byte[2 * 2 * getLength()];
		for (int i = 0; i < getLength(); i++) {
			AudioSample sample = getSample(i);
			int left = (short) (sample.fs[0] * Short.MAX_VALUE);
			int right = (short) (sample.fs[1] * Short.MAX_VALUE);
			// left
			data[4 * i + 0] = (byte) (left >> 8);
			data[4 * i + 1] = (byte) left;
			// right
			data[4 * i + 2] = (byte) (right >> 8);
			data[4 * i + 3] = (byte) right;
		}
		//
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		AudioInputStream ais = new AudioInputStream(bais, format, getLength());
		AudioSystem.write(ais, AudioFileFormat.Type.WAVE, fos);
		//
		fos.close();
	}

	public int getLength() {
		return length;
	}

	public void clearMemory(int time) {
		for (WaveList t : channels) {
			t.clearMemory(time);
		}
	}
}
