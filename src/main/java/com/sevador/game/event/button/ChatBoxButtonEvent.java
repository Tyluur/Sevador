package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;

/**
 * Handles chat box button events.
 * @author Emperor
 *
 */
public final class ChatBoxButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(137, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		// TODO Auto-generated method stub
		return false;
	}

}