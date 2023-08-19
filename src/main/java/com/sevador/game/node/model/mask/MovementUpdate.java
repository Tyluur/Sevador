package com.sevador.game.node.model.mask;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Represents the movement update flag.
 * @author Emperor
 *
 */
public class MovementUpdate extends UpdateFlag {

	/**
	 * The speed to move.
	 */
	private final int type;
	
	/**
	 * Constructs a new {@code MovementUpdate} {@code Object}.
	 * @param player The player.
	 */
	public MovementUpdate(Player player) {
		if (player.getWalkingQueue().getRunDir() != -1) {
			type = 2;
		} else {
			type = 1;
		}
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putByteS(type);
	}

	@Override
	public int getMaskData() {
		return 0x40;
	}

	@Override
	public int getOrdinal() {
		return 17;
	}

}