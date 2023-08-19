package com.sevador.game.node.model.skills.prayer;

import com.sevador.game.node.player.Player;

/**
 * The interface implemented by both player as npc prayer handlers.
 * @author Emperor
 *
 */
public interface PrayerSkeleton {

	/**
	 * Checks if the entity is using ancient curses as prayer book.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isCurses();
	
	/**
	 * Checks if the entity is using a prayer/curse.
	 * @param prayer The prayer type.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean get(PrayerType prayer);

	/**
	 * Gets the modifier for a combat skill.
	 * @param skill The skill id.
	 * @return The modifier.
	 */
	public double getMod(int skill);
	
	/**
	 * Gets the headicon.
	 * @return The head icon.
	 */
	public int getHeadIcon();
	
	/**
	 * Gets the current drain rate.
	 * @return The drain rate.
	 */
	public double getDrainRate();

	/**
	 * Resets all the prayers.
	 * @param p The player.
	 */
	public void reset(Player p);

	/**
	 * Activates a prayer.
	 * @param player The player.
	 * @param prayerType The prayer type to activate.
	 */
	public boolean activate(Player player, PrayerType prayerType);
	
}