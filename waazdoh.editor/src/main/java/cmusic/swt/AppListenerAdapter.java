package cmusic.swt;

import waazdoh.common.model.Track;
import cmusic.app.ESong;
import cmusic.app.swing.AppListener;
import cmusic.client.MClient;

public class AppListenerAdapter implements AppListener {

	@Override
	public void songChanged(ESong s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clientAdded(MClient c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recordingTrackChanged(Track changedtrack) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notification(String notificationsource, String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void waitState(String nwaitstate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void error(String title, String message, Exception e) {
		// TODO Auto-generated method stub

	}
}
