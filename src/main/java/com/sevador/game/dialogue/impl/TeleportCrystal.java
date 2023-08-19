package com.sevador.game.dialogue.impl;

import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Location;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class TeleportCrystal extends Dialogue {

	Item item;

	@Override
	public void start() {
		item = new Item(((Integer) parameters[0]).intValue());
		sendDialogue((short) 237, new String[] { "Choose an option",
				"Save current location", "Teleport to saved location",
				"Show other teleports.", "Erm... nothing" });
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			switch (componentId) {
			case 1:
				player.getSettings().setSavedLocation(player.getLocation());
				player.getPacketSender().sendMessage("Current location saved; you will teleport here the next time you use your crystal.");
				end();
				break;
			case 2:
				if (player.getSettings().getSavedLocation() != null) {
					if (item.getId() == 6099) {
						player.getInventory().remove(item);
						player.getInventory().add(new Item(item.getId() + 1));
						player.getPacketSender().sendMessage(
								"Your crystal has 3 charges left.");
					} else if (item.getId() == 6100) {
						player.getInventory().remove(item);
						player.getInventory().add(new Item(item.getId() + 1));
						player.getPacketSender().sendMessage(
								"Your crystal has 2 charges left.");
					} else if (item.getId() == 6101) {
						player.getInventory().remove(item);
						player.getInventory().add(new Item(item.getId() + 1));
						player.getPacketSender().sendMessage(
								"Your crystal has 1 charges left.");
					} else {
						player.getInventory().remove(item);
						player.getPacketSender().sendMessage(
								"Your crystal's charges are depleted.");
					}
					player.getActionManager()
					.register(
							new TeleportAction(player, player
									.getSettings().getSavedLocation(),
									TeleportAction.MODERN_ANIM,
									TeleportAction.MODERN_GRAPHIC,
									TeleportAction.MODERN_END_ANIM,
									TeleportAction.MODERN_END_GRAPHIC,
									0, 3, 4));
				} else
					player.getDialogueManager()
					.startDialogue("SimpleMessage",
							"You have no saved teleport location to teleport to!");
				break;
			case 3:
				sendDialogue((short) 237, new String[] { "Choose an option",
						"Kill Monsters", "Skilling Locations",
						"Dungeons", "Erm... nothing" });
				stage = 0;
				break;
			case 4:
				end();
				break;
			}
		} else if (stage == 0) {
			switch (componentId) {
			case 1:
				sendDialogue((short) 237, new String[] { "Choose an option",
						"Rock Crabs", "Slayer Tower", "GodWars Entrance", "" });
				stage = 3;
				break;
			case 2:
				sendDialogue((short) 237, new String[] { "Choose an option",
						"Gnome Agility", "RuneCrafting Abyss", "", "" });
				stage = 2;
				break;
			case 3:
				sendDialogue((short) 237, new String[] { "Choose an option",
						"Taverly Dungeon", "Forinthry Dungeon", "", "" });
				stage = 4;
				break;
			case 4:
				end();
				break;
			}
		} else if (stage == 2) {
			switch (componentId) {
			case 1:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(2480, 3437,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 2:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(3040, 4843,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			}
		} else if (stage == 3) {
			System.err.println("Called teleport in stage 3 in teleportcrystal method.");
			switch(componentId) {
			case 1:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(2675, 3714,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 2:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(3429, 3538,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 3:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(2917, 3745,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 4:
				break;
			}
		} else if (stage == 4) {
			switch(componentId) {
			case 1:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(2883, 9800,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 2:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(3077, 10058,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			default:
				end();
			}
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
