package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;
import com.sevador.utility.Misc;

/**
 * Represents the rename NPC update mask.
 * @author Emperor
 *
 */
public class RenameNPCUpdate extends UpdateFlag {

	/**
	 * The name to set.
	 */
	private final String name;
	
	/**
	 * Constructs a new {@code RenameNPCUpdate} {@code Object}.
	 * @param name The name to set.
	 */
	public RenameNPCUpdate(String name) {
		this.name = name;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putRS2String(Misc.formatPlayerNameForDisplay(name));
	}

	@Override
	public int getMaskData() {
		return 0x100000;
	}

	@Override
	public int getOrdinal() {
		return 17;
	}

}
