package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles a bank booth action event.
 * @author Emperor
 *
 */
public final class BankBoothActionEvent implements ObjectEvent {

	@Override
	public boolean init() {
		EventManager.register(42217, this);
		EventManager.register(42377, this);
		EventManager.register(42378, this);
		return EventManager.register(782, this);
	}

	@Override
	public boolean handle(Player p, GameObject obj, OptionType type) {
		switch (type) {
		case FIRST:
			p.getDialogueManager().startDialogue("BankerEvent", new Object[]{ 494 } );
			break;
		case SECOND:
			p.getBank().open();
			return true;
		case FOURTH:
			break;
		case THIRD:
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public void setDestination(Player p, GameObject obj) { }

}