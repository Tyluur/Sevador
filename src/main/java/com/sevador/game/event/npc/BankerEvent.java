package com.sevador.game.event.npc;

import com.sevador.content.grandExchange.ExchangeHandler;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class BankerEvent implements NPCActionEvent {

	@Override
	public boolean init() {
		EventManager.register(44, this);
		EventManager.register(45, this);
		EventManager.register(166, this);
		EventManager.register(494, this);
		EventManager.register(495, this);
		EventManager.register(496, this);
		EventManager.register(497, this);
		EventManager.register(498, this);
		EventManager.register(499, this);
		EventManager.register(553, this);
		EventManager.register(909, this);
		EventManager.register(953, this);
		EventManager.register(1036, this);
		EventManager.register(1360, this);
		EventManager.register(1702, this);
		EventManager.register(2163, this);
		EventManager.register(2164, this);
		EventManager.register(2354, this);
		EventManager.register(2355, this);
		EventManager.register(2568, this);
		EventManager.register(2569, this);
		EventManager.register(2570, this);
		EventManager.register(2718, this);
		EventManager.register(2759, this);
		EventManager.register(3046, this);
		EventManager.register(3198, this);
		EventManager.register(3199, this);
		EventManager.register(3293, this);
		EventManager.register(3416, this);
		EventManager.register(3418, this);
		EventManager.register(4456, this);
		EventManager.register(4457, this);
		EventManager.register(4458, this);
		EventManager.register(1419, this);
		EventManager.register(2240, this);
		EventManager.register(2241, this);
		EventManager.register(2593, this);
		EventManager.register(6521, this);
		EventManager.register(4459, this);
		EventManager.register(4519, this);
		EventManager.register(4907, this);
		EventManager.register(5258, this);
		EventManager.register(5260, this);
		EventManager.register(5776, this);
		EventManager.register(5777, this);
		EventManager.register(6200, this);
		EventManager.register(7049, this);
		EventManager.register(7050, this);
		EventManager.register(7605, this);
		EventManager.register(8948, this);
		EventManager.register(9710, this);
		return true;
	}

	@Override
	public boolean handle(Player p, NPC npc, OptionType option) {
		if (npc.getDefinition().name.toLowerCase().contains("bank") && option == OptionType.FIRST) {
			p.getDialogueManager().startDialogue("BankerEvent", new Object[] { npc.getId() });
			return true;
		}
		if (npc.getDefinition().name.toLowerCase().contains("bank") && option == OptionType.SECOND) {
			p.getBank().open();
			return true;
		} else {
			ExchangeHandler.mainInterface(p);
		}
		return true;
	}

}
