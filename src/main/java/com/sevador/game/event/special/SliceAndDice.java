package com.sevador.game.event.special;

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
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Dragon claws special attack event.
 * @author Emperor
 *
 */
public final class SliceAndDice implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 50;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(10961, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(1950, 0, 0, false);

	@Override
	public boolean init() {
		EventManager.register(14484, this);
		return EventManager.register(14486, this);
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
		int[] hits = new int[] {0, 1};
		double max = getMaximum(p);
		int hit = GaussianGen.getDamage(this, p, i.victim, max);
		if (hit > 0) {
			hits = new int[] {hit, hit / 2, (hit / 2) / 2, (hit / 2) - ((hit / 2) / 2)};
		} else {
			hit = GaussianGen.getDamage(this, p, i.victim, max);
			if (hit > 0) {
				hits = new int[] {0, hit, hit / 2, hit - (hit / 2)};
			} else {
				hit = GaussianGen.getDamage(this, p, i.victim, max);
				if (hit > 0) {
					hits = new int[] {0, 0, hit / 2, (hit / 2) + 10};
				} else {
					hit = GaussianGen.getDamage(this, p, i.victim, max * 1.5);
					if (hit > 0) {
						max *= 1.5;
						hits = new int[] {0, 0, 0, hit};
					} else {
						hits = new int[] {0, p.getRandom().nextInt(8)};
					}
				}
			}
		}
		boolean defl = i.victim.getPrayer().get(PrayerType.DEFLECT_MELEE);
		Animation anim = defl ? new Animation(12573, 0, i.victim.isNPC()) : i.victim.getProperties().getDefenceAnimation();
		UpdateFlag gfx = defl ? new Graphic2(2230, 0, 0, i.victim.isNPC()) : new Graphic(-1, 0, 0, i.victim.isNPC());
		for (int j = 0; j < hits.length; j++) {
			Target t = new Target(i.victim);
			t.deflected = defl;
			t.animation = anim;
			t.graphic = gfx;
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.MELEE, hits[j]);
			if (j > 1) {
				t.damage.setDelay(22);
			}
			t.damage.setMaximum((int) max);
			i.targets.add(t);
		}
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args) * 1.15;
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