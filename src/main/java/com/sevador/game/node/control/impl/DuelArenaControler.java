package com.sevador.game.node.control.impl;

import com.sevador.game.node.control.Controler;
import com.sevador.game.node.model.Location;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class DuelArenaControler extends Controler {

	@Override
	public void start() {

	}

	public static boolean isAtDuelArena(Location player) {
		return (player.getX() >= 3355 && player.getX() <= 3360
				&& player.getY() >= 3267 && player.getY() <= 3279)
				|| (player.getX() >= 3355 && player.getX() <= 3379
						&& player.getY() >= 3272 && player.getY() <= 3279)
				|| (player.getX() >= 3374 && player.getX() <= 3379
						&& player.getY() >= 3267 && player.getY() <= 3271);
	}
}
