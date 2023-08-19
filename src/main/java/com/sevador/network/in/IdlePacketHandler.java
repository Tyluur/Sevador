package com.sevador.network.in;

import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles an incoming idle packet.
 * 
 * @author Emperor
 *
 */
public final class IdlePacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		//packet.readShort();
		int time = packet.readInt();
		System.err.println("Idle Packet Handler called for player: " + player.getCredentials().getUsername() + ": TIME: " + time);
		return true;
	}

}