package com.sevador.game.event.item;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author Tyluur<tyluur@zandium.org>
 * 
 */
public class GodswordHandler implements ItemActionEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.event.ItemActionEvent#init()
	 */	
	@Override
	public boolean init() {
		EventManager.register(11694, this);
		EventManager.register(11696, this);
		EventManager.register(11698, this);
		return EventManager.register(11700, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ItemActionEvent#handle(org.argonite.game.node
	 * .model.player.Player, org.argonite.game.node.Item, int, int,
	 * org.argonite.util.OptionType)
	 */
	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot,
			OptionType type) {
		if (player.getInventory().contains(item.getId(), 1)
				&& player.getInventory().freeSlots() > 3) {
			if (type.equals(OptionType.SECOND)) {
				switch (item.getId()) {
				case 11694:
					player.getInventory().remove(item, slot, false);
					player.getInventory().add(new Item(11690));
					player.getInventory().add(new Item(11702, 1));
					break;
				case 11696:
					player.getInventory().remove(item, slot, false);
					player.getInventory().add(new Item(11690));
					player.getInventory().add(new Item(11704, 1));
					break;
				case 11698:
					player.getInventory().remove(item, slot, false);
					player.getInventory().add(new Item(11690));
					player.getInventory().add(new Item(11706, 1));
					break;
				case 11700:
					player.getInventory().remove(item, slot, false);
					player.getInventory().add(new Item(11690));
					player.getInventory().add(new Item(11708, 1));
					break;
				}
			}
		} else {
			player.getPacketSender().sendMessage("You do not have enough free inventory space.");
			return true;
		}
		return true;
	}

}
