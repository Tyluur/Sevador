package com.sevador.game.event.button;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.impl.TradeAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.player.Player;
import com.sevador.utility.TradeState;

/**
 * Handles a trading action button event.
 * 
 * @author Emperor
 * 
 */
public final class TradeButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(334, this)
				&& EventManager.register(335, this)
				&& EventManager.register(336, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		if (!player.getActionManager().contains(TradeAction.FLAG)) {
			return false;
		}
		switch (interfaceId) {
		case 334:
			switch (opcode) {
			case 85:
				switch (buttonId) {
				case 21: // Accept
					TradeAction t = player.getActionManager().get(
							TradeAction.FLAG);
					t.accept();
					return true;
				case 6:
				case 22: // Decline
					t = player.getActionManager().get(TradeAction.FLAG);
					t.decline();
					return true;
				}
			}
		case 335: // Trade interface.
			switch (opcode) {
			case 85:
				switch (buttonId) {
				case 16: // Accept
					TradeAction t = player.getActionManager().get(
							TradeAction.FLAG);
					t.accept();
					return true;
				case 12:
				case 18: // Decline
					t = player.getActionManager().get(TradeAction.FLAG);
					t.decline();
					return true;
				case 31:
					return remove(player, itemId, slot, 1);
				case 34:
					player.getPacketSender().sendMessage(
							ItemDefinition.forId(itemId).name
									+ ": "
									+ ItemDefinition.forId(itemId)
											.getStorePrice() + "gp.");
					return true;
				}
				return false;
			case 7:
				return remove(player, itemId, slot, 5);
			case 66:
				return remove(player, itemId, slot, 10);
			case 11:
				return remove(player, itemId, slot, Integer.MAX_VALUE);
			case 48:
				InputHandler.requestInput(player, 2,
						"How many would you like to remove?");
				player.setAttribute(InputHandler.ITEM_KEY, itemId);
				player.setAttribute(InputHandler.SLOT_KEY, slot);
				return true;
			case 17:
				player.getPacketSender().sendMessage(
						ItemDefinition.forId(itemId).name + ": "
								+ ItemDefinition.forId(itemId).getStorePrice()
								+ "gp.");
				return true;
			case 54:
				player.getPacketSender().sendMessage(
						ItemDefinition.forId(itemId).getExamine());
				return true;
			}
			return false;
		case 336: // Inventory interface.
			switch (opcode) {
			case 85:
				return offer(player, itemId, slot, 1);
			case 7:
				return offer(player, itemId, slot, 5);
			case 66:
				return offer(player, itemId, slot, 10);
			case 11:
				return offer(player, itemId, slot, Integer.MAX_VALUE);
			case 48:
				InputHandler.requestInput(player, 1,
						"How many would you like to offer?");
				player.setAttribute(InputHandler.ITEM_KEY, itemId);
				player.setAttribute(InputHandler.SLOT_KEY, slot);
				return true;
			case 17:
				player.getPacketSender().sendMessage(
						ItemDefinition.forId(itemId).name + ": "
								+ ItemDefinition.forId(itemId).getStorePrice()
								+ "gp.");
				return true;
			case 84: // Lend
				player.getPacketSender().sendMessage(
						"Lending is unavailable for non-donators.");
				return true;
			case 54:
				player.getPacketSender().sendMessage(
						ItemDefinition.forId(itemId).getExamine());
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Offers an item to the trade.
	 * 
	 * @param player
	 *            The player.
	 * @param itemId
	 *            The item id.
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount.
	 * @return {@code True} if succesful.
	 */
	public static boolean offer(Player player, int itemId, int slot, int amount) {
		if (player.getAttribute("trade:state") != TradeState.FIRST_SCREEN) {
			return true;
		}
		Item item = player.getInventory().get(slot);
		Container container = player.getAttribute("trade-cont");
		if (container == null || item == null || item.getId() != itemId) {
			return false;
		}
		int totalAmount = player.getInventory().getAmount(item);
		if (amount > totalAmount) {
			amount = totalAmount;
		}
		item = new Item(itemId, amount);
		if (player.getInventory().remove(item)) {
			container.add(item);
			return true;
		}
		return false;
	}

	/**
	 * Removes an item from the trade.
	 * 
	 * @param player
	 *            The player.
	 * @param itemId
	 *            The item id.
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount.
	 * @return {@code True} if succesful.
	 */
	public static boolean remove(Player player, int itemId, int slot, int amount) {
		if (player.getAttribute("trade:state") != TradeState.FIRST_SCREEN) {
			return true;
		}
		Container container = player.getAttribute("trade-cont");
		if (container == null) {
			return false;
		}
		Item item = container.get(slot);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		int totalAmount = container.getAmount(item);
		if (amount > totalAmount) {
			amount = totalAmount;
		}
		item = new Item(itemId, amount);
		if (container.remove(item)) {
			player.getInventory().add(item);
			return true;
		}
		return false;
	}

}