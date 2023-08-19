package com.sevador.game.event;

import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Represents an object action event.
 * @author Emperor
 *
 */
public interface ObjectEvent {

	/**
	 * Initializes the object event.
	 * @return {@code True} if succesful.
	 */
	public boolean init();
	
	/**
	 * Handles the object event.
	 * @param p The player.
	 * @param obj The game object.
	 * @param type The option type.
	 * @return {@code True} if the event got handled, {@code false} if not.
	 */
	public boolean handle(Player player, GameObject obj, OptionType type);
	
	/**
	 * Sets the walking destination.
	 * @param p The player.
	 * @param obj The object.
	 */
	public void setDestination(Player player, GameObject obj);
	
}
