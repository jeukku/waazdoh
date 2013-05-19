package cmusic.client;

import java.io.IOException;

import waazdoh.client.MClient;
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
		Song s = c.newSong();
		assertNotNull(s);
		TrackGroup tg = s.addTrackGroup();
		assertNotNull(tg);
		//
		InstrumentTrack t = tg.newInstrumentTrack();
		Instrument i = t.newInstrument();
		i.setSource(new WaveOutput(440.0f));
		//
		t.add(new WNote(WNote.BASE, 0, WNote.getLengthInBeats(2)));
		//
		MOutput aoutput = s.getOutputWave();
		assertTrue(aoutput.getAudioInfo().inSeconds()>=1);

		s = c.loadSong(s.getID());
		MOutput boutput = s.getOutputWave();
		compareOutputs(aoutput, boutput);
	}
}
