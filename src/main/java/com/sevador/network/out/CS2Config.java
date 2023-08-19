package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Creates a new {@code CS2Config} {@code packet}.
 * @author Emperor
 *
 */
public class CS2Config extends OutgoingPacket {

	//TODO CONVERT TO 666!!!
	/**
	 * The config id.
	 */
	private final int id;
	
	/**
	 * The value to send.
	 */
	private final int value;
	
	/**
	 * Constructs a new {@code CS2Config} {@code Object}.
	 * @param player The player.
	 * @param id The config id.
	 * @param value The value.
	 */
	public CS2Config(Player player, int id, int value) {
		super(player);
		this.id = id;
		this.value = value;
	}
	
	@Override
	public OutgoingPacket get() {
		if (value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE) {
			setOpcode(49);
			putShortA(id);
			putByteC(value);
		} else {
			setOpcode(37);
			putInt2(value);
			putLEShort(id);
		}
		return this;
	}

}
