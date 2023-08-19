package com.sevador.game.event.button;

import com.sevador.game.action.impl.packetactions.EmoteAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;

/**
 * Handles an emote tab button event.
 * @author Emperor
 *
 */
public final class EmoteTabButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(590, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		switch (opcode) {
		case 85:
			switch (buttonId) {
			case 8:
				player.getActionManager().register(new EmoteAction(player, slot));
				return true;
			default:
				return false;
			}
		}
		return false;
	}

}
