package com.sevador.game.node.model.combat;

import com.sevador.game.node.model.Entity;


/**
 * Handles a combat type's combat.
 * @author Emperor
 *
 */
public interface TypeHandler {
	
	/**
	 * Initializes the type handler.
	 * @return {@code True} if succesful.
	 */
	public boolean init();
	
	/**
	 * Sets the combat type-related flags on the interaction instance.
	 * @param i The interaction.
	 * @return {@code True} if the entity can continue its attack, {@code false} if not.
	 */
	public boolean handle(Interaction i);
	
	/**
	 * Gets the accuracy of an entity.
	 * @param e The entity.
	 * @return The maximum accuracy.
	 */
	public double getAccuracy(Entity e, Object...args);
	
	/**
	 * Gets the maximum hit of an entity.
	 * @param e The entity.
	 * @return The maximum hit.
	 */
	public double getMaximum(Entity e, Object...args);
	
	/**
	 * Gets the defence of an entity.
	 * @param e The entity.
	 * @param attackBonus The attack bonus used by the attacker.
	 * @return The maximum defence.
	 */
	public double getDefence(Entity e, int attackBonus, Object...args);
	
}