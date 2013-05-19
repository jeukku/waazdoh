package cmusic.client;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import waazdoh.WaazdohInfo;
import waazdoh.client.MClient;
import waazdoh.common.model.AudioInfo;
import waazdoh.common.model.MOutput;
import waazdoh.common.model.MProgress;
import waazdoh.common.model.Song;
import waazdoh.common.model.Track;
import waazdoh.common.model.TrackGroup;
import waazdoh.common.waves.SampleStream;
import waazdoh.common.waves.WaveGenerator;
import waazdoh.cutils.MTimedFlag;
import waazdoh.emodel.ETrack;

public class TestSong extends CMusicTestCase {
	private static final int TRACK_LENGTH = WaazdohInfo.DEFAULT_SAMPLERATE * 12; // two

	// seconds
	// of
	// audio

	public void testCreateSong() throws IOException {
		MClient c = getNewClient();
		Song s = c.newSong();
		assertNotNull(s);
	}

	public void testSaveSong() throws Exception {
		MClient c = getNewClient();
		Song s = c.newSong();
		testCreateTrack(s, TRACK_LENGTH);
		//
		assertTrue(c.publish(s));
		String songname = "test " + new Date();
		s.setName(songname);
		c.save(s);
		MProgress p = new MProgress();
		s.getTrackGroups().get(0).getTracks().get(0).checkWave(p);
		assertTrue(p.ready());

		assertTrue(c.publish(s));

		String songtext = s.getBean().toText();
		MOutput outputwave = s.getOutputWave();
		//
		c = getNewClient(c.getUsername(), false);
		s = c.loadSong(s.getID());
		assertNotNull(s);
		assertEquals(songtext, s.getBean().toText());
		//
		MOutput boutputwave = s.getOutputWave();
		assertTrue(boutputwave.getAudioInfo().inSeconds() > 1);

		compareOutputs(outputwave, boutputwave);
		compareOutputs(outputwave, boutputwave);
		//
		s.clearMemory(0);
		boutputwave = s.getOutputWave();
		compareOutputs(outputwave, boutputwave);
	}

	private void songTest(Song s, Song songb) {
		assertTrue(songb.getTrackGroups().size() > 1);

		MOutput aoutput = songb.getOutputWave();
		if (s.getTrackGroups().get(0).getTracks().size() > 0) {
			AudioInfo length = aoutput.getAudioInfo();
			assertTrue("song length " + length, length.getSampleCount() > 99.99);
		}
		assertEquals(s, songb);
		//

		MOutput boutput = s.getOutputWave();
		compareOutputs(aoutput, boutput);
	}

	public void testTracksShort() throws IOException {
		MClient client = getNewClient();
		MClient bclient = getNewClient();
		doTrackTests(client, bclient, TRACK_LENGTH / 10);
	}

	public void testTracks() throws IOException {
		MClient client = getNewClient();
		MClient bclient = getNewClient();
		doTrackTests(client, bclient, TRACK_LENGTH);
	}

	public void testTracksLong() throws IOException {
		MClient client = getNewClient();
		MClient bclient = getNewClient();
		doTrackTests(client, bclient, TRACK_LENGTH * 10);
		//
		synchronized (this) {
			try {
				this.wait(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void doTrackTests(MClient aclient, MClient bclient, int tracklength) {
		Song s = aclient.newSong();
		testCreateTrack(s, tracklength);
		testCreateTrack(s, tracklength);
		// testCreateTrack(s);
		boolean savesuccess = aclient.save(s);
		aclient.publish(s);
		assertTrue(savesuccess);
		String songname = "SONG " + Math.random();
		s.setName(songname);
		aclient.publish(s);
		assertTrue(savesuccess);
		//
		Song bsong = bclient.loadSong(s.getID());
		assertEquals(s.getName(), bsong.getName());
		assertNotNull(bsong);
		MTimedFlag flag = new MTimedFlag(getSleepTime());
		log.info("waiting " + bsong.getBean());
		while (!bsong.checkTracks().ready() && !flag.isTriggered()) {
			sleep(3000);
		}
		log.info("waiting done " + bsong.checkTracks());
		//
		assertTrue(bsong.checkTracks().ready());
		assertNotNull(bsong);
		assertEquals(s.getBean().toXML(), bsong.getBean().toXML());
		songTest(s, bsong);
	}

	private int getSleepTime() {
		int ret = 10 * 60 * 1000;
		Map<String, String> props = System.getenv();
		// log.info("props " + props);
		Iterator<String> keys = props.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			if (key.equals("DEBUG")) {
				ret *= 100;
			}
			// log.info("key : " + key + " value : " + props.get(key));
		}
		return ret;
	}

	private void testCreateTrack(Song s, final int length) {
		TrackGroup tg = s.addTrackGroup();

		addTrack(s, length, tg);
		addTrack(s, length, tg);
	}

	private void addTrack(Song s, final int length, TrackGroup tg) {
		ETrack et = s.newTrack();

		WaveGenerator gen = new WaveGenerator();
		gen.generate(et, 0, length, new SampleStream() {
			@Override
			public float getSample(float sample) {
				sample *= WaazdohInfo.DEFAULT_SAMPLERATE * 100;
				sample /= length;
				return (float) Math.sin(sample * sample);
				// return sample/length;
				// return (float)Math.random()-0.5f;
			}
		});
		//
		Track t = tg.newTrack();
		t.setName("testname" + new Date());
		t.replaceWave(et);
	}
}
