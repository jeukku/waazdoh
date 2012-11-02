package waazdoh.cp2p.impl;

import waazdoh.cutils.LogObjectHandler;
import waazdoh.cutils.MLogger;

public class MLoggerMessage implements LogObjectHandler {
	@Override
	public void handle(String title, Object message2, MLogger log) {
		MMessage message = (MMessage) message2;
		log.info("#MMessage:" + title + "[" + message.getID() + "(" + message.getResponseTo() +")][" + message.getName() + "][atts:" + message.getAttachments().size() + "][sentby:" + message.getSentBy() + "]" );
	}
}
