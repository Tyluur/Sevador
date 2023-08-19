package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.utility.Misc;

/**
 * Represents a public chat message packet.
 * @author Emperor
 *
 */
public class PublicChatMessage extends OutgoingPacket {

	/**
	 * The player index.
	 */
	private final int index;
	
	/**
	 * The player rights.
	 */
	private final int rights;
	
	/**
	 * The message.
	 */
	private final String message;
	
	/**
	 * The effects.
	 */
	private final int effects;
	
	/**
	 * Constructs a new {@code PublicChatMessage} {@code Object}.
	 * @param player The player.
	 * @param index The player index.
	 * @param rights The player rights.
	 * @param message The message.
	 * @param effects The effects.
	 */
	public PublicChatMessage(Player player, int index, int rights, String message, int effects) {
		super(player, 36, PacketType.VAR_BYTE);
		this.index = index;
		this.rights = rights;
		this.message = message;
		this.effects = effects;
	}
	
	@Override
	public OutgoingPacket get() {
        putShort(index);
        putShort(effects);
        put(rights);
        byte[] chatStr = new byte[256];
        chatStr[0] = (byte) message.length();
        byte offset = (byte)(1 + Misc.huffmanCompress(message, chatStr, 1));//.HuffmanEncode(chatStr, 0, 1, message.length(), Encoding.ASCII.GetBytes(message)));
        putBytes(chatStr, 0, offset);
		return this;
	}

}