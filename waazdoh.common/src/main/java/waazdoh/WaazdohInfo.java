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
package waazdoh;


public class WaazdohInfo {
	public final static String version = "0.2.3";
	public static final int DEFAULT_SAMPLERATE = 44100;
	public static final float MAX_RESOLUTION = 0.001f;

	public static int getMaxWaveLength() {
		int tensec = DEFAULT_SAMPLERATE * 10;
		return tensec;
	}
}
