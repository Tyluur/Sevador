package com.sevador.game.node.model.combat.form;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Interaction;

/**
 * Represents a magic spell.
 * @author Emperor
 *
 */
public interface MagicSpell {

	/**
	 * Initializes this spell.
	 * @return {écode True} if succesful.
	 */
	public boolean init();
	
	/**
	 * Handles the interaction.
	 * @param i The interaction.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public boolean handle(Interaction i);
	
	/**
	 * Gets the runes required for casting this spell.
	 * @return An array of required runes.
	 */
	public Item[] getRunes();
	
	/**
	 * Gets the autocast config.
	 * @return The autocast config value.
	 */
	public int getAutocastConfig();
	
	/**
	 * Gets the base damage.
	 * @return The base damage.
	 */
	public int getBaseDamage();
	
	/**
	 * Gets the normal damage.
	 * @return The normal damage.
	 */
	public int getNormalDamage();
	
	/**
	 * Gets the start damage.
	 * @param e The entity.
	 * @param victim The victim.
	 * @return The start damage.
	 */
	public int getStartDamage(Entity e, Entity victim);
	
}