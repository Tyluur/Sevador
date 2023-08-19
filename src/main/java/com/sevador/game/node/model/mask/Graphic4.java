package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Represents the fourth gfx update mask.
 * @author Emperor
 *
 */
public class Graphic4 extends UpdateFlag {

	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The graphic height.
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
	 * Constructs a new {@code Graphic4} {@code Object}.
	 * @param id The graphic id.
	 * @param height The graphic id.
	 * @param rotation The rotation.
	 * @param npc If the entity is an NPC.
	 */
	public Graphic4(int id, int height, int rotation, boolean npc) {
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
			outgoing.putByteC(rotation);
		} else {
			outgoing.putLEShort(id);
			outgoing.putInt2(height << 16);
			outgoing.putByteA(rotation);
		}
	}

	@Override
	public int getMaskData() {
		return npc ? 0x4 : 0x80;
	}

	@Override
	public int getOrdinal() {
		return 12;
	}

}