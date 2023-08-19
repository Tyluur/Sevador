package com.sevador.game.event.special;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Dragon scimitar special attack event.
 * @author Emperor
 *
 */
public final class Sever implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 55;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(12031, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(2118, 96, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(4587, this);
		return EventManager.register(13477, this);
	}

	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		p.getSettings().setSpecialEnabled(false);
		if (p.getSettings().getSpecialEnergy() < ENERGY_REQUIRED) {
			p.getIOSession().write(new MessagePacket(p, "You do not have enough special attack energy left."));
			return false;
		}
		p.getSettings().updateSpecialEnergy(ENERGY_REQUIRED);
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		i.ticks = 1;
		double max = getMaximum(p);
		Target t = new Target(i.victim);
		if (i.victim.getPrayer().get(PrayerType.DEFLECT_MELEE)) {
			t.deflected = true;
			t.animation = new Animation(12573, 0, i.victim.isNPC());
			t.graphic = new Graphic2(2230, 0, 0, i.victim.isNPC());
		} else {
			t.animation = i.victim.getProperties().getDefenceAnimation();
			t.graphic = new Graphic(-1, 0, 0, i.victim.isNPC());
		}
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MELEE, GaussianGen.getDamage(this, p, i.victim, max));
		t.damage.setMaximum((int) max);
		if (i.victim.isPlayer() && t.damage.getHit() > 0) {
			Player player = i.victim.getPlayer();
			for (int j = 16; j < 20; j++) {
				if (i.victim.getPrayer().get(PrayerType.values()[j])) {
					i.victim.getPrayer().activate(player, PrayerType.values()[j]);
				}
			}
			for (int j = 36; j < 40; j++) {
				if (i.victim.getPrayer().get(PrayerType.values()[j])) {
					i.victim.getPrayer().activate(player, PrayerType.values()[j]);
				}
			}
			i.victim.getPlayer().setAttribute("restrict protection", MajorUpdateWorker.getTicks() + 8);
		}
		i.targets.add(t);
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args) * 1.124;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getMaximum(e, args);
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.MELEE.getHandler().getDefence(e, attackBonus, args);
	}

}