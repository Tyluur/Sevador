package com.sevador.game.event.object;

import com.sevador.game.action.impl.ForceMovementAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles wilderness wall events.
 * @author Emperor
 *
 */
public final class WildernessWallEvent implements ObjectEvent {

	/**
	 * The crossing the wilderness ditch animation.
	 */
	private static final Animation CROSS = new Animation(6703, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(1440, this);
		EventManager.register(1441, this);
		EventManager.register(1442, this);
		EventManager.register(1443, this);
		return EventManager.register(1444, this);
	}

	@Override
	public boolean handle(Player p, GameObject obj, OptionType type) {
        boolean north = p.getLocation().getY() > 3520;
        int dir = north ? 2 : 0;
        int y = north ? 3520 : 3523;
        p.getActionManager().register(new ForceMovementAction(p, CROSS, p.getLocation().getX(), y, 33, 60, dir, 2));
		return true;
	}

	@Override
	public void setDestination(Player p, GameObject obj) {
		if (p.getLocation().getY() <= 3520) {
			p.setAttribute("m_o_d", Location.locate(obj.getLocation().getX(), 3520, 0));
		} else {
			p.setAttribute("m_o_d", Location.locate(obj.getLocation().getX(), 3523, 0));
		}
	}

}