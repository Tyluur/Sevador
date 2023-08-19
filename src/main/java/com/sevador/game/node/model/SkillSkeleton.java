package com.sevador.game.node.model;

/**
 * The skill skeleton used for both NPC and player skills.
 * @author Emperor
 *
 */
public interface SkillSkeleton {

	/**
	 * Sets the dynamic level.
	 * @param slot The slot.
	 * @param level The level to set.
	 */
	public void setLevel(int slot, int level);
	
	/**
	 * Gets the dynamic skill level.
	 * @param slot The slot.
	 * @return The current level.
	 */
	public int getLevel(int slot);

	/**
	 * Gets the static skill level.
	 * @param slot The skill slot.
	 * @return The static level.
	 */
	public int getStaticLevel(int slot);
	
	/**
	 * Heals the player.
	 * @param health The amount to heal.
	 * @return The amount that we couldn't heal due to total lifepoints being over maximum.
	 */
	public int heal(int health);

	/**
	 * Hits the entity.
	 * @param damage The amount of damage to deal.
	 * @return The amount of damage we couldn't deal, due to overflow.
	 */ 
	public int hit(int damage);
	
	/**
	 * Sets the current lifepoints.
	 * @param lifepoints The lifepoints.
	 */
	public void setLifepoints(int lifepoints);

	/**
	 * Gets the current lifepoints.
	 * @return The lifepoints.
	 */
	public int getLifepoints();

	/**
	 * Updates a skill level.
	 * @param skill The skill to update.
	 * @param amount The amount to update with (incremented).
	 * @param maximum The maximum the skill can be.
	 * @return The amount that could not be incremented/decremented.
	 */
	public int updateLevel(int skill, int amount, int maximum);
	
	/**
	 * Gets the current prayer points.
	 * @return The prayer points.
	 */
	public double getPrayerPoints();
	
	/**
	 * Updates the prayer points.
	 * @param amount The amount to decrease with.
	 */
	public void updatePrayerPoints(double amount);
	
	/**
	 * Gets the maximum lifepoints.
	 * @return The maximum amount of lifepoints.
	 */
	public int getMaximumLifepoints();

	/**
	 * Sets the lifepoints increase.
	 * @param amount The amount to increase maximum lifepoints with (Nex gear, ...)
	 */
	public void setLifepointsIncrease(int amount);

}