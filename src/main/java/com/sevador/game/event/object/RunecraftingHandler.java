package com.sevador.game.event.object;

import com.sevador.game.action.impl.RuneCraftingAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 * 
 */
public class RunecraftingHandler implements ObjectEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.event.ObjectEvent#init()
	 */
	@Override
	public boolean init() {
		EventManager.register(2478, this);// Altar
		EventManager.register(2479, this);// Altar
		EventManager.register(2480, this);// Altar
		EventManager.register(2481, this);// Altar
		EventManager.register(2482, this);// Altar
		EventManager.register(2483, this);// Altar
		EventManager.register(2484, this);// Altar
		EventManager.register(2485, this);// Altar
		EventManager.register(2486, this);// Altar
		EventManager.register(2487, this);// Altar
		EventManager.register(30624, this);
		return EventManager.register(2488, this);// Altar

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
		player.getActionManager().register(new RuneCraftingAction(player, obj));
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
		obj.getLocation();
	}

}
