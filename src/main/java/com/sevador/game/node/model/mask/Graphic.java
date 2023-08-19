package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Represents the graphic 1 update flag.
 * @author Emperor
 *
 */
public class Graphic extends UpdateFlag {

	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The graphic height.
	 */
	private final int height;
	
	/**
	 * The speed.
	 */
	private final int speed;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic} {@code Object}.
	 * @param id The graphic id.
	 * @param height The graphic height.
	 * @param speed The speed.
	 * @param npc If the entity is an NPC.
	 */
	public Graphic(int id, int height, int speed, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.npc = npc;
	}
	
	public Graphic(int i) {
		this.id = i;
		this.height = 0;
		this.speed = 0;
		this.npc = false;
	}

	@Override
	public void write(OutgoingPacket outgoing) {
		if (npc) {
			outgoing.putLEShortA(id);
			outgoing.putLEInt(height << 16 | 0 /*delay*/);
			outgoing.putByteA(speed);
		} else {
			outgoing.putLEShort(id);
			outgoing.putInt(height << 16 | 0 /*delay*/);
			outgoing.putByteS(speed << 7);
		}
	}

	@Override
	public int getMaskData() {
		return npc ? 0x1000 : 0x10000;
	}

	@Override
	public int getOrdinal() {
		return npc ? 2 : 13;
	}

	public static Graphic create(int i) {
		return new Graphic(i);
	}

}
