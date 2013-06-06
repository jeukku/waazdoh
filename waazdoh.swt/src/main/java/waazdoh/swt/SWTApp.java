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

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import waazdoh.MJob;
import waazdoh.app.App;
import waazdoh.client.BookmarksListener;
import waazdoh.client.ClientListener;
import waazdoh.client.MClient;
import waazdoh.client.WBookmark;
import waazdoh.client.WBookmarkGroup;
import waazdoh.client.WBookmarkGroupListener;
import waazdoh.client.WUser;
import waazdoh.common.model.MBinarySource;
import waazdoh.common.model.Song;
import waazdoh.cutils.JBeanResponse;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;
import waazdoh.swt.layouts.RowFillLayout;

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
	private Menu menuusers;
	private Menu menubookmarks;

	private MLogger log = MLogger.getLogger(this);
	private Menu menucached;

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
			public void songLoaded(Song song) {
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
		shell.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				System.exit(0); // TODO didn't close on bluetile otherwise :(
			}
		});
		//
		shell.setSize(300, 500);
		shell.setMaximized(false);
		shell.setText("Waazdoh");
		GridLayout gl_shell = new GridLayout(1, false);
		gl_shell.verticalSpacing = 1;
		gl_shell.marginWidth = 1;
		gl_shell.marginHeight = 1;
		gl_shell.horizontalSpacing = 1;
		shell.setLayout(gl_shell);

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
		RowFillLayout rl_cnotifications = new RowFillLayout();
		rl_cnotifications.setWidthComposite(composite);

		cnotifications.setLayout(rl_cnotifications);

		createInfo(shell);

		if (app == null) {
			createMenu();
		}

		shell.layout();
		shell.pack();
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
				shell.setMaximized(true);
				createMenu();
			}
		});
	}

	private void createMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		MenuItem contentmenuitem = new MenuItem(menu, SWT.CASCADE);
		contentmenuitem.setText("Content");
		Menu contentmenu = new Menu(contentmenuitem);
		contentmenuitem.setMenu(contentmenu);

		MenuItem mntmNewItem = new MenuItem(contentmenu, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				app.newSong();
			}
		});
		mntmNewItem.setText("New Song");
		//
		MenuItem menuitemExport = new MenuItem(contentmenu, SWT.NONE);
		menuitemExport.setText("export");
		menuitemExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				exportCurrentSong();
			}
		});

		//
		MenuItem menuUsersItem = new MenuItem(menu, SWT.CASCADE);
		menuUsersItem.setText("Users");
		menuusers = new Menu(menuUsersItem);
		menuUsersItem.setMenu(menuusers);

		MenuItem menuBookmarksItem = new MenuItem(menu, SWT.CASCADE);
		menuBookmarksItem.setText("Bookmarks");
		menubookmarks = new Menu(menuBookmarksItem);
		menuBookmarksItem.setMenu(menubookmarks);

		initBookmarkGroups();

		MenuItem menucacheditem = new MenuItem(menu, SWT.CASCADE);
		menucacheditem.setText("local");
		menucached = new Menu(menucacheditem);
		menucacheditem.setMenu(menucached);
		menucached.addListener(SWT.Show, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				resetCachedMenu();
			}
		});

		app.getClient().getBookmarks().addListener(new BookmarksListener() {
			@Override
			public void groupAdded(WBookmarkGroup group) {
				handleBookmarkGroup(group);
			}
		});

		MenuItem menuApplicationItem = new MenuItem(menu, SWT.CASCADE);
		menuApplicationItem.setText("Application");
		Menu menuApplication = new Menu(menuApplicationItem);
		menuApplicationItem.setMenu(menuApplication);

		MenuItem settingsmenuitem = new MenuItem(menuApplication, SWT.NONE);
		settingsmenuitem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PreferencesDialog dialog = new PreferencesDialog(shell, app);
				dialog.open();
			}
		});
		settingsmenuitem.setText("Settings");
		
		MenuItem version = new MenuItem(menuApplication, SWT.NONE);
		version.setText("Version " + App.VERSION);
		
		//
		layout.topControl = mainview;
		cstack.layout();
		shell.layout();
	}

	protected void exportCurrentSong()  {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.open();
		//
		String path = dialog.getFilterPath();
		app.exportSongTo(path);
	}

	protected void resetCachedMenu() {
		MBinarySource source = app.getClient().getBinarySource();
		Set<MID> ids = source.getLocalObjectIDs();
		for (MID mid : ids) {
			JBeanResponse o = app.getClient().getService().read(mid);
			if (o != null) {
				JBean b = o.getBean();
				JBean song = b.find("song");
				if (song != null) {
					JBean bname = b.find("name");
					if (bname != null) {

						String name = bname.getValue();
						log.info("cachedmenu item name " + name + " with bean "
								+ b);
						final MID songid = song.getIDAttribute("id");
						Menu m = getCachedSongMenu(name);
						if (songid!=null && searchMenuItem(m, "" + songid) == null) {
							MenuItem songitem = new MenuItem(m, SWT.None);
							songitem.setData(songid.toString());

							String screated = song.getAttribute("created");
							String smodified = song.getAttribute("modified");
							if (screated == null) {
								screated = "0";
							}
							if (smodified == null) {
								smodified = "0";
							}

							Date created = new Date(Long.parseLong(screated));
							Date modified = new Date(Long.parseLong(smodified));
							songitem.setText("" + created + " (modified "
									+ modified + ")");

							songitem.addSelectionListener(new SelectionListener() {

								@Override
								public void widgetSelected(SelectionEvent arg0) {
									app.getClient().loadSong(songid);
								}

								@Override
								public void widgetDefaultSelected(
										SelectionEvent arg0) {
									// TODO Auto-generated method stub

								}
							});
						}
					}
				}
			}
		}
	}

	private MenuItem searchMenuItem(Menu m, String string) {
		MenuItem[] items = m.getItems();
		for (MenuItem menuItem : items) {
			String menudata = (String) menuItem.getData();
			if (menudata.indexOf(string) >= 0) {
				return menuItem;
			}
		}
		return null;
	}

	private Menu getCachedSongMenu(String name) {
		MenuItem[] items = menucached.getItems();
		for (MenuItem menuItem : items) {
			if (menuItem.getText().equals(name)) {
				return menuItem.getMenu();
			}
		}
		// creating a new menu
		MenuItem i = new MenuItem(menucached, SWT.CASCADE);
		i.setText(name);
		Menu m = new Menu(i);
		i.setMenu(m);
		return m;
	}

	private void initBookmarkGroups() {
		List<WBookmarkGroup> groups = app.getClient().getBookmarks()
				.getBookmarkGroups();
		for (WBookmarkGroup group : groups) {
			handleBookmarkGroup(group);
		}
	}

	private void handleBookmarkGroup(WBookmarkGroup group) {
		Menu parent;
		if (group.getName().equals("users")) {
			parent = menuusers;
		} else {
			MenuItem i = new MenuItem(menubookmarks, SWT.CASCADE);
			i.setText(group.getName());
			Menu m = new Menu(i);
			i.setMenu(m);

			MenuItem test = new MenuItem(m, SWT.None);
			test.setText("testitem");

			parent = m;
		}
		//
		List<WBookmark> bmarks = group.getBookmarks();
		for (WBookmark wBookmark : bmarks) {
			addBookmark(parent, wBookmark);
		}
		//
		group.addListener(new WBookmarkGroupListener() {

		});
	}

	private void addBookmark(Menu parent, WBookmark wBookmark) {
		String oid = wBookmark.getObjectID();

		MenuItem found = null;
		MenuItem[] items = parent.getItems();
		for (MenuItem menuItem : items) {
			if (menuItem.getData().equals(oid)) {
				// already added
				found = menuItem;
				break;
			}
		}

		if (found == null) {
			MenuItem i = new MenuItem(parent, SWT.CASCADE);
			i.setText("object");
			i.setData(oid);

			if (parent == menuusers) {
				final String userid = oid;
				i.setText("user " + userid);
				WUser o = app.getClient().getUser(new UserID(userid));
				i.setText("" + o.getName());
				//
				Menu m = new Menu(i);
				i.setMenu(m);

				initUserMenu(m, userid);
			} else {
				JBeanResponse o = app.getClient().getService()
						.read(new MID(oid));
				i.setText("" + o);
			}
		}
	}

	private void initUserMenu(final Menu m, final String userid) {
		app.getClient().getJobs().addJob(new MJob() {
			@Override
			public boolean run() {
				final List<MID> result = app.getClient().searchSongs(userid, 0,
						200);
				//
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						for (MID mid : result) {
							initObjectMenu(mid, m);
						}
					}
				});
				return true;
			}

		});
	}

	private void initObjectMenu(final MID mid, Menu m) {
		JBeanResponse resp = app.getClient().getService().read(mid);
		if (resp != null) {
			JBean obean = resp.getBean().find("song");
			if (obean != null) {
				MenuItem mi = new MenuItem(m, SWT.None);
				mi.setText("" + mid);

				String name = obean.getAttribute("name");
				if (name != null) {
					mi.setText(name);
					mi.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							app.getClient().loadSong(mid);
						}

						@Override
						public void widgetDefaultSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
						}
					});
				}
			} else {
				log.info("not adding to bookmark menu " + obean);
			}
		} else {
			log.info("not found " + mid);
		}
	}
}
