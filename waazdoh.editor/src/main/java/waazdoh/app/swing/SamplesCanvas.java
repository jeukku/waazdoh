package waazdoh.app.swing;

import java.awt.Graphics;

import javax.swing.JPanel;

import waazdoh.common.model.Track;


public class SamplesCanvas extends JPanel
{
	private Track track;
	private float end = 1000;
	private float start;
	private float zoom = 1;
	
	public SamplesCanvas(Track track2) {
		this.track = track2;
		setOpaque(false);
		// setPreferredSize(new Dimension(100000, 10));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// super.paintComponent(g);
		//
		float start = getStart();
		float end = getEnd();
		float d = (end - start) / getWidth();
		//
		float currentsample = start;
		for (int x = 0; x < getWidth(); x++) {
			Float fsample = track.getSample((int) currentsample);
			float sample = fsample!=null?fsample:0;
			int y = (int) (sample * getHeight() / 2);
			int middle = getHeight() / 2;
			g.drawLine(x, middle, x, middle + y);
			//
			currentsample += d;
		}
	}
	
	private float getStart() {
		return start;
	}
	
	private float getEnd() {
		return getStart() + getWidth() * zoom;
	}
	
	public void zoomChanged(float zoom) {
		this.zoom = zoom; ///100;
		repaint();
	}
}
