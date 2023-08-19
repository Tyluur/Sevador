package com.sevador.game.misc;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.action.impl.ShoppingAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.container.impl.ShopContainerListener;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.ContainerPacket;
import com.sevador.network.out.StringPacket;

/**
 * Represents a shop.
 * @author Emperor
 *
 */
public final class Shop {

	/**
	 * The maximum amount of items in a shop.
	 */
	public static final int SIZE = 40;
	
	/**
	 * The client script parameters.
	 */
	private static final Object[] SELL_PARAMS = new Object[] {
		"Sell 50", "Sell 10", "Sell 5", "Sell 1", "Value", -1, 1, 7, 4, 93, 40697856
	};
	
	/**
	 * The shop id.
	 */
	private final int id;
	
	/**
	 * The shop name.
	 */
	private final String name;
	
	/**
	 * The shop item container.
	 */
	private final Container container;
	
	/**
	 * The players viewing this shop.
	 */
	private final List<Player> players;
	
	/**
	 * The players viewing this shop.
	 */
	private final List<Item> original;
	
	/**
	 * Constructs a new {@code Shop} {@code Object}.
	 * @param id The shop id.
	 * @param name The shop name.
	 */
	public Shop(int id, String name) {
		this.id = id;
		this.name = name;
		this.players = new ArrayList<Player>();
		this.original = new ArrayList<Item>();
		this.container = new Container(Type.SHOP, SIZE, new ShopContainerListener(players));
	}
	
	/**
	 * Opens the shop.
	 * @param player The player to open for.
	 */
	public void open(Player player) {
		if (player.getAttribute("open:shop") != null || players.contains(player)) {
			return;
		}
		players.add(player);
		player.setAttribute("open:shop", this);
		player.getIOSession().write(new StringPacket(player, name, 620, 20));
		player.getIOSession().write(new ConfigPacket(player, 118, 4));
		player.getIOSession().write(new ConfigPacket(player, 1496, -1));
		player.getIOSession().write(new ConfigPacket(player, 532, 995));
		player.getPacketSender().sendInterface(620);
		sendInventory(player);
		player.getIOSession().write(new AccessMask(player, 0, 12, 620, 26, 0, 1150));
		player.getIOSession().write(new AccessMask(player, 0, 240, 620, 25, 0, 1150));
		player.getIOSession().write(new ContainerPacket(player, 4, container, false));
		player.getActionManager().register(new ShoppingAction(player, this));
	}
	
	/**
	 * Sends the inventory interface packets.
	 * @param player The player.
	 */
	public static void sendInventory(Player player) {
		player.getPacketSender().sendInventoryInterface(621);
		player.getIOSession().write(new CS2Script(player, 149, "IviiiIsssss", SELL_PARAMS));
		player.getIOSession().write(new AccessMask(player, 0, 27, 621, 0, 36, 1086));
		player.getIOSession().write(new ContainerPacket(player, 93, player.getInventory(), false));
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the container
	 */
	public Container getContainer() {
		return container;
	}


	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the original
	 */
	public List<Item> getOriginal() {
		return original;
	}
}