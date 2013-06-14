package waazdoh.swt.components;

import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import waazdoh.common.model.InstrumentTrack;
import waazdoh.common.model.NoteTime;
import waazdoh.common.model.WNote;

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
		int w = getBounds().width;
		int h = getBounds().height;
		if (h > w) {
			drawVertical(e);
		} else {
			drawHorizontal(e);
		}
	}

	private void drawHorizontal(PaintEvent e) {
		GC gc = e.gc;
		int w = getBounds().width;
		int h = getBounds().height;
		//
		List<WNote> notes = track.getNotes();
		for (WNote note : notes) {
			int x = getHorizontalLocation(note.getTime());
			if (x > 0 && x < w) {
				int bx = (int) getHorizontalDistance(note.getLength());
				int y = (int) (note.getNote() / 30.0f * h);
				e.gc.drawRectangle(x, y - 2, bx, 4);
			}
		}

	}

	private int getHorizontalLocation(NoteTime time) {
		return (int) getHorizontalDistance(time); // TODO
	}

	private float getHorizontalDistance(NoteTime time) {
		return time.notetimeInRealtime(track.getTempo()) * 100 * zoom;
	}

	private void drawVertical(PaintEvent e) {
		GC gc = e.gc;
		int w = getBounds().width;
		int h = getBounds().height;
	}

	public void setTrack(InstrumentTrack track) {
		this.track = track;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
		if (zoom > 0.001) {
			reset();
		}
	}

	public void reset() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				redraw();
			}
		});
	}

}
