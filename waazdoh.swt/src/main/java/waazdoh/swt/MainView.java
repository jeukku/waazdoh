package waazdoh.swt;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import waazdoh.app.App;
import waazdoh.app.ESong;
import waazdoh.swt.AppListenerAdapter;
import org.eclipse.swt.widgets.Label;


public class MainView extends Composite {
	private App app;
	private CTabFolder songTabs;
	private SongToolsComposite songtools;
	private int songcounter;

	public MainView(App app, Composite parent, int style) {
		super(parent, style);
		this.app = app;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 1;
		gridLayout.marginHeight = 1;
		gridLayout.verticalSpacing = 1;
		gridLayout.marginWidth = 1;
		setLayout(gridLayout);
		//
		songtools = new SongToolsComposite(app, this, SWT.NONE);
		GridData gd_songtools = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_songtools.heightHint = 46;
		songtools.setLayoutData(gd_songtools);
		
		songTabs = new CTabFolder(this, SWT.BORDER);
		songTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		songTabs.setMaximized(true);
		//
		if (app != null) {
			app.addListener(new AppListenerAdapter() {
				@Override
				public void songChanged(ESong s) {
					appSongChanged(s);
				}

				@Override
				public void error(String title, String message, Exception e) {
					appError(title, message, e);
				}

			});

		}
	}

	public void addAudioNotifiation(String string) {
		songtools.setAudioState(string);
	}

	private void appError(String title, String message, Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		IStatus status = new Status(IStatus.ERROR, "" + title, 0, "" + message,
				e);
		ErrorDialog.openError(this.getShell(), "My error Message Dialog",
				"Reason for the error", status);
	}

	private CTabItem findSongItem(ESong s) {
		CTabItem[] items = songTabs.getItems();
		for (CTabItem cTabItem : items) {
			ESong itemsong = (ESong) cTabItem.getData();
			if (itemsong == s) {
				return cTabItem;
			}

		}
		return null;
	}

	private void appSongChanged(ESong s) {
		if (findSongItem(s) == null) {
			CTabItem i = new CTabItem(songTabs, SWT.CLOSE);
			i.setText(getSongTabName());
			i.setControl(new SongComposite(s, songTabs, app));
			songTabs.setSelection(i);
			//
			songtools.setSong(s);
		}
	}

	private String getSongTabName() {
		return "song" + (songcounter++);
	}
}
