package com.hawklithm.bluehawky.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;

import com.hawklithm.bluehawky.netty.NettyHandler;
import com.multiagent.hawklithm.davinci.exceptioin.MessageTransportException;

public class NettyClient {
	private int port;
	private String address;
	private Channel channel;
	private NettyHandler handler;

	public NettyClient(int port) {
		this.port = port;
		this.address = "127.0.0.1";
		this.handler = new NettyHandler() {

			@Override
			public void onMessageReceived(String message, Channel channel)
					throws MessageTransportException {

			}

			@Override
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
					throws Exception {
				channel = e.getChannel();
			}
		};
		initClient();
	}
	public NettyClient(int port,String address,NettyHandler handler){
		this.port=port;
		this.address=address;
		this.handler=handler;
		initClient();
	}

	public NettyClient(int port, String address) {
		this.port = port;
		this.address = address;
		this.handler = new NettyHandler() {

			@Override
			public void onMessageReceived(String message, Channel channel)
					throws MessageTransportException {

			}

			@Override
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
					throws Exception {
				channel = e.getChannel();
			}
		};
		initClient();
	}

	public void initClient() {
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
		handler.sendMessage(msg,channel);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public NettyHandler getHandler() {
		return handler;
	}

	public void setHandler(NettyHandler handler) {
		this.handler = handler;
	}

}
