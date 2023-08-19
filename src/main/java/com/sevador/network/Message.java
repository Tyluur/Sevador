package com.sevador.network;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

/**
 * Represents a message received from the client.
 * @author Emperor
 *
 */
public interface Message {

	/**
	 * Handles the message.
	 * @param ch The channel handler.
	 * @param ctx The context of the channel handler.
	 * @param e The message event.
	 * @param session The IOSession Object.
	 * @return {@code True}.
	 */
	public boolean execute(ChannelHandler ch, ChannelHandlerContext ctx, MessageEvent e, IOSession session);
	
}