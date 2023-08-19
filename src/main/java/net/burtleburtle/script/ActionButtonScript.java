package net.burtleburtle.script;

import com.sevador.game.node.player.Player;

/**
 * Represents an action button script.
 * @author Emperor
 *
 */
public abstract class ActionButtonScript {

	/**
	 * Handles an action button.
	 * @param player The player.
	 * @param opcode The opcode.
	 * @param buttonId The button id.
	 * @param itemId The item id.
	 * @param slot The item slot.
	 * @return {@code True} if succesful.
	 */
	public abstract boolean handle(Player player, int opcode, int buttonId, int itemId, int slot);

}