package waazdoh.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import waazdoh.app.App;
import waazdoh.client.MClient;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.swt.AppListenerAdapter;

public class LoginScreen extends Composite {
	private static final String PREF_AUTOLOGIN = "cmusic.app.autologin";
	private static final String PREF_SAVEDEMAIL = "cmusic.app.saved_email";
	private static final String PREF_SAVEDAPPNAME = "cmusic.app.saved_appname";
	private static final String PREF_SAVEDSESSION = "cmusic.app.saved_session";
	private App app;
	private Text tuseremail;
	private Text tappname;
	private MID appid = new MID();
	private Composite logincomposite;
	private Composite waitingcompotsite;
	private Composite savedloginscreen;
	private boolean cancelpressed;
	//
	private MLogger log = MLogger.getLogger(this);
	private StackLayout stacklayout;
	private Composite composite;

	private void login() {
		this.cancelpressed = false;
		Thread t = new Thread(new Runnable() {
			public void run() {
				setWaitingScreen();
				while (!cancelpressed && app.getClient().getUserID() == null) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							String useremail = tuseremail.getText();
							String appname = tappname.getText();
							String sessionid = app.getClient().requestAppLogin(
									useremail, appname, appid);
							if (sessionid != null) {
								saveLogin(useremail, sessionid);
								doSavedLogin();
							}
						}
					});
					//
					try {
						synchronized (this) {
							this.wait(6000);
						}
					} catch (InterruptedException e) {
						log.error(e);
					}
				}
			}
		});
		t.start();
	}

	public LoginScreen(App napp, Composite parent, int style) {
		super(parent, style);
		this.app = napp;
		//
		createContent();
		if (app != null) {
			app.addListener(new AppListenerAdapter() {
				@Override
				public void clientAdded(MClient c) {
					init();
				}
			});
		}
	}

	private void init() {
		if (app != null && app.getPreferences().getBoolean(PREF_AUTOLOGIN, false)) {
			setSavedLoginScreen();
			//
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					trySavedLogin();
				}
			});
		} else {
			setLoginScreen();
		}
	}

	private void setWaitingScreen() {
		log.info("set waiting screen");
		setScreen(waitingcompotsite);
	}

	private void setSavedLoginScreen() {
		log.info("set saved login screen");
		setScreen(savedloginscreen);
	}

	private void setLoginScreen() {
		log.info("set login screen");
		Composite current = logincomposite;
		setScreen(current);
	}

	public void setScreen(final Composite current) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				stacklayout.topControl = current;
				composite.layout();
				composite.redraw();
			}
		});
	}

	public void createContent() {
		//
		createLoginComposite();
		createWaitingAuthorizationComposite();
		createSavedLoginScreen();
		//
		this.redraw();
	}

	private void createSavedLoginScreen() {
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		setLayout(new GridLayout());
		new Label(this, SWT.NONE);
		//
		composite = new Composite(this, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				false));
		//
		stacklayout = new StackLayout();
		stacklayout.marginWidth = 10;
		stacklayout.marginHeight = 10;
		composite.setLayout(stacklayout);
		//
		savedloginscreen = new Composite(composite, SWT.NONE);
		savedloginscreen.setLayout(new FillLayout());
		Text text = new Text(savedloginscreen, SWT.NONE);
		text.setText("loggin in...");
		waitingcompotsite = new Composite(composite, SWT.NONE);
		waitingcompotsite.setLayout(new GridLayout(1, false));

		Label waitinlabel = new Label(waitingcompotsite, SWT.CHECK);
		waitinlabel.setAlignment(SWT.CENTER);
		waitinlabel.setText("Waiting for authorization");

		Label label = new Label(waitingcompotsite, SWT.SHADOW_NONE);
		Label checklabel = new Label(waitingcompotsite, SWT.CHECK);
		checklabel.setAlignment(SWT.CENTER);
		checklabel.setText("Check your notifications at");
		Label sitelabel = new Label(waitingcompotsite, SWT.CHECK);
		sitelabel.setText("http://waazdoh.com/notifications");
		logincomposite = new Composite(composite, SWT.NONE);
		logincomposite.setLayout(new GridLayout(2, false));
		//
		Label lbemail = new Label(logincomposite, SWT.NONE);
		lbemail.setText("email");
		tuseremail = new Text(logincomposite, SWT.BORDER);
		Label lappname = new Label(logincomposite, SWT.NONE);
		lappname.setText("App name");
		tappname = new Text(logincomposite, SWT.BORDER);
		new Label(logincomposite, SWT.NONE);
		Button blogin = new Button(logincomposite, SWT.NONE);
		blogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				login();
			}
		});
		blogin.setText("login");
	}

	private void createWaitingAuthorizationComposite() {
	}

	public void createLoginComposite() {
	}

	private void trySavedLogin() {
		if (!doSavedLogin()) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					setLoginScreen();
				}
			});
		}
	}

	private boolean doSavedLogin() {
		String username = app.getClient().getPreferences()
				.get(PREF_SAVEDEMAIL, null);
		String session = app.getClient().getPreferences()
				.get(PREF_SAVEDSESSION, null);
		if (username != null && session != null) {
			return app.getClient().setUsernameAndSession(username, session);
		} else {
			return false;
		}
	}

	private void saveLogin(String useremail, String session) {
		app.getClient().getPreferences().set(PREF_SAVEDEMAIL, useremail);
		app.getClient().getPreferences().set(PREF_SAVEDSESSION, session);
		app.getClient().getPreferences().set(PREF_AUTOLOGIN, true);
	}
}
