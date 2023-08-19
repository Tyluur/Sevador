package com.sevador.game.node.npc.impl;

import com.sevador.game.minigames.Barrows.BarrowsBrother;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.npc.NPC;
import com.sevador.utility.Priority;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class BarrowBrother extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3458858009870441242L;
	private BarrowsBrother brother;

	public BarrowBrother(BarrowsBrother brother, Location loc) {
		super(brother.getNpcId());
		this.brother = brother;
		this.setSpawnLocation(loc);
		this.setLocation(loc);
		this.setRespawns(false);
		this.init();
		switch (this.brother) {
		case DHAROK:
			setAttribute("set-Dharok", true);
			break;
		case GUTHAN:
			setAttribute("set-Guthan", true);
			break;
		case VERAC:
			setAttribute("set-Verac", true);
			break;
		case KARIL:
			this.getProperties().setAttackAnimation(new Animation(2075, 0, true, Priority.HIGHEST));
			setAttribute("set-Karil", true);
			break;
		case AHRIM:
			break;
		case TORAG:
			break;
		default:
			break;
		}
	}

	@Override
	public TypeHandler getHandler(CombatType type) {
		switch(brother) {
		case KARIL:
			return CombatType.RANGE.getHandler();
		case AHRIM:
			return CombatType.MAGIC.getHandler();
		default:
			return CombatType.MELEE.getHandler();
		}
	}
}
