package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.LevelUp;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ConfigPacket;

/**
 * Handles a skillguide button event.
 * 
 * @author Emperor
 * 
 */
public final class SkillGuideButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(499, this);
		return EventManager.register(320, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		boolean levelUp = false;
		if (interfaceId == 320) {
			switch (buttonId) {
			case 200:
				if (player.getAttribute("Attack", false)) {
					levelUp = true;
					player.removeAttribute("Attack");
					player.getIOSession().write(new ConfigPacket(player, 1230, 10));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 1));
					player.setAttribute("skillMenu", 1);
				}
				break;
			case 11:
				if (player.getAttribute("Strength", false)) {
					levelUp = true;
					player.removeAttribute("Strength");
					player.getIOSession().write(new ConfigPacket(player, 1230, 20));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 2));
					player.setAttribute("skillMenu", 2);
				}
				break;
			case 28:
				if (player.getAttribute("Defence", false)) {
					levelUp = true;

					player.removeAttribute("Defence");
					player.getIOSession().write(new ConfigPacket(player, 1230, 40));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 5));
					player.setAttribute("skillMenu", 5);
				}
				break;
			case 52:
				if (player.getAttribute("Ranged", false)) {
					levelUp = true;

					player.removeAttribute("Ranged");
					player.getIOSession().write(new ConfigPacket(player, 1230, 30));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 3));
					player.setAttribute("skillMenu", 3);
				}
				break;
			case 76:
				if (player.getAttribute("Prayer", false)) {
					levelUp = true;
					player.removeAttribute("Prayer");
					player.getIOSession().write(new ConfigPacket(player, 1230, 60));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 7));
					player.setAttribute("skillMenu", 7);
				}
				break;
			case 93:
				if (player.getAttribute("Magic", false)) {
					levelUp = true;
					player.removeAttribute("Magic");
					player.getIOSession().write(new ConfigPacket(player, 1230, 33));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 4));
					player.setAttribute("skillMenu", 4);
				}
				break;
			case 110:
				if (player.getAttribute("Runecrafting", false)) {
					levelUp = true;
					player.removeAttribute("Runecrafting");
					player.getIOSession().write(new ConfigPacket(player, 1230, 100));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 12));
					player.setAttribute("skillMenu", 12);
				}
				break;
			case 134:
				if (player.getAttribute("Construction", false)) {
					levelUp = true;
					player.removeAttribute("Construction");
					player.getIOSession().write(new ConfigPacket(player, 1230, 689));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 22));
					player.setAttribute("skillMenu", 22);
				}
				break;
			case 158:
				if (player.getAttribute("Dungeoneering", false)) {
					levelUp = true;
					player.removeAttribute("Dungeoneering");
					player.getIOSession().write(new ConfigPacket(player, 1230, 715));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 25));
					player.setAttribute("skillMenu", 25);
				}
				break;
			case 193:
				if (player.getAttribute("Constitution", false)) {
					levelUp = true;
					player.removeAttribute("Constitution");
					player.getIOSession().write(new ConfigPacket(player, 1230, 50));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 6));
					player.setAttribute("skillMenu", 6);
				}
				break;
			case 19:
				if (player.getAttribute("Agility", false)) {
					levelUp = true;
					player.removeAttribute("Agility");
					player.getIOSession().write(new ConfigPacket(player, 1230, 65));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 8));
					player.setAttribute("skillMenu", 8);
				}
				break;
			case 36:
				if (player.getAttribute("HerbloreData", false)) {
					levelUp = true;
					player.removeAttribute("HerbloreData");
					player.getIOSession().write(new ConfigPacket(player, 1230, 75));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 9));
					player.setAttribute("skillMenu", 9);
				}
				break;
			case 60:
				if (player.getAttribute("Thieving", false)) {
					levelUp = true;
					player.removeAttribute("Thieving");
					player.getIOSession().write(new ConfigPacket(player, 1230, 80));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 10));
					player.setAttribute("skillMenu", 10);
				}
				break;
			case 84:
				if (player.getAttribute("Crafting", false)) {
					levelUp = true;
					player.removeAttribute("Crafting");
					player.getIOSession().write(new ConfigPacket(player, 1230, 90));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 11));
					player.setAttribute("skillMenu", 11);
				}
				break;
			case 101:
				if (player.getAttribute("Fletching", false)) {
					levelUp = true;
					player.removeAttribute("Fletching");
					player.getIOSession().write(new ConfigPacket(player, 1230, 665));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 19));
					player.setAttribute("skillMenu", 19);
				}
				break;
			case 118:
				if (player.getAttribute("Slayer", false)) {
					levelUp = true;
					player.removeAttribute("Slayer");
					player.getIOSession().write(new ConfigPacket(player, 1230, 673));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 20));
					player.setAttribute("skillMenu", 20);
				}
				break;
			case 142:
				if (player.getAttribute("Hunter", false)) {
					levelUp = true;
					player.removeAttribute("Hunter");
					player.getIOSession().write(new ConfigPacket(player, 1230, 698));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 23));
					player.setAttribute("skillMenu", 23);
				}
				break;
			case 186:
				if (player.getAttribute("Mining", false)) {
					levelUp = true;
					player.removeAttribute("Mining");
					player.getIOSession().write(new ConfigPacket(player, 1230, 110));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 13));
					player.setAttribute("skillMenu", 13);
				}
				break;
			case 179:
				if (player.getAttribute("Smithing", false)) {
					levelUp = true;
					player.removeAttribute("Smithing");
					player.getIOSession().write(new ConfigPacket(player, 1230, 115));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 14));
					player.setAttribute("skillMenu", 14);
				}
				break;
			case 44:
				if (player.getAttribute("FishingData", false)) {
					levelUp = true;
					player.removeAttribute("FishingData");
					player.getIOSession().write(new ConfigPacket(player, 1230, 120));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 15));
					player.setAttribute("skillMenu", 15);
				}
				break;
			case 68:
				if (player.getAttribute("Cooking", false)) {
					levelUp = true;
					player.removeAttribute("Cooking");
					player.getIOSession().write(new ConfigPacket(player, 1230, 641));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 16));
					player.setAttribute("skillMenu", 16);
				}
				break;
			case 172:
				if (player.getAttribute("Firemaking", false)) {
					levelUp = true;
					player.removeAttribute("Firemaking");
					player.getIOSession().write(new ConfigPacket(player, 1230, 649));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 17));
					player.setAttribute("skillMenu", 17);
				}
				break;
			case 165:
				if (player.getAttribute("Woodcutting", false)) {
					levelUp = true;
					player.removeAttribute("Woodcutting");
					player.getIOSession().write(new ConfigPacket(player, 1230, 660));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 18));
					player.setAttribute("skillMenu", 18);
				}
				break;
			case 126:
				if (player.getAttribute("Farming", false)) {
					levelUp = true;
					player.removeAttribute("Farming");
					player.getIOSession().write(new ConfigPacket(player, 1230, 681));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 21));
					player.setAttribute("skillMenu", 21);
				}
				break;
			case 150:
				if (player.getAttribute("Summoning", false)) {
					levelUp = true;
					player.removeAttribute("Summoning");
					player.getIOSession().write(new ConfigPacket(player, 1230, 705));
				} else {
					player.getIOSession().write(new ConfigPacket(player, 965, 24));
					player.setAttribute("skillMenu", 24);
				}
				break;
			}
			player.getPacketSender().sendInterface(499);
			if (levelUp) {
				player.getPacketSender().sendInterface(741);
				LevelUp.sendFlashIcons(player);
				return true;
			}
		} else if (interfaceId == 499) {
			int skillMenu = player.getAttribute("skillMenu");
			if (skillMenu == -1)
				return true;
			switch (buttonId) {
			case 10:
				player.getIOSession().write(new ConfigPacket(player, 965, skillMenu));
				return true;
			case 11:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 1024 + skillMenu));
				return true;
			case 12:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 2048 + skillMenu));
				return true;
			case 13:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 3072 + skillMenu));
				return true;
			case 14:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 4096 + skillMenu));
				return true;
			case 15:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 5120 + skillMenu));
				return true;
			case 16:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 6144 + skillMenu));
				return true;
			case 17:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 7168 + skillMenu));
				return true;
			case 18:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 8192 + skillMenu));
				return true;
			case 19:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 9216 + skillMenu));
				return true;
			case 20:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 10240 + skillMenu));
				return true;
			case 21:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 11264 + skillMenu));
				return true;
			case 22:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 12288 + skillMenu));
				return true;
			case 23:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 13312 + skillMenu));
				return true;
			case 24:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 14336 + skillMenu));
				return true;
			case 25:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 15360 + skillMenu));
				return true;
			case 26:
				player.getIOSession().write(
						new ConfigPacket(player, 965, 16384 + skillMenu));
				return true;
			}
		}
		return true;
	}
}