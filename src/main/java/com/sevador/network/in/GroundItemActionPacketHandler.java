package com.sevador.network.in;

import com.sevador.game.action.impl.movement.GroundItemAction;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles incoming ground item action packets.
 * @author Emperor
 *
 */
public class GroundItemActionPacketHandler implements PacketSkeleton {
	
	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int y = packet.readShort();
		int itemId = packet.readShort();
		int x = packet.readShort();
		boolean running = packet.readByte() == 1;
		player.getActionManager().register(new GroundItemAction(player, x, y, itemId, running));
		//System.out.println("Incoming ground item action packet - [" + x + ", " + y + ", " + itemId + ", " + running + "].");
		return true;
	}

}