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
 * @author Tyluur<lethium@hotmail.co.uk>
 * 
 */
public class EarthStrike implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] { new Item(556, 1), new Item(557, 2), new Item(558, 1) };
	
	private static final Animation animation = new Animation(14209, 0, false,
			Priority.HIGHEST);

	private static final Graphic graphic = new Graphic(2723, 0, 0, false);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.node.model.combat.form.MagicSpell#init()
	 */
	@Override
	public boolean init() {
		return EventManager.register(30, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.node.model.combat.form.MagicSpell#handle(org.argonite
	 * .game.node.model.combat.Interaction)
	 */
	@Override
	public boolean handle(Interaction i) {
		i.start = animation;
		i.startGraphic = graphic;
		Target t = new Target(i.victim);
		if (i.victim.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
			t.animation = new Animation(12573, 0, i.victim.isNPC());
			t.graphic2 = new Graphic2(2228, 0, 0, i.victim.isNPC());
		} else {
			t.animation = i.victim.getProperties().getDefenceAnimation();
		}
		t.graphic = new Graphic(2723, 96, 0, i.victim.isNPC());
		int speed = (int) (46 + i.source.getLocation().distance(
				i.victim.getLocation()) * 10);
		t.projectile.add(Projectile.create(i.source, i.victim, 2718, 22, 32, 60,
				speed, 0, 90));
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this,
				i.victim);
		int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(),
				i.source, t.entity, max, this);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
		t.damage.setMaximum((int) max);
		i.targets.add(t);
		Experience.appendMagicExp(i.source.getPlayer(), hit, getAutocastConfig());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.node.model.combat.form.MagicSpell#getRunes()
	 */
	@Override
	public Item[] getRunes() {
		return RUNES;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.node.model.combat.form.MagicSpell#getAutocastConfig()
	 */
	@Override
	public int getAutocastConfig() {
		return 7;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.node.model.combat.form.MagicSpell#getBaseDamage()
	 */
	@Override
	public int getBaseDamage() {
		return 30;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.node.model.combat.form.MagicSpell#getNormalDamage()
	 */
	@Override
	public int getNormalDamage() {
		return 50;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.node.model.combat.form.MagicSpell#getStartDamage(org
	 * .emperial.game.node.model.Entity, org.argonite.game.node.model.Entity)
	 */
	@Override
	public int getStartDamage(Entity e, Entity victim) {
		return 80;
	}

}
