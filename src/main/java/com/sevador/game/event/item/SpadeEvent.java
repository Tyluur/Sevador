package com.sevador.game.event.item;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.minigames.Barrows;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SpadeEvent implements ItemActionEvent {

	@Override
	public boolean init() {
		return EventManager.register(952, this);
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot,
			OptionType type) {
		player.getWalkingQueue().reset();
		player.setAnimation(new Animation(830));
		if (Barrows.enterCrypt(player)) return true;
		player.getPacketSender().sendMessage("You find nothing of interest...");
		return true;
	}

}
