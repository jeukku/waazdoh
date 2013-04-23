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

public class MProgress {
	private int total = 0;
	private int progress = 0;

	public void add(boolean b) {
		total++;
		if (b) {
			progress++;
		}
	}

	public boolean ready() {
		return total == 0 || total == progress;
	}

	public int getPersentage() {
		if (total == 0) {
			return 100;
		} else {
			return (int) (100.0f * progress / total);
		}
	}

	@Override
	public String toString() {
		return "Progress[" + getPersentage() + "][/" + total + "]";
	}
}
