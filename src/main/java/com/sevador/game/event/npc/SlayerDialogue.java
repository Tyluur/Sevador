package com.sevador.game.event.npc;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.node.model.skills.slayer.SlayerTask.Master;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 *
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SlayerDialogue implements NPCActionEvent{

	@Override
	public boolean init() {
		for (Master masters : Master.values())
			EventManager.register(masters.getId(), this);
		return true;
	}

	@Override
	public boolean handle(Player p, NPC npc, OptionType option) {
		p.getDialogueManager().startDialogue("SlayerDialogue", new Object[] { npc.getId() } );
		return false;
	}

}
