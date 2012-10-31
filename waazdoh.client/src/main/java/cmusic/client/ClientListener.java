package cmusic.client;

import waazdoh.common.model.Song;

public interface ClientListener {
	void newSong(Song s);

	void loggedIn();

	void songLoaded(Song song);
}
