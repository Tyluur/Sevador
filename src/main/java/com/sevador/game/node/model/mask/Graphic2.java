package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Represents the secondary graphic update flag.
 * @author Emperor
 *
 */
public class Graphic2 extends UpdateFlag {
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The graphic delay.
	 */
	private final int delay;
	
	/**
	 * The rotation.
	 */
	private final int rotation;
	
	/**
	 * If we're writing for an npc.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic2} {@code Object}.
	 * @param id The graphic id.
	 * @param delay The graphic delay.
	 * @param rotation The rotation.
	 * @param npc If we're writing for an NPC.
	 */
	public Graphic2(int id, int delay, int rotation, boolean npc) {
		this.id = id;
		this.delay = delay;
		this.rotation = rotation;
		this.npc = npc;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		if (npc) {
			outgoing.putShort(id);
			outgoing.putInt2(delay << 16);
			outgoing.putByteS(rotation);
		} else {
			outgoing.putShort(id);
			outgoing.putInt2(delay << 16);
			outgoing.putByteC(rotation);
		}
	}

	@Override
	public int getMaskData() {
		return npc ? 0x80000 : 0x800;
	}

	@Override
	public int getOrdinal() {
		return npc ? 5 : 0;
	}

}
