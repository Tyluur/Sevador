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
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Bandos godsword's special attack event.
 * @author Emperor
 *
 */
public final class Warstrike implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 100;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(11991, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(2114, 0, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(11696, this);
		return EventManager.register(13451, this);
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
		i.targets.add(t);
		if (t.entity.isPlayer()) {
			t.entity.getPlayer().getPacketSender().sendMessage("You have been drained.");
		}
		int left = -t.entity.getSkills().updateLevel(Skills.DEFENCE, -(t.damage.getHit() / 10), 0);
		if (left > 0) {
			left = -t.entity.getSkills().updateLevel(Skills.STRENGTH, -left, 0);
			if (left > 0) {
				left = -t.entity.getSkills().updateLevel(Skills.ATTACK, -left, 0);
				if (left > 0) {
					left = (int) -(t.entity.getSkills().getPrayerPoints() + left);
					t.entity.getSkills().updatePrayerPoints(left);
					if (left > 0) {
						left = -t.entity.getSkills().updateLevel(Skills.MAGIC, -left, 0);
						if (left > 0) {
							t.entity.getSkills().updateLevel(Skills.RANGE, -left, 0);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args) * 1.049;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getMaximum(e, args) * 1.1;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.MELEE.getHandler().getDefence(e, attackBonus, args) * 0.998;
	}

}