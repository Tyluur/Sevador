package com.sevador.game.node.npc.impl.sum;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.impl.Familiar;

/**
 * Handles the default familiar (when the player does not have an active familiar).
 * @author Emperor
 *
 */
public class NullFamiliar extends Familiar {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 5654024450337270941L;

	/**
	 * Constructs a new {@code NullFamiliar} {@code Object}.
	 */
	public NullFamiliar() {
		super(0, -1);
	}
	
	@Override
	public void tick() {
		/*
		 * empty.
		 */
	}
	
	@Override
	public NPC init() {
		return this;
	}
	
	@Override
	public void onDeath() {
		/*
		 * empty.
		 */
	}
	
	@Override
	public void specialMove(Entity victim) {
		owner.getPacketSender().sendMessage("You do not have a familiar.");
	}
	
	@Override
	public void call() {
		owner.getPacketSender().sendMessage("You do not have a familiar to call.");
	}
	
	@Override
	public void dismiss() {
		owner.getPacketSender().sendMessage("You do not have a familiar to dismiss.");
	}

}