package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;
import com.sevador.utility.Priority;

/**
 * Represents an animation update flag.
 * @author Emperor
 *
 */
public class Animation extends UpdateFlag {

	/**
	 * The animation id.
	 */
	private final int id;

	/**
	 * The speed of the animation.
	 */
	private final int speed;

	/**
	 * If the entity is an NPC.
	 */
	private boolean npc;
	
	/**
	 * The priority.
	 */
	private Priority priority;

	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 * @param id The animation id.
	 * @param speed The speed of the animation.
	 * @param npc If the entity is an NPC.
	 */
	public Animation(int id, int speed, boolean npc) {
		this(id, speed, npc, Priority.NORMAL);
	}
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 * @param id The animation id.
	 */
	
	public Animation(int id) {
		this(id, 0, false, Priority.NORMAL);
	}
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 * @param id The animation id.
	 * @param speed The speed of the animation.
	 * @param npc If the entity is an NPC.
	 * @param priority The animation priority.
	 */
	public Animation(int id, int speed, boolean npc, Priority priority) {
		this.id = id;
		this.speed = speed;
		this.npc = npc;
		this.priority = priority;
	}

	@Override
	public void write(OutgoingPacket outgoing) {
		if (npc) {
			outgoing.putLEShortA(id);
			outgoing.putLEShortA(id);
			outgoing.putLEShortA(id);
			outgoing.putLEShortA(id);
			outgoing.putByteA(speed << 16);
		} else {
			for (int i = 0; i < 4; i++) {
				outgoing.putShortA(id);
			}
			outgoing.put(speed << 16);
		}
	}

	@Override
	public boolean canRegister(UpdateMasks updateMasks) {
		if (priority == Priority.LOWEST && updateMasks.getAnimationPriority().ordinal() > priority.ordinal()) return false;
		if (updateMasks.get(getMaskData())) {
			if (updateMasks.getAnimationPriority().ordinal() > priority.ordinal()) return false;
			updateMasks.setAnimationPriority(priority);
		}
		return true;
	}

	@Override
	public int getMaskData() {
		return npc ? 0x1 : 0x10;
	}

	@Override
	public int getOrdinal() {
		return npc ? 6 : 15;
	}

	public int getId() {
		return id;
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public boolean isNpc() {
		return npc;
	}
	
	public Priority getPriority() {
		return priority;
	}

	public static Animation create(int i) {
		return new Animation(i);
	}

}
