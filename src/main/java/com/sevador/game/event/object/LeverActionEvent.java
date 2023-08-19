package com.sevador.game.event.object;

import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles the lever object action events.
 * @author Emperor
 *
 */
public final class LeverActionEvent implements ObjectEvent {

	/**
	 * The animation used to "pull the lever".
	 */
	private static final Animation ANIMATION = new Animation(2140, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(1814, this);
		EventManager.register(1815, this);
		EventManager.register(5959, this);
		return EventManager.register(5960, this);
	}

	@Override
	public boolean handle(Player p, GameObject obj, OptionType type) {
		Location location;
		switch (obj.getId()) {
		case 1814:
			location = Location.locate(3153, 3923, 0);
			break;
		case 1815:
			location = Location.locate(3090, 3474, 0);
			break;
		case 5959:
			location = Location.locate(2539, 4716, 0);
			break;
		case 5960:
			location = Location.locate(3090, 3956, 0);
			break;
		default:
			return false;
		}
		p.getUpdateMasks().register(ANIMATION);
		p.getPacketSender().sendMessage("You pull the lever...");
		p.getActionManager().register(new TeleportAction(p, location,
				TeleportAction.MODERN_ANIM, TeleportAction.MODERN_GRAPHIC,
				TeleportAction.MODERN_END_ANIM, TeleportAction.MODERN_END_GRAPHIC,
				2, 5, 6, false));
		return true;
	}

	@Override
	public void setDestination(Player p, GameObject obj) {
		p.setAttribute("m_o_d", obj.getLocation());
	}

}