package waazdoh.common.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cutils.MCRC;
import org.cutils.MID;
import org.cutils.MLogger;
import org.cutils.MPreferences;

import waazdoh.service.CMService;


public class MBinaryStorage {
	private List<Binary> streams = new LinkedList<Binary>();
	private Map<MID, MCRC> crcs = new HashMap<MID, MCRC>();
	//
	private MLogger log = MLogger.getLogger(this);
	private final String localpath;
	private boolean running = true;
	private final CMService service;
	private boolean directorytree;

	public MBinaryStorage(MPreferences p, CMService service) {
		this.localpath = p.get(MPreferences.LOCAL_PATH);
		this.service = service;
		//
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning()) {
					try {
						saveWaves();
						synchronized (streams) {
							streams.wait(6000);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						log.error(e);
					} catch (Exception e) {
						log.error(e);
					}
				}
			}
		}, "Storage save binaries");
		t.start();
	}

	public MBinaryStorage(MPreferences preferences, CMService service2,
			boolean usedirectorytree) {
		this.localpath = preferences.get(MPreferences.LOCAL_PATH);
		this.directorytree = usedirectorytree;
		this.service = service2;
	}

	public String getMemoryUserInfo() {
		synchronized (streams) {
			String info = "streams:" + streams.size();

			int memcount = 0;
			for (Binary b : streams) {
				memcount += b.getMemoryUsage();
			}
			info += " usage:" + memcount;
			return info;
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void addNewWave(Binary fs) {
		synchronized (streams) {
			if (findStream(fs.getID()) != null) {
				throw new RuntimeException("Binary " + fs + " already added");
			} else {
				log.info("adding binary " + fs);
				streams.add(fs);
				streams.notifyAll();
			}
		}
	}

	public Binary getFloatStream(MID fsid) {
		synchronized (streams) {
			Binary fs = findStream(fsid);
			//
			if (fs == null) {
				fs = getPersistentStream(fsid);
				if (fs != null) {
					log.info("found persistent data " + fsid);
					streams.add(fs);
				} else {
					log.info("ERROR Stream with id " + fsid + " null");
				}
			}
			return fs;
		}
	}

	public void clearFromMemory(int time, MID binaryid) {
		saveWaves();
		Binary persistentStream = findStream(binaryid);
		log.info("clear from memory " + persistentStream + " time:" + time);
		if (persistentStream != null) {
			if (!persistentStream.isUsed(time)) {
				log.info("removing " + binaryid);
				synchronized (streams) {
					streams.remove(findStream(binaryid));
				}
			}
		}
	}

	public Binary findStream(MID fsid) {
		synchronized (streams) {
			Binary fs = null;
			Iterator<Binary> i = new LinkedList(streams).iterator();
			while (fs == null && i.hasNext()) {
				Binary test = i.next();
				MID testid = test.getID();
				if (testid.equals(fsid)) {
					fs = test;
				}
			}
			return fs;
		}
	}

	public void saveWaves() {
		synchronized (streams) {
			//log.info("save waves " + streams);
			Collection<Binary> lwaves = streams;
			for (Binary mWave : lwaves) {
				try {
					saveFloatStream(mWave);
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e);
				}
			}
		}
	}

	private void saveFloatStream(Binary fs) throws FileNotFoundException {
		synchronized (streams) {
			MCRC persistentWaveCRC = getPersistentWaveTimestamp(fs.getID());
			MCRC fscrc = fs.getCRC();
			if ((persistentWaveCRC == null || !persistentWaveCRC.equals(fscrc))
					&& fs.isReady()) {
				String datapath = getDataPath(fs.getID());
				log.info("saving wave datapath:" + datapath);
				fs.save(new BufferedOutputStream(new FileOutputStream(datapath)));
				crcs.put(fs.getID(), fs.getCRC());
			} else {
				log.info("not saving " + fs + " current persistent: "
						+ persistentWaveCRC + " isReady:" + fs.isReady());
			}
		}
	}

	public Binary reload(Binary binary) {
		try {
			return loadPersistentBinary(binary);
		} catch (FileNotFoundException e) {
			log.error(e);
			return null;
		}
	}

	public synchronized Binary loadPersistentStream(MID id)
			throws FileNotFoundException {
		synchronized (streams) {
			String datapath = getDataPath(id);
			log.info("loading persistent wave " + id + " datapath:" + datapath);
			File f = new File(datapath);
			if (f.exists()) {
				Binary w;
				w = new Binary(id, service);
				return loadPersistentBinary(w);
			} else {
				return null;
			}
		}
	}

	private Binary loadPersistentBinary(Binary w) throws FileNotFoundException {
		BufferedInputStream is = new BufferedInputStream(new FileInputStream(
				getDataPath(w.getID())));
		if (w.load(is)) {
			return w;
		} else {
			log.info("loading Binary " + w.getID() + " failed");
			return null;
		}
	}

	public File getBinaryFile(MID id) {
		return new File(getDataPath(id));
	}

	private MCRC getPersistentWaveTimestamp(MID id) {
		return crcs.get(id);
	}

	private Binary getPersistentStream(MID id) {
		synchronized (streams) {
			try {
				return loadPersistentStream(id);
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e);
				return null;
			}
		}
	}

	private String getDataPath(MID id) {
		String datapath = getWavePath(id) + id + ".bin";
		return datapath;
	}

	private String getWavePath(MID waveid) {
		String sid = waveid.toString();
		String wavepath = this.localpath;
		if (directorytree) {
			int index = 0;
			while (index <= 4) {
				wavepath += File.separatorChar;
				wavepath += sid.substring(index, index + 2);
				index += 2;
			}
		}
		wavepath += File.separatorChar;
		//
		File file = new File(wavepath);
		file.mkdirs();
		//
		return wavepath;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("MBinaryStorage[");
		//
		sb.append("" + streams.size());
		//
		sb.append("]");
		return sb.toString();
	}

	public void close() {
		running = false;
		synchronized (this) {
			notifyAll();
		}
	}

	public void clearMemory(int suggestedmemorytreshold) {
		synchronized (streams) {
			saveWaves();

			List<Binary> bis = this.streams;
			List<Binary> nbis = new LinkedList<Binary>();

			for (Binary binary : bis) {
				if (binary.isUsed(suggestedmemorytreshold)) {
					nbis.add(binary);
				} else {
					log.info("not used " + binary);
				}
			}

			this.streams.clear();
			this.streams.addAll(nbis);
		}
	}

	public Binary newBinary(String string) {
		synchronized (streams) {
			Binary b = new Binary(service, string);
			this.streams.add(b);
			return b;
		}
	}

}
