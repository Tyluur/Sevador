package com.sevador.game.node.npc.impl.gwd;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.npc.NPC;

/**
 * Handles a godwars dungeon minion NPC.
 * @author Emperor
 *
 */
@SuppressWarnings("serial")
public final class Minion extends NPC {

	/**
	 * Constructs a new {@code Minion} {@code Object}.
	 * @param npcId The NPC id.
	 */
	public Minion(int npcId) {
		super(npcId);
	}
	
	@Override
	public boolean isAttackable(Entity e) {
		if (e.getLocation().getRegionID() != 11347 && e.getCombatAction().getCombatType() == CombatType.MELEE) {
			e.getPlayer().getPacketSender().sendMessage("This creature is flying too high to be attacked by melee.");
			return false;
		}
		return true;
	}

}