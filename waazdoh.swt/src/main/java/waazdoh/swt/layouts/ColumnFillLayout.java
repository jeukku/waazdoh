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
package waazdoh.swt.layouts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class ColumnFillLayout extends Layout {
	private Composite heightcomposite;

	public ColumnFillLayout(Composite heightcomposite) {
		this.heightcomposite = heightcomposite;
	}
	
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
		int height = heightcomposite.getSize().y;
		//;
		Control[] cs = composite.getChildren();
		int x = 0;
		for (Control c : cs) {
			Point csize = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			x += csize.x;
		}
		return new Point(x, height);
	}
	
	@Override
	protected void layout(Composite composite, boolean flushCache) {
		int height = heightcomposite.getSize().y;
		//
		Control[] cs = composite.getChildren();
		int x = 0;
		for (Control c : cs) {
			Point csize = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			c.setBounds(x, 0, csize.x, height);
			x += csize.x;
		}
	}
}
