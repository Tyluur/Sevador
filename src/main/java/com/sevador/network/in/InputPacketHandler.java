package com.sevador.network.in;

import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles incoming input packets.
 * @author Emperor
 *
 */
public final class InputPacketHandler implements PacketSkeleton {

	/**
	 * The integer input opcode.
	 */
	private static final int STRING_OPTION = 59, INTEGER_INPUT = 15;
	
	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		case STRING_OPTION:
			InputHandler.handleInput(player, packet.readRS2String());
			return true;
		case INTEGER_INPUT:
			InputHandler.handleInput(player, packet.readInt());
			return true;
		}
		return false;
	}

}