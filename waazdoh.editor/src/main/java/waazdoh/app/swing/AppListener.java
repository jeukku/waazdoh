package waazdoh.app.swing;

import waazdoh.app.ESong;
import waazdoh.client.MClient;
import waazdoh.common.model.Track;

public interface AppListener {

	void songChanged(ESong s);

	void clientAdded(MClient c);

	void recordingTrackChanged(Track changedtrack);

	void error(String title, String message, Exception e);

	void waitState(String nwaitstate);

	void notification(String notificationsource, String string);
}
