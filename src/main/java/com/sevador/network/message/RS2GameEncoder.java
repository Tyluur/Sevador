package com.sevador.network.message;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.sevador.network.Packet;
import com.sevador.network.PacketType;

/**
 * Encodes a packet.
 * @author Dementhium development team
 * @author Emperor
 *
 */
public class RS2GameEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
		if (message instanceof ChannelBuffer) {
			return ChannelBuffers.copiedBuffer((ChannelBuffer) message);
		}
		Packet packetMessage = (Packet) message;
		if (!packetMessage.isRaw()) {
	        int packetLength = packetMessage.getBuffer().readableBytes() + 3;
			ChannelBuffer response = ChannelBuffers.buffer(packetLength);
	        if (packetMessage.getOpcode() > 127) {
	            response.writeByte((byte) 128);
	        }
	        response.writeByte((byte) packetMessage.getOpcode());
			if (packetMessage.getType() == PacketType.VAR_BYTE) {
				response.writeByte((byte) packetMessage.getBuffer().readableBytes());
			} else if (packetMessage.getType() == PacketType.VAR_SHORT) {
				if (packetLength > 65535) // Stack overflow.
                       throw new IllegalStateException("Could not send a packet with " + packetLength + " bytes within 16 bits.");
				response.writeByte((byte)(packetMessage.getBuffer().readableBytes() >> 8));
				response.writeByte((byte) packetMessage.getBuffer().readableBytes());
			}
			response.writeBytes(packetMessage.getBuffer());
			return response;
		}
		return packetMessage.getBuffer();
	}

}
