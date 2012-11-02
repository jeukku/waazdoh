package waazdoh.swt;

import waazdoh.app.ESong;
import waazdoh.app.swing.AppListener;
import waazdoh.client.MClient;
import waazdoh.common.model.Track;

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
