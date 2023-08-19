package com.sevador.network.message;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.sevador.network.IOSession;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketType;

/**
 * Decodes a received packet.
 * @author Dementhium development team
 * @author Emperor
 *
 */
public class RS2GameDecoder extends FrameDecoder {

	/**
	 * The IO session.
	 */
	private final IOSession session;

	/**
	 * If the player exists.
	 */
	private boolean playerExists;

	/**
	 * Constructs a new {@code RS2GameDecoder} {@code Object}.
	 * @param session The session.
	 */
	public RS2GameDecoder(IOSession session) {
		super(true);
		this.session = session;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readable()) {
			int opcode = buffer.readUnsignedByte();
			if (opcode < 0) {
				buffer.discardReadBytes();
				return null;
			}
			int length = PACKET_SIZES[opcode];
			if (length == -1 && buffer.readable()) {
				length = buffer.readUnsignedByte();
			}
			if (length <= buffer.readableBytes()) {
				IncomingPacket message;
				if (length < 1) {
					message = new IncomingPacket(session.getPlayer(), opcode, PacketType.STANDARD, ChannelBuffers.dynamicBuffer());
				} else {
					byte[] payload = new byte[length];
					buffer.readBytes(payload, 0, length);
					message = new IncomingPacket(session.getPlayer(), opcode, PacketType.STANDARD,
							ChannelBuffers.wrappedBuffer(payload));
				}
				if (!playerExists) {
					playerExists = true;
					return new Object[] { session, message };
				}
				return message;
			}
		}
		return null;
	}
	
	/**
	 * The packet sizes.
	 */
	public static final byte[] PACKET_SIZES = { 0, 7, -1, 8, 3, -1, 15, 8, 6,
			-1, // 1-10
			3, 8, -1, -1, 3, 4, 7, 8, 1, -1, // 11-20
			4, 2, -1, 7, 7, 8, 16, 3, 7, 3, // 21-30
			-1, -1, 4, 0, 6, -1, 6, 4, 7, 7, // 31-40
			8, 0, 15, 3, 3, 7, -1, 3, 8, 7, // 41-50
			-1, 3, 4, 18, 8, -1, 5, 11, 7, -1, // 51-60
			1, 3, -1, 4, 0, 11, 8, 2, -1, 3, 3, // 61-70
			16, 3, 2, -1, 7, 4, 2, 3, -1, -1, -1, // 71-80
			-1, 3, 8, 8, 7, 0, -1, -1, 3, 3, 4, // 81-90
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 91-100
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 101-110
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 111-120
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 121-130
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 131-140
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 141-150
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 151-160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 161-170
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 171-180
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 181-190
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 191-200
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 201-210
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 211-220
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 221-230
			0, 0, 0, 0, 0, 0, 0, 0, 0, }; // 231-240

}
