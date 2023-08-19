package com.sevador.game.node.model.mask;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.network.OutgoingPacket;

/**
 * Handles the force movement update flag.
 * @author Emperor
 *
 */
public final class ForceMovement extends UpdateFlag {

	/**
	 * The entity.
	 */
	private final Entity entity;
	
	/**
	 * The movement data.
	 */
	private final int [] movement;
	
	/**
	 * 
	 * Constructs a new {@code ForceMovement} {@code Object}.
	 * @param entity The entity.
	 * @param movement The movement data.
	 */
	public ForceMovement(Entity entity, int[] movement) {
		this.entity = entity;
		this.movement = movement;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
        Location myLocation = entity.getLocation();
        Location fromLocation = entity.getLocation(); //Is this even needed?
        Location toLocation = Location.locate(movement[0], movement[1], 0);
        int distfromx = 0;
        int distfromy = 0;
        boolean positiveFromX = false;
        boolean positiveFromY = false;
        int distanceToX = 0;
        int distanceToY = 0;
        boolean positiveToX = false;
        boolean positiveToY = false;
        if (myLocation.getX() < fromLocation.getX()) {
            positiveFromX = true;
        }
        if (myLocation.getY() < fromLocation.getY()) {
            positiveFromY = true;
        }
        if (fromLocation.getX() < toLocation.getX()) {
            positiveToX = true;
        }
        if (fromLocation.getY() < toLocation.getY()) {
            positiveToY = true;
        }
        if (positiveFromX) {
            distfromx = fromLocation.getX() - myLocation.getX();
        } else {
            distfromx = myLocation.getX() - fromLocation.getX();
        }
        if (positiveFromY) {
            distfromy = fromLocation.getY() - myLocation.getY();
        } else {
            distfromy = myLocation.getY() - fromLocation.getY();
        }
        if (positiveToX) {
            distanceToX = toLocation.getX() - fromLocation.getX();
        } else {
            distanceToX = fromLocation.getX() - toLocation.getX();
        }
        if (positiveToY) {
            distanceToY = toLocation.getY() - fromLocation.getY();
        } else {
            distanceToY = fromLocation.getY() - toLocation.getY();
        }
        outgoing.putByteS(positiveFromX ? distfromx : -distfromx);
        outgoing.put(positiveFromY ? distfromy : -distfromy);
        outgoing.putByteS(positiveToX ? distanceToX : -distanceToX);
        outgoing.putByteS(positiveToY ? distanceToY : -distanceToY);
        outgoing.putShort(movement[2]);
        outgoing.putLEShortA(movement[3]);
        outgoing.putByteC(movement[4]);
	}

	@Override
	public int getMaskData() {
		return 0x1000;
	}

	@Override
	public int getOrdinal() {
		return 8;
	}

}