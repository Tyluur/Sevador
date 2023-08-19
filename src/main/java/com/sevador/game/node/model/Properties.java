package com.sevador.game.node.model;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.ConfigPacket;
import com.sevador.utility.Priority;
import com.sevador.utility.WeaponInterface;

/**
 * Holds the entity's properties such as stat bonuses, curse increases/decreases, ...
 * @author Emperor
 *
 */
public class Properties {

	/**
	 * The entity's stats.
	 */
	private int[] stats;

	/**
	 * The entity's cursed modifier.
	 */
	private double[] cursedModifiers;
	
	/**
	 * The attack speed.
	 */
	private int attackSpeed = 4;

	/**
	 * The entity's attack animation.
	 */
	private Animation attackAnimation;

	/**
	 * The entity's defence animation.
	 */
	private Animation defenceAnimation;

	/**
	 * The teleport location.
	 */
	private Location teleportLocation;

	/**
	 * The attack style used.
	 */
	private int attackStyle;

	/**
	 * The attack type used.
	 */
	private int attackType;

	/**
	 * Constructs a new {@code Properties} {@code Object}.
	 */
	public Properties() {
		this.stats = new int[18];
		this.cursedModifiers = new double[7];
	}

	/**
	 * Sets a stat bonus.
	 * @param slot The slot.
	 * @param amount The amount to set.
	 */
	public void setStat(int slot, int amount) {
		stats[slot] = amount;
	}

	/**
	 * Sets the stats.
	 * @param stats The stats.
	 */
	public void setStats(int[] stats) {
		this.stats = stats;
	}

	/**
	 * Gets the entity's stats.
	 * @return The stats array.
	 */
	public int[] getStats() {
		return stats;
	}

	/**
	 * @param attackAnimation the attackAnimation to set
	 */
	public void setAttackAnimation(Animation attackAnimation) {
		this.attackAnimation = attackAnimation;
	}

	/**
	 * @return the attackAnimation
	 */
	public Animation getAttackAnimation() {
		return attackAnimation;
	}

	/**
	 * @param defenceAnimation the defenceAnimation to set
	 */
	public void setDefenceAnimation(Animation defenceAnimation) {
		this.defenceAnimation = defenceAnimation;
	}

	/**
	 * @return the defenceAnimation
	 */
	public Animation getDefenceAnimation() {
		return new Animation(defenceAnimation.getId(), defenceAnimation.getSpeed(), defenceAnimation.isNpc(), Priority.NORMAL);
	}

	/**
	 * @return the teleportLocation
	 */
	public Location getTeleportLocation() {
		return teleportLocation;
	}

	/**
	 * @param teleportLocation the teleportLocation to set
	 */
	public void setTeleportLocation(Location teleportLocation) {
		this.teleportLocation = teleportLocation;
	}

	/**
	 * @return the attackSpeed
	 */
	public int getAttackSpeed() {
		if (attackStyle == WeaponInterface.STYLE_RAPID) {
			return attackSpeed - 1;
		}
		return attackSpeed;
	}

	/**
	 * @param attackSpeed the attackSpeed to set
	 */
	public void setAttackSpeed(int attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	/**
	 * @return the cursedModifiers
	 */
	public double[] getCursedModifiers() {
		return cursedModifiers;
	}

	/**
	 * Updates the cursed modifiers for an entity.
	 * @param e The entity.
	 * @param mod The modifier to update.
	 * @param amount The amount to increase with.
	 * @param max The maximum amount this modifier can be.
	 */
	public void updateCursedModifiers(Entity e, int mod, double amount, double max) {
		cursedModifiers[mod] += amount;
		if (amount > 0.0 && cursedModifiers[mod] > max) {
			cursedModifiers[mod] = max;
		} else if (amount < 0.0 && cursedModifiers[mod] < max) {
			cursedModifiers[mod] = max;
		}
		if (e.isPlayer()) {
			int stat = 30;
			int value = (stat + (int) (cursedModifiers[Skills.ATTACK] * 100))
					| ((stat + (int) (cursedModifiers[Skills.STRENGTH] * 100)) << 6)
					| ((stat + (int) (cursedModifiers[Skills.DEFENCE] * 100)) << 12)
					| ((stat + (int) (cursedModifiers[Skills.RANGE] * 100)) << 18)
					| ((stat + (int) (cursedModifiers[Skills.MAGIC] * 100)) << 24);
			e.getPlayer().getIOSession().write(new ConfigPacket(e.getPlayer(), 1583, value));
		}
	}


	/**
	 * @param cursedModifiers the cursedModifiers to set
	 */
	public void setCursedModifiers(double[] cursedModifiers) {
		this.cursedModifiers = cursedModifiers;
	}

	/**
	 * @return the attackStyle
	 */
	public int getAttackStyle() {
		return attackStyle;
	}

	/**
	 * @param attackStyle the attackStyle to set
	 */
	public void setAttackStyle(int attackStyle) {
		this.attackStyle = attackStyle;
	}

	/**
	 * @return the attackType
	 */
	public int getAttackType() {
		return attackType;
	}

	/**
	 * @param attackType the attackType to set
	 */
	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}

	public double getCarriedWeight(Player player) {
		double weight = 0.0;
		for (Item item : player.getInventory().toArray()) {
			if (item == null) continue;
			weight += item.getWeight();
		}
		for (Item item : player.getEquipment().toArray()){ 
			if (item == null) continue;
			weight += item.getWeight();
		}
		return weight;
	}
}