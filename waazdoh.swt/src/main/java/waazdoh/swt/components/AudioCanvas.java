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
import waazdoh.common.model.MProgress;
import waazdoh.common.model.WaveTrack;

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
				MProgress p = input.getProgress();
				if (p.ready()) {
					Image image = getImage();
					if (image != null) {
						e.gc.drawImage(image, 0, 0);
					}
				} else {
					e.gc.drawText("" + p, 0, 20);
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
					Float fy = getSample(sample);
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

	private Float getSample(float sample) {
		int isample = (int) sample;
		Float a = input.getSample(getChannel(), isample);

		Float ret = a;
		if (ret == null) {
			ret = 0.0f;
		}

		if (ret < 0) {
			ret = -ret;
		}

		for (int i = -5; i <= 5; i++) {
			int nisample = isample + i;
			if (nisample > 0) {
				Float nsample = input.getSample(getChannel(), nisample);
				if (nsample != null) {
					if (nsample < 0) {
						nsample = -nsample;
					}

					if (nsample > ret) {
						ret = nsample;
					}
				}
			}
		}
		return ret;
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

			@Override
			public MProgress getProgress() {
				return new MProgress();
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

	public void setOutput(final WaveTrack track) {
		input = new CanvasInput() {
			@Override
			public Float getSample(int channel, int sample) {
				if (track != null) {
					Float f = track.getViewSample(sample);
					return f;
				} else {
					return null;
				}
			}

			@Override
			public MProgress getProgress() {
				MProgress p = new MProgress();
				if (track != null) {
					track.checkProgress(p);
				}
				return p;
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

		MProgress getProgress();

	}
}
