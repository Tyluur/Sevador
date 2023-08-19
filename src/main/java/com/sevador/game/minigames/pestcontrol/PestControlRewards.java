package com.sevador.game.minigames.pestcontrol;

import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.StringPacket;

/**
 * 
 * @author 'Tyluur <tyluur@zandium.org>
 * @author Mystic Flow <steven@rune-server.org>
 */
public class PestControlRewards {

	private static int[][] EXPERIENCE_BUTTONS = { { 34, 49, 56 },
			{ 35, 50, 57 }, { 36, 51, 58 }, { 37, 32, 59 }, { 38, 53, 60 },
			{ 39, 54, 61 }, { 40, 55, 62 }, { 45 }, { 46 }, { 48 }, };

	private enum Experience {
		ATTACK, STRENGTH, DEFENCE, CONSTITUTION, RANGED, MAGIC, PRAYER;
	}

	private static final int INTERFACE_ID = 267;

	public static boolean handleExperience(Player player, int buttonId) {
		for (Packages item : Packages.values()) {
			if (item.buttonId == buttonId) {
				for (int i = 0; i < item.items.length; i++) {
					if (player.getCredentials().getPestPoints() > item.points) {
						if (player.getInventory().freeSlots() > item.items.length) {
							player.getInventory().add(
									new Item(item.items[i].getId(),
											item.items[i].getAmount()));
							player.getCredentials().setPestPoints(
									player.getCredentials().getPestPoints()
											- item.points);
							player.getIOSession().write(
									new StringPacket(player, ""
											+ player.getCredentials()
													.getPestPoints(),
											INTERFACE_ID, 105));
						} else {
							player.getDialogueManager()
									.startDialogue(
											"SimpleMessage",
											"You need "
													+ item.items.length
													+ " more free inventory slots open.");
						}
					} else {
						player.getDialogueManager()
								.startDialogue(
										"SimpleMessage",
										"You need "
												+ item.points
												+ " pest control points to purchase this.");
					}
				}
			}
		}
		for (int i = 0; i < EXPERIENCE_BUTTONS.length; i++) {
			for (int j = 0; j < EXPERIENCE_BUTTONS[i].length; j++) {
				if (buttonId == EXPERIENCE_BUTTONS[i][j]) {
					switch (i) {
					case 0:
						appendExperience(player, Experience.ATTACK, j);
						break;
					case 1:
						appendExperience(player, Experience.STRENGTH, j);
						break;
					case 2:
						appendExperience(player, Experience.DEFENCE, j);
						break;
					case 3:
						appendExperience(player, Experience.CONSTITUTION, j);
						break;
					case 4:
						appendExperience(player, Experience.RANGED, j);
						break;
					case 5:
						appendExperience(player, Experience.MAGIC, j);
						break;
					case 6:
						appendExperience(player, Experience.PRAYER, j);
						break;
					}
				}
			}
		}
		return false;
	}

	public static void sendInterface(Player player) {
		player.getIOSession().write(
				new StringPacket(player, "Commendations:", INTERFACE_ID, 101));
		player.getIOSession().write(
				new StringPacket(player, ""
						+ player.getCredentials().getPestPoints(),
						INTERFACE_ID, 105));
		player.getPacketSender().sendInterface(INTERFACE_ID);
	}

	private static int generateExperience(Player player, int skill) {
		for (TableExp exp : TableExp.values()) {
			if (player.getSkills().getLevel(skill) >= exp.low
					&& player.getSkills().getLevel(skill) <= exp.high) {
				if (skill == Skills.ATTACK || skill == Skills.DEFENCE
						|| skill == Skills.HITPOINTS
						|| skill == Skills.STRENGTH)
					return exp.exp[0];
				else if (skill == Skills.RANGE || skill == Skills.MAGIC)
					return exp.exp[1];
				else
					return exp.exp[2];
			}
		}
		player.getDialogueManager().startDialogue(
				"SimpleMessage",
				"You need a " + Skills.SKILL_NAMES[skill].toLowerCase()
						+ " level of 25 to purchase experience.");
		return -1;
	}

	private static void appendExperience(Player player, Experience exp,
			int amount) {
		if (amount == 1)
			amount = 10;
		if (amount == 0)
			amount = 1;
		if (amount == 2)
			amount = 100;
		int required = amount - player.getCredentials().getPestPoints();
		if (player.getCredentials().getPestPoints() < amount) {
			player.getDialogueManager()
			.startDialogue(
					"SimpleMessage", "You need " + required
					+ " more pest control points to buy " + amount + "x "
					+ exp.name().toLowerCase() + " experience.");
			return;
		}
		player.getCredentials().setPestPoints(player.getCredentials().getPestPoints() - amount);
		player.getIOSession().write(new StringPacket(player, "" + player.getCredentials().getPestPoints(), INTERFACE_ID, 105));
		switch (exp) {
		case ATTACK:
			((Skills) player.getSkills()).addExperience(Skills.ATTACK,
					generateExperience(player, Skills.ATTACK));
			break;
		case STRENGTH:
			((Skills) player.getSkills()).addExperience(Skills.STRENGTH,
					generateExperience(player, Skills.STRENGTH));
			break;
		case DEFENCE:
			((Skills) player.getSkills()).addExperience(Skills.DEFENCE,
					generateExperience(player, Skills.DEFENCE));
			break;
		case CONSTITUTION:
			((Skills) player.getSkills()).addExperience(Skills.HITPOINTS,
					generateExperience(player, Skills.HITPOINTS));
			break;
		case RANGED:
			((Skills) player.getSkills()).addExperience(Skills.RANGE,
					generateExperience(player, Skills.RANGE));
			break;
		case MAGIC:
			((Skills) player.getSkills()).addExperience(Skills.MAGIC,
					generateExperience(player, Skills.MAGIC));
			break;
		case PRAYER:
			((Skills) player.getSkills()).addExperience(Skills.PRAYER,
					generateExperience(player, Skills.PRAYER));
			break;
		}
	}

	private enum TableExp {
		FIRST(25, 34, new int[] { 35, 32, 18 }), SECOND(35, 42, new int[] { 70,
				64, 36 }), THIRD(43, 48, new int[] { 105, 96, 54 }), FOURTH(49,
				54, new int[] { 140, 128, 72 }), FIFTH(55, 59, new int[] { 175,
				160, 90 }), SIXTH(60, 64, new int[] { 210, 192, 108 }), SEVENTH(
				65, 69, new int[] { 245, 224, 126 }), EIGTH(70, 73, new int[] {
				280, 256, 144 }), NINTH(74, 77, new int[] { 315, 288, 162 }), TENTH(
				78, 81, new int[] { 350, 320, 180 }), ELEVENTH(82, 84,
				new int[] { 385, 352, 198 }), TWELFTH(85, 88, new int[] { 420,
				384, 216 }), THIRTEENTH(89, 91, new int[] { 455, 416, 234 }), FOURTEENTH(
				92, 94, new int[] { 490, 448, 252 }), FIFTHEETH(95, 97,
				new int[] { 525, 480, 270 }), SIXTEENTH(98, 99, new int[] {
				560, 512, 288 });

		private int low, high;
		private int[] exp;

		TableExp(int low, int high, int[] exp) {
			this.low = low;
			this.high = high;
			this.exp = exp;
		}
	}

	private enum Packages {

		HERB_PACK(30, 45, new Item[] { new Item(255, 2), new Item(207, 2),
				new Item(3049, 1), new Item(209, 3), new Item(211, 4),
				new Item(213, 1) }), MINERAL_PACK(15, 46, new Item[] {
				new Item(440, 25), new Item(453, 18) }), SEED_PACK(15, 48,
				new Item[] { new Item(5320, 3), new Item(5322, 6),
						new Item(5100, 2) }), VOID_KNIGHT_MACE(250, 41,
				new Item[] { new Item(8841) }), VOID_KNIGHT_TOP(250, 42,
				new Item[] { new Item(8839) }), VOID_KNIGHT_ROBES(250, 43,
				new Item[] { new Item(8840) }), VOID_KNIGHT_GLOVES(150, 44,
				new Item[] { new Item(8842) }), VOID_KNIGHT_MAGE_HELM(200, 67,
				new Item[] { new Item(11663) }), VOID_KNIGHT_RANGER_HELM(200,
				68, new Item[] { new Item(11664) }), VOID_KNIGHT_MELEE_HELM(
				200, 69, new Item[] { new Item(11665) }), VOID_KNIGHT_SEAL(10,
				70, new Item[] { new Item(11666) });

		private Item[] items;
		private int points, buttonId;

		Packages(int points, int buttonId, Item[] items) {
			this.buttonId = buttonId;
			this.points = points;
			this.items = items;
		}
	}
}
