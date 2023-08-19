package net.burtleburtle.script;

import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Represents an item action script.
 * @author Emperor
 *
 */
public abstract class ItemActionScript {

	/**
	 * Handles an item action.
	 * @param player The player.
	 * @param type The option type.
	 * @param slot The item slot.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public abstract boolean handle(Player player, OptionType type, int slot);
	
}