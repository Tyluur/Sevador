package com.sevador.content;

import java.io.Serializable;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ContainerPacket;

/**
 * Handles the price check interface.
 *
 * @author Emperor
 */
public class PriceCheck implements Serializable {

	/**
	 * The Serial UID.
	 */
	private static final long serialVersionUID = 389384917095252184L;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The item container used.
	 */
	private Container container = new Container(Type.ALWAYS_STACK, 28);

	/**
	 * If the price check interface is open.
	 */
	private boolean open;

	/**
	 * Constructs a new {@code PriceCheck} {@code Object}.
	 *
	 * @param player The player.
	 */
	public PriceCheck(Player player) {
		this.player = player;
	}

	/**
	 * Opens up the price check interface.
	 */
	public boolean open() {
		Object[] params1 = new Object[]{"", "", "", "", "Add-X", "Add-All", "Add-10", "Add-5", "Add", -1, 1, 7, 4, 93, 13565952};
		player.getPacketSender().sendInterface(206).sendInventoryInterface(207);
		player.getIOSession().write(new CS2Script(player, 150, "IviiiIsssssssss", params1));
		player.getIOSession().write(new AccessMask(player, 0, 54, 206, 15, 0, 1278));
		player.getIOSession().write(new AccessMask(player, 0, 27, 207, 0, 36, 1086));
		open = true;
		return refresh(false);
	}

	/**
	 * Checks the price of an item.
	 *
	 * @param itemId The item id.
	 * @param slot   The slot clicked.
	 * @param amount The amount.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public boolean checkPrice(int itemId, int slot, int amount) {
		Item item = player.getInventory().get(slot);
		if (item == null || item.getId() != itemId) {
			return true;
		}
		if (!item.getDefinition().isTradable() || item.getDefinition().isNoted() || item.getDefinition().getStorePrice() < 1) {
			player.getPacketSender().sendMessage("There is no price for this item.");
			return true;
		}
		int inventoryAmount = player.getInventory().getAmount(itemId);
		if (amount > inventoryAmount) {
			amount = inventoryAmount;
		}
		item = new Item(item.getId(), amount);
		player.getInventory().remove(item);
		container.add(item);
		return refresh(false);
	}

	/**
	 * Removes a price checked item.
	 *
	 * @param itemId The item id.
	 * @param slot   The item slot.
	 * @param amount The amount.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public boolean remove(int itemId, int slot, int amount) {
		Item item = container.get(slot);
		if (item == null || item.getId() != itemId) {
			return true;
		}
		int containerAmount = container.getAmount(itemId);
		if (amount > containerAmount) {
			amount = containerAmount;
		}
		item = new Item(item.getId(), amount);
		container.remove(item);
		player.getInventory().add(item);
		return refresh(true);
	}

	/**
	 * Closes the price check interface.
	 *
	 * @return {@code True}.
	 */
	public boolean close() {
		player.getInventory().addAll(container);
		player.getInventory().refresh();
		container.clear();
		return !(open = false);
	}


	/**
	 * Refreshes the price check interface & inventory interface.
	 *
	 * @param reArrange If the container should be re-arranged.
	 * @return {@code True}.
	 */
	private boolean refresh(boolean reArrange) {
		int totalValue = 0;
		int value = -1;
		int i = 0;
		if (reArrange) {
			Container cont = new Container(Type.ALWAYS_STACK, 28);
			cont.addAll(container);
			container = cont;
		}
		player.getIOSession().write(new ContainerPacket(player, 90, container, false));
		for (Item item : container.toArray()) {
			if (item != null) {
				value = item.getDefinition().getGEPrice();
				totalValue += value * item.getAmount();
			}
			player.getIOSession().write(new CS2Config(player, 700 + i, value));
			i++;
		}
		player.getIOSession().write(new CS2Config(player, 728, totalValue));
		player.getIOSession().write(new ContainerPacket(player, 93, player.getInventory(), false));
		return true;
	}

	/**
	 * @return the isOpen
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Gets the item container.
	 *
	 * @return The container.
	 */
	public Container getContainer() {
		return container;
	}

}
