package com.sevador.game.event.special;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.tick.Tick;

import com.sevador.game.action.impl.combat.HitAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionManager;
import com.sevador.game.world.World;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Korasi's sword special attack event.
 * @author Emperor
 *
 */
public final class Disrupt implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 60;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(14788, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(-1, 0, 0, false);

	@Override
	public boolean init() {
		return EventManager.register(19780, this);
	}

	@Override
	public boolean handle(final Interaction i) {
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
		final int targets = p.getAttribute("area:multi", false) && i.victim.getAttribute("area:multi", false) ? 3 : 1;
		double max = getMaximum(p) * (targets == 1 ? 1.5 : 1);
		if (targets == 1) {
			Target t = new Target(i.victim);
			if (i.victim.getPrayer().get(PrayerType.DEFLECT_MELEE)) {
				t.deflected = true;
				t.animation = new Animation(12573, 0, i.victim.isNPC());
				t.graphic = new Graphic2(2230, 0, 0, i.victim.isNPC());
			} else {
				t.animation = i.victim.getProperties().getDefenceAnimation();
				t.graphic = new Graphic(2795, 0, 0, i.victim.isNPC());
			}
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, p.getRandom().nextInt((int) max));
			t.damage.setMaximum((int) max);
			i.targets.add(t);
			return true;
		}
		final List<Target> targetList = new ArrayList<Target>();
		int hit = p.getRandom().nextInt((int) max);
		targetList.add(new Target(i.victim));		
		List<? extends Entity> possibleVictims = i.victim.isNPC() ? RegionManager.getLocalNPCs(p.getLocation(), 6) : RegionManager.getLocalPlayers(p.getLocation(), 6);
		for (Entity e : possibleVictims) {
			if (e == p) {
				continue;
			}
			Target t = new Target(e);
			if (!targetList.contains(t) && e.isAttackable(p)) {
				if (targetList.size() == 3) {
					break;
				}
				targetList.add(t);
			}
		}
		for (Target t : targetList) {
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
			t.damage.setMaximum((int) max);
			hit /= 2;
		}
		World.getWorld().submit(new Tick(1) {
			int count = 0;
			@Override
			public boolean run() {
				Target t = targetList.get(count++);
				t.entity.getUpdateMasks().register(t.entity.getProperties().getDefenceAnimation());
				t.entity.getUpdateMasks().register(new Graphic(2795, 0, 0, t.entity.isNPC()));
				t.entity.getActionManager().register(new HitAction(t.entity, i.source, t.damage, 0));
				return count == targetList.size();
			}
		});
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args);
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