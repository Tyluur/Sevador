package com.sevador.content.grandExchange;

import java.io.File;

import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.tick.Tick;

import com.sevador.content.grandExchange.ItemOffer.OfferType;
import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.game.world.World;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.ContainerPacket;
import com.sevador.network.out.GrandExchangePacket;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.InterfacePacket;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Constants;

/**
 * Grand Exchange InterfacePacket Worker.
 * 
 * @author Jefferson
 * @author Tyluur
 * 
 */
public class ExchangeHandler {

	/**
	 * Handle the InterfacePacket Buttons Actions.
	 * 
	 * @param player
	 *            The Owner.
	 * @param buttonId
	 *            Clicked button.
	 */
	public static void action(final Player player, final int buttonId) {
		final ItemOffer offer;
		switch (buttonId) {
		case 19:
		case 35:
		case 51:
		case 108:
		case 89:
		case 70:
			resetInterfaceConfigs(player);
			player.setGESlot(getSlot(buttonId));
			offer = player.getGeOffers()[player.getGESlot()];
			if (offer == null) {
				player.getPacketSender().sendMessage("Your offer has been nulled.");
				player.getIOSession().write(new GrandExchangePacket(offer, (byte) 8));
				GrandExchange.getOffers().remove(offer);//buy dag sell boots
				return;
			}
			player.getDialogueManager().startDialogue("GEOffer", new Object[]{offer.getType(), offer.getSlot(), offer.getId(), offer.getAmount(), offer.getPrice()});
			/*offer.getOwner().getIOSession().write(new ConfigPacket(offer.getOwner(), 1112, offer.getSlot()));
			offer.getOwner().getPacketSender().sendConfig(1113, 0);
			Item[] item;
			switch (offer.getType()) {
			case BUY:
				item = new Item[] {new Item(offer.getId(), offer.getAmount()), 
						null};
				setItemSlot(player, offer.getSlot(), item);
				break;
			case SELL:
				item = new Item[] {new Item(995, offer.getTotal()), 
						null};
				setItemSlot(player, offer.getSlot(), item);
				break;
			default:
				break;
			}*/
			break;
		case 31:
		case 47:
		case 63:
		case 82:
		case 101:
		case 120:
			resetInterfaceConfigs(player);
			int slot = getSlot(buttonId);
			player.offerType = OfferType.BUY;
			player.setGESlot(slot);
			player.getIOSession().write(new ConfigPacket(player, 1112, slot));
			searchScript(player);
			break;
		case 32:
		case 48:
		case 64:
		case 83:
		case 102:
		case 121:
			resetInterfaceConfigs(player);
			player.getIOSession().write(new ConfigPacket(player, 1113, 1));
			player.getPacketSender().sendInventoryInterface(107);
			final Object[] params = new Object[]{"", "", "", "", "Offer", -1, 0, 7, 4, 93, 7012370};
			player.getIOSession().write(new CS2Script(player, 149, "IviiiIsssss", params));
			player.getIOSession().write(new AccessMask(player, 0, 27, 107, 18, 0, 1026));
			player.getIOSession().write(new ContainerPacket(player, 93, player.getInventory(),false));
			player.getIOSession().write(new InterfaceConfig(player, 107, 0, false));
			int slot2 = getSlot(buttonId);
			player.offerType = OfferType.SELL;
			player.setGESlot(slot2);
			player.getIOSession().write(new ConfigPacket(player, 1112, slot2));
			player.getIOSession().write(new InterfaceConfig(player, 105, 196, true));
			break;
		case 190:
			searchScript(player);
			break;
		case 128:
			resetInterfaceConfigs(player);
			if (player.offerType.equals(OfferType.SELL)) {
				player.getPacketSender().setDefaultInventory();
			} else if (player.offerType.equals(OfferType.BUY)) {
				player.getIOSession().write(
						new CS2Script(player, 573, "", new Object[] {}));
			}
			break;
		case 155:
			offer = player.getGeOffers()[player.getGESlot()];
			offer.decrease();
			player.getIOSession().write(new ConfigPacket(player, 1110, offer.getAmount()));
			break;
		case 157:
		case 160:
			offer = player.getGeOffers()[player.getGESlot()];
			offer.increase();
			player.getIOSession().write(new ConfigPacket(player, 1110, offer.getAmount()));
			break;
		case 162:
			offer = player.getGeOffers()[player.getGESlot()];
			offer.increase(10);
			player.getIOSession().write(new ConfigPacket(player, 1110, offer.getAmount()));
			break;
		case 164:
			offer = player.getGeOffers()[player.getGESlot()];
			offer.increase(100);
			player.getIOSession().write(new ConfigPacket(player, 1110, offer.getAmount()));
			break;
		case 166:
			offer = player.getGeOffers()[player.getGESlot()];
			if (offer.getType() == OfferType.BUY) {
				offer.increase(1000);
			} else {
				offer.setAmount(player.getInventory().getAmount(new Item(offer.getId())));
			}
			player.getIOSession().write(new ConfigPacket(player, 1110, offer.getAmount()));
			break;
		case 181:
			player.getPacketSender().sendMessage("There is no need to change the price; you will always get the item!");
			/*offer = player.getGeOffers()[player.getGESlot()];
			if (offer.getType() == OfferType.SELL) {
				player.getPacketSender().sendMessage("You cannot change the price while selling an item.");
				return;
			}
			offer.setPrice((int) (offer.getPrice() - offer.getPrice() * 0.05));
			player.getIOSession().write(new ConfigPacket(player, 1111, offer.getPrice()));*/
			break;
		case 179:
			player.getPacketSender().sendMessage("There is no need to change the price; you will always get the item!");
			/*offer = player.getGeOffers()[player.getGESlot()];
			if (offer.getType() == OfferType.SELL) {
				player.getPacketSender().sendMessage("You cannot change the price while selling an item.");
				return;
			}
			offer.setPrice((int) (offer.getPrice() + offer.getPrice() * 0.05));
			player.getIOSession().write(new ConfigPacket(player, 1111, offer.getPrice()));*/
			break;
		case 168:
			InputHandler.requestInput(player, 3, "Enter the amount you wish to purchase:");
			offer = player.getGeOffers()[player.getGESlot()];
			player.setAttribute("geOffer", offer);
			break;
		case 186:
			offer = player.getGeOffers()[player.getGESlot()];
			for (String s : Constants.GRAND_EXCHANGE_RARES) {
				Item item = new Item(offer.getId());
				if (item.getDefinition().name.toLowerCase().contains(s)) {
					player.getDialogueManager().startDialogue("SimpleMessage", "You can only buy or sell " + item.getDefinition().name + "s with other players.");
					return;
				}
			}
			if (offer.getPrice() * offer.getAmount() > Integer.MAX_VALUE ||
					offer.getPrice() * offer.getAmount() == Integer.MAX_VALUE ||
					offer.getPrice() * offer.getAmount() >= Integer.MAX_VALUE ||
					offer.getPrice() * offer.getAmount() < 0) {
				player.getPacketSender().sendMessage("The price of the item is too high!");
				return;
			}
			if (offer.getPrice() == 0 || offer.getAmount() == 0 || offer.getPrice() * offer.getAmount() == 0) {
				player.getPacketSender().sendMessage("Invalid amount.");
				return;
			}
			int coins = player.getInventory().getAmount(new Item(995));
			if (coins < offer.getAmount() * offer.getPrice() && offer.getType() == OfferType.BUY) {
				player.getPacketSender().sendMessage("You do not have enough coins to purchase this item.");
				return;
			}
			if (offer.getType() == OfferType.SELL && offer.getAmount() > player.getInventory().getAmount(new Item(offer.getId()))) {
				player.getPacketSender().sendMessage("You do not have enough of that item to sell.");
				return;
			}
			if (offer.getType() == OfferType.BUY)
				player.getInventory().remove(new Item(995, offer.getPrice() * offer.getAmount()));
			else
				player.getInventory().remove(new Item(offer.getId(), offer.getAmount()));
			mainInterface(player);
			if (!GrandExchange.getOffers().contains(offer))
				GrandExchange.getOffers().add(offer);
			PlayerWorldLoader.store(player, new File(Constants.SAVE_PATH + "" + player.getCredentials().getUsername() + "" + PlayerWorldLoader.suffix));
			switch (offer.getType()) {
			case BUY:
				player.getIOSession().write(new GrandExchangePacket(offer, (byte) 2));
				offer.setFinished(false);
				World.getWorld().submit(new Tick(2) {
					@Override
					public boolean run() {
						offer.setOfferedQuantity(offer.getAmount());
						player.getIOSession().write(new GrandExchangePacket(offer, (byte) 3));
						mainInterface(player);
						stop();
						return true;
					}
				});
				World.getWorld().submit(new Tick(4) {

					@Override
					public boolean run() {
						offer.setOfferedQuantity(offer.getAmount());
						player.getIOSession().write(new GrandExchangePacket(offer, (byte) 5));
						offer.setFinished(true);
						mainInterface(player);
						stop();
						return true;
					}

				});
				break;
			case SELL:
				player.getIOSession().write(new GrandExchangePacket(offer, (byte) 3));
				offer.setFinished(false);
				World.getWorld().submit(new Tick(2) {

					@Override
					public boolean run() {
						offer.setOfferedQuantity(offer.getAmount());
						player.getIOSession().write(new GrandExchangePacket(offer, (byte) 11));
						mainInterface(player);
						stop();
						return true;
					}

				});
				World.getWorld().submit(new Tick(4) {

					@Override
					public boolean run() {
						offer.setOfferedQuantity(offer.getAmount());
						player.getIOSession().write(new GrandExchangePacket(offer, (byte) 13));
						offer.setFinished(true);
						mainInterface(player);
						stop();
						return true;
					}
				});
				break;
			default:
				break;
			}
			break;
		}
	}

	/**
	 * Show the Main InterfacePacket.
	 * 
	 * @param player
	 *            The Owner.
	 */
	public static void mainInterface(Player player) {
		player.getIOSession().write(new ConfigPacket(player, 563, 4194304));
		player.getIOSession().write(new ConfigPacket(player, 1112, -1));
		player.getIOSession().write(new ConfigPacket(player, 1113, -1));
		player.getIOSession().write(new ConfigPacket(player, 1109, -1));
		player.getIOSession().write(new ConfigPacket(player, 1110, 0));
		player.getPacketSender().setDefaultInventory();
		player.getPacketSender().sendInterface(105);
		player.getIOSession().write(new AccessMask(player, -1, -1, 105, 209, 0, 6));
		player.getIOSession().write(new AccessMask(player, -1, -1, 105, 211, 0, 6));
		if (GrandExchange.getOffers() != null)
			for (ItemOffer o : GrandExchange.getOffers()) {
				if (o == null) continue;
				if (player.getCredentials().getUsername().equals(o.getOwner().getCredentials().getUsername())) {
					player.geOrdinal[o.getId()] = o.getType().ordinal();
					player.getIOSession().write(new GrandExchangePacket(o, o.getType() == OfferType.BUY ? (byte) 5 : (byte) 13));
				}
			}
	}

	/**
	 * Resets the Configurations of the InterfacePacket.
	 * 
	 * @param player
	 *            The Owner.
	 */
	private static void resetInterfaceConfigs(Player player) {
		player.getIOSession().write(new ConfigPacket(player, 1109, -1));
		player.getIOSession().write(new ConfigPacket(player, 1110, 0));
		player.getIOSession().write(new ConfigPacket(player, 1111, 0));
		player.getIOSession().write(new ConfigPacket(player, 1112, -1));
		player.getIOSession().write(new ConfigPacket(player, 1113, 0));
	}

	/**
	 * Show the Search InterfacePacket.
	 * 
	 * @param player
	 *            The Owner.
	 */
	private static void searchScript(Player player) {
		player.getIOSession().write(new ConfigPacket(player, 1109, -1));
		player.getIOSession().write(new ConfigPacket(player, 1112, 0));
		player.getIOSession().write(new ConfigPacket(player, 1113, 0));
		player.getIOSession().write(new CS2Config(player, 1241, 16750848));
		player.getIOSession().write(new CS2Config(player, 1242, 15439903));
		player.getIOSession().write(new CS2Config(player, 741, -1));
		player.getIOSession().write(new CS2Config(player, 743, -1));
		player.getIOSession().write(new CS2Config(player, 744, 0));
		player.getIOSession().write(new InterfacePacket(player, 752, 7, 389, false));
		player.getIOSession().write(new CS2Script(player, 570, "s", new Object[] { "Grand Exchange Item Search" }));
	}

	/**
	 * Gets the Box Slot id by switching the buttons,<br>	
	 * </br> Also helps you to figure the box slot configuration value.
	 * 
	 * @param buttonId
	 * @return Slot id.
	 */
	private static int getSlot(int buttonId) {
		switch (buttonId) {
		case 31:
		case 32:
		case 19:
			return 0;
		case 47:
		case 35:
		case 48:
			return 1;
		case 63:
		case 51:
		case 64:
			return 2;
		case 82:
		case 83:
		case 70:
			return 3;
		case 101:
		case 102:
		case 89:
			return 4;
		case 120:
		case 108:
		case 121:
			return 5;
		default:
			return -1;
		}
	}

	/**
	 * Sets the Items over the Reward Slot.
	 * 
	 * @param player
	 * 				The Owner.
	 * @param slot
	 * 				The Box Slot.
	 * @param item
	 * 				Items Reward Array.
	 */
	public static void setItemSlot(Player player, int slot, Item[] item) {
		player.getIOSession().write(new ContainerPacket(player, 523 + slot, item, false));
	}

	public static void sendSellItem(Player player, int itemId, int slot, int buttonId) {
		switch(buttonId) {
		case 18:
			if (player.getInventory().contains(itemId, 1)) {
				if (!ItemDefinition.forId(itemId).isTradable() || itemId == 995) {
					player.getPacketSender().sendMessage("You cannot sell this item to the grand exchange.");
					return;
				}
				final ItemOffer offer = new ItemOffer(player, itemId, player.offerType, (Integer) player.getGESlot());
				offer.setPrice((int) (offer.getPrice() * 0.75));
				player.getGeOffers()[offer.getSlot()] = offer;
				offer.getOwner().getIOSession().write(
						new ConfigPacket(offer.getOwner(), 1109, offer.getId()));
				offer.getOwner().getIOSession().write(
						new ConfigPacket(offer.getOwner(), 1110, 1));
				offer.getOwner().getIOSession().write(
						new ConfigPacket(offer.getOwner(), 1114, offer.getPrice()));
				offer.getOwner().getIOSession().write(
						new ConfigPacket(offer.getOwner(), 1115, offer.getDefinition().getLowAlch()));
				offer.getOwner().getIOSession().write(
						new ConfigPacket(offer.getOwner(), 1116, offer.getDefinition().getHighAlch()));
				offer.getOwner().getIOSession().write(
						new ConfigPacket(offer.getOwner(), 1111, offer.getPrice()));
				offer.getOwner().getIOSession().write(
						new StringPacket(offer.getOwner(), offer.getDefinition().getExamine(), 105, 143));
			}
			break;
		}
	}

	/*
	 * XXX - Notes
	 * 
	 * Configuration id 1109: sets the item offer
	 * Configuration id 1110: sets the item amount 
	 * Configuration id 1111: sets the market item price
	 * Configuration id 1112: changes the offer slot 
	 * Configuration id 1113: changes the interface to buy or sell offer 
	 * Configuration id 1114: sets the normal item price 
	 * Configuration id 1115: sets the minimum item price
	 * Configuration id 1116: sets the maximum item price
	 * 
	 */

}
