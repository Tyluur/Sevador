package com.sevador.network.in;

import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles an incoming mouse motion packet.
 * @author Emperor
 *
 */
public class MouseMotionPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		if (packet.getBuffer().readableBytes() > 0) {
			packet.readByte();
			//System.out.println("Mouse motion packet handled; " + packet.readByte());
		}
		return true;
	}

}