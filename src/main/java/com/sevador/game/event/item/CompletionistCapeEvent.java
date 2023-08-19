package com.sevador.game.event.item;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles a completionist cape item action event.
 * @author Emperor
 *
 */
public final class CompletionistCapeEvent implements ItemActionEvent {

	@Override
	public boolean init() {
		EventManager.register(20769, this);
		return EventManager.register(20771, this);
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot, OptionType type) {
		switch (type) {
		case FIRST:
			return false;
		case SECOND:
			switch (interfaceId) {
			case 679:
				player.setAttribute("customizingCapeId", item.getId());
				player.getCapeRecolouring().displayInterface();
				return true;
			}
			return true;
		case THIRD:
			return false;
		case FOURTH:
			switch (interfaceId) {
			case 378:
				player.getPacketSender().sendInterface(20);
				return true;
			}
		}
		return true;
	}

}