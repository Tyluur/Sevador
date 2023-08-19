package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * The interface implemented by all update flags.
 * @author Emperor
 *
 */
public abstract class UpdateFlag implements Comparable<UpdateFlag> {
	
	/**
	 * Writes the data to the outgoing packet specified.
	 * @param outgoing The outgoing packet.
	 */
	public abstract void write(OutgoingPacket outgoing);
	
	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public abstract int getMaskData();
	
	/**
	 * Gets the mask ordinal.
	 * @return The ordinal.
	 */
	public abstract int getOrdinal();
	
	@Override
	public int compareTo(UpdateFlag flag) {
		if (flag.getOrdinal() == getOrdinal()) {
			return 0;
		}
		if (flag.getOrdinal() < getOrdinal()) {
			return 1;
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof UpdateFlag)) {
			return false;
		}
		return getMaskData() == ((UpdateFlag) o).getMaskData() && getOrdinal() == ((UpdateFlag) o).getOrdinal();
	}

	/**
	 * Used to check if the update flag can be registered.
	 * @param updateMasks The update masks instance used.
	 * @return {@code True} if the update mask can be registered, {@code false} if not.
	 */
	public boolean canRegister(UpdateMasks updateMasks) {
		return true;
	}
	
}