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
 * Handles the Enchanted onyx bolt special event.
 * @author Emperor
 *
 */
public final class LifeLeech implements TypeHandler {
	
	@Override
	public boolean init() {
		return EventManager.register(9245, this);
	}

	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		Target t = i.targets.get(0);
		t.entity.getUpdateMasks().register(new Graphic4(753, 0, 0, t.entity.isNPC()));
		double maximum = getMaximum(p);
		int hit = GaussianGen.getDamage(this, i.source, t.entity, maximum);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.RANGE, hit);
		t.damage.setMaximum((int) maximum);
		p.getSkills().heal(t.damage.getHit() / 4);
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getAccuracy(e, args) * 1.26;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getMaximum(e, args) * 1.3;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.RANGE.getHandler().getDefence(e, attackBonus, args);
	}
}