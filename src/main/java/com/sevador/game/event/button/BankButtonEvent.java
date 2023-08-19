package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;

/**
 * Handles a button event for the bank interfaces.
 * @author Emperor
 *
 */
public class BankButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(762, this);
		return EventManager.register(763, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		if (interfaceId == 762) {
			return handleBankButton(player, opcode, buttonId, itemId, slot);
		}
		switch (opcode) {
		case 85:
			player.getBank().addItem(slot, 1);
			return true;
		case 7:
			player.getBank().addItem(slot, 5);
			return true;
		case 66:
			player.getBank().addItem(slot, 10);
			return true;
		case 11:
			player.getBank().addItem(slot, player.getBank().getLastAmount());
			return true;
		case 17:
			player.getBank().addItem(slot, Integer.MAX_VALUE);
			return true;
		case 54:
			player.getIOSession().write(new MessagePacket(player, player.getInventory().get(slot).getDefinition().getExamine()));
			return true;
		case 48:
			if (interfaceId == 763) {
				player.setAttribute("bank_deposit_slot", slot);
				InputHandler.requestInput(player, 6, "How many would you like to deposit?");
			}
			return true;
		}
		return false;
	}

	/**
	 * Handles a bank button.
	 * @param player The player.
	 * @param opcode The opcode.
	 * @param buttonId The button id.
	 * @param itemId The item id.
	 * @param slot The slot.
	 * @return {@code True} if succesful.
	 */
	private boolean handleBankButton(Player player, int opcode, int buttonId, int itemId, int slot) {
		switch (opcode) {
		case 85:
			switch (buttonId) {
			case 15:
				player.setAttribute("inserting", !player.getAttribute("inserting", false));
				return true;
			case 19:
				player.setAttribute("noted", !player.getAttribute("noted", false));
				return true;
			case 33:
				player.getBank().bankInventory();
				return true;
			case 35:
				player.getBank().bankEquipment();
				return true;
			case 43:
				player.getBank().close();
				return true;
			case 93:
				player.getBank().removeItem(slot, 1);
				return true;
			default:
				return false;
			}
		case 48:
			player.setAttribute("bank_withdraw_slot", slot);
			InputHandler.requestInput(player, 7, "How many would you like to withdraw?");
			return true;
		case 7:
			switch (buttonId) {
			case 93:
				player.getBank().removeItem(slot, 5);
				return true;
			case 60:
				player.getBank().collapseTab(1);
				return true;
			default:
				return false;
			}
		case 66:
			player.getBank().removeItem(slot, 10);
			return true;
		case 11:
			player.getBank().removeItem(slot, player.getBank().getLastAmount());
			return true;
		case 17:
			player.getBank().removeItem(slot, Integer.MAX_VALUE);
			return true;
		case 84:
			player.getBank().removeItem(slot, player.getBank().getContainer().get(slot).getAmount() - 1);
			return true;
		case 54:
			player.getIOSession().write(new MessagePacket(player, player.getBank().getContainer().get(slot).getDefinition().getExamine()));
			return true;
		}
		return false;
	}
}