package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.skills.thieving.StallThieving;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 * 
 */
public class ThievingStallAction implements ObjectEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.event.ObjectEvent#init()
	 */
	@Override
	public boolean init() {
		EventManager.register(4874, this);
		EventManager.register(4875, this);
		EventManager.register(4876, this);
		EventManager.register(4877, this);
		return EventManager.register(4878, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ObjectEvent#handle(org.argonite.game.node.model
	 * .player.Player, org.argonite.game.node.model.gameobject.GameObject,
	 * org.argonite.util.OptionType)
	 */
	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		player.getSkillAction().setSkill(new StallThieving(obj));
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ObjectEvent#setDestination(org.argonite.game.
	 * node.model.player.Player,
	 * org.argonite.game.node.model.gameobject.GameObject)
	 */
	@Override
	public void setDestination(Player p, GameObject obj) {
		p.setAttribute("m_o_d", obj.getLocation());
	}

}
