package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.network.in.ChatPacketHandler.Censoring;
import com.sevador.utility.Misc;

/**
 * Represents the send private message packet.
 * @author Emperor
 *
 */
public class SendPrivateMessage extends OutgoingPacket {

	/**
	 * The player's name.
	 */
	private final String name;
	
	/**
	 * The message.
	 */
	private final String message;
	
	/**
	 * Constructs a new {@code SendPrivateMessage} {@code Object}.
	 * @param player The player.
	 * @param name The player name.
	 * @param message The message to send.
	 */
	public SendPrivateMessage(Player player, String name, String message) {
		super(player, 127, PacketType.VAR_BYTE);
		this.name = name;
		this.message = message;
		message = Censoring.checkMessage(player, message);
	}

	@Override
	public OutgoingPacket get() {
		byte[] encryptedData = new byte[message.length() + 1];
		Misc.huffmanCompress(message, encryptedData, 0);
		putRS2String(name);
		put((byte) message.length());
		return putBytes(encryptedData);
	}
}