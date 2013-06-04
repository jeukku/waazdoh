package waazdoh.swt.components;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import waazdoh.common.model.InstrumentTrack;

public class InsturmentTrackCanvas extends Canvas {

	private InstrumentTrack track;
	private float zoom;

	public InsturmentTrackCanvas(Composite parent, int border) {
		super(parent, border);
		//
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				paint(e);
			}
		});
	}

	protected void paint(PaintEvent e) {
		e.gc.drawText("" + track, 0, 20);
	}

	public void setTrack(InstrumentTrack track) {
		this.track = track;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public void reset() {
		// should redraw
	}

}
