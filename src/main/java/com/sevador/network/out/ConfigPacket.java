package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Creates a new {@code ConfigPacket} packet.
 * @author Emperor
 *
 */
public class ConfigPacket extends OutgoingPacket {

	/**
	 * The config id.
	 */
	private final int id;
	
	/**
	 * The value to send.
	 */
	private final int value;
	
	/**
	 * Constructs a new {@code ConfigPacket} {@code Object}.
	 * @param player The player.
	 * @param id The config id.
	 * @param value The value.
	 */
	public ConfigPacket(Player player, int id, int value) {
		super(player);
		this.id = id;
		this.value = value;
	}
	
	@Override
	public OutgoingPacket get() {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			setOpcode(135);
			putInt(value);
			putLEShort(id);
		} else {
			setOpcode(123);
			put(value);
			putShort(id);
		}
		return this;
	}

}
