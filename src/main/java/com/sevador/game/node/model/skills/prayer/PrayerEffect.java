package com.sevador.game.node.model.skills.prayer;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.Player;

/**
 * Represents a prayer effect.
 * @author Emperor
 *
 */
public interface PrayerEffect {

	/**
	 * Handles the effect.
	 * @param source The player.
	 * @param victim The victim.
	 */
	public void handle(Player source, Entity victim);
	
	/**
	 * Resets the effects.
	 * @param player The player.
	 */
	public void reset(Player player);
	
}