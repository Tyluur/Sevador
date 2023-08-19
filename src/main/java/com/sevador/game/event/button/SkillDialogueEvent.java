package com.sevador.game.event.button;

import com.sevador.content.SkillsDialogue;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SkillDialogueEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(916, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		SkillsDialogue.handleSetQuantityButtons(player, buttonId);
		return true;
	}

}
