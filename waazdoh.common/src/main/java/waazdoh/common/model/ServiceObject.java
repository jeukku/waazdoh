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

import waazdoh.cutils.JBeanResponse;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;

public class ServiceObject {
	private UserID creatorid;

	private MID id = new MID();
	private long created = System.currentTimeMillis();
	private long modifytime;

	private MEnvironment env;

	private ServiceObjectData data;

	private MLogger log = MLogger.getLogger(this);

	private String name;
	private MID copyof;
	private JBean storedbean = new JBean("temp");

	private List<ServiceObjectListener> listeners = new LinkedList<ServiceObjectListener>();

	public ServiceObject(String string, MEnvironment env, UserID creatorid,
			ServiceObjectData data) {
		this.name = string;
		this.creatorid = creatorid;
		this.data = data;
		this.env = env;
		this.created = System.currentTimeMillis();
	}

	public boolean load(MID oid) {
		JBeanResponse response = env.getService().read(oid);
		if (response.isSuccess()) {
			return parseBean(response.getBean());
		} else {
			log.info("loading " + name + " bean failed " + oid);
			return false;
		}
	}

	private boolean parseBean(JBean bean) {
		creatorid = bean.getUserAttribute("creator");
		created = bean.getAttributeLong("created");
		return data.parseBean(bean);
	}

	public MEnvironment getEnvironment() {
		return env;
	}

	public MID getID() {
		return id;
	}

	public JBean getBean() {
		JBean bt = new JBean(name);
		bt.add("id").setValue(id.toString());
		bt.addAttribute("created", created);
		bt.addAttribute("modified", modifytime);
		bt.addAttribute("creator", creatorid.toString());
		return bt;
	}

	public long getModifytime() {
		return modifytime;
	}

	public void publish() {
		save();
		env.getService().publish(id);
	}

	public void save() {
		if (!env.getUserID().equals(creatorid)) {
			copyof = getID().copy();
			id = new MID();
			creatorid = env.getUserID();
		}

		if (!storedbean.equals(data.getBean())) {
			modified();
			JBean databean = data.getBean();
			storedbean = databean;
			env.getService().write(getID(), databean);
		}
	}

	void modified() {
		updateVersion();
		modifytime = System.currentTimeMillis();
		//
		LinkedList<ServiceObjectListener> ls = new LinkedList<ServiceObjectListener>(
				listeners);
		for (ServiceObjectListener trackListener : ls) {
			trackListener.modified();
		}
	}

	public void updateVersion() {
		getID().updateVersion();
	}

	public void addListener(ServiceObjectListener trackListener) {
		listeners.add(trackListener);
	}
}
