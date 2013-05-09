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

import java.io.File;
import java.io.IOException;
import java.nio.ShortBuffer;

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

import com.xuggle.mediatool.IMediaGenerator;
import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IError;

public class MWave {
	private static final Object TYPE_FLAC = "flac";

	private static final Object ENCODING_VORBIS = "ogg";

	private MLogger log = MLogger.getLogger(this);

	private FloatArray fs;

	private int start, length;
	private long modified;
	private int samplespersecond;

	private String type = "integers";
	private String encoding = "flac";

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
		this.binaryid = b.getIDAttribute("binaryid");
		if (binaryid == null) {
			// Random binaryid. Wave will never be ready, but wont cause
			// exception either
			log.error("BinaryID null");
			binaryid = new MID();
		}
		this.type = b.getAttribute("type");
		this.encoding = b.getAttribute("encoding");
		this.version = b.getAttribute("version");
		this.id = b.getIDAttribute("id");
		if (id == null) {
			log.error("Invalid bean, but will load anyway");
			id = new MID();
		}
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
		// try {
		File file = env.getBinarySource().getBinaryFile(binary);
		log.info("reading " + file + " exists:" + file.exists());

		if (encoding.equals(TYPE_FLAC)) {
			readXugglerFlac(binary, format, file);
		}

		if (length != getFS().length()) {
			String s = "read different count of floats from binary than stored "
					+ length + "!=" + getFS().length();
			log.error("error " + s);
			throw new RuntimeException(s);
		}
		/*
		 * } catch (IOException e) { log.info("reading binary " + e + " got " +
		 * fs.length() + " floats with binaryid " + binaryid); log.error(e); fs
		 * = null; } catch (UnsupportedAudioFileException e) {
		 * log.info("reading binary " + e + " got " + fs.length() +
		 * " floats with binaryid " + binaryid); log.error(e); fs = null; }
		 */

		used();
	}

	@Override
	public String toString() {
		return "NewWave[" + start + "->" + (start + getLength()) + "]["
				+ getLength() + "]][" + (1.0f * getLength() / samplespersecond)
				+ "][" + id + ":" + super.hashCode() + "]";
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

				Binary binary = null;
				//
				if (encoding.equals(TYPE_FLAC)) {
					binary = createFlacXugglerBinary();
				} else if (encoding.equals(ENCODING_VORBIS)) {
					binary = createVorbisBinary();
				}

				//
				if (binary != null) {
					binaryid = binary.getID();

					boolean reloadsuccess = env.getBinarySource()
							.reload(binary);
					log.info("reloading wave binary " + binary + " success:"
							+ reloadsuccess);
					if (reloadsuccess) {
						binary.setReady();
						binary.save(); //
						log.info("creating binary done " + binary + " id:"
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
					log.info("not creating binary length:" + getLength()
							+ " oktoedit:" + okToEdit());
				}
			}
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}

	private void readXugglerFlac(Binary binary, AudioFormat format, File file) {
		IMediaListener myListener = new MediaListenerAdapter() {
			public void onOpen(IMediaGenerator pipe) {
				log.info("opened: " + ((IMediaReader) pipe).getUrl());
			}

			@Override
			public void onAudioSamples(IAudioSamplesEvent event) {
				IAudioSamples samples = event.getAudioSamples();
				log.info("onaudiosamples " + samples.getNumSamples() + " "
						+ " fs:" + getFS().length() + " " + samples);
				ShortBuffer sb = samples.getByteBuffer().asShortBuffer();

				for (int i = 0; i < sb.limit() && getFS().length() < length; i++) {
					short num = sb.get(i);
					getFS().add(1.0f * num / Short.MAX_VALUE);
				}
				//
				super.onAudioSamples(event);
			}
		};

		IMediaReader r = ToolFactory.makeReader(file.getAbsolutePath());
		r.addListener(myListener);

		while (true) {
			IError p;
			p = r.readPacket();
			if (p != null) {
				break;
			}
		}
		//
		log.info("read flac done");
	}

	private Binary createFlacXugglerBinary() throws IOException {

		Binary binary = env.getBinarySource().newBinary("" + this, "flac");
		File file = env.getBinarySource().getBinaryFile(binary);

		ICodec codec = ICodec.findEncodingCodec(ICodec.ID.CODEC_ID_FLAC);
		String flacfilepath = file.getAbsolutePath();
		IMediaWriter writer = ToolFactory.makeWriter(flacfilepath);

		writer.addAudioStream(0, 0, ICodec.ID.CODEC_ID_FLAC, 1,
				this.samplespersecond);
		//
		int index = 0;
		while (index < getFS().length()) {
			short ss[] = new short[1024];
			//
			int ssindex = 0;
			while (ssindex < ss.length && index < getFS().length()) {
				float f = getFS().getSample(index);
				// short i = (short) ((f / 2 + 0.5f) * Short.MAX_VALUE);
				short i = (short) (f * Short.MAX_VALUE);
				ss[ssindex] = i;
				index++;
				ssindex++;
			}

			writer.encodeAudio(0, ss);
		}

		int extrasamples = 6000;
		writer.encodeAudio(0, new short[extrasamples]);

		writer.close();
		//
		File nf = new File(flacfilepath);
		nf.renameTo(file);

		return binary;
	}

	private Binary createVorbisBinary() {
		Binary binary = env.getBinarySource().newBinary("" + this, "ogg");
		File file = env.getBinarySource().getBinaryFile(binary);
		return binary;
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
		if (binaryid == null) {
			b.addAttribute("error", "no binaryid");
		} else if (id == null) {
			b.addAttribute("error", "no id");
		} else {
			b.addAttribute("binaryid", binaryid);
			b.addAttribute("length", fs != null ? fs.length() : length);
			b.addAttribute("type", type);
			b.addAttribute("encoding", encoding);
			b.addAttribute("version", version);
			b.addAttribute("start", start);
			b.addAttribute("id", id);
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
