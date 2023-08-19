package com.sevador.game.event.special;

import com.sevador.game.action.impl.combat.HitAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.mask.Graphic4;
import com.sevador.game.node.player.Player;

/**
 * Handles the Enchanted ruby bolts special event.
 * @author Emperor
 *
 */
public final class BloodForfeit implements TypeHandler {
	
	@Override
	public boolean init() {
		return EventManager.register(9242, this);
	}

	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		if (p.getSkills().getLifepoints() <= p.getSkills().getMaximumLifepoints() / 10) {
			return true;
		}
		int recoil = p.getSkills().getLifepoints() / 10;
		p.getActionManager().register(new HitAction(p, p, new Damage(recoil).setType(DamageType.DEFLECT), i.ticks - 1));
		Target t = i.targets.get(0);
		t.entity.getUpdateMasks().register(new Graphic4(754, 0, 0, t.entity.isNPC()));
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.RANGE, (int) (t.entity.getSkills().getLifepoints() * 0.2));
		t.damage.setMaximum((int) 1);
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return 0;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return 0;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return 0;
	}
}