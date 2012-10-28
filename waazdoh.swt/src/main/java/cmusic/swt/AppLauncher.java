package cmusic.swt;

import java.net.MalformedURLException;
import java.util.StringTokenizer;

import org.cutils.MLogger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

import cmusic.app.App;
import cmusic.app.AppPreferences;

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
			p.get("local.path", "waazdohmusic");
			//
			for (String string : args) {
				StringTokenizer st = new StringTokenizer(string, "=");
				String name = st.nextToken();
				p.get(name, st.nextToken()); // default value used if value
												// doesn't exist
			}
			new AppLauncher(p);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "" +e;
			String title = "failed to start";
			ErrorDialog.openError(null, title, message, new Status(IStatus.ERROR, title, message ));
		}
	}
}
