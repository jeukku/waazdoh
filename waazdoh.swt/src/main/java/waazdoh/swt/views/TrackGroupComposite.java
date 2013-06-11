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
import waazdoh.common.model.InstrumentTrack;
import waazdoh.common.model.MProgress;
import waazdoh.common.model.Track;
import waazdoh.common.model.TrackGroup;
import waazdoh.common.model.TrackGroupListener;
import waazdoh.common.model.WaveTrack;
import waazdoh.cutils.MLogger;
import waazdoh.swt.TitleLayout;
import waazdoh.swt.layouts.RowFillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

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

		Composite ctop = new Composite(this, SWT.BORDER);

		GridLayout gl_ctop = new GridLayout(4, false);
		gl_ctop.marginWidth = 0;
		gl_ctop.marginHeight = 1;
		gl_ctop.verticalSpacing = 1;
		ctop.setLayout(gl_ctop);

		final Text lgroupname = new Text(ctop, SWT.BORDER);
		lgroupname
				.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD));
		GridData gd_lgroupname = new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1);
		gd_lgroupname.widthHint = 297;
		lgroupname.setLayoutData(gd_lgroupname);
		lgroupname.setText("trackgroup");

		Button bnewtrack = new Button(ctop, SWT.NONE);
		bnewtrack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// createTestTrack();
				trackgroup.newTrack();
			}
		});
		bnewtrack.setText("New Tack");

		Button bnewitrack = new Button(ctop, SWT.NONE);
		bnewitrack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// createTestTrack();
				trackgroup.newInstrumentTrack();
			}
		});
		bnewitrack.setText("New Insturment Track");

		lready = new Label(ctop, SWT.NONE);
		lready.setText("0%");
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
		} else if (track instanceof InstrumentTrack) {
			InstrumentTrack itrack = (InstrumentTrack) track;
			add(itrack);
		}
	}

	private void add(InstrumentTrack itrack) {
		new InstrumentTrackComposite(app, song, itrack, ctracks);
		layout();
		ctracks.layout();
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
					WComponentComposite ctrack = (WComponentComposite) control;
					ctrack.checkReady();
				}
			}
		});
	}
}
