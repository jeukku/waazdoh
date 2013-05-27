/*******************************************************************************
 * Copyright (c) 2013 Juuso Vilmunen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Juuso Vilmunen - initial API and implementation
 ******************************************************************************/
package waazdoh.swt;

import waazdoh.app.ESong;
import waazdoh.app.swing.AppListener;
import waazdoh.client.MClient;
import waazdoh.common.model.WaveTrack;

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
	public void recordingTrackChanged(WaveTrack changedtrack) {
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
