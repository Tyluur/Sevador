package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Represents the CS2String ("special string") outgoing packet.
 * @author Tyluur
 *
 */
public class CS2String extends OutgoingPacket {

	/**
	 * The child id.
	 */
	private final int id;
	
	/**
	 * The string.
	 */
	private final String string;
	
	/**
	 * Constructs a new {@code CS2String} {@code Object}.
	 * @param player The player.
	 * @param id The child id.
	 * @param string The string.
	 */
	public CS2String(Player player, int id, String string) {
		super(player, 52, PacketType.VAR_BYTE);
		this.id = id;
		this.string = string;
	}

	@Override
	public OutgoingPacket get() {
        putRS2String(string);
        return putShortA(id);
	}
}