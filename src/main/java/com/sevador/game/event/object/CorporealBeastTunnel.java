package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 *
 */
public class CorporealBeastTunnel implements ObjectEvent {

	/* (non-Javadoc)
	 * @see org.argonite.game.event.ObjectEvent#init()
	 */
	@Override
	public boolean init() {
		return EventManager.register(38811, this);
	}

	/* (non-Javadoc)
	 * @see org.argonite.game.event.ObjectEvent#handle(org.argonite.game.node.model.player.Player, org.argonite.game.node.model.gameobject.GameObject, org.argonite.util.OptionType)
	 */
	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		if (player.getLocation().getX() < 2971) {
			player.teleportToLocation(Location.locate(2974, 4384, 2));
		} else {
			player.teleportToLocation(Location.locate(2970, 4384, 2));
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.argonite.game.event.ObjectEvent#setDestination(org.argonite.game.node.model.player.Player, org.argonite.game.node.model.gameobject.GameObject)
	 */
	@Override
	public void setDestination(Player p, GameObject obj) {
		p.setAttribute("m_o_d", obj.getLocation());
	}

}
