package com.sevador.game.node.model.container.impl;

import java.text.NumberFormat;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.ContainerListener;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ContainerPacket;
import com.sevador.network.out.StringPacket;

/**
 * Handles the trade container listening.
 * 
 * @author Emperor
 * 
 */
public class TradeContainerListener implements ContainerListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The player being traded with.
	 */
	private final Player other;

	/**
	 * Constructs a new {@code TradeContainerListener} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 * @param other
	 *            The other player.
	 */
	public TradeContainerListener(Player player, Player other) {
		this.player = player;
		this.other = other;
	}

	@Override
	public boolean itemChanged(Container container, int slot) {
		return refresh(container);
	}

	@Override
	public boolean itemsAdded(Container container, Item... items) {
		return refresh(container);
	}

	@Override
	public boolean itemsRemoved(Container container, Item... items) {
		return refresh(container);
	}

	@Override
	public boolean refresh(Container container) {
		int price = 0;
		for (Item item : container.toArray()) {
			if (item == null)
				continue;
			price += item.getDefinition().getStorePrice();
			if (item.getId() == 995)
				price += item.getAmount();
		}
		NumberFormat nf = NumberFormat.getInstance();
		String color = "" + (price > 1000000 ? "FF0000" : "FFFFFF");
		player.getIOSession().write(
				new StringPacket(player, " <col=" + color + ">"
						+ nf.format(price) + " coins", 335, 45));
		other.getIOSession().write(
				new StringPacket(player, " <col=" + color + ">"
						+ nf.format(price) + " coins", 335, 45));
		other.setAttribute("trade:accepted", false);
		player.setAttribute("trade:accepted", false);
		other.getIOSession().write(new StringPacket(other, "", 335, 37));
		player.getIOSession().write(new StringPacket(other, "", 335, 37));
		player.getIOSession().write(
				new ContainerPacket(player, 90, container, false));
		other.getIOSession().write(
				new ContainerPacket(other, 90, container, true));
		other.getIOSession().write(
				new StringPacket(other, " has "
						+ player.getInventory().freeSlots()
						+ " free inventory slots.", 335, 21));
		return true;
	}

}