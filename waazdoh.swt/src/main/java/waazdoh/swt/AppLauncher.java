package waazdoh.swt;

import java.io.File;
import java.net.MalformedURLException;
import java.util.StringTokenizer;
import java.util.logging.LogManager;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;

import waazdoh.app.App;
import waazdoh.app.AppPreferences;
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
			String path = "";
			for (String string : args) {
				if (string.indexOf("=") < 0) {
					path = string;
				}
			}

			AppPreferences p = new AppPreferences(path);
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
