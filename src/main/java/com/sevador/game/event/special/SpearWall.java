package com.sevador.game.event.special;

import java.util.List;

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
import com.sevador.game.region.RegionManager;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Vesta's spear special attack event.
 * @author Emperor
 *
 */
public final class SpearWall implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 50;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(10499, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(1835, 0, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(13905, this);
		return EventManager.register(13907, this);
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
		int amount = p.getAttribute("area:multi", false) && i.victim.getAttribute("area:multi", false) ? 8 : 0;
		i.targets.add(new Target(i.victim));
		List<? extends Entity> possibleVictims = i.victim.isNPC() ? RegionManager.getLocalNPCs(p.getLocation(), 1) : RegionManager.getLocalPlayers(p.getLocation(), 1);
		for (Entity e : possibleVictims) {
			if (e == p) {
				continue;
			}
			Target t = new Target(e);
			if (!i.targets.contains(t) && e.isAttackable(p) && --amount > -1) {
				i.targets.add(t);
			}
		}
		for (Target t : i.targets) {
			if (t.entity.getPrayer().get(PrayerType.DEFLECT_MELEE)) {
				t.deflected = true;
				t.animation = new Animation(12573, 0, t.entity.isNPC());
				t.graphic = new Graphic2(2230, 0, 0, t.entity.isNPC());
			} else {
				t.animation = t.entity.getProperties().getDefenceAnimation();
				t.graphic = new Graphic(-1, 0, 0, t.entity.isNPC());
			}
			int hit = GaussianGen.getDamage(this, p, t.entity, max);
			if (hit > 0 && t.entity != i.victim) {
				hit = (int) (max / 2);
			}
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.MELEE, hit);
			t.damage.setMaximum((int) (t.entity != i.victim ? max / 2 : max));
		}
		p.setAttribute("spearWall", MajorUpdateWorker.getTicks() + 8);
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args) * 1.132;
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
