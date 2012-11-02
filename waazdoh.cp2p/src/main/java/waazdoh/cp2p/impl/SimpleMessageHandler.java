package waazdoh.cp2p.impl;



public abstract class SimpleMessageHandler implements MMessageHandler {
	private MMessageFactory factory;
	
	public MMessageFactory getFactory() {
		return factory;
	}
	
	public void setFactory(MMessageFactory factory) {
		this.factory = factory;
	}
}
