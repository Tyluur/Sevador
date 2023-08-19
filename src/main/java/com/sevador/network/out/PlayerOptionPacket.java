package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Represents the player options outgoing packet.
 * @author Emperor
 *
 */
public class PlayerOptionPacket extends OutgoingPacket {

	/**
	 * The option text.
	 */
	private final String option;
	
	/**
	 * If it's the first option.
	 */
	private final boolean first;
	
	/**
	 * The slot.
	 */
	private final int slot;

	/**
	 * Constructs a new {@code PlayerOptionPacket} {@code Object}.
	 * @param player The player.
	 * @param option The option text.
	 * @param first If the option is the first option.
	 * @param slot The option slot.
	 */
	public PlayerOptionPacket(Player player, String option, boolean first, int slot) {
		super(player, 144, PacketType.VAR_BYTE);
		this.option = option;
		this.first = first;
		this.slot = slot;
	}
	
	@Override
	public OutgoingPacket get() {
		return putRS2String(option).putByteC(first ? 1 : 0).put(slot).putShort(100);
	}

}