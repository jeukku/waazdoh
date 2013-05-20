package cmusic.client;

import java.io.IOException;

import waazdoh.client.MClient;
import waazdoh.common.model.AudioSampleStream;
import waazdoh.common.model.Instrument;
import waazdoh.common.model.InstrumentTrack;
import waazdoh.common.model.MOutput;
import waazdoh.common.model.Song;
import waazdoh.common.model.TrackGroup;
import waazdoh.common.model.WNote;
import waazdoh.common.waves.WaveOutput;
import waazdoh.cutils.MLogger;

public class TestTracker extends CMusicTestCase {
	private MLogger log = MLogger.getLogger(this);

	public void testTracker() throws IOException {
		MClient c = getNewClient();
		Song asong = c.newSong();
		assertNotNull(asong);
		TrackGroup tg = asong.addTrackGroup();
		assertNotNull(tg);
		//
		InstrumentTrack t = tg.newInstrumentTrack();
		Instrument i = t.newInstrument();
		i.setSource(new WaveOutput(440.0f));
		//
		t.add(new WNote(WNote.BASE, 0, WNote.getLengthInBeats(12)));
		//
		MOutput aoutput = asong.getOutputWave();
		assertTrue(aoutput.getAudioInfo().inSeconds() >= 1);

		Song bsong = c.loadSong(asong.getID());
		assertEquals(asong, bsong);
		//
		MOutput boutput = bsong.getOutputWave();
		compareOutputs(aoutput, boutput);
	}

	//
	public void testInstrument() throws IOException {
		MClient c = getNewClient();
		Song s = c.newSong();
		TrackGroup tg = s.addTrackGroup();
		assertNotNull(tg);
		InstrumentTrack it = tg.newInstrumentTrack();
		assertNotNull(it);
		Instrument i = it.newInstrument();
		assertNotNull(i);
		i.setSource(new WaveOutput(440));
		AudioSampleStream stream = it.getStream();
		assertNotNull(stream);
	}
}
