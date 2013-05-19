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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import waazdoh.app.App;
import waazdoh.app.ESong;
import waazdoh.common.model.InstrumentTrack;
import waazdoh.common.model.MProgress;
import waazdoh.cutils.MLogger;
import waazdoh.swt.TitleLayout;

public class TrackerComposite extends Composite {
	private Composite ctracks;
	private ESong song;
	final private App app;
	private MLogger log = MLogger.getLogger(this);
	private Label lready;
	private InstrumentTrack tracker;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param nsong
	 * @param napp
	 */
	public TrackerComposite(ESong nsong, final InstrumentTrack tracker,
			Composite parent, App napp) {
		super(parent, SWT.NONE);
		this.tracker = tracker;
		this.song = nsong;
		this.app = napp;
		//
		setLayout(new TitleLayout());

		Composite ctop = new Composite(this, SWT.NONE);

		Composite cbuttons = new Composite(this, SWT.NONE);
		cbuttons.setLayout(new RowLayout(SWT.HORIZONTAL));

		ctop.setLayout(new GridLayout(2, false));

		final Text lname = new Text(ctop, SWT.BORDER);
		lname.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1));
		lname.setText("trackgroup");

		lready = new Label(ctop, SWT.NONE);
		lready.setText("is ready?");
		lname.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				tracker.setName(lname.getText());
			}
		});
	}

	public void checkReady() {
		final MProgress p = new MProgress();
		tracker.checkProgress(p);
	}
}
