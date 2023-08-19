package com.sevador.game.event.special;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.mask.Graphic4;
import com.sevador.game.node.player.Player;

/**
 * Handles the Enchanted diamond bolt special event.
 * @author Emperor
 *
 */
public final class ArmourPiercing implements TypeHandler {
	
	@Override
	public boolean init() {
		return EventManager.register(9243, this);
	}

	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		Target t = i.targets.get(0);
		t.entity.getUpdateMasks().register(new Graphic4(758, 0, 0, t.entity.isNPC()));
		double maximum = CombatType.RANGE.getHandler().getMaximum(p) * 1.15;
		int hit = GaussianGen.getDamage(this, i.source, t.entity, maximum);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.RANGE, hit);
		t.damage.setMaximum((int) maximum);
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getAccuracy(e, args);
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return 0;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.RANGE.getHandler().getDefence(e, attackBonus, args) * 0.55;
	}
}