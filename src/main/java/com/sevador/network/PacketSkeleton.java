package com.sevador.network;

import com.sevador.game.node.player.Player;

/**
 * The interface implemented by all the packet handlers.
 * 
 * @author Emperor
 *
 */
public interface PacketSkeleton {

	/**
	 * Executes the packet.
	 * @param player The player.
	 * @param packet The packet.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public boolean execute(Player player, IncomingPacket packet);
	
}