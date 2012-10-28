package org.cp2p.impl;

public class ConnectedNodeHandler extends SimpleMessageHandler {
	private P2PServer source;

	public ConnectedNodeHandler(P2PServer server) {
		super();
		this.source = server;
	}

	@Override
	public boolean handle(MMessage childb, Node node) {
		// TODO Auto-generated method stub
		return false;
	}
}
