package com.sevador.game.node.model.container.impl;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.ContainerListener;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ContainerPacket;

/**
 * The inventory container listener, used for updating the player's inventory.
 * @author Emperor
 *
 */
public class InventoryContainerListener implements ContainerListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code EquipmentContainerListener} {@code Object}.
	 * @param player The player.
	 */
	public InventoryContainerListener(Player player) {
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
			player.getIOSession().write(new ContainerPacket(player, 93, container, false));
		return true;
	}

}
