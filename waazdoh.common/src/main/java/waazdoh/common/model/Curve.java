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

import java.util.LinkedList;
import java.util.List;

import waazdoh.WaazdohInfo;
import waazdoh.cutils.xml.JBean;


public class Curve {
	private static final String TYPE_STATIC = "static";
	private String type = TYPE_STATIC;
	private List<Float> points = new LinkedList<Float>();
	private float baselevel = 1.0f;
	private String version = WaazdohInfo.version;
	
	public Curve() {
		type = TYPE_STATIC;
		points.add(1.0f);
	}

	public Curve(JBean curvebean) {
		if (curvebean.getName() != "curve") {
			curvebean = curvebean.get("curve");
		}
		type = curvebean.getAttribute("type");
		String sbaselevel = curvebean.getAttribute("baselevel");
		if (sbaselevel == null) {
			sbaselevel = "1.0";
		}
		baselevel = Float.parseFloat(sbaselevel);
		//
		points = new LinkedList<Float>();
		JBean bpoints = curvebean.get("points");
		for (JBean bpoint : bpoints.getChildren()) {
			points.add(Float.parseFloat(bpoint.getValue()));
		}
		//
		version = curvebean.getAttribute("version");
	}

	public void addTo(JBean add) {
		JBean cb = add.add("curve");
		cb.addAttribute("type", type);
		cb.addAttribute("baselevel", "" + baselevel);
		cb.addAttribute("version", version);
		//
		JBean bpoints = cb.add("points");
		for (Float point : points) {
			bpoints.add("point").setValue("" + point);
		}
	}

	public float getLevel() {
		return baselevel;
	}

	public void setLevel(float f) {
		this.baselevel = f;
	}
}
