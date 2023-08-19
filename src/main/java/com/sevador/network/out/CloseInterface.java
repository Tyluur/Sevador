package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * The packet used for closing an interface.
 * @author Tyluur
 *
 */
public class CloseInterface extends OutgoingPacket {

	/**
	 * The window id.
	 */
	private final int window;
	
	/**
	 * The tab id.
	 */
	private final int tab;
	
	/**
	 * Constructs a new {@code CloseInterface} {@code Object}.
	 * @param player The player.
	 * @param window The window id.
	 * @param tab The tab id.
	 */
	public CloseInterface(Player player, int window, int tab) {
		super(player, 29);
		this.window = window;
		this.tab = tab;
	}
	
	@Override
	public OutgoingPacket get() {
		putInt2(window << 16 | tab);
		return this;
	}

}
