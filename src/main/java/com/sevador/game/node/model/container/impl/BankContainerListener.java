package com.sevador.game.node.model.container.impl;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.ContainerListener;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.ContainerPacket;

/**
 * A ContainerListener implementation handling the bank container.
 * @author Emperor
 *
 */
public class BankContainerListener implements ContainerListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code BankContainerListener} {@code Object}.
	 * @param player The player.
	 */
	public BankContainerListener(Player player) {
		this.player = player;
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
		if (player.getIOSession() != null)
			player.getIOSession().write(new ContainerPacket(player, 95, container, false));
		sendTabConfig();
		return true;
	}

	/**
	 * Sends the tab config.
	 */
	public void sendTabConfig() {
		int config = 0;
		config += player.getBank().getItemsInTab(2);
		config += player.getBank().getItemsInTab(3) << 10;
		config += player.getBank().getItemsInTab(4) << 20;
		if (player.getIOSession() != null)
			player.getIOSession().write(new ConfigPacket(player, 1246, config));
		config = 0;
		config += player.getBank().getItemsInTab(5);
		config += player.getBank().getItemsInTab(6) << 10;
		config += player.getBank().getItemsInTab(7) << 20;
		if (player.getIOSession() != null)
			player.getIOSession().write(new ConfigPacket(player, 1247, config));
		int tab = player.getAttribute("currentTab", 10);
		config = -2013265920;
		config += (134217728 * (tab == 10 ? 0 : tab - 1));
		config += player.getBank().getItemsInTab(8);
		config += player.getBank().getItemsInTab(9) << 10;
		if (player.getIOSession() != null)
			player.getIOSession().write(new ConfigPacket(player, 1248, config));
	}

}