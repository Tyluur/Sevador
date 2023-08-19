package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

public class HealUpdate extends UpdateFlag {

	public HealUpdate() {
		/*
		 * TODO
		 */
	}
	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putLEShort(200);
		outgoing.putByteC(1);
		outgoing.putByteA(1);
	}
	
	@Override
	public int getMaskData() {
		return 0x8000;
	}

	@Override
	public int getOrdinal() {
		return 10;
	}

}
