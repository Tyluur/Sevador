package com.sevador.game.minigames.pestcontrol;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;

/**
 * 
 * @author 'Mystic Flow
 */
public class PestControlObjects {

	public static boolean handle(Player player, GameObject obj) {
		switch (obj.getId()) {
		case 14315: // Enter easy
			return PestControlActivity.EASY_PC.onEnter(player);
		case 14314: // Easy boat leave
			if (PestControlActivity.EASY_PC.onLeave(player))
			player.getProperties().setTeleportLocation(Location.locate(2657, 2639, 0));
			return true;
		}
		return false;
	}

}
