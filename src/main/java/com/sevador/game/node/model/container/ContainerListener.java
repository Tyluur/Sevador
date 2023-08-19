package com.sevador.game.node.model.container;

import com.sevador.game.node.Item;

/**
 * The container listener.
 * @author Emperor
 * @author Graham
 *
 */
public interface ContainerListener {

	/**
	 * Gets called when an item changed.
	 * @param container The container.
	 * @param slot The item slot.
	 * @return {@code True}.
	 */
	public boolean itemChanged(Container container, int slot);
	
	/**
	 * Gets called when items are added to the container.
	 * @param container The container.
	 * @param items The items.
	 * @return {@code True}.
	 */
	public boolean itemsAdded(Container container, Item... items);
	
	/**
	 * Gets called when items are removed from the container.
	 * @param container The container.
	 * @param items The items.
	 * @return {@code True}.
	 */
	public boolean itemsRemoved(Container container, Item... items);

	/**
	 * Refreshes the container.
	 * @param container The container.
	 * @return {@code True}.
	 */
	public boolean refresh(Container container);
	
}