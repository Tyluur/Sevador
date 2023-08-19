package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

/**
 * Handles the chat update flag.
 * @author Emperor
 *
 */
public class ForceTextUpdate extends UpdateFlag {

	/**
	 * The message to send.
	 */
	private final String message;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code ChatUpdate} {@code Object}.
	 * @param message The message.
	 */
	public ForceTextUpdate(String message, boolean npc) {
		this.message = message;
		this.npc = npc;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putRS2String(message);
	}

	@Override
	public int getMaskData() {
		return npc ? 0x80 : 0x4000;
	}

	@Override
	public int getOrdinal() {
		return npc ? 18 : 7;
	}

}