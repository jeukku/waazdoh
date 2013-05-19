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

import waazdoh.cutils.MID;
import waazdoh.cutils.xml.JBean;

public interface MObjectFactory {

	MWave newWave(int start, MEnvironment env);

	MWave newWave(JBean b, MEnvironment env);

	Instrument getInstrument(MID id, MEnvironment env);

}
