package com.sevador.content.grandExchange;

import java.io.Serializable;

import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Constants;

/**
 * Represent one item offer no matters the type.
 * 
 * @author Jefferson
 * 
 */
public class ItemOffer extends Item {
	
	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -2705020820488558530L;

	/**
	 * Offer Owner.
	 */
	private final Player owner;
	
	/**
	 * Box slot which the offer is located.
	 */
	private final int slot;
	
	/**
	 * The offer type, buy or sell.
	 */
	private OfferType type;
	
	/**
	 * The item price, normally this is changed.
	 */
	private int price;
	
	/**
	 * Other players offeredQuantity Quantity.
	 */
	private int offeredQuantity;
	
	/**
	 * Abort flag.
	 */
	private boolean aborted;
	
	/**
	 * Completed flag.
	 */
	private boolean finished;

	/**
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}
	
	/**
	 * @return the type
	 */
	public OfferType getType() {
		return type;
	}
	
	/**
	 * @param type - the type to set.
	 */
	public void setType(OfferType type) {
		this.type = type;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	
	/**
	 * @return the offeredQuantity
	 */
	public int getOfferedQuantity() {
		return offeredQuantity;
	}

	/**
	 * @param offeredQuantity the offeredQuantity to set
	 */
	public void setOfferedQuantity(int offeredQuantity) {
		this.offeredQuantity = offeredQuantity;
	}

	/**
	 * @return the aborted
	 */
	public boolean isAborted() {
		return aborted;
	}

	/**
	 * @param aborted the aborted to set
	 */
	public void setAborted(boolean aborted) {
		this.aborted = aborted;
	}

	/**
	 * Check if this offer is Completed.
	 * 
	 * @return
	 */
	public boolean isCompleted() {
		return (getAmount() - offeredQuantity) == 0 || isAborted();
	}
	
	/**
	 * Returns the Percentage of the Offer.
	 * 
	 * @return
	 */
	public int getPercentage() {
		return (getAmount() - offeredQuantity) / 100;
	}
	
	/**
	 * This plus the Amount and the Price.
	 * 
	 * @return
	 */
	public int getTotal() {
		return getAmount() + getPrice();
	}
	
	/**
	 * Increases one to the Quantity.
	 */
	public void increase() {
		increase(1);
	}
	
	/**
	 * Increases a determinate Quantity to the Item.
	 * 
	 * @param quantity
	 */
	public void increase(int quantity) {
		if (getAmount() == Constants.MAX_AMOUNT)
			return;
		setAmount(getAmount() + quantity);
	}
	
	/**
	 * Decreases one to the Quantity.
	 */
	public void decrease() {
		decrease(1);
	}
	
	/**
	 * Decreases a determinate Quantity to the Item.
	 * 
	 * @param Quantity
	 */
	public void decrease(int Quantity) {
		if (getAmount() == 0 || (getAmount() - Quantity) < 0)
			return;
		setAmount(getAmount() - Quantity);
	}
	
	/**
	 * Creates an Offer.
	 * 
	 * @param username
	 * @param itemId
	 * @param type
	 * @param slot
	 */
	public ItemOffer(Player player, int itemId, OfferType type, int slot) {
		super(itemId);
		this.owner = player;
		this.type = type;
		this.slot = slot;
		this.price = (this.getDefinition().getGEPrice() == -1 ? this.getDefinition().getStorePrice() : this.getDefinition().getGEPrice());
		this.offeredQuantity = 0;
		this.aborted = false;
		this.finished = false;
	}
	
	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	/**
	 * Represents the Offer type.
	 * 
	 * @author Jefferson
	 *
	 */
	public enum OfferType implements Serializable {
		
		BUY, SELL;
		
	}

}
