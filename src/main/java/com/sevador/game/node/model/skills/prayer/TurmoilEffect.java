package com.sevador.game.node.model.skills.prayer;

import java.util.HashMap;
import java.util.Map;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;

/**
 * Handles the turmoil curse's effect.
 * @author Emperor
 *
 */
public class TurmoilEffect implements PrayerEffect {

	/**
	 * The cache used to determine which skill modifiers should be decreased/increased
	 */
	private final Map<Player, double[]> cache = new HashMap<Player, double[]>();
	
	@Override
	public void handle(Player source, Entity victim) {
		Entity e = source.getAttribute("turmoilEntity", null);
		if (cache.containsKey(source)) {
			if (e == victim) {
				return;
			}
			reset(source);
		}
		double strength = victim.getSkills().getStaticLevel(Skills.STRENGTH) * 0.001;
		double attack = victim.getSkills().getStaticLevel(Skills.ATTACK) * 0.0015;
		double defence = victim.getSkills().getStaticLevel(Skills.DEFENCE) * 0.0015;
		strength = strength > 0.099 ? 0.099 : strength;
		attack = attack > 0.1485 ? 0.1485 : attack;
		defence = defence > 0.1485 ? 0.1485 : defence;
		source.getProperties().updateCursedModifiers(source, Skills.STRENGTH, strength, strength);
		source.getProperties().updateCursedModifiers(source, Skills.ATTACK, attack, attack);
		source.getProperties().updateCursedModifiers(source, Skills.DEFENCE, defence, defence);
		cache.put(source, new double[] {attack, strength, defence});
		source.setAttribute("turmoilEntity", victim);
	}

	@Override
	public void reset(Player player) {
		double[] skills = cache.get(player);
		if (skills == null || skills.length < 3) {
			return;
		}
		player.getProperties().updateCursedModifiers(player, Skills.STRENGTH, -skills[1], -0.25);
		player.getProperties().updateCursedModifiers(player, Skills.ATTACK, -skills[0], -0.25);
		player.getProperties().updateCursedModifiers(player, Skills.DEFENCE, -skills[2], -0.25);
		cache.remove(player);
	}

}