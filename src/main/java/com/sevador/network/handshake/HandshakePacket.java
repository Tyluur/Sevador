package com.sevador.network.handshake;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.sevador.network.ChannelHandler;
import com.sevador.network.IOSession;
import com.sevador.network.Message;
import com.sevador.network.OutgoingPacket;

/**
 * The packet send for the handshake event.
 * @author Dementhium development team
 * @author Emperor
 *
 */
public class HandshakePacket implements Message {

	/**
	 * The opcode.
	 */
	private final int opcode;

	/**
	 * The outgoing packet instance.
	 */
	private final OutgoingPacket message;
	
	/**
	 * Constructs a new {@code HandshakePacket} {@code Object}.
	 * @param opcode The opcode.
	 * @param buffer The packet.
	 */
	public HandshakePacket(int opcode, OutgoingPacket buffer) {
		this.opcode = opcode;
		this.message = buffer;
	}

	@Override
	public boolean execute(ChannelHandler ch, ChannelHandlerContext ctx, MessageEvent e, IOSession session) {
		/*if (opcode == HandshakeDecoder.JS5_REQUEST) {
			e.getChannel().write(message.getBuffer());
			return true;
		}*/
		e.getChannel().write(message.getBuffer());
		return true;
	}
	
	/**
	 * Gets the opcode.
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Gets the packet.
	 * @return The packet.
	 */
	public OutgoingPacket getPacket() {
		return message;
	}

}
