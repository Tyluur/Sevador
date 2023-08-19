package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.content.grandExchange.GrandExchange;
import com.sevador.content.grandExchange.ItemOffer;
import com.sevador.content.grandExchange.ItemOffer.OfferType;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;
import com.sevador.network.out.GrandExchangePacket;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class GEOffer extends Dialogue {

	OfferType type;
	int geSlot, itemId, amount, price;

	@Override
	public void start() {
		type = (OfferType) parameters[0];
		geSlot = ((Integer) parameters[1]).intValue();
		itemId = ((Integer) parameters[2]).intValue();
		amount = ((Integer) parameters[3]).intValue();
		price = ((Integer) parameters[4]).intValue();
		sendDialogue((short) 236, new String[] { "Choose an option",
				"Cancel", "Claim offer." });
	}

	@Override
	public void run(int interfaceId, int componentId) {
		OfferType type = null;
		System.err.print(player.geOrdinal[itemId]);
		switch(player.geOrdinal[itemId]) {
		case 0:
			type = OfferType.BUY;
			break;
		case 1:
			type = OfferType.SELL;
			break;
		}
		ItemOffer offer = new ItemOffer(player, itemId, type, player.getGESlot());
		offer.setAmount(amount);
		offer.setPrice(price);
		if (stage == -1) {
			if (!GrandExchange.getOffers().contains(offer)) {
				player.getPacketSender().sendMessage("That offer has already been processed.");
				player.getIOSession().write(new GrandExchangePacket(offer, (byte) 8));
				GrandExchange.getOffers().remove(offer);
				end();
				return;
			}
			if (player.getInventory().freeSlots() == 0) {
				player.getPacketSender().sendMessage("You need to have some inventory space open.");
				return;
			}
			switch(componentId) {
			case 1:
				if (offer.isFinished()) {
					player.getPacketSender().sendMessage("You cannot abort an offer that is already completed!");
					return;
				}
				switch(offer.getType()) {
				case BUY:
					player.getInventory().add(new Item(995, offer.getPrice()));
					player.getIOSession().write(new GrandExchangePacket(offer, (byte) 8));
					GrandExchange.getOffers().remove(offer);
					end();
					break;
				case SELL:
					if (player.getInventory().freeSlots() < offer.getAmount() && !ItemDefinition.forId(itemId).isStackable()) 
						player.getInventory().add(new Item(ItemDefinition.forId(itemId).noteId, offer.getAmount()));
					else
						player.getInventory().add(new Item(offer.getId(), offer.getAmount() < 1 ? 1 : offer.getAmount()));
					player.getIOSession().write(new GrandExchangePacket(offer, (byte) 8));
					GrandExchange.getOffers().remove(offer);
					end();
					break;
				}
				break;
			case 2:
				if (offer.getType() == null) { 
					player.getPacketSender().sendMessage("This offer has no offer type.");
					return;
				}
				switch(offer.getType()) {
				case BUY:
					if (player.getInventory().freeSlots() < offer.getAmount() && !ItemDefinition.forId(itemId).isStackable()) 
						player.getInventory().add(new Item(ItemDefinition.forId(itemId).noteId, offer.getAmount()));
					else
						player.getInventory().add(new Item(offer.getId(), offer.getAmount()));
					player.getIOSession().write(new GrandExchangePacket(offer, (byte) 8));
					player.getPacketSender().sendMessage("You have claimed your grand exchange offer which you were buying.");
					GrandExchange.getOffers().remove(offer);
					end();	
					break;
				case SELL:
					player.getInventory().add(new Item(995, offer.getPrice() * offer.getAmount()));
					player.getIOSession().write(new GrandExchangePacket(offer, (byte) 8));
					GrandExchange.getOffers().remove(offer);
					player.getPacketSender().sendMessage("You have claimed your grand exchange offer which you were selling.");
					end();	
					break;
				}
				break;
			}
		}
	}

	@Override
	public void finish() {

	}

}
