package com.sevador.game.event.npc;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.node.model.skills.thieving.PickPocketAction;
import com.sevador.game.node.model.skills.thieving.PickPocketableNPC;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class PickPocketEvent implements NPCActionEvent {

	@Override
	public boolean init() {
		for (PickPocketableNPC n : PickPocketableNPC.values()) {
			for(int i = 0; i < n.getNpcIds().length; i++) {
				EventManager.register(n.getNpcIds()[i], this);
			}
		}
		return true;
	}

	@Override
	public boolean handle(Player player, NPC npc, OptionType option) {
		PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
		if (pocket != null) {
			player.getSkillAction().setSkill(new PickPocketAction(npc, pocket));
			return true;
		}
		return false;
	}

}
