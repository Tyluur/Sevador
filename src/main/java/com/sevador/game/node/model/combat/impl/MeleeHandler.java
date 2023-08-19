package com.sevador.game.node.model.combat.impl;

import com.sevador.game.action.impl.packetactions.MovementAction;
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
import com.sevador.game.node.player.Skills;
import com.sevador.utility.Priority;
import com.sevador.utility.WeaponInterface;

/**
 * Handles the melee combat.
 * 
 * @author Emperor
 * 
 */
public class MeleeHandler implements TypeHandler {

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public boolean handle(Interaction i) {
		i.start = new Animation(i.source.getProperties().getAttackAnimation().getId(), 0, i.source.isNPC(), Priority.HIGHEST);
		i.startGraphic = new Graphic(-1, 0, 0, i.source.isNPC());
		i.ticks = 1;
		boolean guthanEffect = i.source.getAttribute("set-Guthan", false)
				&& i.source.getRandom().nextInt(50) < 15;
		Target t = new Target(i.victim);
		if (t.entity.getLocation().equals(i.source.getLocation())) {
			i.source.getActionManager().register(
					new MovementAction(i.source,
							t.entity.getLocation().getX() - 1, t.entity
							.getLocation().getY(), false));
		}
		if (t.entity.getPrayer().isCurses()
				&& t.entity.getPrayer().get(PrayerType.DEFLECT_MELEE)) {
			t.deflected = true;
			t.animation = new Animation(12573, 0, t.entity.isNPC());
			t.graphic2 = new Graphic2(2230, 0, 0, t.entity.isNPC());
		} else {
			t.animation = t.entity.getProperties().getDefenceAnimation();
		}
		t.graphic = new Graphic(guthanEffect ? 398 : -1, 0, 0, t.entity.isNPC());
		double maximum = getMaximum(i.source);
		int hit = GaussianGen.getDamage(this, i.source, t.entity, maximum);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MELEE, hit);
		t.damage.setMaximum((int) maximum);
		i.targets.add(t);
		if (guthanEffect && t.damage.getHit() > 0) {
			i.source.getSkills().heal(t.damage.getHit());
		}
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		int style = 0;
		if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_ACCURATE) {
			style = 3;
		} else if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_CONTROLLED) {
			style = 1;
		}
		int attLvl = e.getSkills().getLevel(Skills.ATTACK);
		int attBonus = e.getProperties().getStats()[e.getProperties().getAttackType()];
		double accuracyMultiplier = 1.0;
		double attMult = 1.0 + e.getPrayer().getMod(Skills.ATTACK) + e.getProperties().getCursedModifiers()[Skills.ATTACK];
		if (e.getAttribute("set-Void:melee", false)) {
			accuracyMultiplier += 0.15;
		}
		double cumulativeAtt = attLvl * attMult + style;
		return ((14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64)) * 1.2) * accuracyMultiplier;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		int style = 0;
		if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_AGGRESSIVE) {
			style = 3;
		} else if (e.getProperties().getAttackStyle() == WeaponInterface.STYLE_CONTROLLED) {
			style = 1;
		}
		int strLvl = e.getSkills().getLevel(Skills.STRENGTH);
		int strBonus = e.getProperties().getStats()[11];
		double strMult = 1.0 + e.getPrayer().getMod(Skills.STRENGTH)
				+ e.getProperties().getCursedModifiers()[Skills.STRENGTH];
		int dhp = 0;
		double dharokMod = 1.0;
		double hitMultiplier = 1.0;
		if (e.getAttribute("set-Dharok", false)) {
			dhp = e.getSkills().getMaximumLifepoints()
					- e.getSkills().getLifepoints();
			dharokMod = (dhp * 0.001) + 1;
		}
		if (e.getAttribute("set-Void:melee", false)) {
			hitMultiplier += 0.1;
		}
		double cumulativeStr = (strLvl * strMult + style) * dharokMod;
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