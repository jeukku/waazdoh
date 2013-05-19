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

import waazdoh.common.waves.SampleStream;
import waazdoh.cutils.MID;
import waazdoh.cutils.xml.JBean;

public class Instrument implements ServiceObjectData {
	private ServiceObject so;
	private SampleStream source;

	public Instrument(MEnvironment env) {
		so = new ServiceObject("instrument", env, env.getUserID(), this);
	}

	public MID getID() {
		return so.getID();
	}

	public void checkProgress(MProgress p) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean parseBean(JBean bean) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JBean getBean() {
		JBean b = so.getBean();
		return b;
	}

	public void load(MID id) {
		so.load(id);
	}

	public void setSource(SampleStream source) {
		this.source = source;
	}
}
