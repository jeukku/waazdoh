package org.cp2p.impl;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;

import org.cutils.MLogger;
import org.cutils.MTimedFlag;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.utils.xml.JBean;

public class TCPNode {
	private static final long MAX_GIVEUP_TIME = 10000;

	private Channel channel;
	private MLogger log = MLogger.getLogger(this);
	private ClientBootstrap bootstrap;
	private long created = System.currentTimeMillis();

	private MTimedFlag connectionwaiter;
	private boolean offline;

	private MHost host;
	private int port;

	private Node node;

	private long lastmessage = System.currentTimeMillis();

	private boolean closed;

	public TCPNode(MHost host2, int port2, Node node) {
		this.host = host2;
		this.port = port2;
		this.node = node;
	}

	public int sendMessages(List<MMessage> smessages) {
		if (isConnected()) {
			if (smessages.size() > 0) {
				log.info("writing messages");

				channel.write(smessages).addListener(
						ChannelFutureListener.CLOSE_ON_FAILURE);
				//
				int bytecount = 0;
				for (MMessage mMessage : smessages) {
					bytecount += mMessage.getByteCount();
				}
				log.info("messages written");
				return bytecount;
			} else {
				log.info("not writing zero messages");
				return 0;
			}
		} else {
			log.info("not sending messages because not connected");
			return 0;
		}
	}

	public synchronized boolean isConnected() {
		checkConnection();
		return channel != null && channel.isConnected();
	}

	private synchronized void checkConnection() {
		// if (future != null) {
		// channel = future.getChannel();
		// }
		//
		if (!closed && !offline && channel == null
				&& (connectionwaiter == null || connectionwaiter.isTriggered())) {
			log.debug("creating connection " + this + " trigger "
					+ connectionwaiter);
			ClientBootstrap bs = getBootstrap();
			ChannelFuture future = bs.connect(new InetSocketAddress(host
					.toString(), port));
			future.awaitUninterruptibly(200);
			connectionwaiter = new MTimedFlag(10000);
		}
	}

	@Override
	public String toString() {
		return "TCPNode[" + host + ":" + port + "]["
				+ (System.currentTimeMillis() - lastmessage) + "]";
	}

	public synchronized ClientBootstrap getBootstrap() {
		if (bootstrap == null) {
			NioClientSocketChannelFactory factory = new NioClientSocketChannelFactory(
					Executors.newCachedThreadPool(),
					Executors.newCachedThreadPool());
			bootstrap = new ClientBootstrap(factory);
			bootstrap.setPipelineFactory(new NodePipelineFactory());
		}
		return bootstrap;
	}

	private class NodeHandler extends SimpleChannelHandler {
		@Override
		public void channelConnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			if (channel != null) {
				log.info("channel already exists... should close " + channel);
			}
			//
			channel = e.getChannel();
			if (!closed) {
				log.info("channel connected " + channel);
				//
				sendMessages(node.getMessages());
			} else {
				channel.close();
			}
		}

		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
				throws Exception {
			super.channelClosed(ctx, e);
			log.info("channel closed " + e);
			channel = null;
			if (shouldGiveUp()) {
				offline = true;
			}
			trigger();
		}

		@Override
		public void channelDisconnected(ChannelHandlerContext ctx,
				ChannelStateEvent e) throws Exception {
			super.channelDisconnected(ctx, e);
			log.info("channel disconnected " + e);
			channel = null;
			trigger();
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			if (!(e.getCause() instanceof ConnectException)) {
				log.info("Exception with " + host + ":" + port + " e:" + e);
				log.error(e.getCause());
			} else {
				log.debug("Connection failed " + ctx);
			}
			close();
			channel = null;
			trigger();
		}

		public void trigger() {
			if (connectionwaiter != null) {
				connectionwaiter.trigger();
			}
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
				throws Exception {
			log.info("TCPNode messageReceived " + e);
			touch();
			//
			List<MMessage> messages = (List<MMessage>) e.getMessage();
			List<MMessage> response = node.incomingMessages(messages);
			sendMessages(response);
		}

	}

	private void touch() {
		lastmessage = System.currentTimeMillis();
	}

	private class NodePipelineFactory implements ChannelPipelineFactory {
		@Override
		public ChannelPipeline getPipeline() throws Exception {
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast("zipencoder", new ZipEncoder());
			pipeline.addLast("zipdecoder", new ZipDecoder());
			pipeline.addLast("channels", new NodeHandler());
			return pipeline;
		}
	}

	public synchronized void close() {
		closed = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (channel != null) {
					log.info("closing channel " + channel);
					channel.close();
					channel = null;
					if (connectionwaiter != null) {
						connectionwaiter.trigger();
					}
				}
			}
		}, "Node closing").start();
	}

	public boolean shouldGiveUp() {
		return !isConnected()
				&& (System.currentTimeMillis() - lastmessage) > MAX_GIVEUP_TIME;
	}

	public void logChannel() {
		if (channel != null) {
			log.info("channel " + host + ":" + port + " channel " + channel
					+ " connected:" + channel.isConnected() + " writable:"
					+ channel.isWritable() + " readable:"
					+ channel.isReadable() + " open:" + channel.isOpen()
					+ " local:" + channel.getLocalAddress() + " remote:"
					+ channel.getRemoteAddress());
		} else {
			log.info("channel is null");
		}
	}

	public void addInfoTo(MMessage message) {
		if (host != null) {
			JBean nodeinfo = message.add("nodeinfo");
			JBean nodeid = nodeinfo.add(node.getID().toString());
			nodeid.add("host").setValue(host.toString());
			nodeid.add("port").setValue("" + port);
		}
	}

}
