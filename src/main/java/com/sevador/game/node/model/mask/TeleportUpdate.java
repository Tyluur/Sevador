package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Represents an animation update flag.
 * @author Emperor
 *
 */
public class TeleportUpdate extends UpdateFlag {

	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putByteC(127);
	}

	@Override
	public int getMaskData() {
		return 0x400;
	}

	@Override
	public int getOrdinal() {
		return 11;
	}

}
