package waazdoh.cp2p.impl;



public interface MMessageHandler {
	boolean handle(MMessage childb, Node node);
	
	void setFactory(MMessageFactory factory);
}
