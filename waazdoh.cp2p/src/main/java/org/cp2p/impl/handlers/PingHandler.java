package org.cp2p.impl.handlers;

import org.cp2p.impl.MMessage;
import org.cp2p.impl.MessageResponseListener;
import org.cp2p.impl.Node;
import org.cp2p.impl.SimpleMessageHandler;
import org.cutils.MLogger;

public class PingHandler extends SimpleMessageHandler {
	private MLogger log = MLogger.getLogger(this);

	@Override
	public boolean handle(MMessage childb, Node n) {
		 final long st = System.currentTimeMillis();
		//
		MMessage response = getFactory().newResponseMessage(childb,
				"pingresponse");
		final long senttime = System.currentTimeMillis();
		n.addMessage(response, new MessageResponseListener() {
			@Override
			public void messageReceived(Node n, MMessage message) {
				long responsetime = System.currentTimeMillis();
				log.info("ping in " + (responsetime - senttime));
			}

			@Override
			public boolean isDone() {
				return (System.currentTimeMillis()-st)>50000;
			}
		});
		return true;
	}
}
