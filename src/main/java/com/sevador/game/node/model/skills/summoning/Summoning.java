package com.sevador.game.node.model.skills.summoning;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.Main;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.impl.Familiar;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.world.NPCWorldLoader;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Script;

/**
 * Handles the Summoning skill.
 * 
 * @author Emperor
 * 
 */
public class Summoning {

	/**
	 * Summons a familiar.
	 * 
	 * @param itemId
	 *            The pouch item id.
	 * @param slot
	 *            The slot.
	 * @return {@code True} if the item was a pouch.
	 */
	public static boolean summon(Player player, int itemId, int slot) {
		SummoningPouch pouch = SummoningPouch.get(itemId);
		if (pouch == null) {
			return false;
		} else if (player.getFamiliar().getId() > 0) {
			player.getPacketSender()
					.sendMessage("You already have a familiar.");
			return true;
		} else if (player.getSkills().getStaticLevel(Skills.SUMMONING) < pouch
				.getLevelRequired()) {
			player.getPacketSender().sendMessage(
					"You need a summoning level of " + pouch.getLevelRequired()
							+ " to summon this familiar.");
			return true;
		} else if (player.getSkills().getLevel(Skills.SUMMONING) < pouch
				.getSummonCost()) {
			player.getPacketSender()
					.sendMessage(
							"You do not have enough summoning points left to summon this familiar.");
			return true;
		}
		NPC familiar = NPCWorldLoader.getNPC(pouch.getNpcId());
		if (!(familiar instanceof Familiar)) {
			player.getPacketSender().sendMessage("Invalid familiar, report to an administrator - pouch id: " + itemId);
			return true;
		}
		player.getSkills().setLevel(
				Skills.SUMMONING,
				player.getSkills().getLevel(Skills.SUMMONING)
						- pouch.getSummonCost());
		player.getInventory().replace(null, slot);
		((Familiar) familiar).setOwner(player);
		player.setFamiliar((Familiar) familiar);
		((Skills) player.getSkills()).addExperience(Skills.SUMMONING,
				pouch.getSummonExperience());
		player.getFamiliar().unlockOrb();
		Main.getNodeWorker().offer(familiar);
		return true;
	}

	public static int INTERFACE = 672;

	/**
	 * The child id.
	 */
	private static final int CHILD = 16;
	
	/**
	 * The parameters for the pouch creating client script.
	 */
	private static final Object[] POUCH_PARAMS = { 78, 1, "List<col=FF9040>", "Infuse-X<col=FF9040>", "Infuse-All<col=FF9040>", "Infuse-10<col=FF9040>", "Infuse-5<col=FF9040>", "Infuse<col=FF9040>", 10, 8, 672 << 16 | CHILD };

	/**
	 * The parameters for the scroll creating client script.
	 */
	@SuppressWarnings("unused")
	private static final Object[] SCROLL_PARAMS = { 78, 1, "Transform-X<col=ff9040>", "Transform-All<col=ff9040>", "Transform-10<col=ff9040>", "Transform-5<col=ff9040>", "Transform<col=ff9040>", 10, 8, 666 << 16 | CHILD };

	public static void sendInterface(Player player) {
		player.getIOSession().write(new CS2Script(player, 757, "Iiissssssii", POUCH_PARAMS));
		player.getIOSession().write(new AccessMask(player, 0, 462, 672, 16, 0, 190));
		player.getPacketSender().sendInterface(INTERFACE);
	}

	public static void createPouch(final Player player, int itemId, int amount) {
		SummoningPouch pouch = SummoningPouch.get(itemId);
		if (pouch == null) {
			player.getPacketSender().sendMessage("You do not have the items required to create this pouch.");
			return;
		}
		if (player.getInventory().freeSlots() < 1) {
			player.getPacketSender().sendMessage("Not enough inventory space.");
			return;
		}
		player.getPacketSender().sendCloseInterface();
		boolean end = false;
		int i = 0;
		for (i = 0; i < amount; i++) {
			for (Item item : pouch.getItems()) {
				if (!player.getInventory().contains(item.getId(),
						item.getAmount())) {
					if (amount == 1) {
						player.getPacketSender().sendMessage("You do not have the items required to create this pouch.");
					}
					end = true;
					break;
				}
			}
			if (end) {
				break;
			}
			double multiplier = 1;
			player.getInventory().remove(pouch.getItems());
			player.getInventory().add(new Item(pouch.getPouchId(), 1));
			((Skills) player.getSkills()).addExperience(Skills.SUMMONING,
					(pouch.getSummonExperience() * amount) * multiplier);
		}
		player.getPacketSender().sendMessage("You infuse " + (i == 1 ? "a" : "some") + " " + ItemDefinition.forId(itemId).getName().toLowerCase() + ".");
		player.setAnimation(new Animation(9068));
	}
}