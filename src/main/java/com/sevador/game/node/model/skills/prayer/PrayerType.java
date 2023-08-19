package com.sevador.game.node.model.skills.prayer;

import com.sevador.game.node.player.Skills;

/**
 * Represents the prayer types.
 * @author Emperor
 *
 */
public enum PrayerType {

	/**
	 * The thick skin prayer.
	 */
	THICK_SKIN(1, 0.5, Skills.DEFENCE, 5),

	/**
	 * The burst of strength prayer.
	 */
	BURST_OF_STRENGTH(4, 0.5, Skills.STRENGTH, 5),

	/**
	 * The clarity of thought prayer.
	 */
	CLARITY_OF_THOUGHT(7, 0.5, Skills.ATTACK, 5),

	/**
	 * The sharp eye prayer.
	 */
	SHARP_EYE(8, 0.5, Skills.RANGE, 5),

	/**
	 * The mystic will prayer.
	 */
	MYSTIC_WILL(9, 0.5, Skills.MAGIC, 5),

	/**
	 * The rock skin prayer.
	 */
	ROCK_SKIN(10, 1.0, Skills.DEFENCE, 10),

	/**
	 * The superhuman strength prayer.
	 */
	SUPERHUMAN_STRENGTH(13, 1.0, Skills.STRENGTH, 10),

	/**
	 * The improved reflexes prayer.
	 */
	IMPROVED_REFLEXES(16, 1.0, Skills.ATTACK, 10),

	/**
	 * The rapid restore prayer.
	 */
	RAPID_RESTORE(19, 0.1666666666666667),

	/**
	 * The rapid heal prayer.
	 */
	RAPID_HEAL(22, 0.3333333333333333),

	/**
	 * The protect item prayer.
	 */
	PROTECT_ITEM(25, 0.3333333333333333),

	/**
	 * The hawk eye prayer.
	 */
	HAWK_EYE(26, 1.0, Skills.RANGE, 10),

	/**
	 * The mystic lore prayer.
	 */
	MYSTIC_LORE(27, 1.0, Skills.MAGIC, 10),

	/**
	 * The steel skin prayer.
	 */
	STEEL_SKIN(28, 2.0, Skills.DEFENCE, 15),

	/**
	 * The ultimate strength prayer.
	 */
	ULTIMATE_STRENGTH(31, 2.0, Skills.STRENGTH, 15),

	/**
	 * The incredible reflexes prayer.
	 */
	INCREDIBLE_REFLEXES(34, 2.0, Skills.ATTACK, 15),

	/**
	 * The protect from summoning prayer.
	 */
	PROTECT_FROM_SUMMONING(35, 2.0, 8),
	
	/**
	 * The protect from magic prayer.
	 */
	PROTECT_FROM_MAGIC(37, 2.0, 3),

	/**
	 * The protect from missiles prayer.
	 */
	PROTECT_FROM_MISSILES(40, 2.0, 2),

	/**
	 * The protect from melee prayer.
	 */
	PROTECT_FROM_MELEE(43, 2.0, 1),

	/**
	 * The eagle eye prayer.
	 */
	EAGLE_EYE(44, 2.0, Skills.RANGE, 15),

	/**
	 * The mystic might prayer.
	 */
	MYSTIC_MIGHT(45, 2.0, Skills.MAGIC, 15),

	/**
	 * The retribution prayer.
	 */
	RETRIBUTION(46, 0.5, 4),

	/**
	 * The redemption prayer.
	 */
	REDEMPTION(49, 1.0, 6),

	/**
	 * The smite prayer.
	 */
	SMITE(52, 3.333333333333333, 5),
	
	/**
	 * The chivalry prayer.
	 */
	CHIVALRY(60, 3.333333333333333, Skills.DEFENCE, 20, Skills.STRENGTH, 18, Skills.ATTACK, 15),
	
	/**
	 * The rapid renewal prayer.
	 */
	RAPID_RENEWAL(65, 2.5),
	
	/**
	 * The piety prayer.
	 */
	PIETY(70, 4.0, Skills.DEFENCE, 25, Skills.STRENGTH, 23, Skills.ATTACK, 20),
	
	/**
	 * The rigour prayer.
	 */
	RIGOUR(74, 3.0, Skills.RANGE, 20, Skills.DEFENCE, 25),
	
	/**
	 * The augury prayer.
	 */
	AUGURY(77, 3.333333333333333, Skills.MAGIC, 20, Skills.DEFENCE, 25), 
	
	/**
	 * The protect item curse.
	 */
	PROTECT_ITEM_CURSE(50, 0.3333333333333333),
	
	/**
	 * The sap warrior curse.
	 */
	SAP_WARRIOR(50, 2.5),
	
	/**
	 * The sap ranger curse.
	 */
	SAP_RANGER(52, 2.5),
	
	/**
	 * The sap mage curse.
	 */
	SAP_MAGE(54, 2.5),
	
	/**
	 * The sap spirit curse.
	 */
	SAP_SPIRIT(56, 2.5),
	
	/**
	 * The berserker curse.
	 */
	BERSERKER(59, 0.3333333333333333),
	
	/**
	 * The deflect summoning curse.
	 */
	DEFLECT_SUMMONING(62, 2.0, 16),

	/**
	 * The deflect magic curse.
	 */
	DEFLECT_MAGIC(65, 2.0, 14),

	/**
	 * The deflect missiles curse.
	 */
	DEFLECT_MISSILES(68, 2.0, 15),
	
	/**
	 * The deflect melee curse.
	 */
	DEFLECT_MELEE(71, 2.0, 13),

	/**
	 * The leech attack curse.
	 */
	LEECH_ATTACK(74, 1.666666666666667, Skills.ATTACK, 5),

	/**
	 * The leech range curse.
	 */
	LEECH_RANGED(76, 1.666666666666667, Skills.RANGE, 5),

	/**
	 * The leech magic curse.
	 */
	LEECH_MAGIC(78, 1.666666666666667, Skills.MAGIC, 5),

	/**
	 * The leech defence curse.
	 */
	LEECH_DEFENCE(80, 1.666666666666667, Skills.DEFENCE, 5),

	/**
	 * The leech strength curse.
	 */
	LEECH_STRENGTH(82, 1.666666666666667, Skills.STRENGTH, 5),

	/**
	 * The leech energy curse.
	 */
	LEECH_ENERGY(84, 1.666666666666667),

	/**
	 * The leech special attack curse.
	 */
	LEECH_SPECIAL_ATTACK(86, 1.666666666666667),

	/**
	 * The wrath curse.
	 */
	WRATH(89, 0.5, 20),
	
	/**
	 * The soul split curse.
	 */
	SOUL_SPLIT(92, 3.0, 21),
	
	/**
	 * The turmoil curse.
	 */
	TURMOIL(95, 3.0, Skills.ATTACK, 15, Skills.STRENGTH, 23, Skills.DEFENCE, 15);
	
	/**
	 * Set the prayer types to remove on activation per prayer type.
	 */
	static {
		THICK_SKIN.remove = new PrayerType[] {ROCK_SKIN, STEEL_SKIN, CHIVALRY, PIETY, RIGOUR, AUGURY};
		BURST_OF_STRENGTH.remove = new PrayerType[] {SHARP_EYE, MYSTIC_WILL, SUPERHUMAN_STRENGTH, HAWK_EYE, MYSTIC_LORE, ULTIMATE_STRENGTH, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		CLARITY_OF_THOUGHT.remove = new PrayerType[] {SHARP_EYE, MYSTIC_WILL, IMPROVED_REFLEXES, HAWK_EYE, MYSTIC_LORE, INCREDIBLE_REFLEXES, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		SHARP_EYE.remove = new PrayerType[] {BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, MYSTIC_WILL, IMPROVED_REFLEXES, SUPERHUMAN_STRENGTH, HAWK_EYE, MYSTIC_LORE, INCREDIBLE_REFLEXES, ULTIMATE_STRENGTH, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		MYSTIC_WILL.remove = new PrayerType[] {BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, IMPROVED_REFLEXES, SUPERHUMAN_STRENGTH, HAWK_EYE, MYSTIC_LORE, INCREDIBLE_REFLEXES, ULTIMATE_STRENGTH, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		ROCK_SKIN.remove = new PrayerType[] {THICK_SKIN, STEEL_SKIN, CHIVALRY, PIETY, RIGOUR, AUGURY};
		SUPERHUMAN_STRENGTH.remove = new PrayerType[] {BURST_OF_STRENGTH, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, ULTIMATE_STRENGTH, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		IMPROVED_REFLEXES.remove = new PrayerType[] {CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, HAWK_EYE, MYSTIC_LORE, INCREDIBLE_REFLEXES, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		RAPID_RESTORE.remove = new PrayerType[0];
		RAPID_HEAL.remove = new PrayerType[] {RAPID_RENEWAL};
		PROTECT_ITEM.remove = new PrayerType[0];
		HAWK_EYE.remove = new PrayerType[] {BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, IMPROVED_REFLEXES, SUPERHUMAN_STRENGTH, MYSTIC_LORE, INCREDIBLE_REFLEXES, ULTIMATE_STRENGTH, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		MYSTIC_LORE.remove = new PrayerType[] {BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, IMPROVED_REFLEXES, SUPERHUMAN_STRENGTH, HAWK_EYE, INCREDIBLE_REFLEXES, ULTIMATE_STRENGTH, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		STEEL_SKIN.remove = new PrayerType[] {THICK_SKIN, ROCK_SKIN, CHIVALRY, PIETY, RIGOUR, AUGURY};
		ULTIMATE_STRENGTH.remove = new PrayerType[] {BURST_OF_STRENGTH, SHARP_EYE, MYSTIC_WILL, SUPERHUMAN_STRENGTH, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		INCREDIBLE_REFLEXES.remove = new PrayerType[] {CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, IMPROVED_REFLEXES, HAWK_EYE, MYSTIC_LORE, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		PROTECT_FROM_SUMMONING.remove = new PrayerType[] {RETRIBUTION, REDEMPTION, SMITE};
		PROTECT_FROM_MAGIC.remove = new PrayerType[] {PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION, SMITE};
		PROTECT_FROM_MISSILES.remove = new PrayerType[] {PROTECT_FROM_MAGIC, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION, SMITE};
		PROTECT_FROM_MELEE.remove = new PrayerType[] {PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, RETRIBUTION, REDEMPTION, SMITE};
		EAGLE_EYE.remove = new PrayerType[] {BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, IMPROVED_REFLEXES, SUPERHUMAN_STRENGTH, HAWK_EYE, MYSTIC_LORE, INCREDIBLE_REFLEXES, ULTIMATE_STRENGTH, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR, AUGURY};
		MYSTIC_MIGHT.remove = new PrayerType[] {BURST_OF_STRENGTH, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, IMPROVED_REFLEXES, SUPERHUMAN_STRENGTH, HAWK_EYE, MYSTIC_LORE, INCREDIBLE_REFLEXES, ULTIMATE_STRENGTH, EAGLE_EYE, CHIVALRY, PIETY, RIGOUR, AUGURY};
		RETRIBUTION.remove = new PrayerType[] {PROTECT_FROM_SUMMONING, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, REDEMPTION, SMITE};
		REDEMPTION.remove = new PrayerType[] {PROTECT_FROM_SUMMONING, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, RETRIBUTION, SMITE};
		SMITE.remove = new PrayerType[] {PROTECT_FROM_SUMMONING, PROTECT_FROM_MAGIC, PROTECT_FROM_MISSILES, PROTECT_FROM_MELEE, RETRIBUTION, REDEMPTION};
		CHIVALRY.remove = new PrayerType[] {THICK_SKIN, THICK_SKIN, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, ROCK_SKIN, SUPERHUMAN_STRENGTH, IMPROVED_REFLEXES, HAWK_EYE, MYSTIC_LORE, STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES, EAGLE_EYE, MYSTIC_MIGHT, PIETY, RIGOUR, AUGURY};
		RAPID_RENEWAL.remove = new PrayerType[] {RAPID_HEAL};
		PIETY.remove = new PrayerType[] {THICK_SKIN, THICK_SKIN, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, ROCK_SKIN, SUPERHUMAN_STRENGTH, IMPROVED_REFLEXES, HAWK_EYE, MYSTIC_LORE, STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, RIGOUR, AUGURY};
		RIGOUR.remove = new PrayerType[] {THICK_SKIN, THICK_SKIN, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, ROCK_SKIN, SUPERHUMAN_STRENGTH, IMPROVED_REFLEXES, HAWK_EYE, MYSTIC_LORE, STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, AUGURY};
		AUGURY.remove = new PrayerType[] {THICK_SKIN, THICK_SKIN, CLARITY_OF_THOUGHT, SHARP_EYE, MYSTIC_WILL, ROCK_SKIN, SUPERHUMAN_STRENGTH, IMPROVED_REFLEXES, HAWK_EYE, MYSTIC_LORE, STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES, EAGLE_EYE, MYSTIC_MIGHT, CHIVALRY, PIETY, RIGOUR};
		PROTECT_ITEM_CURSE.remove = new PrayerType[0];
		PrayerType[] pt = new PrayerType[] { LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY, LEECH_SPECIAL_ATTACK, TURMOIL};
		SAP_WARRIOR.remove = pt;
		SAP_RANGER.remove = pt;
		SAP_MAGE.remove = pt;
		SAP_SPIRIT.remove = pt;
		BERSERKER.remove = new PrayerType[0];
		DEFLECT_SUMMONING.remove = new PrayerType[] { WRATH, SOUL_SPLIT};
		DEFLECT_MAGIC.remove = new PrayerType[] { DEFLECT_MISSILES, DEFLECT_MELEE, WRATH, SOUL_SPLIT};
		DEFLECT_MISSILES.remove = new PrayerType[] { DEFLECT_MAGIC, DEFLECT_MELEE, WRATH, SOUL_SPLIT};
		DEFLECT_MELEE.remove = new PrayerType[] { DEFLECT_MAGIC, DEFLECT_MISSILES, WRATH, SOUL_SPLIT};
		pt = new PrayerType[] { SAP_WARRIOR, SAP_RANGER, SAP_MAGE, SAP_SPIRIT, TURMOIL};
		LEECH_ATTACK.remove = pt;
		LEECH_RANGED.remove = pt;
		LEECH_MAGIC.remove = pt;
		LEECH_DEFENCE.remove = pt;
		LEECH_STRENGTH.remove = pt;
		LEECH_ENERGY.remove = pt;
		LEECH_SPECIAL_ATTACK.remove = pt;
		WRATH.remove = new PrayerType[] { DEFLECT_SUMMONING, DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, SOUL_SPLIT};
		SOUL_SPLIT.remove = new PrayerType[] { DEFLECT_SUMMONING, DEFLECT_MAGIC, DEFLECT_MISSILES, DEFLECT_MELEE, WRATH};
		TURMOIL.remove = new PrayerType[] { SAP_WARRIOR, SAP_RANGER, SAP_MAGE, SAP_SPIRIT, LEECH_ATTACK, LEECH_RANGED, LEECH_MAGIC, LEECH_DEFENCE, LEECH_STRENGTH, LEECH_ENERGY, LEECH_SPECIAL_ATTACK};
		LEECH_ATTACK.effect = new LeechEffect(Skills.ATTACK, LEECH_ATTACK.ordinal() - 30, false);
		LEECH_RANGED.effect = new LeechEffect(Skills.RANGE, LEECH_RANGED.ordinal() - 30, false);
		LEECH_MAGIC.effect = new LeechEffect(Skills.MAGIC, LEECH_MAGIC.ordinal() - 30, false);
		LEECH_DEFENCE.effect = new LeechEffect(Skills.DEFENCE, LEECH_DEFENCE.ordinal() - 30, false);
		LEECH_STRENGTH.effect = new LeechEffect(Skills.STRENGTH, LEECH_STRENGTH.ordinal() - 30, false);
		LEECH_ENERGY.effect = new LeechEffect(0, LEECH_ENERGY.ordinal() - 30, true);
		LEECH_SPECIAL_ATTACK.effect = new LeechEffect(1, LEECH_SPECIAL_ATTACK.ordinal() - 30, true);
		TURMOIL.effect = new TurmoilEffect();
	};
	
	/**
	 * The level required.
	 */
	private final int level;
	
	/**
	 * The modifiers.
	 */
	private final int[][] modifiers;
	
	/**
	 * The headicon.
	 */
	private final int headIcon;
	
	/**
	 * The prayer types to remove when activating.
	 */
	private PrayerType[] remove;
	
	/**
	 * The prayer effect.
	 */
	private PrayerEffect effect;
	
	/**
	 * This prayer's drainrate.
	 */
	private final double drainRate;
	
	/**
	 * Constructs a new {@code PrayerType} {@code Object}.
	 * @param level The level required.
	 * @param modifiers The modifiers, or headicon.
	 */
	private PrayerType(int level, double drainRate, int... modifiers) {
		this.level = level;
		this.drainRate = drainRate;
		if (modifiers.length % 2 != 0) {
			this.headIcon = modifiers[0];
			this.modifiers = new int[0][0];
		} else {
			this.headIcon = -1;
			this.modifiers = new int[modifiers.length / 2][2];
			int slot = 0;
			for (int i = 0; i < modifiers.length; i++) {
				this.modifiers[slot++] = new int[] {modifiers[i], modifiers[++i]};
			}
		}
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the modifiers
	 */
	public int[][] getModifiers() {
		return modifiers;
	}

	/**
	 * @return the headIcon
	 */
	public int getHeadIcon() {
		return headIcon;
	}

	/**
	 * @return the remove
	 */
	public PrayerType[] getRemove() {
		return remove;
	}

	/**
	 * @return the drainRate
	 */
	public double getDrainRate() {
		return drainRate;
	}

	/**
	 * @return the effect
	 */
	public PrayerEffect getEffect() {
		return effect;
	}
}