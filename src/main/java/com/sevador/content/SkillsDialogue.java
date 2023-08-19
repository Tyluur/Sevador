package com.sevador.content;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.CS2String;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.InterfacePacket;
import com.sevador.network.out.StringPacket;

/**
 * 
 * @author Tyluur
 * 
 */
public class SkillsDialogue {

	public static interface ItemNameFilter {
		public String rename(String name);
	}

	public static final int MAKE = 0, MAKE_SETS = 1, COOK = 2, ROAST = 3,
			OFFER = 4, SELL = 5, BAKE = 6, CUT = 7, DEPOSIT = 8,
			MAKE_NO_ALL_NO_CUSTOM = 9, TELEPORT = 10, SELECT = 11, TAKE = 13;

	public static final int[] NAME_CONFIGS = { 132, 133, 134, 135, 136, 137,
		275, 316, 317, 318 };

	public static final int[] CONFIG_IDS = {
		755, 756, 757, 758, 759, 760, 120, 185, 87, 90
	};

	public static void sendSkillsDialogue(Player player, int option, String explanation, int maxQuantity, int[][] items, ItemNameFilter filter) {
		player.getIOSession().write(new CS2Config(player, 754, option));
		player.getPacketSender().sendChatBoxInterface(905);
		player.getIOSession().write(new StringPacket(player, explanation, 916, 1));
		if (option != MAKE_SETS && option != MAKE_NO_ALL_NO_CUSTOM)
			player.getIOSession().write(new AccessMask(player, 0, 27, 916, 31, 916, 1150));
		player.getIOSession().write(new InterfacePacket(player, 905, 4, 916, false));
		for (int i = 0; i < items[0].length; i++) {
			String name = ItemDefinition.forId(items[0][i]).getName();
			if (filter != null)
				name = filter.rename(name);
			player.getIOSession().write(new CS2Config(player, CONFIG_IDS[i], items[0][i]));
			player.getIOSession().write(new CS2String(player, NAME_CONFIGS[i], name));
		}
		int max = -1;
		for (int i = 0; i < items[1].length; i++) { 
			int amt = player.getInventory().getAmount(new Item(items[1][i]));
			if (max < amt) {
				max = amt;
			}
		}
		int amount = max;
		setMaxQuantity(player, amount);
		setQuantity(player, amount, false);
		player.getIOSession().write(new ConfigPacket(player, 1363, amount << 20 | amount << 26));
	}

	public static void handleSetQuantityButtons(Player player, int componentId) {
		if (componentId == 5)
			setQuantity(player, 1, false);
		else if (componentId == 6)
			setQuantity(player, 5, false);
		else if (componentId == 7)
			setQuantity(player, 10, false);
		else if (componentId == 8)
			setQuantity(player, getMaxQuantity(player), false);
		else if (componentId == 19)
			setQuantity(player, getQuantity(player) + 1, false);
		else if (componentId == 20)
			setQuantity(player, getQuantity(player) - 1, false);
	}

	public static void setMaxQuantity(Player player, int maxQuantity) {
		player.getTemporaryAttributtes().put("SkillsDialogueMaxQuantity", maxQuantity);
		player.getIOSession().write(new ConfigPacket(player, 1363, maxQuantity));
	}

	public static void setQuantity(Player player, int quantity) {
		setQuantity(player, quantity, true);
	}

	public static void setQuantity(Player player, int quantity, boolean refresh) {
		int maxQuantity = getMaxQuantity(player);
		if (quantity > maxQuantity)
			quantity = maxQuantity;
		else if (quantity < 0)
			quantity = 0;
		player.getTemporaryAttributtes().put("SkillsDialogueQuantity", quantity);
		if (refresh)
			player.getIOSession().write(new ConfigPacket(player, 1363, quantity));
	}

	public static int getMaxQuantity(Player player) {
		Integer maxQuantity = (Integer) player.getTemporaryAttributtes().get("SkillsDialogueMaxQuantity");
		if (maxQuantity == null)
			return 0;
		return maxQuantity;
	}

	public static int getQuantity(Player player) {
		Integer quantity = (Integer) player.getTemporaryAttributtes().get(
				"SkillsDialogueQuantity");
		if (quantity == null)
			return 0;
		return quantity;
	}

	public static int getItemSlot(int componentId) {
		if (componentId < 14)
			return 0;
		return componentId - 14;
	}

	public static void sendSkillsDialogue(Player player, int option,
			String explanation, int maxQuantity, int[] items,
			ItemNameFilter filter) {
		player.getPacketSender().sendChatBoxInterface(905);
		player.getIOSession().write(new CS2Config(player, 754, option));
		player.getIOSession().write(new StringPacket(player, explanation, 916, 1));
		if (option != MAKE_SETS && option != MAKE_NO_ALL_NO_CUSTOM)
			player.getIOSession().write(new AccessMask(player, 0, 27, 916, 31, 916, 1150));
		player.getIOSession().write(new InterfacePacket(player, 905, 4, 916, false));
		for (int i = 0; i < 10; i++) {
			if (i >= items.length) {
				player.getIOSession().write(new CS2Config(player, i >= 6 ? (1139 + i - 6) : 755 + i, -1));
				continue;
			}
			player.getIOSession().write(new CS2Config(player, i >= 6 ? (1139 + i - 6) : 755 + i, items[i]));
			String name = ItemDefinition.forId(items[i]).getName();
			if (filter != null)
				name = filter.rename(name);
			player.getIOSession().write(new CS2Config(player, CONFIG_IDS[i], items[i]));
			player.getIOSession().write(new CS2String(player, NAME_CONFIGS[i], name));
		}
		setMaxQuantity(player, maxQuantity);
		setQuantity(player, maxQuantity, false);
		player.getIOSession().write(new ConfigPacket(player, 1363, maxQuantity << 20 | maxQuantity << 26));
	}
}
