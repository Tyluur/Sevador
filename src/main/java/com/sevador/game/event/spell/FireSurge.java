package com.sevador.game.event.spell;

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
import com.sevador.utility.Priority;

/**
 * Handles the Fire surge spell event.
 * 
 * @author Emperor
 * 
 */
public final class FireSurge implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] { new Item(556, 7),
			new Item(554, 10), new Item(560, 1), new Item(565, 1) };

	/**
	 * The start animation.
	 */
	private static final Animation ANIMATION = new Animation(2791, 0, false,
			Priority.HIGH);

	/**
	 * The start graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(2728, 0, 0, false);

	@Override
	public boolean init() {
		return EventManager.register(91, this);
	}

	@Override
	public boolean handle(Interaction i) {
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		Target t = new Target(i.victim);
		if (i.victim.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
			t.animation = new Animation(12573, 0, i.victim.isNPC());
			t.graphic2 = new Graphic2(2228, 0, 0, i.victim.isNPC());
		} else {
			t.animation = i.victim.getProperties().getDefenceAnimation();
		}
		t.graphic = new Graphic(2741, 96, 0, i.victim.isNPC());
		int speed = (int) (46 + i.source.getLocation().distance(
				i.victim.getLocation()) * 10);
		t.projectile.add(Projectile.create(i.source, i.victim, 2735, 30, 32,
				52, speed, 3, 150));
		t.projectile.add(Projectile.create(i.source, i.victim, 2736, 30, 32,
				52, speed, 20, 150));
		t.projectile.add(Projectile.create(i.source, i.victim, 2736, 30, 32,
				52, speed, 110, 150));
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this,
				i.victim);
		int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(),
				i.source, t.entity, max, this);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
		t.damage.setMaximum((int) max);
		i.targets.add(t);
		Experience.appendMagicExp(i.source.getPlayer(), hit,
				getAutocastConfig());
		return true;
	}

	@Override
	public Item[] getRunes() {
		return RUNES;
	}

	@Override
	public int getAutocastConfig() {
		return 53;
	}

	@Override
	public int getStartDamage(Entity e, Entity victim) {
		return 200 + (2 * getBaseDamage());
	}

	@Override
	public int getNormalDamage() {
		return 21;
	}

	@Override
	public int getBaseDamage() {
		return 40;
	}
}