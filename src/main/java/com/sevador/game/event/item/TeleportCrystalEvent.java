package com.sevador.game.event.item;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class TeleportCrystalEvent implements ItemActionEvent {

	@Override
	public boolean init() {
		EventManager.register(6099, this);
		EventManager.register(6100, this);
		EventManager.register(6101, this);
		EventManager.register(6102, this);
		return true;
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot,
			OptionType type) {
		player.getDialogueManager().startDialogue("TeleportCrystal", new Object[] {  item.getId() } );
		return true;
	}

}
