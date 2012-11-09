package waazdoh.swt;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import waazdoh.app.App;
import waazdoh.app.ESong;
import waazdoh.app.ESongListener;
import waazdoh.common.model.MProgress;
import waazdoh.common.model.Song;
import waazdoh.common.model.Track;
import waazdoh.common.model.TrackGroup;
import waazdoh.common.model.TrackGroupListener;
import waazdoh.swt.layouts.RowFillLayout;

public class SongComposite extends Composite implements ESongListener {
	private ESong song;
	private Composite trackgroups;
	private ScrolledComposite scrolledComposite;
	private final App app;
	private Label lready;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param app
	 */
	public SongComposite(ESong s, Composite parent, final App app) {
		super(parent, SWT.CLOSE);
		this.song = s;
		this.app = app;
		//
		this.setLayout(new TitleLayout());
		//
		getSong().addSongListener(this);
		Composite cbuttons = new Composite(this, SWT.NONE);
		RowLayout rl_cbuttons = new RowLayout(SWT.HORIZONTAL);
		rl_cbuttons.center = true;
		cbuttons.setLayout(rl_cbuttons);

		final Text songname = new Text(cbuttons, SWT.NONE);
		songname.setLayoutData(new RowData(164, 21));
		songname.setText(getSong().getName());
		songname.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				getSong().setName(songname.getText());
			}
		});
		//
		Button bnewtg = new org.eclipse.swt.widgets.Button(cbuttons, SWT.NONE);
		bnewtg.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getSong().addTrackGroup();
			}
		});
		bnewtg.setText("New Trackgroup");

		lready = new Label(cbuttons, SWT.NONE);
		lready.setText("is ready?");
		scrolledComposite = new ScrolledComposite(this, SWT.BORDER
				| SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		trackgroups = new Composite(scrolledComposite, SWT.NONE);
		// Label test = new Label(trackgroups, SWT.BORDER);
		// test.setLayoutData(new RowData(179, 43));
		// test.setText("TEST");

		RowFillLayout trackgroupslayout = new RowFillLayout();
		trackgroupslayout.setWidthComposite(this);
		trackgroups.setLayout(trackgroupslayout);

		scrolledComposite.setContent(trackgroups);
		addTrackGroups();
		//
		Thread readychecker = new Thread(new Runnable() {
			@Override
			public void run() {
				while (app.getClient().isRunning()) {
					try {
						checkReady();
					} catch (Exception e) {
						doWait(2000);
					}
				}
			}
		}, "SongReadyChecker");
		readychecker.start();
	}

	protected void checkReady() {
		final MProgress progress = song.getSong().checkTracks();
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				lready.setText("" + progress.getPersentage() + "%");
				//
				Control[] trackgroupchildren = trackgroups.getChildren();
				for (Control control : trackgroupchildren) {
					TrackGroupComposite tgc = (TrackGroupComposite) control;
					tgc.checkReady();
				}
			}
		});
		//
		doWait(1000);
	}

	private void doWait(int time) {
		synchronized (song) {
			try {
				song.wait(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void addTrackGroups() {
		List<TrackGroup> ts = getSong().getTrackGroups();
		for (TrackGroup tg : ts) {
			add(tg);
		}
		trackgroups.layout();
		scrolledComposite.layout();
	}

	private Song getSong() {
		return song.getSong();
	}

	@Override
	public void zoomChanged(int zoom) {
		// TODO Auto-generated method stub
	}

	private void add(TrackGroup trackgroup) {
		Control[] cs = trackgroups.getChildren();
		boolean found = false;
		for (Control control : cs) {
			if (control instanceof TrackGroupComposite) {
				TrackGroupComposite tgc = (TrackGroupComposite) control;
				if (tgc.getTrackgroup() == trackgroup) {
					found = true;
				}
				// w += tgc.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			}
		}
		if (!found) {
			new TrackGroupComposite(song, trackgroup, trackgroups, app);
		}
		trackgroup.addListener(new TrackGroupListener() {
			@Override
			public void trackAdded(Track etrack) {
				updateSize();
			}
		});
		//
		updateSize();
	}

	public void updateSize() {
		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public void trackGroupAdded(TrackGroup track) {
		add(track);
	}
}
