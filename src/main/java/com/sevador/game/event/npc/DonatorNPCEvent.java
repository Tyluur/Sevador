package com.sevador.game.event.npc;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class DonatorNPCEvent implements NPCActionEvent {

	@Override
	public boolean init() {
		return EventManager.register(947, this);
	}

	@Override
	public boolean handle(Player p, NPC npc, OptionType option) {
		p.getDialogueManager().startDialogue("FinancialAdvisor", new Object[] { npc.getId() }) ;
		return true;
	}

}
