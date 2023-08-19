package com.sevador.game.event;

import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Represents an item action event.
 * @author Emperor
 *
 */
public interface ItemActionEvent {

	/**
	 * Initializes the event.
	 * @return {@code True} if succesful.
	 */
	public boolean init();
	
	/**
	 * Handles the item action event.
	 * @param player The player.
	 * @param item The item.
	 * @param interfaceId The interface id.
	 * @param slot The item slot.
	 * @param type The option type.
	 * @return {@code True} if the item got handled, {@code false} if not.
	 */
	public boolean handle(Player player, Item item, int interfaceId, int slot, OptionType type);
	
}