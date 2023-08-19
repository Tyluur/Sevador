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
import com.sevador.game.node.model.combat.form.NPCMagicFormula;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.skills.prayer.PrayerType;

/**
 * Handles the NPC magic combat.
 * @author Emperor
 *
 */
public final class NPCSpell implements MagicSpell {

	/**
	 * The magic formula to use.
	 */
	public static final NPCMagicFormula FORMULA = new NPCMagicFormula();
	
	@Override
	public boolean init() {
		return EventManager.register(-5, this);
	}

	@Override
	public boolean handle(Interaction i) {
		i.start = i.source.getProperties().getAttackAnimation();
		i.startGraphic = new Graphic(i.source.getNPC().getDefinition().getStartGraphics(), 0, 0, true);
		Target t = new Target(i.victim);
		if (i.victim.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
			t.animation = new Animation(12573, 0, i.victim.isNPC());
			t.graphic2 = new Graphic2(2228, 0, 0, i.victim.isNPC());
		} else {
			t.animation = i.victim.getProperties().getDefenceAnimation();
		}
		t.graphic = new Graphic(i.source.getNPC().getDefinition().getEndGraphics(), 96, 0, i.victim.isNPC());
		int speed = (int) (46 + i.source.getLocation().distance(i.victim.getLocation()) * 10);
		t.projectile.add(Projectile.create(i.source, i.victim, 
				i.source.getNPC().getDefinition().getProjectileId(), 30, 32, 52, speed, 3));
		double max = FORMULA.getMaximum(i.source, this, i.victim);
		int hit = GaussianGen.getDamage(FORMULA, i.source, t.entity, max, this);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
		t.damage.setMaximum((int) max);
		i.targets.add(t);
		return true;
	}

	@Override
	public Item[] getRunes() {
		return null;
	}

	@Override
	public int getAutocastConfig() {
		return 0;
	}

	@Override
	public int getBaseDamage() {
		return 0;
	}

	@Override
	public int getNormalDamage() {
		return 0;
	}

	@Override
	public int getStartDamage(Entity e, Entity victim) {
		return 0;
	}

}