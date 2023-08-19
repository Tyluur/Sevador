package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Handles the NPC combat level update mask.
 * @author Emperor
 *
 */
public class CombatLevelUpdate extends UpdateFlag {

	/**
	 * The combat level to set.
	 */
	private final int level;
	
	/**
	 * Constructs a new {@code CombatLevelUpdate} {@code Object}.
	 * @param level The combat level to set.
	 */
	public CombatLevelUpdate(int level) {
		this.level = level;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putLEShort(level);
	}

	@Override
	public int getMaskData() {
		return 0x20000;
	}

	@Override
	public int getOrdinal() {
		return 4;
	}

}
