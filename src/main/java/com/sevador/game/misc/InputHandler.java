package com.sevador.game.misc;

import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.content.grandExchange.ItemOffer;
import com.sevador.game.event.button.TradeButtonEvent;
import com.sevador.game.node.model.skills.prayer.BoneAltar;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Constants;

/**
 * Handles input requests.
 * 
 * @author Emperor
 * 
 */
public final class InputHandler {

	/**
	 * The slot attribute key.
	 */
	public static final String SLOT_KEY = "id:slot";

	/**
	 * The item id attribute key.
	 */
	public static final String ITEM_KEY = "id:item";

	/**
	 * Sends a new integer input request.
	 * 
	 * @param player
	 *            The player.
	 * @param id
	 *            The request id.
	 * @param question
	 *            The question.
	 */
	public static void requestInput(Player player, int id, String question) {
		player.setAttribute("id:IRequest", id);
		player.getIOSession().write(new CS2Script(player, 108, "s", question));
	}

	public static void requestStringInput(Player player, int inputId,
			String question) {
		player.setAttribute("id:IRequest", inputId);
		player.getIOSession().write(new CS2Script(player, 109, "s", question));
	}

	/**
	 * Handles an integer input request.
	 * 
	 * @param player
	 *            The player.
	 * @param value
	 *            The value.
	 */
	public static void handleInput(final Player player, int value) {
		int id = player.getAttribute("id:IRequest", -1);
		int slot = player.getAttribute(SLOT_KEY, -1);
		final int itemId = player.getAttribute(ITEM_KEY, -1);
		player.removeAttribute(SLOT_KEY);
		player.removeAttribute(ITEM_KEY);
		player.removeAttribute("id:IRequest");
		if (value < 1 || value > Integer.MAX_VALUE || id < 0) {
			return;
		}
		switch (id) {
		case 0:
			((Skills) player.getSkills()).setStaticLevel(slot, value);
			return;
		case 1:
			TradeButtonEvent.offer(player, itemId, slot, value);
			return;
		case 2:
			TradeButtonEvent.remove(player, itemId, slot, value);
			return;
		case 3:
			ItemOffer offer = player.getAttribute("geOffer");
			if (value < Constants.MAX_AMOUNT && value > 0)
				offer.setAmount(value);
			else
				player.getPacketSender().sendMessage("Invalid amount entered.");
			player.getIOSession().write(new ConfigPacket(player, 1110, offer.getAmount()));
			return;
		case 4:
			if (player.getAttribute("inventoryAction").equals(true))
				player.getPriceCheck().checkPrice((Integer) player.getAttribute("itemPriceCheckId"), (Integer) player.getAttribute("slotId"), value);
			else
				player.getPriceCheck().remove((Integer) (player.getAttribute("itemPriceCheckId")), (Integer) player.getAttribute("slotId"), value);
			break;
		case 5:
			player.getSkillAction().setSkill(new BoneAltar(itemId, value));
			break;
		case 6:
			player.getBank().addItem((Integer) player.getAttribute("bank_deposit_slot"), value);
			player.getBank().setLastAmount(value);
			player.removeAttribute("bank_deposit_slot");
			break;
		case 7:
			player.getBank().removeItem((Integer) player.getAttribute("bank_withdraw_slot"), value);
			player.getBank().setLastAmount(value);
			player.removeAttribute("bank_withdraw_slot");
			break;
		}
	}

	public static void handleInput(Player player, String value) {
		int id = player.getAttribute("id:IRequest", -1);
		player.removeAttribute("id:IRequest");
		switch (id) {
		case 1:
			if (value.matches("-?\\d+(.\\d+)?")) {
				player.getCredentials().setBankPin(value);
				player.getPacketSender().sendMessage("You have set your bank pin to " + value
						+ ", do not forget this!");
			} else {
				player.getPacketSender().sendMessage("You have entered an invalid number.");
			}
			return;
		case 2:
			if (player.getCredentials().getBankPin().equalsIgnoreCase(value)) {
				player.getBank().setAllowedToOpen(true);
				player.getBank().open();
				player.getPacketSender().sendMessage("You have entered the correct bank pin!");
			} else {
				player.getPacketSender().sendMessage("You have entered an incorrect bank pin; try again.");
			}
			return;
		case 3:
			String clan = value.replaceAll("_", " ");
			player.getIOSession().write(
					new StringPacket(player, clan, 1108, 22));
			FriendChatManager.getFriendChatManager().createFriendChat(player, clan);
			return;
		case 4:
			player.getCredentials().setPassword(value);
			player.getPacketSender().sendMessage("You have changed your password to " + player.getCredentials().getPassword() + ". Do not forget this!");
			return;
		case 5:
			ItemOffer offer = player.getGeOffers()[player.getGESlot()];
			offer.setAmount(Integer.parseInt(value));
			player.getIOSession().write(new ConfigPacket(player, 1110, offer.getAmount()));
			break;
		}
	}
}