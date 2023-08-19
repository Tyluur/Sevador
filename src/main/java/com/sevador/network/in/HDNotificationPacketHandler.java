package com.sevador.network.in;

import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles the incoming hd notification packet.
 * @author Emperor
 *
 */
public class HDNotificationPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int screenSizeMode = packet.readByte();
		int screenSizeX = packet.readShort();
		int screenSizeY = packet.readShort();
		int displayMode = packet.readByte();
		if (screenSizeMode < 0 || screenSizeMode > 3) {
			//System.out.println("Incorrect screen sizing mode: " + screenSizeMode);
			return false;
		}
		switch (packet.getOpcode()) {
		case 34:
			boolean send = false;
			if (screenSizeMode != player.getIOSession().getScreenSizeMode()) {
				send = true;
			}
			player.getIOSession().setScreenSizeMode(screenSizeMode);
			player.getIOSession().setScreenSizeX(screenSizeX);
			player.getIOSession().setScreenSizeY(screenSizeY);
			player.getIOSession().setDisplayMode(displayMode);
			if (send) {
				player.getPacketSender().sendLoginInterfaces();
				player.getPacketSender().sendInterface(742);
				if (screenSizeMode < 2) {
					player.getPacketSender().sendFixedAMasks();
				} else {
					player.getPacketSender().sendFullScreenAMasks();
				}
			}
			break;
		case 2:
			break;
		}
		return true;
	}

}
