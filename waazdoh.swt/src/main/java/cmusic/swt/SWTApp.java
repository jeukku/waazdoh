package cmusic.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import waazdoh.common.model.Song;

import cmusic.app.App;
import cmusic.client.ClientListener;
import cmusic.client.MClient;
import cmusic.swt.layouts.RowFillLayout;
import cmusic.swt.updater.Updater;

public class SWTApp {
	protected Shell shell;
	private App app;
	private StackLayout layout;
	private MainView mainview;
	private WaitView waitview;
	private Composite cstack;
	private Composite composite;
	private Composite cnotifications;
	private Label linfo;

	public SWTApp(App app) {
		this.app = app;
		//
		app.addListener(new AppListenerAdapter() {
			@Override
			public void clientAdded(MClient c) {
				thisClientAdded(c);
			}

			@Override
			public void waitState(String nwaitstate) {
				appWaitState(nwaitstate);
			}

			@Override
			public void notification(final String notificationsource,
					final String string) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						appNotification(notificationsource, string);
					}
				});
			}
		});
		//
		new Updater(app, app.getPreferences());
		//
		open();
	}

	protected void appNotification(String notificationsource, String string) {
		if (notificationsource.equals(App.NOTIFICATION_AUDIO)) {
			mainview.addAudioNotifiation(string);
		} else {
			WNotification n = new WNotification(shell, cnotifications,
					notificationsource, string);
			shell.layout();
		}
	}

	protected void appWaitState(final String nwaitstate) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (nwaitstate != null) {
					layout.topControl = waitview;
				} else {
					layout.topControl = mainview;
				}
				doLayout();
			}
		});
	}

	private void doLayout() {
		shell.layout();
	}

	private void thisClientAdded(MClient c) {
		app.getClient().addClientListener(new ClientListener() {
			@Override
			public void newSong(Song s) {
				// TODO Auto-generated method stub
			}

			@Override
			public void loggedIn() {
				clientLoggedIn();
			}
		});
	}

	/**
	 * Open the window.
	 * 
	 * @param c
	 */
	private void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed() && display != null) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.exit(0); // TODO didn't close on bluetile otherwise :(
			}
		});
		//
		shell.setSize(450, 300);
		shell.setMaximized(true);
		shell.setText("Waazdoh");
		shell.setLayout(new GridLayout(1, false));

		cstack = new Composite(shell, SWT.NONE);
		cstack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		layout = new StackLayout();
		cstack.setLayout(layout);
		//

		mainview = new MainView(app, cstack, SWT.NONE);
		LoginScreen loginScreen = new LoginScreen(app, cstack, SWT.NONE);
		layout.topControl = loginScreen;

		this.waitview = new WaitView(app, cstack, SWT.NONE);

		composite = new Composite(shell, SWT.NONE);
		composite.setForeground(SWTResourceManager.getColor(0, 0, 0));
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		cnotifications = new Composite(composite, SWT.NONE);
		RowFillLayout rl_cnotifications = new RowFillLayout(composite);
		cnotifications.setLayout(rl_cnotifications);

		createInfo(shell);

		shell.layout();
	}

	private void createInfo(Shell shell) {
		linfo = new Label(shell, SWT.NONE);
		linfo.setText("Info");

		final Display display = shell.getDisplay();

		final int time = 500;

		Runnable timer = new Runnable() {
			public void run() {
				String s = getInfoText();
				linfo.setText(s);

				display.timerExec(time, this);
			}
		};
		display.timerExec(time, timer);
	}

	protected String getInfoText() {
		return app.getInfo();
	}

	private void clientLoggedIn() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				Menu menu = new Menu(shell, SWT.BAR);
				shell.setMenuBar(menu);
				MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
				mntmNewSubmenu.setText("Content");
				Menu menu_1 = new Menu(mntmNewSubmenu);
				mntmNewSubmenu.setMenu(menu_1);
				MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
				mntmNewItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						app.newSong();
					}
				});
				mntmNewItem.setText("New Song");
				//
				MenuItem menuApplicationItem = new MenuItem(menu, SWT.CASCADE);
				menuApplicationItem.setText("Application");
				Menu menuApplication = new Menu(menuApplicationItem);
				menuApplicationItem.setMenu(menuApplication);
				MenuItem miApplicationSettings = new MenuItem(menuApplication,
						SWT.NONE);
				miApplicationSettings
						.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								PreferencesDialog dialog = new PreferencesDialog(
										shell, app);
								dialog.open();
							}
						});
				miApplicationSettings.setText("Settings");

				//
				layout.topControl = mainview;
				cstack.layout();
				shell.layout();
			}
		});
	}
}
