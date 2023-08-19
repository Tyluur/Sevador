package com.sevador.network.ondemand;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * Handles the encoding of a js5 task.
 * @author Dementhium development team
 * @author Emperor
 *
 */
public class OnDemandEncoder extends OneToOneEncoder {
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, final Channel channel, final Object message) throws Exception {
		if (!(message instanceof OnDemandRequest)) {
			return message;
		}
		if (!channel.isConnected()) {
			return null;
		}
		return null;
	}
}
