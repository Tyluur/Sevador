package com.sevador.game.event.button;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.ActionFlag;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.misc.Shop;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.CS2String;
import com.sevador.network.out.StringPacket;

/**
 * Handles a shop button event.
 * 
 * @author Emperor
 * 
 */
public final class ShopButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(620, this);
		return EventManager.register(621, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		if (interfaceId == 620) {
			return handleShopInterface(player, opcode, buttonId, itemId, slot);
		}
		switch (opcode) {
		case 7:
			sell(player, itemId, slot, 1);
			return true;
		case 66:
			sell(player, itemId, slot, 5);
			return true;
		case 11:
			sell(player, itemId, slot, 10);
			return true;
		case 48:
			sell(player, itemId, slot, 50);
			return true;
		case 54:
			player.getPacketSender().sendMessage(
					ItemDefinition.forId(itemId).getExamine());
			return true;
		}
		return false;
	}

	public static void customShopPrices(Player player, int slot) {
		int shopSlot = slot / 6;
		final Shop shop = player.getAttribute("open:shop");
		final Item item = shop.getContainer().get(shopSlot);
		ItemDefinition def = ItemDefinition.forId(item.getId());
		if (shop.getName().contains("Thi")) {
			switch (def.id) {
			case 1897:
				def.setStorePrice(4000);
				break;
			case 950:
				def.setStorePrice(9000);
				break;
			case 1635:
				def.setStorePrice(17000);
				break;
			case 7650:
				def.setStorePrice(23000);
				break;
			case 1613:
				def.setStorePrice(31000);
				break;
			}
		}
	}
	private static final String[] ATTACK_SIDE = new String[]{"Attack", "<col=ffff00>---", "Strength", "Ranged Strength", "Magic Damage", "Absorb Melee", "Absorb Ranged", "Prayer Bonus"};

	private static final String MIDDLE_COLUMN = " <col=ff9900><br><col=ff9900>Stab<br><col=ff9900>Slash<br><col=ff9900>Crush<br><col=ff9900>Magic<br><col=ff9900>Ranged<br><col=ff9900>Summoning";


	private static final void sendChatboxInterface(Player p, Item item, int slot) {
		ItemDefinition def = item.getDefinition();
		for (int i = 730; i < 750; i++)
			p.getPacketSender().sendBConfig(p, i, i);
		p.getPacketSender().sendBConfig(p, 740, 1);
		p.getPacketSender().sendBConfig(p, 741, item.getId());
		p.getPacketSender().sendBConfig(p, 742, 1);
		p.getPacketSender().sendBConfig(p, 743, 995);
		//p.getPacketSender().sendAMask(p, 30, 449, 15, 0, 0);
		p.getPacketSender().sendBConfig(p, 744, item.getDefinition().getStorePrice());
		p.getIOSession().write(new CS2String(p, 25, "<col=ff9900>" + def.getExamine()));
		p.getIOSession().write(new CS2String(p, 36, MIDDLE_COLUMN));
		p.getIOSession().write(new CS2String(p, 52, getDefenceBonuses(def)));
		//p.getIOSession().write(new CS2String(p, 35, getAttackBonuses(def)));
		p.getIOSession().write(new CS2String(p, 26, "<br>"));
		p.getIOSession().write(new CS2String(p, 34, "<br>"));
		p.getPacketSender().sendBConfig(p, 746, 10);
		p.getPacketSender().sendBConfig(p, 168, 98);
		p.getIOSession().write(new StringPacket(p, "<col=ff9900>Item Information.", 449, 2));
		p.getIOSession().write(new StringPacket(p, "<col=ff9900>You have " + p.getInventory().getAmount(995) + " coins.", 449, 25));
		p.getPacketSender().sendInventoryInterface(449);
	}

	@SuppressWarnings("unused")
	private static String getAttackBonuses(ItemDefinition def) {
		StringBuilder bldr = new StringBuilder();
		for (int i = 0; i < 13; i++) {
			if (i > 0 && i < 6) {
				bldr.append("<col=ffff00>").append(def.getBonus()[i - 1]).append("<br>");
			} else {
				bldr.append("<col=ff9900>").append(ATTACK_SIDE[i == 0 ? i : i - 5]).append("<br>");
			}
		}
		return bldr.toString();
	}


	private static String getDefenceBonuses(ItemDefinition def) {
		StringBuilder bldr = new StringBuilder();
		for (int i = 0; i < 13; i++) {
			if (i > 0) {
				bldr.append("<col=ffff00>").append(def.getBonus()[i > 9 ? 0 : i + 5]).append("<br>");
			} else {
				bldr.append("<col=ff9900>Defence<br>");
			}
		}
		return bldr.toString();
	}

	/**
	 * Handles a shop interface button.
	 * 
	 * @param player
	 *            The player.
	 * @param opcode
	 *            The opcode handled.
	 * @param buttonId
	 *            The button id.
	 * @param itemId
	 *            The item id.
	 * @param slot
	 *            The item slot.
	 * @return {@code True} if the event got handled.
	 */
	private static final boolean handleShopInterface(Player player, int opcode,
			int buttonId, int itemId, int slot) {
		switch (opcode) {
		case 85:
			switch (buttonId) {
			case 18:
				player.getActionManager()
				.unregister(ActionFlag.CLOSE_INTERFACE);
				return true;
			case 25:
				int shopSlot = slot / 6;
				final Shop shop = player.getAttribute("open:shop");
				final Item item = shop.getContainer().get(shopSlot);
				ItemDefinition def = ItemDefinition.forId(item.getId());
				customShopPrices(player, slot);
				if (shop.getId() != 8 && shop.getId() != 9) {
					player.getPacketSender().sendMessage(""
							+ def.name
							+ "s cost	 "
							+ (shop.getName().toLowerCase()
									.equalsIgnoreCase("Zeke's Gear") ? def
											.getStorePrice() * 10 : def.getStorePrice())
											+ " coins in " + shop.getName() + ".");
				} else if (shop.getId() == 8) {
					for (int i = 0; i < DONATOR_ITEMS.length; i++) {
						if (item.getId() == DONATOR_ITEMS[i][0]) {
							player.getPacketSender().sendMessage(def.name + "s cost " + DONATOR_ITEMS[i][1] + " donator tickets in " + shop.getName() + ".");
						}
					}
				}
				sendChatboxInterface(player, item, slot);
				return false;
			}
			break;
		case 7:
			switch (buttonId) {
			case 25:
				buy(player, slot / 6, 1);
				return true;
			}
			break;
		case 66:
			switch (buttonId) {
			case 25:
				buy(player, slot / 6, 5);
				return true;
			}
			break;
		case 11:
			switch (buttonId) {
			case 25:
				buy(player, slot / 6, 10);
				return true;
			}
			break;
		case 48:
			switch (buttonId) {
			case 25:
				buy(player, slot / 6, 50);
				return true;
			}
			break;
		case 17:
			switch (buttonId) {
			case 25:
				buy(player, slot / 6, 500);
				return true;
			}
			break;
		case 54:
			Shop shop = player.getAttribute("open:shop");
			if (shop != null && shop.getContainer().get(slot / 6) != null) {
				player.getPacketSender().sendMessage(
						shop.getContainer().get(slot / 6).getDefinition()
						.getExamine());
			}
			return true;
		}
		return false;
	}

	/**
	 * Sells an item to the store.
	 * 
	 * @param player
	 *            The player.
	 * @param id
	 *            The item id.
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount to sell.
	 */
	private static void sell(Player player, final int id, final int slot,
			final int amount) {
		final Shop shop = player.getAttribute("open:shop");
		if (shop == null) {
			return;
		}
		final Item item = player.getInventory().get(slot);
		if (item == null || item.getId() != id) {
			return;
		}
		if (!shop.getOriginal().contains(item)) {
			player.getPacketSender().sendMessage(
					"This shop does not buy this item.");
			return;
		}
		final ItemDefinition def = item.getDefinition();
		customShopPrices(player, slot);
		finishSale(player, 995, id, slot, amount, def.getStorePrice());
	}

	/**
	 * Finishes the selling of an item.
	 * 
	 * @param p
	 *            The player.
	 * @param currency
	 *            The currency used.
	 * @param id
	 *            The item id.
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount.
	 * @param value
	 *            The value per item.
	 */
	private static void finishSale(Player p, int currency, int id, int slot,
			int amount, int value) {
		final Shop shop = p.getAttribute("open:shop");
		if (shop == null) {
			return;
		}
		Item item = p.getInventory().get(slot);
		if (item == null || item.getId() != id) {
			return;
		}
		int inventoryAmount = p.getInventory().getAmount(item);
		if (amount > inventoryAmount) {
			amount = inventoryAmount;
		}
		long maximum = Integer.MAX_VALUE - shop.getContainer().getAmount(item);
		if (maximum <= 0) {
			return;
		}
		if (amount > maximum) {
			amount = (int) maximum;
		}
		switch (currency) {
		case 995:
			if (!p.getInventory().add(new Item(995, value * amount))) {
				p.getPacketSender().sendMessage(
						"Not enough space in your inventory.");
				return;
			}
			break;
		}
		if (p.getInventory().remove(new Item(id, amount))) {
			shop.getContainer().add(new Item(id, amount));
		}
	}

	/**
	 * Buys an item from the store.
	 * 
	 * @param player
	 *            The player.
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount.
	 */
	public static void buy(Player player, final int slot, final int amount) {
		final Shop shop = player.getAttribute("open:shop");
		if (shop == null) {
			return;
		}
		final Item item = shop.getContainer().get(slot);
		if (player.getInventory().freeSlots() < amount
				&& !(item.getDefinition().isStackable() || item.getDefinition()
						.isNoted())) {
			player.getPacketSender().sendMessage("Not enough inventory space.");
			return;
		}
		if (item == null || item.getAmount() < 1) {
			player.getPacketSender().sendMessage(
					"The shop has run out of stock.");
			return;
		}
		final ItemDefinition def = ItemDefinition.forId(item.getId());
		customShopPrices(player, slot);
		long value = (shop.getName().toLowerCase()
				.equalsIgnoreCase("Zeke's Gear") ? def.getStorePrice() * amount
						* 10 : def.getStorePrice() * amount);
		customShopPrices(player, slot);
		if (value < 0 || value > Integer.MAX_VALUE) {
			player.getPacketSender().sendMessage(
					"You do not have enough to buy this many.");
			return;
		}
		finishBuy(player, item, amount, 995, def.getStorePrice());
	}

	private static final int[][] DONATOR_ITEMS = { {10551, 10}, { 22302, 15}, {18349, 30}, {18351, 30}, {18353, 30}, {18355, 30}};

	/**
	 * Finishes the buying of an item.
	 * 
	 * @param p
	 *            The player.
	 * @param item
	 *            The item to buy.
	 * @param amount
	 *            The amount to buy.
	 * @param currency
	 *            The currency.
	 * @param value
	 *            The amount to pay per item.
	 */
	private static final void finishBuy(Player p, Item item, int amount,
			int currency, int value) {
		Shop shop = p.getAttribute("open:shop");
		if (shop == null) {
			return;
		}

		if (shop.getName().contains("Thi")) {
			switch (item.getId()) {
			case 1897:
				item.getDefinition().setStorePrice(4000);
				break;
			case 950:
				item.getDefinition().setStorePrice(9000);
				break;
			case 1635:
				item.getDefinition().setStorePrice(17000);
				break;
			case 7650:
				item.getDefinition().setStorePrice(23000);
				break;
			case 1613:
				item.getDefinition().setStorePrice(31000);
				break;
			}
		}
		int slot = shop.getContainer().getSlotById(item.getId());
		if (amount > item.getAmount()) {
			amount = item.getAmount();
		}
		int current = amount;
		if (value < 1) {
			value = 1;//
		}
		switch (currency) {
		case 995:
			current = p.getInventory().getAmount(new Item(995, 1))
			/ (value * amount);
			break;
		}
		if (amount > current) {
			amount = current;
		}
		int ticketAmount = p.getInventory().getAmount(4278);
		if (shop.getId() == 8) {
			for (int i = 0; i < DONATOR_ITEMS.length; i++) {
				if (item.getId() == DONATOR_ITEMS[i][0]) {
					if (ticketAmount < DONATOR_ITEMS[i][1] * amount) {
						p.getPacketSender().sendMessage("You need " + DONATOR_ITEMS[i][1] + " donator tickets to buy this.");
						return;
					} else {
						p.getInventory().add(item);
						p.getInventory().remove(4278, DONATOR_ITEMS[i][1] * amount);
					}
				}
			}
		}
		if (amount == 0 && shop.getId() != 11) {
			p.getPacketSender().sendMessage(
					"You do not have enough coins to buy this.");
			return;
		}
		int total = value * amount;
		if (!shop.getContainer().remove(new Item(item.getId(), amount))) {
			return;
		}
		if (shop.getContainer().get(slot) == null
				&& shop.getOriginal().contains(item)) {
			shop.getContainer().replace(new Item(item.getId(), 0), slot);
		}
		if (!p.getInventory().add(new Item(item.getId(), amount))) {
			return;
		}
		switch (currency) {
		case 995:
			p.getInventory().remove(new Item(995, total));
			break;
		}
	}
}