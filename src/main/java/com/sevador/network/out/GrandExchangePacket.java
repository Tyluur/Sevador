package com.sevador.network.out;

import com.sevador.content.grandExchange.ItemOffer;
import com.sevador.network.OutgoingPacket;

/**
 * Sets the Offer Data.
 * 
 * @author Jefferson
 * 
 */
public class GrandExchangePacket extends OutgoingPacket {

	/**
	 * The offer to be set.
	 */
	private final ItemOffer offer;
	
	/**
	 * The offer progress type.
	 */
	private final byte progressType;
	
	/**
	 * Creates a new packet.
	 * 
	 * @param player
	 * @param offer
	 */
	public GrandExchangePacket(ItemOffer offer, byte progressType) {
		super(offer.getOwner(), 7);
		this.offer = offer;
		this.progressType = progressType;
	}
	
	@Override
	public OutgoingPacket get() {
		put(offer.getSlot());
		put(progressType);
		putShort(offer.getId());
		putInt(offer.getPrice());
		putInt(offer.getAmount());
		putInt(offer.getOfferedQuantity());
		putInt(offer.getPrice() * offer.getOfferedQuantity());
		return this;
	}
	
	/*
	 * XXX - Notes
	 * 
	 * Progress type values:
	 * 
	 * 0 empty slot
	 * 1 buy and submitting message
	 * 2 buy 10%
	 * 5 cancelled buy offer if the offeredAmount is not equal as offerAmount
	 * 8 empty slot
	 * 9 sell and submitting message
	 * 11 sell 10%
	 * 13 cancelled sell offer if the offeredAmount is not equal as offerAmount
	 * 
	 */

}
