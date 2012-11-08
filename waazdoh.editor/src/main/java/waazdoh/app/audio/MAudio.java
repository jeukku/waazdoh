package waazdoh.app.audio;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import waazdoh.WaazdohInfo;
import waazdoh.app.IMessages;
import waazdoh.common.model.AudioSample;
import waazdoh.common.model.MOutput;
import waazdoh.common.model.Song;
import waazdoh.cutils.MLogger;
import waazdoh.emodel.ETrack;

public class MAudio {
	private MLogger log = MLogger.getLogger(this);
	private SourceDataLine outputline;
	private TargetDataLine inputline;

	private Song currentsong;
	private boolean outputrunning;
	private boolean inputrunning;

	private List<MAudioListener> listeners = new LinkedList<MAudioListener>();
	private boolean recording;
	private ETrack track;
	private int outputcapacity;
	private int inputcapacity;
	private int skipinput;
	private int outputSampleRate;
	private boolean dooutput;
	private boolean doinput;
	private IMessages messages;
	private IMessages errors;
	float outputsampleindex = 0;
	private float forwardbuffer = WaazdohInfo.DEFAULT_SAMPLERATE;
	protected boolean readytogo;
	private Thread outputthread;
	private Thread inputthread;
	private Object inputsync;

	public MAudio(IMessages messages, IMessages errors) {
		this.messages = messages;
		this.errors = errors;
	}

	public void play(int buffersize, Song currentsong) {
		outputcapacity = buffersize / 4;
		outputcapacity *= 4;

		this.currentsong = currentsong;
		if (outputline == null) {
			dooutput = true;
			doinput = false;
			start();
		}
	}

	public void record(int buffersize, int skipsampels, Song currentsong) {
		outputcapacity = buffersize / 4;
		outputcapacity *= 4;
		inputcapacity = outputcapacity / 2;
		skipinput = outputcapacity / 2 + skipsampels;
		//
		this.currentsong = currentsong;
		if (!recording && outputline == null && inputline == null) {
			dooutput = true;
			doinput = true;
			start();
		}
	}

	public synchronized void stopAudio() {
		log.info("stopping audio");
		//
		inputrunning = false;
		outputrunning = false;
		while (inputthread != null || outputthread != null) {
			doWait(100);
		}
	}

	private void stopped() {
		log.info("Stopped. Saving track " + track);

		if (track != null) {
			track.setReady();
		}

		fireStopped();
	}

	private void start() {
		messages.add("Getting output audio");
		final MOutput wave = currentsong.getOutputWave();

		readytogo = false;

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				audioRun(wave);
			}
		});
		t.start();

		Thread forwardrunner = new Thread(new Runnable() {
			@Override
			public void run() {
				int index = 0;
				while (outputrunning) {
					if (index < outputsampleindex + forwardbuffer) {
						wave.getSample(index++);
					} else {
						readytogo = true;
						doWait(10);
					}
				}
				readytogo = true;
			}
		}, "AudioForwardRunner");
		forwardrunner.start();
	}

	private void audioRun(final MOutput wave) {
		if (wave != null) {
			if (doinput) {
				track = currentsong.newTrack();
			}
			outputthread = new Thread(new Runnable() {

				@Override
				public void run() {
					doOutputLoop(wave);
					outputthread = null;
				}
			});
			outputthread.start();

			if (doinput) {
				inputthread = new Thread(new Runnable() {
					@Override
					public void run() {
						doInputLoop();
						inputthread = null;
					}
				});
				inputthread.start();
			}
		}
	}

	protected void doInputLoop() {
		try {
			messages.add("Getting audio lines");

			inputrunning = true;

			inputline = getInputLine();

			int inputsampleindex = 0;

			float inputlevel = 0;
			int levelindex = 0;
			//
			messages.add("Starting audio lines");
			//
			inputline.start();

			while (!readytogo) {
				doWait(10);
			}
			//
			messages.add("Recording");

			byte inputbytes[] = new byte[outputcapacity];
			inputline.read(inputbytes, 0, inputbytes.length);

			inputsync = new Object();
			synchronized (inputsync) {
				inputsync.wait(20000);
			}

			log.info("input starting");

			while (inputrunning) {
				int inputlength = inputbytes.length;
				if (inputline.available() < inputlength) {
					inputlength = inputline.available();
				}
				int bytesread = inputline.read(inputbytes, 0, inputlength);
				if (bytesread > 0) {
					ByteBuffer wrap = ByteBuffer.wrap(inputbytes, 0, bytesread);
					ShortBuffer shorts = wrap.asShortBuffer();
					int index = 0;
					float fs[] = new float[shorts.capacity()];
					//
					int dstindex = 0;
					while (index < shorts.capacity() && inputrunning) {
						float f = (1.0f * shorts.get(index) / Short.MAX_VALUE);
						if (skipinput-- < 0) {
							fs[dstindex++] = f;
							inputsampleindex++;
						}

						index += 2; // FIX? Inputline is stereo
									// and reading only mono
									// track
						inputlevel += f > 0 ? f : -f;
					}

					track.addSamples(fs, dstindex);

					/*
					 * log.info("input:" + bytesread + " level:" + (inputlevel /
					 * levelindex) + " inputavail:" + inputline.available() +
					 * " outputavail:" + outputline.available() +
					 * " outputindex:" + outputsampleindex + " inputindex:" +
					 * inputsampleindex + " read fs:" + fs.length);
					 */

					levelindex += index;
					if (levelindex > 100000) {
						inputlevel /= levelindex;
						levelindex = 0;
						// TODO fireLevels(inputlevel, outputlevel);
					}
				} else {
					doWait(10);
					// log.info("zero bytes read");
				}
				//
				float outputtime = outputsampleindex * 1000.0f
						/ WaazdohInfo.DEFAULT_SAMPLERATE;
				if (inputsampleindex + skipinput > outputsampleindex) {
					outputtime = (inputsampleindex + skipinput) * 1000.0f
							/ WaazdohInfo.DEFAULT_SAMPLERATE;
				}
				/*
				 * float runtime = System.currentTimeMillis() - starttime; if
				 * (Math.abs(outputtime - runtime) > 1500) {
				 * log.info("stopping outputtime " + outputtime + " runtime:" +
				 * runtime + " dt:" + (outputtime - runtime)); running = false;
				 * errors.add("stopped audio because runtime is " + runtime +
				 * " and output/input time " + outputtime);
				 * errors.add("samples written " + outputsampleindex + " s/sec:"
				 * + (outputsampleindex * 1000 / runtime)); }
				 */

				fireTimeChange(outputtime);
			}
		} catch (Exception e) {
			log.error(e);
			errors.add("" + e);
		}

		inputrunning = false;

		messages.add("Stopping input");

		try {
			if (inputline != null) {
				inputline.stop();
				inputline = null;
			}

			if (track != null) {
				log.info("recorded " + track);
			}
		} catch (Exception e) {
			log.error(e);
			errors.add("" + e);
		}

		stopped();

		messages.add("");
	}

	protected void doOutputLoop(MOutput wave) {
		try {
			messages.add("Getting audio lines");

			outputline = getOutputLine();
			outputsampleindex = 0;

			java.nio.ByteBuffer outputbb = java.nio.ByteBuffer
					.allocate(outputcapacity); // TODO

			float outputlevel = 0;
			int levelindex = 0;
			//
			messages.add("Starting audio lines");
			//
			outputline.start();

			while (!readytogo) {
				doWait(10);
			}

			outputline.flush();

			byte[] outputarray = new byte[0];
			int position = 0;
			//
			messages.add("Running");

			float leftsample = 0;
			float rightsample = 0;

			if (doinput) {
				while (inputsync == null) {
					doWait(10);
				}

				doWait(200);
				
				synchronized (inputsync) {
					inputsync.notifyAll();
				}
			}

			log.info("output starting");

			log.info("playing " + wave.getLength()
					/ WaazdohInfo.DEFAULT_SAMPLERATE + " sec");

			outputrunning = true;

			while (outputrunning) {
				// OUTPUT
				// if (inputline == null
				// || inputline.available() < outputline
				// .available()) {
				outputbb.clear();
				while (outputrunning
						&& outputbb.position() < outputbb.capacity()
						&& outputbb.position() < outputline.available()) {
					AudioSample sample = wave
							.getSample((int) outputsampleindex);
					// AudioSample sample = new AudioSample();
					if (sample != null) {
						Float fleftsample = sample.fs[0];
						if (fleftsample != null) {
							if (Math.abs(leftsample - fleftsample) > 0.5) {
								log.info("SNAP samples " + fleftsample
										+ " old:" + leftsample);
							}
							leftsample = fleftsample;
						} else {
							leftsample = 0;
						}

						Float frightsample = sample.fs[1];
						if (frightsample != null) {
							rightsample = frightsample;
						} else {
							rightsample = 0;
						}
					} else if (inputline == null) {
						log.info("Output sample is null. Setting running false");
						outputrunning = false;
					}

					outputlevel += leftsample > 0 ? leftsample : -leftsample;
					outputlevel += rightsample > 0 ? rightsample : -rightsample;

					// in stereo
					outputbb.putShort((short) (Short.MAX_VALUE * leftsample));
					outputbb.putShort((short) (Short.MAX_VALUE * rightsample));

					outputsampleindex += (1.0f * WaazdohInfo.DEFAULT_SAMPLERATE)
							/ outputSampleRate;
				}
				//
				outputarray = outputbb.array();
				position = outputbb.position();
				outputline.write(outputarray, 0, position);
				//
				float outputtime = outputsampleindex * 1000.0f
						/ WaazdohInfo.DEFAULT_SAMPLERATE;

				fireTimeChange(outputtime);
			}
		} catch (Exception e) {
			log.error(e);
			errors.add("" + e);
		}

		outputrunning = false;

		messages.add("Stopping output");

		try {
			if (outputline != null) {
				outputline.flush();
				outputline.drain();
				outputline.stop();
				outputline = null;
			}
		} catch (Exception e) {
			log.error(e);
			errors.add("" + e);
		}

		if (!doinput) {
			stopped();
		}

		messages.add("");
	}

	protected void doWait(int i) {
		synchronized (this) {
			try {
				this.wait(i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void fireStopped() {
		for (MAudioListener l : this.listeners) {
			l.stopped();
		}
	}

	private void fireLevels(float input, float output) {
		for (MAudioListener l : this.listeners) {
			l.level(input, output);
		}
	}

	private void fireTimeChange(float time) {
		for (MAudioListener l : this.listeners) {
			l.timeChanged(time);
		}
	}

	public SourceDataLine getOutputLine() {
		AudioFormat format = getOutputAudioFormat();
		//
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		try {
			SourceDataLine dataline;
			dataline = (SourceDataLine) AudioSystem.getLine(info);
			dataline.open(format, outputcapacity);
			return dataline;
		} catch (LineUnavailableException e) {
			audioError(info, e);
			return null;
		}
	}

	public TargetDataLine getInputLine() {
		AudioFormat format = getInputAudioFormat();
		//
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		try {
			TargetDataLine dataline;
			dataline = (TargetDataLine) AudioSystem.getLine(info);
			dataline.open(format, inputcapacity);
			logInfo();
			log.info("returning line " + dataline);
			return dataline;
		} catch (Exception e) {
			audioError(info, e);
			inputrunning = false;
			outputrunning = false;

			return null;
		}
	}

	public void audioError(DataLine.Info info, Exception e) {
		log.error(e);
		log.error("audioline not supported " + info);
		//
		logInfo();
	}

	public void logInfo() {
		Mixer.Info[] infos = AudioSystem.getMixerInfo();
		for (Mixer.Info info2 : infos) {
			log.info("available mixer.info : " + info2);
			Mixer m = AudioSystem.getMixer(info2);
			log.info("got mixer " + m);
			Line[] sourcelines = m.getSourceLines();
			log.info("mixer sourcelines " + sourcelines.length);
			for (Line imixerline : sourcelines) {
				log.info("\tsource line in mixer " + imixerline);
			}
			Line[] targetlines = m.getTargetLines();
			log.info("mixer targetlines " + targetlines.length);
			for (Line imixerline : targetlines) {
				log.info("\ttarget line in mixer " + imixerline);
			}
		}
		//
	}

	public AudioFormat getOutputAudioFormat() {
		outputSampleRate = WaazdohInfo.DEFAULT_SAMPLERATE;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(outputSampleRate,
				sampleSizeInBits, channels, signed, bigEndian);
		log.info("returning " + format);
		return format;
	}

	public AudioFormat getInputAudioFormat() {
		int sampleRate = WaazdohInfo.DEFAULT_SAMPLERATE;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
				channels, signed, bigEndian);
		log.info("returning " + format);
		return format;
	}

	public void addListener(MAudioListener mAudioListener) {
		listeners.add(mAudioListener);
	}

	public ETrack getRecordedTrack() {
		ETrack ret = track;
		track = null;
		return ret;
	}
}
