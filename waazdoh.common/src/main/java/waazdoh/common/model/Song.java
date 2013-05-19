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

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import waazdoh.WaazdohInfo;
import waazdoh.cutils.JBeanResponse;
import waazdoh.cutils.MID;
import waazdoh.cutils.MLogger;
import waazdoh.cutils.UserID;
import waazdoh.cutils.xml.JBean;
import waazdoh.cutils.xml.XML;
import waazdoh.emodel.ETrack;

public class Song {
	private static final int MAX_WAITCOUNT = 10;
	private MID id;
	private List<TrackGroup> trackgroups = new LinkedList<TrackGroup>();
	private MLogger log = MLogger.getLogger(this);
	private String name;
	private UserID creatorid;
	private String version;
	//
	private Set<SongListener> listeners = new HashSet<SongListener>();
	private JBean storedbean;
	private MEnvironment env;
	private MOutput output;
	private MID copyof;
	private long modified, created;

	// new song
	public Song(MEnvironment env) {
		this.creatorid = env.getUserID();
		id = new MID();
		version = WaazdohInfo.version;
		created = System.currentTimeMillis();
		this.env = env;
	}

	/*
	 * public Song(JBean b, MFloatStreamSource source, CMService service) {
	 * this.streamsource = source; this.service = service; load(b); }
	 */
	public Song(MID id, MEnvironment env) {
		this.env = env;
		this.id = id;
		load(id);
	}

	private String getDetails() {
		String d = "";
		d += "MEMORY:" + getMemoryUsageInfo() + "\n";
		d += getBean();
		for (TrackGroup tg : trackgroups) {
			d += tg.getDetailInfo();
			d += ",";
		}

		return d;
	}

	public String getMemoryUsageInfo() {
		String info = "";

		info += "tgs[" + trackgroups.size() + "]";

		return info;
	}

	public boolean isOK() {
		return id != null;
	}

	private boolean load(MID id2) {
		this.id = id2;
		JBeanResponse response = env.getService().read(getID());
		log.info("got response " + response + " with id " + id);
		if (response.isSuccess()) {
			JBean songbean = response.getBean().find("song");
			return load(songbean);
		} else {
			reset();
			return false;
		}
	}

	private void reset() {
		this.id = null;
		this.name = null;
		this.creatorid = null;
		this.storedbean = null;
		this.env = null;
		listeners = null;
		trackgroups = null;
	}

	@Override
	public String toString() {
		return "Song[" + id + "][tgs:" + trackgroups.size() + "]";
	}

	private XML getAsXML() {
		JBean b = getBean();
		XML beanxml = b.toXML();
		return beanxml;
	}

	public synchronized JBean getBean() {
		JBean b = new JBean("song");
		if (id != null) {
			b.add("id").setValue(id.toString());
			b.addAttribute("name", getName());
			b.addAttribute("version", version);
			b.addAttribute("copyof", "" + copyof);
			b.addAttribute("modified", modified);
			b.addAttribute("created", created);
			//
			b.addAttribute("creator", creatorid.toString());
			//
			JBean btrackgroups = b.add("trackgroups").add("list");
			List<TrackGroup> ntracks = trackgroups;
			for (TrackGroup tg : ntracks) {
				btrackgroups.add("id").setValue(tg.getID().toString());
			}
		}
		return b;
	}

	public synchronized boolean load(Reader stringReader) {
		XML xml;
		try {
			xml = new XML(stringReader);
			return load(xml);
		} catch (IOException e) {
			log.error(e);
			return false;
		}
	}

	private boolean load(XML xml) {
		JBean bean = new JBean(xml);
		return load(bean);
	}

	private boolean load(JBean bean) {
		log.info("loading " + bean);
		//
		storedbean = bean.find("song");
		if (storedbean != null) {
			id = new MID(storedbean.get("id").getValue());
			copyof = storedbean.getIDAttribute("copyof");
			creatorid = new UserID(storedbean.getAttribute("creator"));
			version = storedbean.getAttribute("version");
			modified = storedbean.getAttributeLong("modified");
			created = storedbean.getAttributeLong("created");

			this.name = bean.getAttribute("name");
			//
			return parseGroupsTracks(bean);
		} else {
			return false;
		}
	}

	private boolean parseGroupsTracks(JBean bean) {
		List<JBean> btrackgroups = bean.get("trackgroups").get("list")
				.getChildren();
		for (JBean jBean : btrackgroups) {
			TrackGroup g = new TrackGroup(creatorid, env);
			if (g.load(new MID(jBean.getValue()))) {
				addTrackGroup(g);
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Song) {
			Song bsong = (Song) obj;
			XML asXML = getAsXML();
			XML basXML = bsong.getAsXML();
			boolean eq = asXML.equals(basXML);
			if (!eq) {
				log.info("song xml representations aren't equal");
			}
			return eq;
		} else {
			return false;
		}
	}

	private void updateVersion() {
		output = null;
		modified = System.currentTimeMillis();
		id.updateVersion();
	}

	// calls checkWave for every track
	// returning false, if some track returned false.
	public MProgress checkTracks() {
		MProgress p = new MProgress();
		//
		List<TrackGroup> ts = this.trackgroups;
		boolean ret = true;
		for (TrackGroup trackgroup : ts) {
			trackgroup.checkTracks(p);
		}
		//
		return p;
	}

	public TrackGroup addTrackGroup() {
		TrackGroup tg = new TrackGroup(creatorid, env);
		return addTrackGroup(tg);
	}

	private TrackGroup addTrackGroup(TrackGroup tg) {
		trackgroups.add(tg);
		//
		Set<SongListener> ls = listeners;
		for (SongListener songListener : ls) {
			songListener.trackGroupAdded(tg);
		}
		//
		return tg;
	}

	public synchronized MOutput getOutputWave() {
		if (output == null || hasChanged()) {
			int waitcount = 0;
			while (env.getService().isLoggedIn()) {
				MProgress p = checkTracks();
				if (p.ready()) {
					break;
				} else {
					synchronized (this) {
						try {
							waitcount++;
							if (waitcount > MAX_WAITCOUNT) {
								log.info("Not waiting for output more than "
										+ (waitcount * 300));
								return null;
							}
							this.wait(300);
						} catch (InterruptedException e) {
							log.error(e);
						}
					}
				}
			}
			//

			output = new MOutput(env);

			for (TrackGroup t : this.trackgroups) {
				FloatStream s = t.getStream();
				output.add(s);
			}

		}

		return output;
	}

	public String getName() {
		if (this.name == null) {
			name = "Song";
		}
		return this.name;
	}

	public MID getID() {
		return id;
	}

	public void addSongListener(SongListener songListener) {
		listeners.add(songListener);
	}

	public synchronized boolean publish() {
		List<TrackGroup> ts = trackgroups;
		for (TrackGroup track : ts) {
			track.save();
			track.publish();
		}
		//
		save();
		//
		return env.getService().publish(getID());
	}

	public synchronized boolean save() {
		if (!env.getUserID().equals(creatorid)) {
			copyof = getID().copy();
			id = new MID();
			creatorid = env.getUserID();
			created = System.currentTimeMillis();
		}

		List<TrackGroup> ts = trackgroups;
		for (TrackGroup track : ts) {
			track.save();
		}
		//
		boolean changed = hasChanged();
		if (!changed) {
			log.info("song has changed. Saving. " + storedbean + " current:"
					+ getBean());

			updateVersion();
			//
			MID sid = getID();
			storedbean = getBean();
			log.info("storing " + storedbean);
			JBeanResponse response = env.getService().write(sid, storedbean);
			log.info("save song got response " + response);
			//
			return response.isSuccess();
		} else {
			return false;
		}
	}

	private synchronized boolean hasChanged() {
		JBean bean = getBean();
		boolean changed = bean.equals(storedbean);
		return changed;
	}

	public synchronized List<TrackGroup> getTrackGroups() {
		return new LinkedList<TrackGroup>(trackgroups);
	}

	public synchronized void clearMemory(int time) {
		for (TrackGroup t : this.trackgroups) {
			t.clearMemory(time);
		}
		//
		if (output != null) {
			output.clearMemory(time);
		}
	}

	public ETrack newTrack() {
		return new ETrack(env);
	}

	public synchronized void setName(String string) {
		this.name = string;
		this.updateVersion();
	}

}
