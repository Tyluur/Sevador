package com.sevador.network.in;

import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles the unhandled packets.
 * 
 * @author Emperor
 * 
 */
public class DefaultPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
//		if (Main.DEBUG) {
//			System.out.println(new StringBuilder("Unhandled packet - [")
//					.append(packet.getOpcode()).append(", ")
//					.append(packet.getSize()).append("]."));
//		}
		packet.getBuffer().discardReadBytes();
		return true;
	}

}