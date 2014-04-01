package com.hawklithm.bluehawky.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;

import com.google.gson.Gson;
import com.multiagent.hawklithm.davinci.exceptioin.MessageTransportException;

public class RFIDSender {
	
	private int port;
	private String address;
	private Channel channel;
	private NettyHandler handler;
	private Gson gson = new Gson();

	public RFIDSender(int port) {
		this.port = port;
		this.address = "127.0.0.1";
		initRPCClient();
	}

	public RFIDSender(int port, String address) {
		this.port = port;
		this.address = address;
		initRPCClient();
	}

	public void initRPCClient() {
		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("UP_FRAME_HANDLER", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
				pipeline.addLast("DOWN_FRAME_HANDLER", new LengthFieldPrepender(2, false));
				pipeline.addLast("myHandler", handler);
				return pipeline;
			}
		});
		bootstrap.connect(new InetSocketAddress(address, port));
	}

	public void sendMessage(String msg) throws MessageTransportException {
		handler.sendMessage(msg, channel);
	}

	public void sendRFIDData(String msg) throws MessageTransportException {
//		String message = gson.toJson(msg);
		System.out.println(msg);
//		handler.sendMessage(message, channel);
	}

	public NettyHandler getHandler() {
		return handler;
	}

	public void setHandler(NettyHandler handler) {
		this.handler = handler;
	}
}
