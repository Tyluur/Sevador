package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * The outgoing packet used to log a player out.
 * @author Emperor
 *
 */
public class LogoutPacket extends OutgoingPacket {

	/**
	 * The button id used.
	 */
	private final int buttonId;

	/**
	 * Constructs a new {@code LogoutPacket} {@code Object}.
	 * @param player The player.
	 * @param buttonId The button id.
	 */
	public LogoutPacket(Player player, int buttonId) {
		super(player);
		this.buttonId = buttonId;
	}

	@Override
	public OutgoingPacket get() {
		setOpcode(buttonId == 6 ? 59 : 126);
		return this;
	}

}
