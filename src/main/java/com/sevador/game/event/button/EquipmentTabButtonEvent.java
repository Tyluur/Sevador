package com.sevador.game.event.button;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.impl.EquipAction;
import com.sevador.game.action.impl.EquipStatsAction;
import com.sevador.game.action.impl.ItemAction;
import com.sevador.game.action.impl.PriceCheckerAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.OptionType;

/**
 * Handles an equipment tab button event.
 * 
 * @author Emperor
 * 
 */
public final class EquipmentTabButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(387, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		switch(buttonId) {
		case 50:
			if (opcode == 7) {
				if (itemId == 20767)
					player.getCapeRecolouring().displayInterface();
				player.getAuraManager().activate();
			} else if (opcode == 66) {
				player.getAuraManager().sendAuraRemainingTime();
			}
			return true;
		}
		switch (opcode) {
		case 54:
			player.getIOSession().write(
					new MessagePacket(player, ItemDefinition.forId(itemId)
							.getExamine()));
			return true;
		case 85:
			switch (buttonId) {
			case 42:
				player.getActionManager().register(
						new PriceCheckerAction(player));
				return true;
			case 45:
				player.getPacketSender().sendInterface(17);
				return true;
			case 8:
			case 11:
			case 14:
			case 17:
			case 20:
			case 23:
			case 26:
			case 29:
			case 32:
			case 35:
			case 38:
				player.getActionManager().register(new EquipAction(player, itemId, slot, false));
				return true;
			case 39:
				player.getActionManager().register(
						new EquipStatsAction(player, false));
				return true;
			default:
				return false;
			}
		case 7:
			player.getActionManager()
			.register(
					new ItemAction(player, OptionType.FIRST, itemId,
							387, slot));
			return true;
		case 48:
			if (itemId == 20769) 
				player.getCapeRecolouring().displayInterface();
			player.getActionManager().register(new ItemAction(player, OptionType.FOURTH, itemId, 387, slot));
			return true;
		}
		return false;
	}

}