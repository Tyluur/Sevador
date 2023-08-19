package com.sevador.network.in;

import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles an incoming mouse click packet.
 * @author Emperor
 *
 */
public class MouseClickPacketHandler implements PacketSkeleton {

	@SuppressWarnings("unused")
	@Override
	public boolean execute(Player player, IncomingPacket packet) {
        int clickData = packet.readLEShortA();
        int timePassed = clickData & 0x7FFF;
        boolean leftClick = (clickData >> 15) == 0;
        int positionData = packet.readLEInt();
        int screenClickX = positionData >> 16;
        int screenClickY = positionData & 0xFFFF;
        //System.out.println("Mouse clicked: [left click=" + leftClick + ", last click " + timePassed + "ms, x=" + screenClickX + ", y=" + screenClickY + "].");
		return true;
	}

}