package waazdoh.common.model;

import java.io.File;
import java.io.IOException;
import javaFlacEncoder.FLACFileOutputStream;
import javaFlacEncoder.StreamConfiguration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.kc7bfi.jflac.sound.spi.FlacAudioFileReader;
import org.kc7bfi.jflac.sound.spi.FlacFormatConversionProvider;

import waazdoh.MJob;
import waazdoh.WaazdohInfo;
import waazdoh.cutils.MCRC;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.xml.JBean;

public class MWave {
	private MLogger log = MLogger.getLogger(this);

	private FloatArray fs;

	private int start, length;
	private long modified;
	private int samplespersecond;

	private String type = "integers";
	private MID binaryid;

	private boolean saved;

	private String version = WaazdohInfo.version;

	private long lastusedtime;
	private MID id;

	private MEnvironment env;

	private boolean hasbeenready;

	MWave(int nstart, MEnvironment env) {
		this.start = nstart;
		fs = new FloatArray();
		id = new MID();
		//
		this.env = env;
		//
		samplespersecond = WaazdohInfo.DEFAULT_SAMPLERATE;

		used();
	}

	MWave(JBean b, final MEnvironment env) {
		this.env = env;
		samplespersecond = WaazdohInfo.DEFAULT_SAMPLERATE;
		//
		parseBean(b);
		saved = true;
		env.getJobs().addJob(new MJob() {
			@Override
			public boolean run() {
				if (getBinary() == null && env.getService().isLoggedIn()) {
					return false;
				} else {
					if (getBinary().isReady()) {
						binaryReady();
					} else {
						getBinary().addListener(new BinaryListener() {
							@Override
							public void ready(Binary binary) {
								binaryReady();
							}
						});
					}
					return true;
				}
			}
		});

		used();
	}

	private void parseBean(JBean b) {
		this.start = b.getAttributeInt("start");
		this.length = b.getAttributeInt("length");
		this.binaryid = new MID(b.getAttribute("binaryid"));
		this.type = b.getAttribute("type");
		this.version = b.getAttribute("version");
		this.id = b.getIDAttribute("id");
	}

	//
	public synchronized void addSamples(float[] fs2, int count) {
		if (okToEdit()) {
			used();
			// ok to add more content
			for (int i = 0; i < count; i++) {
				float f = fs2[i];
				if (f < -1) {
					fs2[i] = -1;
				} else if (f > 1) {
					fs2[i] = 1;
				}
			}
			getFS().addArray(fs2, count);
			length = getFS().length();
		}
	}

	private boolean okToEdit() {
		if (!saved) {
			// ok to add more content
			return true;
		} else {
			throw new RuntimeException("cannot add samples to a ready wave");
		}
	}

	private synchronized FloatArray getFS() {
		if (fs == null && getBinary().isReady()) {
			binaryReady();
		}

		return fs;
	}

	private synchronized void binaryReady() {
		used();

		log.info("binaryReady loading FloatArray");

		fs = new FloatArray();
		Binary binary = getBinary();

		log.info("saving waves to make sure there is a file");
		env.getBinarySource().saveWaves();
		AudioFormat format = new AudioFormat(samplespersecond, 16, 1, true,
				false);
		try {
			File file = env.getBinarySource().getBinaryFile(binary.getID());
			log.info("reading " + file + " exists:" + file.exists());

			AudioInputStream flacAIS = new FlacAudioFileReader()
					.getAudioInputStream(file);

			// AudioFormat audioFormat = new AudioFormat(Encoding.PCM_SIGNED,
			// flacAIS.getFormat().getSampleRate(), flacAIS.getFormat()
			// .getSampleSizeInBits(), flacAIS.getFormat()
			// .getChannels(),
			// flacAIS.getFormat().getChannels() * 2, flacAIS.getFormat()
			// .getSampleRate(), false);

			AudioInputStream is = new FlacFormatConversionProvider()
					.getAudioInputStream(format, flacAIS);
			// AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,
			// wavfile);
			// FlacFormatConversionProvider prov = new
			// FlacFormatConversionProvider();
			// AudioInputStream is = prov.getAudioInputStream(format,
			// new AudioInputStream(bin.getInputStream(), new AudioFormat(
			// prov.getSourceEncodings()[0], samplespersecond, 16,
			// 1, 2, samplespersecond, true), length));

			byte bs[] = new byte[2];
			int index = 0;
			while (index < length) {
				int count = is.read(bs);
				if (count == 0) {
					break;
				}

				short a = (short) (bs[0] & 0xff);
				short b = (short) ((bs[1] & 0xff) << 8);
				short num = (short) (a + b);

				if (index == 0) {
					// TODO debug
					log.info("getting sample as short " + num + " at " + index
							+ " binary " + binary.getID());
					log.info("getting sample as short " + num + " at " + index
							+ " fs:" + getFS().length() + " bin:"
							+ binary.getID());
				}

				getFS().add(1.0f * num / Short.MAX_VALUE);

				index++;
			}

			if (length != getFS().length()) {
				String s = "read different count of floats from binary than stored "
						+ length + "!=" + getFS().length();
				log.error("error " + s);
				throw new RuntimeException(s);
			}

			log.info("reading floats from flac done " + binary.getID()
					+ " read:" + index);
			flacAIS.close();
		} catch (IOException e) {
			log.info("reading binary " + e + " got " + fs.length()
					+ " floats with binaryid " + binaryid);
			log.error(e);
			fs = null;
		} catch (UnsupportedAudioFileException e) {
			log.info("reading binary " + e + " got " + fs.length()
					+ " floats with binaryid " + binaryid);
			log.error(e);
			fs = null;
		}

		used();
	}

	@Override
	public String toString() {
		return "NewWave[" + start + "->" + (start + getLength()) + "]["
				+ getLength() + "]][" + (1.0f * getLength() / samplespersecond)
				+ "][" + super.toString() + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MWave) {
			MWave bwave = (MWave) obj;
			if (!getBean().equals(bwave.getBean())) {
				log.info("wave beans are different");
				return false;
			}
			return areSame(bwave);
		} else {
			return false;
		}
	}

	public synchronized boolean areSame(MWave bwave) {
		if (getLength() != bwave.getLength()) {
			return false;
		}
		for (int i = 0; i < getLength(); i++) {
			float ab = bwave.getSample(i) - getSample(i);
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

	@Override
	public int hashCode() {
		return getBean().toString().hashCode();
	}

	public void setSamples(int isample, float fsample[]) {
		used();
		getFS().setSamples(isample, fsample);
	}

	/**
	 * 
	 * @param sample
	 * @return Returns null if sample >= size
	 */
	public synchronized Float getSample(int sample) {
		used();

		int index = sample - getStart();
		return getFS().getSample(index);
	}

	public synchronized int getLength() {
		return length;
	}

	private synchronized Binary createBinary() {
		try {
			if (okToEdit() && getLength() > 0) {
				used();

				Binary binary = env.getBinarySource().newBinary("" + this);
				File file = env.getBinarySource().getBinaryFile(binary.getID());
				//
				javaFlacEncoder.FLACEncoder enc = new javaFlacEncoder.FLACEncoder();
				StreamConfiguration sc = new StreamConfiguration(1,
						StreamConfiguration.DEFAULT_MIN_BLOCK_SIZE,
						StreamConfiguration.DEFAULT_MAX_BLOCK_SIZE,
						samplespersecond, 16);
				enc.setStreamConfiguration(sc);

				log.info("stream config " + sc + " with id " + binary.getID());

				FLACFileOutputStream fos = new FLACFileOutputStream(file);
				enc.setOutputStream(fos);
				enc.openFLACStream();

				int index = 0;
				int samples[] = new int[getFS().length()];

				while (index < samples.length) {
					float f = getFS().getSample(index);
					// short i = (short) ((f / 2 + 0.5f) * Short.MAX_VALUE);
					short i = (short) (f * Short.MAX_VALUE);
					if (index == 0) {
						// TODO debug
						log.info("adding sample as short " + i + "[" + f + "] "
								+ " at " + index + " binary " + binary.getID());
					}
					samples[index++] = i;
				}
				//
				boolean addsamples = enc.addSamples(samples, index);

				int extrasamples = 1000000 - index;
				if (extrasamples < 0) {
					extrasamples = 0;
				}
				enc.addSamples(new int[extrasamples], extrasamples);

				while (true) {
					int encodedcount = enc.encodeSamples(index + extrasamples,
							true);
					index -= encodedcount;
					if (encodedcount <= 0) {
						break;
					}
				}

				log.info("encoded fully. Closing fos " + binary.getID()
						+ " samplesleft:" + index + " wrote "
						+ getFS().length());
				fos.close();
				//
				if (addsamples) {
					binaryid = binary.getID();

					boolean reloadsuccess = env.getBinarySource()
							.reload(binary);
					log.info("reloading wave binary " + binary + " success:"
							+ reloadsuccess);
					if (reloadsuccess) {
						binary.setReady();
						binary.save(); //
						log.info("creating binary done " + file + " id:"
								+ binary.getID()); // log.info("TEST calling binaryReady "+
													// // binary.getID()); //
													// binaryReady();

						return binary;
					} else {
						log.info("failed to load packaged bytes in binary "
								+ binaryid);
						return null;
					}

				} else {
					log.error("failed to add samples to flac");
				}
				return binary;
			} else {
				log.info("not creating binary length:" + getLength()
						+ " oktoedit:" + okToEdit());
				return null;
			}
		} catch (IOException e) {
			log.error(e);
			return null;
		}
	}

	public Binary getBinary() {
		if (env != null && binaryid != null) {
			return this.env.getBinarySource().getOrDownload(binaryid);
		} else {
			return null;
		}
	}

	public long getTimestamp() {
		return modified;
	}

	public synchronized boolean save() {
		used();
		if (env != null && !saved && getFS().length() > 0) {
			length = getFS().length();

			Binary binary = createBinary();
			binaryid = binary.getID();
			//
			if (binary != null) {
				binary.setReady();
				binary.save();
			}

			//
			used();

			saved = true;

			log.info("saved " + binary + " binaryid:" + binaryid);

			return saved;
		} else {
			log.info("Not saving because env:" + env + " saved:" + saved
					+ " fs.length:"
					+ (getFS() != null ? getFS().length() : "NULL"));
			return saved;
		}
	}

	JBean getBean() {
		JBean b = new JBean("wave");
		if (binaryid != null) {
			b.addAttribute("binaryid", binaryid);
			b.addAttribute("length", fs != null ? fs.length() : length);
			b.addAttribute("type", type);
			b.addAttribute("version", version);
			b.addAttribute("start", start);
			b.addAttribute("id", id);
		} else {
			b.addAttribute("error", "no binaryid");
		}
		return b;
	}

	public int getSampleRate() {
		return samplespersecond;
	}

	public int getStart() {
		return start;
	}

	public synchronized void setReady() {
		save();
	}

	public synchronized MCRC getCRC() {
		if (getBinary() != null) {
			return getBinary().getCRC();
		} else {
			return MCRC.error();
		}
	}

	public void isReady(MProgress p) {
		if (!hasbeenready) {
			if (getBinary() != null) {
				boolean binaryready = getBinary().isReady();
				log.info("isReady " + binaryready);
				hasbeenready = binaryready;
				p.add(binaryready);
			} else {
				hasbeenready = false;
				p.add(false);
			}
		} else {
			p.add(true);
		}
	}

	public void publish() {
		used();
		getBinary().publish();
	}

	private void used() {
		lastusedtime = System.currentTimeMillis();
	}

	public synchronized void clearMemory(int time) {
		if (saved && fs != null && !isUsed(time)) {
			log.info("clearing " + this + " threshold:" + time);
			env.getBinarySource().clearFromMemory(time, binaryid);
			if (!isUsed(time * 2)) {
				log.info("memory clearing fs");
				fs = null;
			}
		}
	}

	private boolean isUsed(int time) {
		return System.currentTimeMillis() - lastusedtime < time;
	}

	public String getDetailInfo() {
		String s = "";
		s += getBean();

		s += "fs:" + fs;
		s += " saved:" + saved;
		if (saved) {
			s += " bin:" + getBinary();
		}

		return s;
	}

	public MID getID() {
		return id;
	}
}
