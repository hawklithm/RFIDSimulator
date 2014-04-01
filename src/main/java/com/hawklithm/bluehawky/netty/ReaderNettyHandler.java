package com.hawklithm.bluehawky.netty;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

import com.google.gson.Gson;
import com.multiagent.hawklithm.davinci.exceptioin.MessageTransportException;

public class ReaderNettyHandler extends NettyHandler {

	private Gson gson = new Gson();
	private Channel channel;

	@Override
	public void onMessageReceived(String message, Channel channel) throws MessageTransportException {
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		channel=e.getChannel();
	}


}
