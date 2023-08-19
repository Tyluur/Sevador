package com.sevador.network.ondemand;

import net.burtleburtle.cache.CacheManager;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.sevador.Main;

/**
 * Decodes the channel buffer to create a new js5 request.
 * 
 * @author Dementhium development team
 * @author Emperor
 *
 */
public class OnDemandDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, final Channel channel, ChannelBuffer buffer) throws Exception {
		while (buffer.readableBytes() >= 4) {
			int priority = buffer.readByte() & 0xFF;
			final int container = buffer.readByte() & 0xFF;
			final int file = buffer.readShort() & 0xFFFF;
			if (priority == 0 || priority == 1) {
				final OnDemandRequest request = new OnDemandRequest(priority == 1, container, file);
				Main.getWorkingSet().submitJs5Work(new Runnable() {
					@Override
					public void run() {
						ChannelBuffer response = CacheManager.generateFile(request.getContainer(), request.getFile(), request.isHighPriority());
						if (response != null) {
							channel.write(response);
						}
					}
				});
			}
		}
		return null;
	}

}