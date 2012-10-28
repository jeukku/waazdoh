package cmusic.swt.components;

import org.cutils.MLogger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

public class AudioLevelMeter extends Composite {
	private static final long UPDATEDELAY = 30;
	private float level;
	private long lastupdate;
	private Canvas canvas;
	private MLogger log = MLogger.getLogger(this);

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public AudioLevelMeter(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		setLayout(new FillLayout(SWT.HORIZONTAL));
		canvas = new Canvas(this, SWT.NONE);
		canvas.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = canvas.getBounds();
				float w = clientArea.width * level;
				int y = clientArea.height;
				e.gc.fillRectangle(0, y, (int) w, (int) y);
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setLevel(float level) {
		this.level = level;
		if (System.currentTimeMillis() - lastupdate > UPDATEDELAY) {
			log.info("redraw " + level);
			//
			lastupdate = System.currentTimeMillis();
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					canvas.redraw();
				}
			});
		}
	}
}
