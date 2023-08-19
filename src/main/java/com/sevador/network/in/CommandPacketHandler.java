package com.sevador.network.in;

import com.sevador.content.Command;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles a command sent from the command panel.
 * 
 * @author Emperor
 * 
 */
public class CommandPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(final Player player, IncomingPacket packet) {
		if (packet.getBuffer().readableBytes() < 1) {
			return false;
		}
		packet.readUnsignedByte();
		packet.readUnsignedByte();
		String command = packet.readRS2String().toLowerCase();
		Command.handleCommand(player, command);
		return true;
	}

}