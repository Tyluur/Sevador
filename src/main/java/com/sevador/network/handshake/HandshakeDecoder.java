package com.sevador.network.handshake;

import java.util.NoSuchElementException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.sevador.network.OutgoingPacket;
import com.sevador.network.login.RS2LoginDecoder;
import com.sevador.network.ondemand.OnDemandDecoder;
import com.sevador.network.ondemand.OnDemandEncoder;
import com.sevador.utility.Constants;

/**
 * Decodes the handshake message received.
 * 
 * @author Dementhium development team
 * @author Emperor
 * 
 */
public class HandshakeDecoder extends FrameDecoder {

	/**
	 * The js5-request opcode.
	 */
	public static final int JS5_REQUEST = 15;

	/**
	 * The login request opcode.
	 */
	public static final int LOGIN_REQUEST = 14;

	/**
	 * The on demand encoder to use.
	 */
	// private static final OnDemandEncoder ON_DEMAND_ENCODER = new
	// OnDemandEncoder();

	/**
	 * The on demand decoder to use.
	 */
	// private static final OnDemandDecoder ON_DEMAND_DECODER = new
	// OnDemandDecoder();

	/**
	 * The JS5-data
	 */
	public static int[] DATA = { 56, 79325, 55568, 46770, 24563, 299978, 44375,
			0, 4176, 3589, 109125, 604031, 176138, 292288, 350498, 686783,
			18008, 20836, 16339, 1244, 8142, 743, 119, 699632, 932831, 3931,
			2974, };

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		try {
			ctx.getPipeline().remove(this);
		} catch (NoSuchElementException e) {
		}
		int opcode = buffer.readByte() & 0xFF;
		OutgoingPacket response = new OutgoingPacket(null, opcode);
		try {
			switch(opcode) {
			case JS5_REQUEST:
				if (buffer.readableBytes() > 2) {
					int version = buffer.readInt();
					if(version != Constants.REVISION) {
						response.put(6);
					} else {
						for (int i = 0; i < 27; i++) {
							response.putInt(DATA[i]);
						}
						response.put(0);
						ctx.getPipeline().replace("encoder", 
								"encoder", new OnDemandEncoder());
						ctx.getPipeline().addBefore("encoder", 
								"decoder", new OnDemandDecoder());
					}
				} else {
					System.err.println("Closed the channel because of " + buffer.readableBytes() + " readable bytes; must be > 2");
					channel.close();
				}
				break;
			case LOGIN_REQUEST:
				if (ctx.getPipeline().get("decoder") == null) {
					ctx.getPipeline().addBefore("handler", 
							"decoder", new RS2LoginDecoder());
				}
				response.put(0);
				break;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return new HandshakePacket(opcode, response);
	}
}
