package com.sevador.game.event.npc;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.node.model.skills.fishing.Fishing;
import com.sevador.game.node.model.skills.fishing.FishingData.FishingSpots;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class FishingEvent implements NPCActionEvent {

	@Override
	public boolean init() {
		for (FishingSpots fish : FishingSpots.values()) {
			if (!EventManager.getNPCActions().containsKey(fish.getId()))
				EventManager.register(fish.getId(), this);
		}
		return true;
	}

	@Override
	public boolean handle(Player p, NPC npc, OptionType option) {
		for (FishingSpots spot : FishingSpots.values()) {
			if (spot.getId() == npc.getId()) {
				if (spot.getOption() == (option.ordinal() + 1)) {
					p.getSkillAction().setSkill(new Fishing(spot, npc));
					return true;
				}
			}
		}
		return true;
	}

}
