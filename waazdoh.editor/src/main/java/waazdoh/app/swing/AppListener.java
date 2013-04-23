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
