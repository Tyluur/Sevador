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
public class StairsCaseEvent implements ObjectEvent {

	@Override
	public boolean init() {
		EventManager.register(4493, this);
		EventManager.register(4494, this);
		EventManager.register(4495, this);
		return true;
	}

	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		player.getProperties().setTeleportLocation(new Location(obj.getLocation().getX(), obj.getLocation().getY(), obj.getLocation().getZ() + 1));
		return true;
	}

	@Override
	public void setDestination(Player player, GameObject obj) {
		
	}

}
