package com.sevador.game.event.special;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.combat.form.RangeData;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Magic shortbow special attack event.
 * @author Emperor
 *
 */
public final class SnapShot implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 55;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(1074, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(249, 0, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(861, this);
		return EventManager.register(13528, this);
	}
	
	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		RangeData data = p.getRangeData(i);
		if (data == null) {
			return false;
		}
		p.getSettings().setSpecialEnabled(false);
		if (p.getSettings().getSpecialEnergy() < ENERGY_REQUIRED) {
			p.getIOSession().write(new MessagePacket(p, "You do not have enough special attack energy left."));
			return false;
		}
		p.getSettings().updateSpecialEnergy(ENERGY_REQUIRED);
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		i.ticks = 1 + (int) Math.ceil(i.source.getLocation().distance(i.victim.getLocation()) * 0.3);
		double max = getMaximum(p);
		for (int j = 0; j < 2; j++) {
			Target t = new Target(i.victim);
			if (i.victim.getPrayer().get(PrayerType.DEFLECT_MISSILES)) {
				t.deflected = true;
				t.animation = new Animation(12573, 0, i.victim.isNPC());
				t.graphic = new Graphic2(2229, 0, 0, i.victim.isNPC());
			} else {
				t.animation = i.victim.getProperties().getDefenceAnimation();
				t.graphic = new Graphic(-1, 0, 0, i.victim.isNPC());
			}
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.RANGE, GaussianGen.getDamage(this, p, i.victim, max));
			t.damage.setMaximum((int) max);
			i.targets.add(t);
		}
		int speed = (int) (27 + (p.getLocation().distance(i.victim.getLocation()) * 5));
		int speed2 = (int) (32 + (p.getLocation().distance(i.victim.getLocation()) * 10));
		i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 249, 40, 36, 20, speed, 15, 11));
		i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 249, 40, 36, 50, speed2, 15, 11));
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getAccuracy(e, args) * 0.95;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getMaximum(e, args);
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.RANGE.getHandler().getDefence(e, attackBonus, args) * 1.02;
	}

}