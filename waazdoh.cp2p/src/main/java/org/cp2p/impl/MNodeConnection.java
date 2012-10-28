package org.cp2p.impl;

import java.util.Set;

import org.cutils.MID;


public interface MNodeConnection {

	void removeDownload(MID id);

	void addSourceListener(SourceListener sourceListener);

	boolean isRunning();

	MMessage getMessage(String string);

	void broadcastMessage(MMessage notification,
			MessageResponseListener messageResponseListener);

	Node getNode(MID throughtid);

	void broadcastMessage(MMessage childb,
			MessageResponseListener messageResponseListener, Set<MID> exceptions);

	MID getID();

	Download getDownload(MID streamid);

	void reportDownload(MID id, boolean ready);
}
