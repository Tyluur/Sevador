package com.sevador.game.event.npc;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.NPCActionEvent;
import com.sevador.game.misc.ShopManager;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles the shop owner action events.
 * @author Emperor
 *
 */
public class ShopOwnerEvent implements NPCActionEvent {

	@Override
	public boolean init() {
		for (int i : ShopManager.getShopOwners().keySet()) {
			if (!EventManager.register(i & 0xFFFF, this)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean handle(Player p, NPC npc, OptionType option) {
		if (ShopManager.open(p, npc.getId(), option.ordinal() + 1)) {
			return true;
		}
		return false;
	}

}