package com.sevador.game.event.button;

import com.sevador.content.quest.impl.CooksAssistant;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class QuestTabListener implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(190, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		switch(buttonId) {
		case 18:
			switch(slot) {
			case 1:
				player.getQuestListener().start(CooksAssistant.ID);
				player.getQuestListener().start(CooksAssistant.ID).sendQuestGuide(player);
				break;
			default:
				player.getPacketSender().sendMessage("Quest ID: " + slot + " has not been added yet, the server is still in development...");
				break;
			}
			break;
		}
		return true;
	}

}
