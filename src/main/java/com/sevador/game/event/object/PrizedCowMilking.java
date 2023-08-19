package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class PrizedCowMilking implements ObjectEvent {

	@Override
	public boolean init() {
		return EventManager.register(47721, this);
	}

	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		if (player.getInventory().contains(3727, 1)) {
			player.getInventory().remove(new Item(3727, 1));
			player.getInventory().add(new Item(15413, 1));
			player.getPacketSender().sendMessage("You milk the cow for some top-quality milk.");
			player.setAnimation(new Animation(2292));
		} else {
			player.getPacketSender().sendMessage("You don't have a bucket to put the milk into!");
		}
		return true;
	}

	@Override
	public void setDestination(Player player, GameObject obj) {
		// TODO Auto-generated method stub
		
	}

}
