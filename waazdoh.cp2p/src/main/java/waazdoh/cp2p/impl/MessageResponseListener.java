package waazdoh.cp2p.impl;


public interface MessageResponseListener {
	public void messageReceived(Node n, MMessage message);

	public boolean isDone();
}
