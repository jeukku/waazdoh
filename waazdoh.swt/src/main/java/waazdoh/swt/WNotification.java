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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class WNotification extends Composite {	
	private Button bclosenotification;

	public WNotification(final Shell shell, Composite parent, String notificationsource, String string) {
		super(parent, SWT.BORDER);
		
		this.setBackground(SWTResourceManager.getColor(240, 230, 140));
		this.setLayoutData(new RowData(147, SWT.DEFAULT));
		this.setLayout(new GridLayout(3, false));
		Label sourcelabel = new Label(this, SWT.NONE);
		sourcelabel.setForeground(SWTResourceManager.getColor(76, 76, 76));
		sourcelabel.setBackground(SWTResourceManager.getColor(240, 230, 140));
		GridData gd_sourcelabel = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_sourcelabel.widthHint = 64;
		sourcelabel.setLayoutData(gd_sourcelabel);
		sourcelabel.setText("" + notificationsource);
		Label messagelabel = new Label(this, SWT.BORDER);
		messagelabel.setBackground(SWTResourceManager.getColor(240, 230, 140));
		GridData gd_messagelabel = new GridData(SWT.LEFT, SWT.CENTER, true,
				true, 1, 1);
		gd_messagelabel.widthHint = 1610;
		messagelabel.setLayoutData(gd_messagelabel);
		messagelabel.setText("" + string);

		final WNotification me = this;
		
		bclosenotification = new Button(this, SWT.NONE);
		bclosenotification.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				me.dispose();
				shell.layout();
			}
		});
		bclosenotification.setText("X");
	}

}
