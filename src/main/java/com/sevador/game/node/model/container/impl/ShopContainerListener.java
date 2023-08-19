package com.sevador.game.node.model.container.impl;

import java.util.List;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.ContainerListener;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ContainerPacket;

/**
 * Handles the listening for a shop container.
 * @author Emperor
 *
 */
public final class ShopContainerListener implements ContainerListener {

	/**
	 * The players currently viewing this shop.
	 */
	private final List<Player> players;
	
	/**
	 * Constructs a new {@code ShopContainerListener} {@code Object}.
	 * @param players The players viewing this shop.
	 */
	public ShopContainerListener(List<Player> players) {
		this.players = players;
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
		for (Player p : players) {
			p.getIOSession().write(new ContainerPacket(p, 4, container, false));
		}
		return true;
	}

}