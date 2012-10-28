package cmusic.app.swing;

import waazdoh.common.model.Track;
import cmusic.app.ESong;
import cmusic.client.MClient;

public interface AppListener {

	void songChanged(ESong s);

	void clientAdded(MClient c);

	void recordingTrackChanged(Track changedtrack);

	void error(String title, String message, Exception e);

	void waitState(String nwaitstate);

	void notification(String notificationsource, String string);
}
