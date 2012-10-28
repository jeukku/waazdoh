package cmusic.app;

import java.util.LinkedList;
import java.util.List;

import waazdoh.common.model.Song;


public class ESong {
	private final Song song;
	private int zoom;

	private List<ESongListener> listeners = new LinkedList<ESongListener>();

	public ESong(Song song) {
		this.song = song;
	}

	public Song getSong() {
		return song;
	}

	public void zoomChanged(int zoom) {
		this.zoom = zoom;
		List<ESongListener> ls = new LinkedList<ESongListener>(listeners);
		for (ESongListener eSongListener : ls) {
			eSongListener.zoomChanged(zoom);
		}
	}

	public void addSongListener(ESongListener l) {
		listeners.add(l);
		song.addSongListener(l);
	}

	public float getZoom() {
		return zoom;
	}

}
