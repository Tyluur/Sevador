package com.sevador.game.node.model.combat.impl;

import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.combat.form.RangeData;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Skills;
import com.sevador.utility.WeaponInterface;

/**
 * Handles the range combat style.
 * 
 * @author Emperor
 * 
 */
public class RangeHandler implements TypeHandler {

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public boolean handle(Interaction i) {
		RangeData data = i.source.getRangeData(i);
		if (data == null) {
			return false;
		}
		i.start = data.getAnimation();
		i.startGraphic = data.getGraphics();
		i.ticks = 1 + (int) Math.ceil(i.source.getLocation().distance(
				i.victim.getLocation()) * 0.3);
		boolean deflect = i.victim.getPrayer().get(PrayerType.DEFLECT_MISSILES);
		Animation anim = deflect ? new Animation(12573, 0, i.victim.isNPC())
		: i.victim.getProperties().getDefenceAnimation();
		UpdateFlag graphic = deflect ? new Graphic2(2229, 0, 0,
				i.victim.isNPC()) : new Graphic(-1, 0, 0, i.victim.isNPC());
		double maximum = getMaximum(i.source);
		Target t = new Target(i.victim);
		if (t.entity.getLocation().equals(i.source.getLocation())) {
			i.source.getActionManager().register(
					new MovementAction(i.source,
							t.entity.getLocation().getX() - 1, t.entity
							.getLocation().getY(), false));
		}
		t.deflected = deflect;
		t.animation = anim;
		t.graphic = graphic;
		t.projectile.add(data.getProjectile().copy(i.source, i.victim, 5));
		int hit = GaussianGen.getDamage(this, i.source, t.entity, maximum);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.RANGE, hit);
		t.damage.setMaximum((int) maximum);
		i.targets.add(t);
		if (data.getWeapon().getType() == 2
				&& i.source.getPlayer().getEquipment()
				.get(data.getWeapon().getAmmunitionSlot()).getAmount() > 1) {
			Target target = new Target(i.victim);
			target.deflected = deflect;
			target.animation = anim;
			target.graphic = graphic;
			int speed = (int) (55 + (i.source.getLocation().distance(
					i.victim.getLocation()) * 10));
			target.projectile.add(Projectile.create(i.source, i.victim, data
					.getAmmo().getProjectile().getProjectileId(), 40, 36, 41,
					speed, 25));
			hit = GaussianGen.getDamage(this, i.source, target.entity, maximum);
			target.damage = Damage.getDamage(i.source, target.entity,
					CombatType.RANGE, hit);
			target.damage.setDelay(18);
			target.damage.setMaximum((int) maximum);
			i.targets.add(target);
		} else if (data.getWeapon().getType() == 1
				&& i.source.getRandom().nextInt(100) < 15) {
			TypeHandler spec = EventManager.getSpecialAttackEvent(data
					.getAmmo().getItemId());
			if (spec != null) {
				return spec.handle(i);
			}
		}
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		int style = 0;
		if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_ACCURATE) {
			style = 3;
		}
		int attLvl = e.getSkills().getLevel(Skills.RANGE);
		int attBonus = e.getProperties().getStats()[4];
		double attMult = 1.0 + e.getPrayer().getMod(Skills.RANGE)
				+ e.getProperties().getCursedModifiers()[Skills.RANGE];
		double accuracyMultiplier = 1.0;
		if (e.getAttribute("set-Void:range", false)) {
			accuracyMultiplier += 0.1;
		}
		double cumulativeAtt = attLvl * attMult + style;
		return (14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64))
				* accuracyMultiplier;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		int style = 0;
		if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_ACCURATE) {
			style = 3;
		}
		int strLvl = e.getSkills().getLevel(Skills.RANGE);
		int strBonus = e.getProperties().getStats()[12];
		double strMult = 1.0 + e.getPrayer().getMod(Skills.RANGE)
				+ e.getProperties().getCursedModifiers()[Skills.RANGE];
		double hitMultiplier = 1.0;
		if (e.isPlayer())
			hitMultiplier *= e.getPlayer().getAuraManager().getRangeAccurayMultiplier();
		if (e.getAttribute("set-Void:range", false)) {
			hitMultiplier += 0.1;
		}
		double cumulativeStr = strLvl * strMult + style;
		return (14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64))
				* hitMultiplier;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		int style = 0;
		if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_DEFENSIVE) {
			style = 3;
		} else if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_CONTROLLED) {
			style = 1;
		}
		int defLvl = e.getSkills().getLevel(Skills.DEFENCE);
		int defBonus = e.getProperties().getStats()[attackBonus + 5];
		double defMult = 1.0 + e.getPrayer().getMod(Skills.DEFENCE)
				+ e.getProperties().getCursedModifiers()[Skills.DEFENCE];
		double cumulativeDef = defLvl * defMult + style;
		return 14 + cumulativeDef + (defBonus / 8)
				+ ((cumulativeDef * defBonus) / 64);
	}

}
