package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Represents the secondary animation mask?
 * @author Emperor
 *
 */
public class Graphic3 extends UpdateFlag {

	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The height.
	 */
	private final int height;
	
	/**
	 * The rotation.
	 */
	private final int rotation;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic3} {@code Object}.
	 * @param id The graphic id.
	 * @param height The height.
	 * @param rotation The rotation.
	 * @param npc If the entity is an NPC.
	 */
	public Graphic3(int id, int height, int rotation, boolean npc) {
		this.id = id;
		this.height = height;
		this.rotation = rotation;
		this.npc = npc;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		if (npc) {
			outgoing.putLEShortA(id);
			outgoing.putLEInt(height << 16);
			outgoing.putByteA(rotation);
		} else {
			outgoing.putLEShort(id);
			outgoing.putInt(height << 16);
			outgoing.putByteC(rotation);
		}
	}

	@Override
	public int getMaskData() {
		return npc ? 0x10000 : 0x100000;
	}

	@Override
	public int getOrdinal() {
		return npc ? 9 : 5;
	}

}