package com.sevador.game.event.item;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.skills.summoning.Summoning;
import com.sevador.game.node.model.skills.summoning.SummoningPouch;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles summoning pouch item action events.
 * @author Emperor
 *
 */
public class SummonPouchEvent implements ItemActionEvent {

	@Override
	public boolean init() {
		for (SummoningPouch pouch : SummoningPouch.values()) {
			if (!EventManager.register(pouch.getPouchId(), this)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot, OptionType type) {
		if (type == OptionType.THIRD) {
			return Summoning.summon(player, item.getId(), slot);
		}
		return false;
	}

}