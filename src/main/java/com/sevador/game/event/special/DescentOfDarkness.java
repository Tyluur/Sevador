package com.sevador.game.event.special;

import net.burtleburtle.cache.format.ItemDefinition;

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

/**
 * Handles the Dark bow special attack event.
 * @author Emperor
 *
 */
public final class DescentOfDarkness implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 65;
	
	@Override
	public boolean init() {
		EventManager.register(11235, this);
		EventManager.register(13405, this);
		EventManager.register(15701, this);
		EventManager.register(15702, this);
		EventManager.register(15703, this);
		return EventManager.register(15704, this);
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
		i.start = data.getAnimation();
		i.startGraphic = data.getGraphics();
		i.ticks = 1 + (int) Math.ceil(i.source.getLocation().distance(i.victim.getLocation()) * 0.3);
		boolean dragonArrows = ItemDefinition.forId(data.getAmmo().getItemId()).name.contains("Dragon arrow");
		int minimum = dragonArrows ? (i.victim.isPlayer() ? 80 : 50) : (i.victim.isPlayer() ? 50 : 5);
		double max = getMaximum(p) * (dragonArrows ? 1.5 : 1.3);
		for (int j = 0; j < 2; j++) {
			Target t = new Target(i.victim);
			if (i.victim.getPrayer().get(PrayerType.DEFLECT_MISSILES)) {
				t.deflected = true;
				t.animation = new Animation(12573, 0, i.victim.isNPC());
				t.graphic2 = new Graphic2(2229, 0, 0, i.victim.isNPC());
			} else {
				t.animation = i.victim.getProperties().getDefenceAnimation();
			}
			t.graphic = new Graphic(dragonArrows ? 1100 : 1103, 96, 0, i.victim.isNPC());
			int hit = GaussianGen.getDamage(this, p, i.victim, max);
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.RANGE, hit < minimum ? minimum : hit);
			t.damage.setMaximum((int) max);
			if (j == 1) {
				t.damage.setDelay(18);
			}
			i.targets.add(t);
		}
		int speed = (int) (46 + (p.getLocation().distance(i.victim.getLocation()) * 5));
		int speed2 = (int) (55 + (p.getLocation().distance(i.victim.getLocation()) * 10));
		if (dragonArrows) {
			i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 1099, 40, 36, 41, speed, 5, 11));
			i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 1099, 40, 36, 41, speed2, 25, 11));
			return true;
		}
		i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 1101, 40, 36, 41, speed, 5, 11));
		i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 1101, 40, 36, 41, speed2, 25, 11));
		i.targets.get(0).projectile.add(data.getProjectile().copy(i.source, i.victim, 5));
		i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 
				data.getAmmo().getProjectile().getProjectileId(), 40, 36, 41, speed2, 25, 11));
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getAccuracy(e, args) * 1.2499;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getMaximum(e, args);
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.RANGE.getHandler().getDefence(e, attackBonus, args);
	}

}