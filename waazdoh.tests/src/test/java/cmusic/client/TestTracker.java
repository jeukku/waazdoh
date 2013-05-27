package cmusic.client;

import java.io.FileOutputStream;
import java.io.IOException;

import waazdoh.WaazdohInfo;
import waazdoh.client.MClient;
import waazdoh.common.model.AudioSampleStream;
import waazdoh.common.model.Instrument;
import waazdoh.common.model.InstrumentTrack;
import waazdoh.common.model.MOutput;
import waazdoh.common.model.NoteTime;
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
		i.setSource(new WaveOutput());
		//
		t.add(new WNote(WNote.BASE + 0, new NoteTime(0), new NoteTime(1.1f)));
		t.add(new WNote(WNote.BASE + 2, new NoteTime(1), new NoteTime(1.1f)));
		t.add(new WNote(WNote.BASE + 4, new NoteTime(2), new NoteTime(1.1f)));
		t.add(new WNote(WNote.BASE + 5, new NoteTime(3), new NoteTime(1.1f)));
		t.add(new WNote(WNote.BASE + 7, new NoteTime(4), new NoteTime(1.1f)));
		t.add(new WNote(WNote.BASE + 9, new NoteTime(5), new NoteTime(1.1f)));
		t.add(new WNote(WNote.BASE + 11, new NoteTime(6), new NoteTime(1.1f)));
		t.add(new WNote(WNote.BASE + 12, new NoteTime(7), new NoteTime(1.9f)));
		t.add(new WNote(WNote.BASE + 12, new NoteTime(9), new NoteTime(0.9f)));
		t.add(new WNote(WNote.BASE + 11, new NoteTime(10), new NoteTime(0.9f)));
		t.add(new WNote(WNote.BASE + 9, new NoteTime(11), new NoteTime(0.9f)));
		t.add(new WNote(WNote.BASE + 7, new NoteTime(12), new NoteTime(0.9f)));
		t.add(new WNote(WNote.BASE + 5, new NoteTime(13), new NoteTime(0.9f)));
		t.add(new WNote(WNote.BASE + 4, new NoteTime(14), new NoteTime(0.9f)));
		t.add(new WNote(WNote.BASE + 2, new NoteTime(15), new NoteTime(0.9f)));
		t.add(new WNote(WNote.BASE + 0, new NoteTime(16), new NoteTime(2.9f)));
		//
		assertTrue(t.getStream().getInfo().inSeconds() > 1);

		MOutput aoutput = asong.getOutputWave();
		assertTrue(aoutput.getAudioInfo().inSeconds() >= 1);
		asong.getOutputWave().writeWAV(new FileOutputStream("asong.wav"));

		Song bsong = c.loadSong(asong.getID());
		assertEquals(asong, bsong);
		//
		assertTrue(aoutput.getAudioInfo().getSampleCount() > WaazdohInfo.DEFAULT_SAMPLERATE);
		//
		MOutput boutput = bsong.getOutputWave();
		compareOutputs(aoutput, boutput);

		assertTrue(aoutput.getAudioInfo().inSeconds() >= 1);
	}

	//
	public void testInstrument() throws IOException, InterruptedException {
		MClient c = getNewClient();
		Song s = c.newSong();
		TrackGroup tg = s.addTrackGroup();
		assertNotNull(tg);
		InstrumentTrack it = tg.newInstrumentTrack();
		assertNotNull(it);
		Instrument i = it.newInstrument();
		assertNotNull(i);
		WaveOutput source = new WaveOutput();
		i.setSource(source);
		it.add(new WNote(0, new NoteTime(0), new NoteTime(1))); // one beat

		AudioSampleStream stream = it.getStream();
		assertNotNull(stream);
		assertTrue(stream.getInfo().getSampleCount() >= WaazdohInfo.DEFAULT_SAMPLERATE / 2);

		s.getOutputWave().writeWAV(new FileOutputStream("test.wav"));
	}
}
