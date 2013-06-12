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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import waazdoh.app.App;
import waazdoh.app.ESong;
import waazdoh.common.model.InstrumentTrack;
import waazdoh.common.model.MProgress;
import waazdoh.common.model.ServiceObjectListener;
import waazdoh.swt.ESongListenerAdapter;
import waazdoh.swt.components.InsturmentTrackCanvas;

public class InstrumentTrackComposite extends Composite implements
		WComponentComposite {
	private static final int VOLUMEMAX = 10000;
	private InstrumentTrack track;
	private ESong song;
	private Label lready;
	private InsturmentTrackCanvas canvas;

	public InstrumentTrackComposite(final App app, ESong s,
			InstrumentTrack itrack, final Composite parent) {
		super(parent, SWT.BORDER);
		this.track = itrack;
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
		ltrackname.setText("" + track.getName());
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

		final Button bmute = new Button(cbuttons, SWT.TOGGLE);
		bmute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				track.setMute(bmute.getSelection());
			}
		});
		bmute.setText("M");
		bmute.setSelection(track.isMuted());

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
		GridData gd_svolume = new GridData(SWT.CENTER, SWT.CENTER, false, false,
				1, 1);
		gd_svolume.heightHint = 22;
		svolume.setLayoutData(gd_svolume);
		svolume.setMaximum(VOLUMEMAX);
		svolume.setMinimum(0);
		svolume.setSelection((int) (track.getVolume().getLevel() * VOLUMEMAX));

		lready = new Label(bottom, SWT.NONE);
		lready.setAlignment(SWT.CENTER);
		GridData gd_lready = new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1);
		gd_lready.widthHint = 32;
		lready.setLayoutData(gd_lready);
		lready.setText("0%");

		canvas = new InsturmentTrackCanvas(this, SWT.NONE);
		GridData gd_canvas = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_canvas.heightHint = 59;
		canvas.setLayoutData(gd_canvas);
		canvas.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		canvas.setTrack(track);
		canvas.setZoom(s.getZoom());

		svolume.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				int s = svolume.getSelection();
				track.getVolume().setLevel(1.0f * s / VOLUMEMAX);
			}
		});

		track.getServiceObject().addListener(new ServiceObjectListener() {
			@Override
			public void modified() {
				trackModified();
			}
		});

		s.addSongListener(new ESongListenerAdapter() {
			@Override
			public void zoomChanged(int zoom) {
				setZoom(zoom);
			}
		});
	}

	protected void trackModified() {
		canvas.reset();
	}

	protected void setZoom(int zoom) {
		canvas.setZoom(zoom);
	}

	public void checkReady() {
		MProgress p = new MProgress();
		track.checkProgress(p);
		//
		lready.setText("" + p.getPersentage() + "%");
	}
}
