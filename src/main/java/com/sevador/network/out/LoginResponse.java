package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Constructs the login response packet.
 * @author Emperor
 *
 */
public class LoginResponse extends OutgoingPacket {

	/**
	 * Constructs a new {@code LoginResponse} {@code Object}.
	 * @param player The player.
	 */
	public LoginResponse(Player player) {
		super(player);
	}
	
	@Override
	public OutgoingPacket get() {
		put(13 + 1); //length
		put(getPlayer().getCredentials().getRights());
		put(0);
		put(0);
		put(0);
		put(1); //1
		put(0);
		putShort(getPlayer().getIndex());
		put(1);
		putMedium(0);
		put(1); // members
		putRS2String("");
		return this;
	}

}
