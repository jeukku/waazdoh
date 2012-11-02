package waazdoh.swt.layouts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class RowFillLayout extends Layout {
	private Composite widthcomposite;

	public RowFillLayout() {
	}

	public void setWidthComposite(Composite nwidthcomposite) {
		this.widthcomposite = nwidthcomposite;
	}
	
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
		int width = widthcomposite.getSize().x;
		// ;
		Control[] cs = composite.getChildren();
		int y = 0;
		for (Control c : cs) {
			Point csize = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			y += csize.y;
		}
		return new Point(width, y);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		int width = widthcomposite.getSize().x;
		//
		Control[] cs = composite.getChildren();
		int y = 0;
		for (Control c : cs) {
			Point csize = c.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			c.setBounds(0, y, width, csize.y);
			y += csize.y;
		}
	}
}
