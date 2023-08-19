package com.sevador.network.out;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Creates a new sendContainer packet.
 * @author Emperor
 *
 */
public class ContainerPacket extends OutgoingPacket {

	//TODO CONVERT TO 666!!!
	/**
	 * The type.
	 */
	private final int type;
	
	/**
	 * The container of items to send.
	 */
	private final Item[] container;
	
	/**
	 * If split interfaces.
	 */
	private final boolean split;
	
	/**
	 * Constructs a new {@code ContainerPacket} {@code Object}.
	 * 
	 * @param player The player.
	 * @param type The container type.
	 * @param container The container.
	 * @param split If split interfaces.
	 */
	public ContainerPacket(Player player, int type, Container container, boolean split) {
		this(player, type, container.toArray(), split);
	}
	
	/**
	 * Constructs a new {@code ContainerPacket} {@code Object}.
	 * 
	 * @param player The player.
	 * @param type The container type.
	 * @param container The container array.
	 * @param split If split interfaces.
	 */
	public ContainerPacket(Player player, int type, Item[] container, boolean split) {
		super(player, 122, PacketType.VAR_SHORT);
		this.type = type;
		this.container = container;
		this.split = split;
	}
	
	@Override
	public OutgoingPacket get() {
		putShort(type);
		put(split ? 1 : 0);
		putShort(container.length);
		for (int i = 0; i < container.length; i++) {
			Item item = container[i];
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getId();
				amt = item.getAmount();
			}
			put(amt > 254 ? 255 : amt);
			if (amt > 254)
				putInt1(amt);
			putLEShortA(id + 1);
		}
		return this;
	}

}
