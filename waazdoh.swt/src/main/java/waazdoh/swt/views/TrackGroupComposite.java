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
package waazdoh.swt.views;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import waazdoh.app.App;
import waazdoh.app.ESong;
import waazdoh.common.model.MProgress;
import waazdoh.common.model.Track;
import waazdoh.common.model.TrackGroup;
import waazdoh.common.model.TrackGroupListener;
import waazdoh.common.model.WaveTrack;
import waazdoh.cutils.MLogger;
import waazdoh.swt.TitleLayout;
import waazdoh.swt.layouts.RowFillLayout;

public class TrackGroupComposite extends Composite {
	private TrackGroup trackgroup;
	private Composite ctracks;
	private ESong song;
	final private App app;
	private MLogger log = MLogger.getLogger(this);
	private Label lready;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param nsong
	 * @param napp
	 */
	public TrackGroupComposite(ESong nsong, TrackGroup eTrackGroup,
			Composite parent, App napp) {
		super(parent, SWT.NONE);
		this.trackgroup = eTrackGroup;
		this.song = nsong;
		this.app = napp;
		//
		setLayout(new TitleLayout());

		Composite ctop = new Composite(this, SWT.NONE);

		Composite cbuttons = new Composite(this, SWT.NONE);
		cbuttons.setLayout(new RowLayout(SWT.HORIZONTAL));

		Button bnewtrack = new Button(cbuttons, SWT.NONE);
		bnewtrack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// createTestTrack();
				trackgroup.newTrack();
			}
		});
		bnewtrack.setText("New Tack");
		ctop.setLayout(new GridLayout(2, false));

		final Text lgroupname = new Text(ctop, SWT.BORDER);
		lgroupname.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		lgroupname.setText("trackgroup");

		lready = new Label(ctop, SWT.NONE);
		lready.setText("is ready?");
		lgroupname.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				trackgroup.setName(lgroupname.getText());
			}
		});

		ctracks = new Composite(this, SWT.BORDER);
		RowFillLayout ctrackslayout = new RowFillLayout();
		ctrackslayout.setWidthComposite(this);
		ctracks.setLayout(ctrackslayout);
		addTracks();
		//
		trackgroup.addListener(new TrackGroupListener() {
			@Override
			public void trackAdded(Track track) {
				add(track);
			}
		});

	}

	// public org.eclipse.swt.graphics.Point computeSize(int wHint, int hHint) {
	// return new Point(400 * ctracks.getChildren().length, 200);
	// };

	private void addTracks() {
		List<Track> ts = trackgroup.getTracks();
		for (Track eTrack : ts) {
			add(eTrack);
		}
	}

	private void add(Track track) {
		if (track instanceof WaveTrack) {
			WaveTrack wtrack = (WaveTrack) track;
			add(wtrack);
		}
	}

	private void add(WaveTrack eTrack) {
		new MixerTrackComposite(app, song, eTrack, ctracks);
		layout();
		ctracks.layout();
	}

	public TrackGroup getTrackgroup() {
		return this.trackgroup;
	}

	public void checkReady() {
		final MProgress p = new MProgress();
		getTrackgroup().checkTracks(p);

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				lready.setText("" + p.getPersentage() + "%");
				//
				Control[] cs = ctracks.getChildren();
				for (Control control : cs) {
					MixerTrackComposite ctrack = (MixerTrackComposite) control;
					ctrack.checkReady();
				}
			}
		});
	}
}
