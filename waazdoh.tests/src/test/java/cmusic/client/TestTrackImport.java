package cmusic.client;

import java.net.MalformedURLException;

import org.junit.Test;

import waazdoh.common.model.Song;
import waazdoh.emodel.ETrack;


public class TestTrackImport extends CMusicTestCase {
	@Test
	public void testWav() throws Exception {
		ETrack track = getNewTrack();
		track.importFile("src/test/440Hz_44100Hz_16bit_30sec.wav");
		assertTrue(track.getLength() > 1000);
	}

	/*
	public void testOgg() throws Exception {
		ETrack track = getNewTrack();
		track.importFile("src/test/testaudio.ogg");
		assertTrue(track.getLength() > 1000);
	}

	public void testFlac() throws Exception {
		ETrack track = getNewTrack();
		track.importFile("src/test/testaudio.flac");
		assertTrue(track.getLength() > 1000);
	}
	*/

	private ETrack getNewTrack() throws MalformedURLException {
		MClient c = getNewDumbClient();
		Song song = c.newSong();
		ETrack track = song.newTrack();
		return track;
	}
}
