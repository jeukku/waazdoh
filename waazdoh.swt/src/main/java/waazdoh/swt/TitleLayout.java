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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class TitleLayout extends Layout {
	/**
	 * @param mainView
	 */
	TitleLayout() {
	}
	
	@Override
	protected Point computeSize(Composite arg0, int arg1, int arg2, boolean arg3) {
		Control[] children = arg0.getChildren();
		Control first = children[0];
		int h = first.computeSize(arg1, arg2).y;
		int w = 0;
		for (int i = 1; i < children.length; i++) {
			Control child = children[i];
			h += child.computeSize(arg1, arg2).y;
			w += child.computeSize(arg1, arg2).x;
		}
		return new Point(w, h);
	}
	
	@Override
	protected void layout(Composite arg0, boolean arg1) {
		Control[] children = arg0.getChildren();
		Control first = children[0];
		//
		int w = arg0.getSize().x;
		int toolsh = first.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		first.setBounds(0, 0, w, toolsh);
		//
		int cx = 0;
		for (int i = 1; i < children.length; i++) {
			Control child = children[i];
			int cw = child.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
			if(i==children.length-1) {
				cw = w;
			}
			child.setBounds(cx, toolsh, cw, arg0.getSize().y - toolsh);
			w-=cw;
			cx+=cw;
		}
	}
}
