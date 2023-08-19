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
 * Handles an Air blast spell event.
 * @author Emperor
 *
 */
public final class AirBlast implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] {new Item(556, 3), new Item(560, 1)};
	
	/**
	 * The start animation.
	 */
	private static final Animation ANIMATION = new Animation(10546, 0, false, Priority.HIGH);
	
	/**
	 * The start graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(457, 0, 0, false);
	
	@Override
	public boolean init() {
		return EventManager.register(49, this);
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
		t.graphic = new Graphic(1863, 96, 0, i.victim.isNPC());
		int speed = (int) (46 + i.source.getLocation().distance(i.victim.getLocation()) * 10);
		t.projectile.add(Projectile.create(i.source, i.victim, 460, 22, 32, 60, speed, 0, 90));
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this, i.victim);
		int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(), i.source, t.entity, max, this);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
		t.damage.setMaximum((int) max);
		i.targets.add(t);
		Experience.appendMagicExp(i.source.getPlayer(), hit, getAutocastConfig());
		return true;
	}

	@Override
	public Item[] getRunes() {
		return RUNES;
	}

	@Override
	public int getStartDamage(Entity e, Entity victim) {
		return 120 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 9;
	}

	@Override
	public int getBaseDamage() {
		return 10;
	}
	
	@Override
	public int getAutocastConfig() {
		return 19;
	}

}