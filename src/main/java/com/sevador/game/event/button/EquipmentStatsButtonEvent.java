package com.sevador.game.event.button;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.EquipAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;

/**
 * Handles an equipment stats interface button event.
 * 
 * @author Emperor
 * 
 */
public final class EquipmentStatsButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(667, this);
		return EventManager.register(670, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		switch (opcode) {
		case 85:
			switch (interfaceId) {
			case 667:
				switch (buttonId) {
				case 7:
					player.getActionManager().register(new EquipAction(player, itemId, slot, false));
				case 75:
					player.getActionManager().unregister(ActionFlag.CLOSE_INTERFACE);
					return true;
				}
				return false;
			case 670:
				switch (buttonId) {
				case 0:
					player.getActionManager().register(
							new EquipAction(player, itemId, slot, true));
					return true;
				}
				return false;
			}
			return false;
		case 54:
			player.getIOSession().write(
					new MessagePacket(player, ItemDefinition.forId(itemId)
							.getExamine()));
			return true;
		}
		return false;
	}

}