package com.sevador.game.event.npc;

import com.sevador.game.action.impl.packetactions.RestAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles the musician NPC action events.
 * @author Emperor
 *
 */
public final class MusicianNPCEvent implements NPCActionEvent {

	@Override
	public boolean init() {
		return EventManager.register(29, this);
	}

	@Override
	public boolean handle(Player p, NPC npc, OptionType option) {
		switch (option) {
		case FIRST:
			return false;
		case SECOND: //?
			p.turnTo(npc);
			p.getActionManager().register(new RestAction(p));
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

}