package com.sevador.game.node.model.mask;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.network.OutgoingPacket;

/**
 * Represents the face location update mask.
 * @author Emperor
 *
 */
public class FaceLocationUpdate extends UpdateFlag {

	/**
	 * The location to face.
	 */
	private final Location location;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * The entity's location.
	 */
	private final Location currentLocation;
	
	/**
	 * Constructs a new {@code FaceLocationUpdate} {@code Object}.
	 * @param entity The entity facing a location.
	 * @param location The location to face.
	 */
	public FaceLocationUpdate(Entity entity, Location location) {
		if (location != null) {
			this.location = location;
		} else {
			this.location = Location.locate(0, 0, 0);
		}
		this.npc = entity.isNPC();
		this.currentLocation = entity.getLocation();
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		if (npc) {
			outgoing.putLEShortA(location.getX() << 1);
			outgoing.putLEShortA(location.getY() << 1);
		} else {
	        int dX = currentLocation.getX() - location.getX();
	        int dY = currentLocation.getY() - location.getY();
	        outgoing.putLEShortA(((int) (Math.atan2(dX, dY) * 2607.5945876176133)) & 0x3fff);
		}
	}

	@Override
	public int getMaskData() {
		return 0x20;
	}

	@Override
	public int getOrdinal() {
		return npc ? 13 : 14;
	}

}