package com.sevador.network.login;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import com.sevador.Main;
import com.sevador.game.node.player.Player;
import com.sevador.network.IOSession;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.message.RS2GameDecoder;
import com.sevador.utility.BufferUtils;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;
import com.sevador.utility.ReturnCodes;
import com.sevador.utility.XTEA;

/**
 * The RS2LoginDecoder decodes the login packets received.
 * 
 * @author Dementhium development team
 * @author Emperor
 *
 */
public class RS2LoginDecoder extends ReplayingDecoder<LoginState> {

	/**
	 * The IO session.
	 */
	private IOSession session;

	/**
	 * Constructs a new {@code RS2LoginDecoder} {@code Object}.
	 */
	public RS2LoginDecoder() {
		checkpoint(LoginState.PRE_STAGE);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, LoginState state) throws Exception {
		if (state == LoginState.LOBBY_FINALIZATION || state == LoginState.LOGIN_FINALIZATION) {
			session = new IOSession(channel);
		}
		switch (state) {
		case PRE_STAGE:
			if (readableBytes(buffer) >= 3) {
				int loginType = buffer.readByte() & 0xFF;
				int loginPacketSize = buffer.readShort() & 0xFFFF;
				if (loginPacketSize != readableBytes(buffer)) {
					channel.close();
					System.err.println("SEVERE! Mismatched login packet size.");
					return session;
				}
				int clientVersion = buffer.readInt();
				if (clientVersion != Constants.REVISION) {
					channel.close();
					System.err.println("SEVERE! Incorrect revision read");
					return session;
				}
				if (loginType == 16 || loginType == 18) {
					checkpoint(LoginState.LOGIN_FINALIZATION);
				} else if (loginType == 19) {
					checkpoint(LoginState.LOBBY_FINALIZATION);
				} else {
					channel.close();
					System.err.println("SEVERE! Incorrect login type");
					return session;
				}
			}
			break;
		case LOBBY_FINALIZATION:
			if (buffer.readable()) {
				int rsaHeader = buffer.readByte();
				if (rsaHeader != 10) {
					channel.close();
					System.err.println("SEVERE! Invalid RSA header.");
					return session;
				}
				int[] keys = new int[4];
				for (int i = 0; i < keys.length; i++) {
					keys[i] = buffer.readInt();
				}
				buffer.readLong();
				String password = Misc.handleValidation(BufferUtils.readRS2String(buffer));
				buffer.readLong(); // client key
				buffer.readLong(); // other client key
				byte[] block = new byte[readableBytes(buffer)];
				buffer.readBytes(block);
				ChannelBuffer decryptedPayload = ChannelBuffers.wrappedBuffer(XTEA.decrypt(keys, block, 0, block.length));
				String name = BufferUtils.readRS2String(decryptedPayload).toLowerCase();
				for (char c : name.toCharArray()) {
					if (!Misc.allowed(c)) {
						session.write(new OutgoingPacket(null).put(ReturnCodes.INVALID_CREDENTIALS.getValue())).addListener(ChannelFutureListener.CLOSE);
						return null;
					}
				}
				decryptedPayload.readByte(); // screen settings?
				decryptedPayload.readByte();
				for (int i = 0; i < 24; i++) {
					decryptedPayload.readByte();
				}
				BufferUtils.readRS2String(decryptedPayload); // settings
				decryptedPayload.readInt();
				for (int i = 0; i < 34; i++) {
					decryptedPayload.readInt();
				}
				session.setInLobby(true);
				Player player = new Player(name, password);
				player.setIOSession(session);
				player.getCredentials().setLastAddress(Misc.formatIp(session.getChannel().getRemoteAddress().toString()));
				session.setPlayer(player);
				Main.getNodeWorker().offer(player);
				ctx.getPipeline().replace("decoder", "decoder", new RS2GameDecoder(session));
			}
			return session;
		case LOGIN_FINALIZATION:
			if (buffer.readable()) {
				buffer.readByte();
				int rsaHeader = buffer.readByte();
				if (rsaHeader != 10) {
					channel.close();
					System.err.println("SEVERE! Invalid RSA header.");
					return session;
				}
				int[] keys = new int[4];
				for (int i = 0; i < keys.length; i++) {
					keys[i] = buffer.readInt();
				}
				buffer.readLong();
				String password = Misc.handleValidation(BufferUtils.readRS2String(buffer));
				buffer.readLong(); // client key
				buffer.readLong(); // other client key
				byte[] block = new byte[readableBytes(buffer)];
				buffer.readBytes(block);
				ChannelBuffer decryptedPayload = ChannelBuffers.wrappedBuffer(XTEA.decrypt(keys, block, 0, block.length));
				String name = BufferUtils.readRS2String(decryptedPayload).toLowerCase();
				for (char c : name.toCharArray()) {
					if (!Misc.allowed(c)) {
						session.write(new OutgoingPacket(null).put(ReturnCodes.INVALID_CREDENTIALS.getValue())).addListener(ChannelFutureListener.CLOSE);
						return null;
					}
				}
				decryptedPayload.readByte();
				int mode = decryptedPayload.readByte();
				int width = decryptedPayload.readShort();
				int height = decryptedPayload.readShort();
				int displayMode = decryptedPayload.readByte();
				for (int i = 0; i < 24; i++) {
					decryptedPayload.readByte();
				}
				BufferUtils.readRS2String(decryptedPayload);
				decryptedPayload.readInt();
				decryptedPayload.skipBytes(decryptedPayload.readByte() & 0xff);
				session.setInLobby(false);
				Player player = new Player(name, password);
				player.setIOSession(session);
				player.getCredentials().setLastAddress(Misc.formatIp(session.getChannel().getRemoteAddress().toString()));
				session.setPlayer(player);
				Main.getNodeWorker().offer(player);
				ctx.getPipeline().replace("decoder", "decoder", new RS2GameDecoder(session));
				session.setScreenSizeMode(mode);
				session.setScreenSizeX(width);
				session.setScreenSizeY(height);
				session.setDisplayMode(displayMode);
			}
			return session;
		}
		return session;
	}

	/**
	 * Checks the readable bytes left.
	 * 
	 * @param buffer
	 *            The buffer.
	 * @return The amount of readable bytes left.
	 */
	public int readableBytes(ChannelBuffer buffer) {
		return buffer.writerIndex() - buffer.readerIndex();
	}
}
