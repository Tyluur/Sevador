package com.sevador.game.node.model.combat.form;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.player.Skills;

/**
 * Handles the NPC magic formula.
 * @author Emperor
 *
 */
public final class NPCMagicFormula implements TypeHandler {

	/**
	 * The singleton.
	 */
	public static final NPCMagicFormula SINGLETON = new NPCMagicFormula();

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public boolean handle(Interaction i) {
		return false;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
        int magicLevel = e.getSkills().getLevel(Skills.MAGIC) + 1;
        int magicBonus = e.getProperties().getStats()[4];
        double accuracy = (magicLevel + (magicBonus * 2)) + 45;
        return accuracy < 1 ? 1 : accuracy;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
        int mageLvl = e.getSkills().getLevel(Skills.MAGIC) + 1;
        int magicBonus = e.getProperties().getStats()[13];
        return 14 + mageLvl + (magicBonus / 8) + ((mageLvl * magicBonus) / 64);
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.MAGIC.getHandler().getDefence(e, attackBonus, args);
	}

}