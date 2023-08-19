package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.minigames.pestcontrol.PestControlObjects;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 *
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class PestControlObject implements ObjectEvent{

	@Override
	public boolean init() {
		EventManager.register(14315, this);
		return EventManager.register(14314, this);
	}

	@Override
	public boolean handle(Player p, GameObject obj, OptionType type) {
		if (PestControlObjects.handle(p, obj)) {
			return true;
		}
		return true;
	}

	@Override
	public void setDestination(Player p, GameObject obj) {
		
	}

}
