package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.utility.Misc;

/**
 * Represents the receive private message outgoing packet.
 * @author Emperor
 *
 */
public class ReceivePrivateMessage extends OutgoingPacket {

	/**
	 * The message counter used for creating the unique message id.
	 */
	private static int messageCounter = 1;
	
	/**
	 * The player name.
	 */
	private final String name;
	
	/**
	 * The message to receive.
	 */
	private final String message;
	
	/**
	 * The player rights.
	 */
	private final int rights;
	
	/**
	 * The message id.
	 */
	private final long messageId;
	
	/**
	 * Constructs a new {@code ReceivePrivateMessage} {@code Object}.
	 * @param player The player.
	 * @param name The name.
	 * @param message The message.
	 * @param rights The rights.
	 */
	public ReceivePrivateMessage(Player player, String name, String message, int rights) {
		super(player, 118, PacketType.VAR_BYTE);
		this.name = name;
		this.message = message;
		this.rights = rights;
		this.messageId = ++messageCounter;
	}
	
	@Override
	public OutgoingPacket get() {
		byte[] encryptedData = new byte[message.length() + 1];
		encryptedData[0] = (byte) message.length();
		Misc.huffmanCompress(message, encryptedData, 1);
		put(1);
		putRS2String(name);
		putRS2String(name);
		putShort(1);
		put((byte)((messageId << 16) & 0xFF));
		put((byte)((messageId << 8) & 0xFF));
		put((byte)(messageId & 0xFF));
		put((byte)rights);
		return putBytes(encryptedData);
	}

}