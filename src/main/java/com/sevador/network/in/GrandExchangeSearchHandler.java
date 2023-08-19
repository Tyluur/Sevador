package com.sevador.network.in;

import com.sevador.content.grandExchange.ItemOffer;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.StringPacket;

/**
 * Retrieves the Item id from the Search interface.
 * 
 * @author Jefferson
 * 
 */
public class GrandExchangeSearchHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		final short itemId = packet.readShort();
		final ItemOffer offer = new ItemOffer(player, itemId, player.offerType, player.getGESlot());
		offer.setPrice((int) (offer.getPrice() * 1.25));
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
		return true;
	}

}
