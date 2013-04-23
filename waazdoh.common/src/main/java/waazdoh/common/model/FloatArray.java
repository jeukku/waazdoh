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
package waazdoh.common.model;

import waazdoh.cutils.MLogger;

public class FloatArray {
	private float fs[] = new float[10000];
	private int flength;

	public void clear() {
		// TODO Auto-generated method stub
	}

	public void add(float f) {
		checkLength(flength + 1);
		fs[flength++] = f;
	}

	public void addArray(float[] fs2, int count) {
		checkLength(flength + fs2.length);
		int index = 0;
		while (index < count) {
			fs[flength++] = fs2[index++];
		}
	}

	private synchronized void checkLength(int i) {
		if (fs == null) {
			fs = new float[10000];
		}

		int count = 0;
		while (fs.length < i) {
			try {
				i = (int) (i * 1.1 + 1000);
				float[] nfs = new float[i];
				System.arraycopy(fs, 0, nfs, 0, fs.length);
				fs = nfs;
			} catch (OutOfMemoryError e) {
				MLogger.getLogger(this).error(e);
				System.gc();
				if (count++ > 10) {
					throw (e);
				}
				try {
					this.wait(10000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public void setSamples(int isample, float[] fsample) {
		checkLength(isample + fsample.length);
		int index = 0;
		while (index < fsample.length) {
			fs[isample++] = fsample[index++];
		}
		if (isample > flength) {
			flength = isample;
		}
	}

	public Float getSample(int index) {
		if (index < fs.length) {
			return fs[index];
		} else {
			return null;
		}
	}

	public int length() {
		return flength;
	}
}
