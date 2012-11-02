package waazdoh.cp2p.impl;

public class NewNodeHandler implements MMessageHandler {
	private MMessageFactory factory;
	private P2PServer server;

	public NewNodeHandler(P2PServer p2pServer) {
		this.server = p2pServer;
	}

	@Override
	public boolean handle(MMessage childb, Node node) {
		return false;
	}
	
	@Override
	public void setFactory(MMessageFactory factory) {
		this.factory = factory;
	}
}
