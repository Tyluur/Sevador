package com.sevador.network.in;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.action.impl.TradeAction;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles the accept request packets.
 * @author Emperor
 *
 */
public final class AcceptRequestPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int index = packet.readShort();
		int type = packet.readByte();
		if (index < 1 || index > 2047) {
			return false;
		}
		Player p = NodeWorker.getPlayers().get(index);
		if (p == null) {
			return false;
		}
		switch (type) {
		case 0: //Trade
			player.getActionManager().register(new TradeAction(player, p));
			return true;
		}
		return true;
	}

}