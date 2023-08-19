package com.sevador.game.dialogue;

import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.player.Player;

/**
 * 
 * @author Tyluur <tyluur@zandium.org>
 *
 */

public class DialogueManager {

	private Player player;
	Dialogue lastDialogue;

	public DialogueManager(Player player) {
		this.player = player;
	}

	public void startDialogue(Object key, Object... parameters) {
		try {
			if (lastDialogue != null)
				lastDialogue.finish();
			lastDialogue = DialogueHandler.getDialogue(key);
			if (lastDialogue == null)
				return;
			lastDialogue.parameters = parameters;
			lastDialogue.setPlayer(player);
			lastDialogue.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void continueDialogue(int interfaceId, int componentId) {
		if (lastDialogue == null)
			return;
		lastDialogue.run(interfaceId, componentId);
	}
	
	public boolean inDialogue() {
		return lastDialogue.parameters == null;
	}

	public void finishDialogue() {
		if (lastDialogue == null)
			return;
		lastDialogue.finish();
		lastDialogue.stage = -1;
		lastDialogue = null;
		player.getPacketSender().sendChatBoxInterface(137);
		player.getActionManager().register(new MovementAction(player, player.getLocation().getX(), player.getLocation().getY(), false));
	}

}
