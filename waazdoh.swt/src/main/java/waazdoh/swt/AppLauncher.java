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

import java.io.File;
import java.net.MalformedURLException;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

import waazdoh.app.App;
import waazdoh.cutils.AppPreferences;
import waazdoh.cutils.MLogger;

public class AppLauncher {
	private App app;
	private MLogger log = MLogger.getLogger(this);

	public AppLauncher(AppPreferences p) throws MalformedURLException {
		log.info("running app wit preferences " + p);
		app = new App(p);
		SWTApp window = new SWTApp(app);
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		try {
			AppPreferences p = new AppPreferences();
			//
			p.get("cmusic.app.autologin", "false");
			String localpath = p.get("local.path", "waazdohmusic");
			String logspath = localpath + File.separator + "logs";
			File logsdir = new File(logspath);
			logsdir.mkdirs();
			//
			for (String string : args) {
				StringTokenizer st = new StringTokenizer(string, "=");
				String name = st.nextToken();
				if (st.hasMoreTokens()) {
					p.get(name, st.nextToken()); // default value used if value
													// doesn't exist
				}
			}

			new AppLauncher(p);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "" + e;
			String title = "failed to start";
			MLogger.getLogger(e).error(e);
			MLogger.getLogger(e).error(message);
			ErrorDialog.openError(null, title, message, new Status(
					IStatus.ERROR, title, message));
			System.exit(1);
		}
	}
}
