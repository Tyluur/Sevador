package com.sevador.game.event;

import com.sevador.game.node.player.Player;

/**
 * Represents a single button event.
 * @author Emperor
 *
 */
public interface ButtonEvent {

	/**
	 * Initializes the event.
	 */
	public boolean init();
	
	/**
	 * Handles an action button.
	 * @param player The player.
	 * @param opcode The opcode.
	 * @param interfaceId The interface id.
	 * @param buttonId The button id.
	 * @param itemId The item id.
	 * @param slot The item slot.
	 * @return {@code True} if succesful.
	 */
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot);
	
}
