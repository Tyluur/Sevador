package com.sevador.game.event.npc;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class CooksAssistantEvent implements NPCActionEvent {

	@Override
	public boolean init() {
		EventManager.register(13236, this);
		return EventManager.register(278, this);
	}

	@Override
	public boolean handle(Player p, NPC npc, OptionType option) {
		p.getDialogueManager().startDialogue("CooksAssistantChat", new Object[] { npc.getId() } ) ;
		return true;
	}

}
