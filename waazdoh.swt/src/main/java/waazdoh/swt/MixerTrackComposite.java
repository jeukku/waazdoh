package waazdoh.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import waazdoh.app.App;
import waazdoh.app.ESong;
import waazdoh.common.model.MProgress;
import waazdoh.common.model.Track;
import waazdoh.common.model.TrackListener;
import waazdoh.swt.components.AudioCanvas;

public class MixerTrackComposite extends Composite {
	private static final int VOLUMEMAX = 10000;
	private Track track;
	private ESong song;
	private AudioCanvas canvas;
	private Label lready;

	public MixerTrackComposite(final App app, ESong s, Track eTrack,
			final Composite parent) {
		super(parent, SWT.BORDER);
		this.track = eTrack;
		this.song = s;
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);

		Composite composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite.heightHint = 59;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new RowLayout(SWT.VERTICAL));

		Composite top = new Composite(composite, SWT.NONE);
		GridLayout gl_top = new GridLayout(2, false);
		gl_top.verticalSpacing = 0;
		gl_top.marginWidth = 0;
		gl_top.horizontalSpacing = 0;
		gl_top.marginHeight = 0;
		top.setLayout(gl_top);

		//
		final Text ltrackname = new Text(top, SWT.NONE);
		GridData gd_ltrackname = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_ltrackname.widthHint = 135;
		ltrackname.setLayoutData(gd_ltrackname);
		ltrackname.setText("" + eTrack.getName());
		ltrackname.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				track.setName(ltrackname.getText());
			}
		});

		Composite cbuttons = new Composite(top, SWT.NONE);
		RowLayout rl_cbuttons = new RowLayout(SWT.HORIZONTAL);
		rl_cbuttons.marginRight = 0;
		rl_cbuttons.marginTop = 0;
		rl_cbuttons.marginLeft = 0;
		rl_cbuttons.marginBottom = 0;
		cbuttons.setLayout(rl_cbuttons);
		final Button brecord = new Button(cbuttons, SWT.TOGGLE);

		brecord.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (brecord.getSelection()) {
					app.setRecording(song, track);
				} else {
					app.setRecording(song, null);
				}
			}
		});
		brecord.setText("R");

		final Button bmute = new Button(cbuttons, SWT.TOGGLE);
		bmute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				track.setMute(bmute.getSelection());
			}
		});
		bmute.setText("M");
		bmute.setSelection(track.isMuted());

		Button bimport = new Button(cbuttons, SWT.NONE);
		bimport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fd = new FileDialog(parent.getShell());
				String sfile = fd.open();
				track.replaceWave(app.importTrack(sfile));
			}
		});
		bimport.setText("Import");
		//
		Composite bottom = new Composite(composite, SWT.NONE);
		bottom.setLayoutData(new RowData(205, 25));
		GridLayout gl_bottom = new GridLayout(6, false);
		gl_bottom.verticalSpacing = 0;
		gl_bottom.marginWidth = 0;
		gl_bottom.marginHeight = 0;
		gl_bottom.horizontalSpacing = 0;
		bottom.setLayout(gl_bottom);

		final Spinner svolume = new Spinner(bottom, SWT.NONE);
		svolume.setDigits(2);
		GridData gd_svolume = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_svolume.heightHint = 22;
		svolume.setLayoutData(gd_svolume);
		svolume.setMaximum(VOLUMEMAX);
		svolume.setMinimum(0);
		svolume.setSelection((int) (track.getVolume().getLevel() * VOLUMEMAX));
		new Label(bottom, SWT.NONE);
		new Label(bottom, SWT.NONE);
		new Label(bottom, SWT.NONE);
		new Label(bottom, SWT.NONE);

		lready = new Label(bottom, SWT.NONE);
		lready.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lready.setText("is ready?");
		svolume.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				int s = svolume.getSelection();
				track.getVolume().setLevel(1.0f * s / VOLUMEMAX);
			}
		});

		/*
		 * Spinner pan = new Spinner(bottom, SWT.NONE); pan.setDigits(3);
		 * GridData gd_pan = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
		 * 1); gd_pan.widthHint = 67; pan.setLayoutData(gd_pan);
		 * pan.setMaximum(1000); pan.setMinimum(0); pan.setSelection(500);
		 */

		canvas = new AudioCanvas(this, SWT.BORDER);
		GridData gd_canvas = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_canvas.heightHint = 59;
		canvas.setLayoutData(gd_canvas);
		canvas.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		canvas.setOutput(track);
		canvas.setZoom(s.getZoom());

		app.addListener(new AppListenerAdapter() {
			@Override
			public void recordingTrackChanged(Track changedtrack) {
				if (changedtrack != track) {
					brecord.setSelection(false);
				} else {
					brecord.setSelection(true);
				}
			}
		});

		track.addListener(new TrackListener() {
			@Override
			public void modified() {
				canvas.reset();
			}
		});

		s.addSongListener(new ESongListenerAdapter() {
			@Override
			public void zoomChanged(int zoom) {
				canvas.setZoom(zoom);
			}
		});
	}

	public void checkReady() {
		MProgress p = new MProgress();
		track.checkWave(p);
		//
		lready.setText("" + p.getPersentage() + "%");
	}
}
