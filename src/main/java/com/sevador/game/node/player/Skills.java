package com.sevador.game.node.player;

import java.io.Serializable;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.model.SkillSkeleton;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.SkillLevel;
import com.sevador.utility.Constants;

/**
 * Holds the player's current skill levels.
 * 
 * @author Emperor
 * 
 */
public class Skills implements Serializable, SkillSkeleton {

	/**
	 * The Serial UID.
	 */
	private static final long serialVersionUID = 215819687571101130L;

	/**
	 * The experience multiplier.
	 */
	public static final double EXPERIENCE_MULTIPLIER = 1;

	/**
	 * The skill names.
	 */
	public static final String[] SKILL_NAMES = { "Attack", "Defence",
		"Strength", "Constitution", "Ranged", "Prayer", "Magic", "Cooking",
		"Woodcutting", "Fletching", "Fishing", "Firemaking",
		"Crafting", "Smithing", "Mining", "HerbloreData", "Agility",
		"Thieving", "Slayer", "Farming", "Runecrafting", "Hunter",
		"Construction", "Summoning", "Dungeoneering" };

	/**
	 * Constants for the skill numbers.
	 */
	public static final byte ATTACK = 0;
	public static final byte DEFENCE = 1;
	public static final byte STRENGTH = 2;
	public static final byte HITPOINTS = 3;
	public static final byte RANGE = 4;
	public static final byte PRAYER = 5;
	public static final byte MAGIC = 6;
	public static final byte COOKING = 7;
	public static final byte WOODCUTTING = 8;
	public static final byte FLETCHING = 9;
	public static final byte FISHING = 10;
	public static final byte FIREMAKING = 11;
	public static final byte CRAFTING = 12;
	public static final byte SMITHING = 13;
	public static final byte MINING = 14;
	public static final byte HERBLORE = 15;
	public static final byte AGILITY = 16;
	public static final byte THIEVING = 17;
	public static final byte SLAYER = 18;
	public static final byte FARMING = 19;
	public static final byte RUNECRAFTING = 20;
	public static final byte HUNTER = 21;
	public static final byte CONSTRUCTION = 22;
	public static final byte SUMMONING = 23;
	public static final byte DUNGEONEERING = 24;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * An array containing all the player's experience.
	 */
	private final double[] experience;

	/**
	 * An array containing all the maximum levels.
	 */
	private final int[] staticLevels;

	/**
	 * An array containing all the current levels.
	 */
	private final int[] dynamicLevels;

	/**
	 * The amount of prayer points left.
	 */
	private double prayerPoints = 10.;

	/**
	 * The player's life-points.
	 */
	private int lifepoints = 100;

	/**
	 * The maximum lifepoints.
	 */
	private int maximumLifepoints = lifepoints;

	/**
	 * The amount of increased maximum lifepoints.
	 */
	private int lifepointsIncrease = 0;

	/**
	 * The total experience gained.
	 */
	private int experienceGained = 0;

	/**
	 * Constructs a new {@code Skills} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 */
	public Skills(Player player) {
		this.player = player;
		this.experience = new double[25];
		this.staticLevels = new int[25];
		this.dynamicLevels = new int[25];
		for (int i = 0; i < 25; i++) {
			this.staticLevels[i] = 1;
			this.dynamicLevels[i] = 1;
		}
		this.experience[HITPOINTS] = 1154;
		this.dynamicLevels[HITPOINTS] = 10;
		this.staticLevels[HITPOINTS] = 10;
		this.player.setCombatLevel(3);
	}

	/**
	 * Adds experience to a skill.
	 * 
	 * @param skill
	 *            The skill slot.
	 * @param experience
	 *            The experience.
	 */
	public void addExperience(int skill, double experience) {
		if (player.getCredentials().isExperienceLocked()) return;
		switch(skill) {
		case Skills.ATTACK:
		case Skills.STRENGTH:
		case Skills.DEFENCE:
		case Skills.MAGIC:
		case Skills.HITPOINTS:
		case Skills.RANGE:
			experience *= player.getSettings().getExpMode();
			break;
		default:
			experience *= Constants.EXP_MULTIPLIER;
			break;
		}
		if (player.getAuraManager().usingWisdom())
			experience *= 1.025;
		this.experience[skill] += (experience);
		if (this.experience[skill] > 200000005) {
			this.experience[skill] = 200000005;
		}
		int oldTotal = 0;
		for (int i = 0; i < 25; i++) {
			oldTotal += player.getSkills().getLevel(i);
		}	
		int oldLevel = getStaticLevelByExperience(skill);
		int newLevel = getStaticLevelByExperience(skill);
		if (oldLevel == 98 && newLevel == 99) {
			for (Player pl : NodeWorker.getPlayers()) {
				pl.getPacketSender().sendMessage("<img=6><col=FFFF00>" + player.getCredentials().getDisplayName()
						+ " has achieved level " + newLevel + " in "
						+ SKILL_NAMES[skill] + ".");
			}
		}
		int total = 0;
		for (int i = 0; i < 25; i++) {
			total += player.getSkills().getLevel(i);
		}
		if (oldTotal < 1500 && total >= 1500) {
			for (Player pl : NodeWorker.getPlayers()) {
				pl.getPacketSender().sendMessage(
						"<col=FF6000>" + player.getCredentials().getDisplayName()
						+ " has achieved a total level of 1500!");
			}
		}
		setExperienceGained(experienceGained + (int) experience);
		if (newLevel > staticLevels[skill]) {
			if (dynamicLevels[skill] < newLevel) {
				dynamicLevels[skill] += newLevel - staticLevels[skill];
			}
			if (skill == HITPOINTS) {
				lifepoints += (newLevel - staticLevels[skill]) * 10;
				maximumLifepoints = newLevel * 10;
				player.getIOSession().write(
						new ConfigPacket(player, 1240, lifepoints * 2));
			}
			staticLevels[skill] = newLevel;
			updateCombatLevel();
			LevelUp.sendLevelUp(player, skill);
		}
		player.getIOSession().write(new SkillLevel(player, skill));
	}

	/**
	 * Refreshes all the skill levels.
	 */
	public void refresh() {
		for (byte i = 0; i < 25; i++) {
			player.getIOSession().write(new SkillLevel(player, i));
		}
		player.getIOSession().write(new ConfigPacket(player, 1240, lifepoints * 2));
		player.getIOSession().write(
				new ConfigPacket(player, 2382, (int) prayerPoints));
	}

	/**
	 * Gets the static level.
	 * 
	 * @param slot
	 *            The skill's slot.
	 * @return The level.
	 */
	public int getStaticLevelByExperience(int slot) {
		double exp = experience[slot];
		int points = 0;
		int output = 0;
		for (byte lvl = 1; lvl < (slot == 24 ? 121 : 100); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return slot == 24 ? 120 : 99;
	}

	/**
	 * Gets the experience for a certain level.
	 * 
	 * @param level
	 *            The level.
	 * @return The experience needed.
	 */
	public int getExperienceByLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * Updates the combat level.
	 */
	private void updateCombatLevel() {
		player.setCombatLevel(getCombatLevel());
	}

	public int getCombatLevel() {
		int attack = staticLevels[ATTACK];
		int strength = staticLevels[STRENGTH];
		int defence = staticLevels[DEFENCE];
		int range = staticLevels[RANGE];
		int magic = staticLevels[MAGIC];
		int prayer = staticLevels[PRAYER];
		int constitution = staticLevels[HITPOINTS];
		int summoning = staticLevels[SUMMONING];
		int combatLevel = (int) (((defence + constitution
				+ Math.floor(prayer / 2) + Math.floor(summoning / 2.1)) * 0.25) + 1);
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(range * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the experience.
	 * 
	 * @param slot
	 *            The slot.
	 * @return The experience.
	 */
	public double getExperience(int slot) {
		return experience[slot];
	}

	/**
	 * Gets the static skill level.
	 * 
	 * @param slot
	 *            The slot.
	 * @return The static level.
	 */
	@Override
	public int getStaticLevel(int slot) {
		return staticLevels[slot];
	}

	/**
	 * Sets the experience gained.
	 * 
	 * @param experienceGained
	 *            The experience gained.
	 */
	public void setExperienceGained(int experienceGained) {
		this.experienceGained = experienceGained;
	}

	/**
	 * Gets the experience gained.
	 * 
	 * @return The experience gained.
	 */
	public int getExperienceGained() {
		return experienceGained;
	}

	@Override
	public void setLevel(int slot, int level) {
		dynamicLevels[slot] = level;
		player.getIOSession().write(new SkillLevel(player, slot));
	}

	@Override
	public int getLevel(int slot) {
		return dynamicLevels[slot];
	}

	@Override
	public void setLifepoints(int lifepoints) {
		this.lifepoints = lifepoints;
		player.getIOSession().write(new ConfigPacket(player, 1240, lifepoints << 1));
	}

	@Override
	public int getLifepoints() {
		return lifepoints;
	}

	@Override
	public int getMaximumLifepoints() {
		return maximumLifepoints + lifepointsIncrease;
	}

	@Override
	public void setLifepointsIncrease(int amount) {
		this.lifepointsIncrease = amount;
	}

	@Override
	public int heal(int health) {
		lifepoints += health;
		int left = 0;
		if (lifepoints > getMaximumLifepoints()) {
			left = lifepoints - getMaximumLifepoints();
			lifepoints = getMaximumLifepoints();
		}
		if (player.getIOSession() != null)
			player.getIOSession().write(new ConfigPacket(player, 1240, lifepoints << 1));
		return left;
	}

	@Override
	public int hit(int damage) {
		lifepoints -= damage;
		int left = 0;
		if (lifepoints < 0) {
			left = -lifepoints;
			lifepoints = 0;
		}
		player.getIOSession().write(new ConfigPacket(player, 1240, lifepoints << 1));
		return left;
	}

	@Override
	public double getPrayerPoints() {
		return prayerPoints;
	}

	@Override
	public void updatePrayerPoints(double amount) {
		/*
		 * if (amount > 0 && player.getCredentials().getRights() > 1) { return;
		 * }
		 */
		prayerPoints -= amount;
		if (prayerPoints < 0) {
			prayerPoints = 0;
		}
		player.getIOSession().write(
				new ConfigPacket(player, 2382, (int) prayerPoints));
	}

	@Override
	public int updateLevel(int skill, int amount, int maximum) {
		if (amount > 0 && dynamicLevels[skill] > maximum) {
			return 0;
		}
		int left = (dynamicLevels[skill] + amount) - maximum;
		int level = dynamicLevels[skill] += amount;
		if (level < 0) {
			dynamicLevels[skill] = 0;
		} else if (amount < 0 && level < maximum) {
			dynamicLevels[skill] = maximum;
		} else if (amount > 0 && level > maximum) {
			dynamicLevels[skill] = maximum;
		}
		player.getIOSession().write(new SkillLevel(player, skill));
		return left;
	}

	/**
	 * Sets the current level.
	 * 
	 * @param slot
	 *            The skill id.
	 * @param value
	 *            The level to set.
	 */
	public void setStaticLevel(int slot, int value) {
		if (slot < 0 || slot > 24 || value < 0
				|| value > (slot == DUNGEONEERING ? 120 : 99)) {
			return;
		}
		/*
		 * if (player.getEquipment().size() !=
		 * player.getEquipment().freeSlots()) {
		 * player.getActionSender().getPacketSender().sendMessage
		 * ("You can't change your levels while having items equipped.");
		 * return; } else if (WorldArea.isRisk(player)) {
		 * player.getActionSender(
		 * ).sendMessage("You can't change your levels in a risk zone.");
		 * return; }
		 */
		if (slot == 3 && value < 10) {
			value = 10;
		}
		int difference = value - staticLevels[slot];
		experience[slot] = getExperienceByLevel(value);
		staticLevels[slot] = value;
		updateLevel(slot, difference, difference < 0 ? 0
				: (slot == DUNGEONEERING ? 120 : 99));
		int level = player.getCombatLevel();
		updateCombatLevel();
		maximumLifepoints = staticLevels[3] * 10;
		if (level != player.getCombatLevel()) {
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}
	}

	public void setExperieneGained(int s) {
		this.experienceGained = s;
	}
}