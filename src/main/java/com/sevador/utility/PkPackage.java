package com.sevador.utility;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.node.Item;

/**
 * Represents a pk-package.
 * @author Emperor
 *
 */
public final class PkPackage {

	/**
	 * The list of inventory items to add.
	 */
	private final List<Item> inventoryItems = new ArrayList<Item>();

	/**
	 * The list of equip items to add.
	 */
	private final List<Item> equipItems = new ArrayList<Item>();
	
	/**
	 * The item id.
	 */
	private final int itemId;
	
	/**
	 * Constructs a new {@code PkPackage} {@code Object}.
	 * @param itemId The item id.
	 */
	public PkPackage(int itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the inventoryItems
	 */
	public List<Item> getInventoryItems() {
		return inventoryItems;
	}

	/**
	 * @return the equipItems
	 */
	public List<Item> getEquipItems() {
		return equipItems;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}
}