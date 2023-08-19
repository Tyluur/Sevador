package com.sevador.game.event.item;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class EnchantedGem implements ItemActionEvent {

	@Override
	public boolean init() {
		return EventManager.register(4155, this);
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot,
			OptionType type) {
		switch (type) {
		case FIRST:
			player.getDialogueManager().startDialogue("SlayerGem", new Object[] { 1597 });
			break;
		case FOURTH:
			break;
		case SECOND:
			break;
		case THIRD:
			break;
		default:
			break;
		}
		return true;
	}

}
