package com.sevador.game.event.spell;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.combat.form.MagicSpell;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.skills.Experience;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Skills;
import com.sevador.utility.Priority;

/**
 * Handles the storm of armadyl spell.
 * 
 * @author Emperor
 * 
 */
public class StormOfArmadyl implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] { new Item(21773, 1) };

	/**
	 * The start animation.
	 */
	private static final Animation ANIMATION = new Animation(10546, 0, false,
			Priority.HIGH);

	/**
	 * The start graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(457, 0, 0, false);

	@Override
	public boolean init() {
		return EventManager.register(99, this);
	}

	@Override
	public boolean handle(Interaction i) {
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		i.ticks = (int) (1 + Math.floor(i.source.getLocation().distance(
				i.victim.getLocation()) * 0.6));
		Target t = new Target(i.victim);
		if (i.victim.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
			t.animation = new Animation(12573, 0, i.victim.isNPC());
			t.graphic2 = new Graphic2(2228, 0, 0, i.victim.isNPC());
		} else {
			t.animation = i.victim.getProperties().getDefenceAnimation();
		}
		t.graphic = new Graphic(1019, 96, 0, i.victim.isNPC());
		int speed = (int) (46 + i.source.getLocation().distance(
				i.victim.getLocation()) * 13);
		t.projectile.add(Projectile.create(i.source, i.victim, 1019, 22, 32,
				56, speed, 0, 90));
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this,
				i.victim);
		int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(),
				i.source, t.entity, max, this);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
		if (t.damage.getHit() > 0) {
			int minimum = (i.source.getSkills().getStaticLevel(Skills.MAGIC) - 77) * 5;
			if (t.damage.getHit() < minimum) {
				t.damage.setHit(minimum);
			}
		}
		t.damage.setMaximum((int) max);
		i.victim.getSkills().updateLevel(Skills.DEFENCE, -1, 0);
		i.targets.add(t);
		Experience.appendMagicExp(i.source.getPlayer(), hit,
				getAutocastConfig());
		if (i.source.getPlayer().getEquipment().getItem(3).getId() == 21777) {
			i.source.getCombatAction().increaseTicks(
					(MajorUpdateWorker.getTicks() + 4)
							- i.source.getCombatAction().getTicks(), true);
		}
		return true;
	}

	@Override
	public Item[] getRunes() {
		return RUNES;
	}

	@Override
	public int getAutocastConfig() {
		return 145;
	}

	@Override
	public int getBaseDamage() {
		return 50;
	}

	@Override
	public int getNormalDamage() {
		return 50;
	}

	@Override
	public int getStartDamage(Entity e, Entity victim) {
		return 160 + ((e.getSkills().getStaticLevel(Skills.MAGIC) - 77) * 5);
	}

}