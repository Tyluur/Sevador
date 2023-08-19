package com.sevador.network.in;

import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ColorSelectHandler implements PacketSkeleton {

	@Override
	public boolean execute(com.sevador.game.node.player.Player player,
			IncomingPacket packet) {
		int color = packet.readShort();
		if (player.getAttribute("color_editing_index") != null) {
			int colorIndex = player.getAttribute("color_editing_index", 0);
			player.getCapeRecolouring().getColours()[colorIndex] = color;
			player.removeAttribute("color_editing_index");
		}
		return true;
	}

}
