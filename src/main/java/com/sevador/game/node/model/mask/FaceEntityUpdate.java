package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Represents a face entity update flag.
 * @author Emperor
 *
 */
public class FaceEntityUpdate extends UpdateFlag {

	/**
	 * If the entity using this update flag is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * The client index of the entity to face.
	 */
	private final int index;
	
	/**
	 * Constructs a new {@code FaceEntityUpdate} {@code Object}.
	 * @param index The client index of the entity to face.
	 * @param npc If the entity using this update flag is an NPC.
	 */
	public FaceEntityUpdate(int index, boolean npc) {
		this.npc = npc;
		this.index = index;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		if (npc) {
			outgoing.putLEShortA(index);
		} else {
			outgoing.putShort(index);
		}
	}

	@Override
	public int getMaskData() {
		return npc ? 0x8 : 0x4;
	}

	@Override
	public int getOrdinal() {
		return npc ? 1 : 18;
	}

}
