package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.skills.summoning.Summoning;
import com.sevador.game.node.player.Player;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SummoningPouchCreation implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(672, this);
		return EventManager.register(666, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		switch(opcode) {
		case 85:
			switch(buttonId) {
			case 16:
				Summoning.createPouch(player, itemId, 1);
				break;
			}
			break;
		case 7:
			switch(buttonId) {
			case 16:
				Summoning.createPouch(player, itemId, 5);
				break;
			}
			break;
		case 66:
			switch(buttonId) {
			case 16:
				Summoning.createPouch(player, itemId, 10);
				break;
			}
			break;
		case 1:
			switch(buttonId) {
			case 16:
				Summoning.createPouch(player, itemId, Integer.MAX_VALUE - 1);
				break;
			}
			break;
		}
		return true;
	}

}
