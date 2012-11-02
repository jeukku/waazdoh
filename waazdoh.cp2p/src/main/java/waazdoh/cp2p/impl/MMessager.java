package waazdoh.cp2p.impl;

import java.util.List;

public interface MMessager {

	MMessage getMessage(String string);

	void broadcastMessage(MMessage b);

	List<MMessage> handle(List<MMessage> messages);

	Node addNode(MHost mHost, int nport);
}
