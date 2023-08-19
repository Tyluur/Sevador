package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Handles the sending of a blank client script packet.
 * @author Emperor
 *
 */
public final class BlankCS2Script extends OutgoingPacket {

	/**
	 * The script id.
	 */
	private final int scriptId;
	
	/**
	 * Constructs a new {@code BlankCS2Script} {@code Object}.
	 * @param player The player.
	 * @param scriptId The script id.
	 */
	public BlankCS2Script(Player player, int scriptId) {
		super(player, 23, PacketType.VAR_SHORT);
		this.scriptId = scriptId;
	}

	@Override
	public OutgoingPacket get() {
		return putShort(0).putRS2String("").putInt(scriptId);
	}
}