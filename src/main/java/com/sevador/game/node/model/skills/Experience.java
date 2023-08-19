package com.sevador.game.node.model.skills;

import java.util.HashMap;
import java.util.Map;

import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Experience {
	public static final Map<Integer, Spells> SPELLS = new HashMap<Integer, Spells>();

	public static enum Spells {

		AIR_BLAST(25.5, 19, 41), AIR_BOLT(13.5, 11, 17), AIR_SURGE(75, 47, 81), AIR_WAVE(
				36, 27, 62), AIR_STRIKE(5.5, 3, 1), BLOOD_BARRAGE(51, 91, 92), BLOOD_BLITZ(
				45, 83, 80), BLOOD_BURST(39, 75, 68), EARTH_BLAST(31.5, 23, 53), EARTH_BOLT(
				19.5, 15, 29), EARTH_SURGE(85, 51, 90), EARTH_WAVE(40, 31, 70), EARTH_STRIKE(
				9.5, 7, 9), FIRE_BLAST(34.5, 25, 59), FIRE_BOLT(22.5, 17, 35), FIRE_SURGE(
				90, 53, 95), FIRE_WAVE(42.5, 33, 75), FIRE_STRIKE(11.5, 9, 13), ICE_BLITZ(
				48, 85, 82), ICE_BARRAGE(52, 93, 94), ICE_BURST(40, 77, 70), MIASMIC_BARRAGE(
				54, 101, 97), MIASMIC_BLITZ(48, 99, 85), MIASMIC_BURST(42, 97, 73), SHADOW_BARRAGE(
				49, 89, 88), SHADOW_BLITZ(43, 81, 76), SMOKE_BARRAGE(48, 87, 86), SMOKE_BLITZ(
				42, 79, 74), STORM_OF_ARMADYL(70, 145,77), WATER_BLAST(47, 21, 47), WATER_BOLT(
				16.5, 13, 23), WATER_SURGE(80, 49, 85), WATER_WAVE(37.5, 29, 65), WATER_STRIKE(
				7.5, 5,  5);


		Spells(double xp, int autoCastConfig, int requiredLevel) {
			this.xp = xp;
			this.autoCastConfig = autoCastConfig;
			this.setRequiredLevel(requiredLevel);
		}

		private double xp;
		private int autoCastConfig;
		private int requiredLevel;

		public int getConfig() {
			return autoCastConfig;
		}

		public double getXp() {
			return xp;
		}

		/**
		 * @return the requiredLevel
		 */
		public int getRequiredLevel() {
			return requiredLevel;
		}

		/**
		 * @param requiredLevel the requiredLevel to set
		 */
		public void setRequiredLevel(int requiredLevel) {
			this.requiredLevel = requiredLevel;
		}
	}

	public static void appendMagicExp(Player player, int hit, int spellConfig) {
		for (Spells spells : Spells.values()) {
			if (spellConfig == spells.getConfig()) {
				if (hit > 0) {
					((Skills) player.getSkills()).addExperience(Skills.MAGIC,
							((int) hit * 0.4));
					((Skills) player.getSkills()).addExperience(
							Skills.HITPOINTS, (hit * 0.4) + spells.getXp());
				} else {
					((Skills) player.getSkills()).addExperience(Skills.MAGIC,
							spells.getXp());
				}
			}
		}
		((Skills) player.getSkills()).addExperience(Skills.HITPOINTS, hit * 0.133);
	}
}
