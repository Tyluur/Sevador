package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class GateLumbridgeEvent implements ObjectEvent {

	@Override
	public boolean init() {
		EventManager.register(45212, this);
		return EventManager.register(45210, this);
	}

	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		if (player.getX() == 3253) {
			player.getProperties().setTeleportLocation(new Location(player.getX() - 1, player.getY(), 0));
		} else {
			player.getProperties().setTeleportLocation(new Location(player.getX() + 1, player.getY(), 0));
		}
		return true;
	}

	@Override
	public void setDestination(Player player, GameObject obj) {
		
	}

}
