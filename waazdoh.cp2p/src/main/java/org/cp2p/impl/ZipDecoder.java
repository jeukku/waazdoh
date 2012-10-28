package org.cp2p.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.cutils.MLogger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;


public class ZipDecoder extends FrameDecoder {
	private MLogger log = MLogger.getLogger(this);
	private ByteArrayOutputStream baos;
	private int expectedlength = -1;
	
	@Override
	protected Object decode(ChannelHandlerContext arg0, Channel arg1,
			ChannelBuffer cb) throws Exception {
		if (baos == null) {
			expectedlength = cb.readInt();
			baos = new ByteArrayOutputStream();
		}
		int toread = cb.readableBytes();
		if (toread + baos.size() > expectedlength) {
			toread = expectedlength - baos.size();
		}
		//
		cb.readBytes(baos, toread);
		if (baos.size() >= expectedlength) {
			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(
					baos.toByteArray()));
			baos = null;
			zis.getNextEntry();
			// log.info("zipdecoder entry " + entry + " context:" + arg0 +
			// " channel:" + arg1 + " cb:" + cb);
			//
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (true) {
				int b = zis.read();
				if (b < 0) {
					break;
				}
				baos.write(b);
			}
			byte[] bytes = baos.toByteArray();
			return parse(bytes);
		} else {
			log.info("expected " + expectedlength + " baos has:" + baos.size()
					+ " available:" + toread);
			return null;
		}
	}
	
	private List<MMessage> parse(byte[] bytes) throws IOException {
		List<MMessage> ret = new LinkedList<MMessage>();
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				bytes));
		int messagecount = dis.readInt();
		log.info("messagecount : " + messagecount);
		for (int i = 0; i < messagecount; i++) {
			MMessage m = readMessage(dis);
			ret.add(m);
		}
		return ret;
	}
	
	private MMessage readMessage(DataInputStream dis) throws IOException {
		int messagelength = dis.readInt();
		byte messagebytes[] = new byte[messagelength];
		dis.read(messagebytes);
		MMessage m = new MMessage(messagebytes);
		log.info("channelread " + m);
		return m;
	}
}
