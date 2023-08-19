package com.sevador.game.node.npc.impl.sum;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.npc.impl.Familiar;

/**
 * Represents a combat familliar.
 * @author Emperor
 *
 */
@SuppressWarnings("serial")
public class CombatFamiliar extends Familiar {

	/**
	 * Constructs a new {@code CombatFamiliar} {@code Object}.
	 * @param id The npc id.
	 * @param ticks The amount of ticks.
	 */
	public CombatFamiliar(int id, int ticks) {
		super(id, ticks);
	}

	@Override
	public void specialMove(Entity victim) {
		setAttribute("specialMove", true);
		requestCombat(victim);
	}
	
	@Override
	public boolean isCombatFamiliar() {
		return true;
	}
	
	@Override
	public CombatFamiliar getCombatFamiliar() {
		return this;
	}
}