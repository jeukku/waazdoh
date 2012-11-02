package waazdoh.swt.components;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import waazdoh.WaazdohInfo;
import waazdoh.common.model.MOutput;
import waazdoh.common.model.Track;


public class AudioCanvas extends Canvas {
	private float zoom = 1;
	private float start = 0;
	private CanvasInput input;
	private int channel;
	private Image image;

	public AudioCanvas(Composite parent, int arg1) {
		super(parent, arg1);

		setZoom(100);

		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image image = getImage();
				if (image != null) {
					e.gc.drawImage(image, 0, 0);
				}
			}
		});
	}

	protected Image getImage() {
		if (input != null) {
			if (image == null || image.getBounds().width != getBounds().width
					|| image.getBounds().height != getBounds().height) {
				image = new Image(getDisplay(), getBounds().width,
						getBounds().height);

				GC gc = new GC(image);
				Rectangle clientArea = getBounds();
				float sample = getStart();

				int w = clientArea.width;
				int h = clientArea.height - 10;

				/*
				 * float maxy = 0; for (int x = 0; x < w; x++) { Float fy =
				 * input.getSample(getChannel(), (int) sample); if (fy != null
				 * && (Math.abs(fy) > maxy)) { maxy = Math.abs(fy); } }
				 */

				for (int x = 0; x < w; x++) {
					Float fy = input.getSample(getChannel(), (int) sample);
					if (fy != null) {
						float y = fy;
						y *= h / 2;
						y += h / 2;

						gc.drawLine(x, h / 2, x, (int) y);

						sample += zoom;
						x++;
					} else {
						break;
					}
				}
			}
			return image;
		} else {
			return null;
		}
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoom(float nzoom) {
		if (zoom > 0.001) {
			this.zoom = WaazdohInfo.DEFAULT_SAMPLERATE / 3 * 100.1f / nzoom;
			reset();
		}
	}

	public void setOutput(final MOutput output) {
		this.input = new CanvasInput() {
			@Override
			public Float getSample(int channel, int sample) {
				return output.getSample(sample).fs[channel];
			}
		};
		reset();
	}

	public float getStart() {
		return start;
	}

	public void setStart(float start) {
		this.start = start;
		reset();
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
		reset();
	}

	public void setOutput(final Track track) {
		input = new CanvasInput() {
			@Override
			public Float getSample(int channel, int sample) {
				if (track != null) {
					Float f = track.getSample(sample);
					return f;
				} else {
					return null;
				}
			}
		};
		reset();
	}

	public void reset() {
		image = null;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				redraw();
			}
		});
	}

	private interface CanvasInput {

		Float getSample(int channel, int sample);

	}
}
