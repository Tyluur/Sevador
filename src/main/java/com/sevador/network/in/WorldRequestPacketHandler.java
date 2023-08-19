package com.sevador.network.in;

import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;
import com.sevador.utility.world.WorldList;

/**
 * The world list request packet handler.
 * @author Emperor
 *
 */
public class WorldRequestPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		if (player.getIOSession().isInLobby()) {
			player.getIOSession().write(WorldList.getData(player, true, true));
		}
		return false;
	}

}
