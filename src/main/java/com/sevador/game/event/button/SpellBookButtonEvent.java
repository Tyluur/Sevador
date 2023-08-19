package com.sevador.game.event.button;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.Main;
import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.item.TeletabActionEvent.Teletab;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.form.MagicSpell;
import com.sevador.game.node.model.combat.impl.MagicHandler;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.region.GroundItem;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Constants;
import com.sevador.utility.Priority;

/**
 * Handles a spellbook button event for modern, ancient, dungeoneering and lunar
 * magic.
 * 
 * @author Emperor
 * 
 */
public class SpellBookButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(192, this);
		EventManager.register(193, this);
		return EventManager.register(430, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		/*player.sendMessage("BUTTON: " + buttonId + " interfaceID: "
				+ interfaceId);*/
		if (handleMiscSpell(player, interfaceId, buttonId)) {
			return true;
		}
		boolean ancient = interfaceId == 193;
		boolean lunar = interfaceId == 430;
		boolean modern = interfaceId == 192;
		switch (buttonId) {
		case 24:
		case 48:
			player.teleport(Entity.DEFAULT_LOCATION);
			return true;
		case 40:
			if (player.getInventory().contains(554, 1)
					&& player.getInventory().contains(556, 3)
					&& player.getInventory().contains(563, 1)
					&& player.getSkills().getLevel(Skills.MAGIC) >= 25) {
				player.teleport(Location.locate(3212, 3421, 0));
				return true;
			} else {
				player.getPacketSender().sendMessage("You do not have the requirements to cast this spell.");
			}
			return true;
		case 43:
			if (player.getInventory().contains(557, 1)
					&& player.getInventory().contains(556, 3)
					&& player.getInventory().contains(563, 1)
					&& player.getSkills().getLevel(Skills.MAGIC) >= 37) {
				player.teleport(Teletab.LUMBRIDGE_TELEPORT.getLocation());
				return true;
			} else {
				player.getPacketSender().sendMessage("You do not have the requirements to cast this spell.");
			}
			return true;
		case 46:
			if (player.getInventory().contains(555, 1)
					&& player.getInventory().contains(556, 3)
					&& player.getInventory().contains(563, 1)
					&& player.getSkills().getLevel(Skills.MAGIC) >= 31) {
				player.teleport(Teletab.FALADOR_TELEPORT.getLocation());
				return true;
			} else {
				player.getPacketSender().sendMessage("You do not have the requirements to cast this spell.");
			}
			return true;
		case 51:
			if (player.getInventory().contains(563, 1) && player.getInventory().contains(556, 5) && player.getSkills().getLevel(Skills.MAGIC) >= 45) {
				player.teleport(Teletab.CAMELOT_TELEPORT.getLocation());
				return true;
			} else {
				player.getPacketSender().sendMessage("You do not have the requirements to cast this spell.");
			}
			return true;
		case 57:
			if (player.getInventory().contains(555, 2)
					&& player.getInventory().contains(563, 2)
					&& player.getSkills().getLevel(Skills.MAGIC) >= 51) {
				player.teleport(Teletab.ARDOUGNE_TELEPORT.getLocation());
				return true;
			} else {
				player.getPacketSender().sendMessage("You do not have the requirements to cast this spell.");
			}
			return true;
		case 62:
			if (player.getInventory().contains(557, 2)
					&& player.getInventory().contains(563, 2)
					&& player.getSkills().getLevel(Skills.MAGIC) >= 58) {
				player.teleport(Teletab.LUMBRIDGE_TELEPORT.getLocation());
				return true;
			} else {
				player.getPacketSender().sendMessage("You do not have the requirements to cast this spell.");
			}
			return true;
		case 69:
			if (player.getInventory().contains(554, 2)
					&& player.getInventory().contains(563, 2)
					&& player.getSkills().getLevel(Skills.MAGIC) >= 61) {
				player.teleport(Teletab.LUMBRIDGE_TELEPORT.getLocation());
				return true;
			} else {
				player.getPacketSender().sendMessage("You do not have the requirements to cast this spell.");
			}
			return true;
		default:
			//player.sendMessage("Button id: "+buttonId+" on interface "+interfaceId+" has no value. Report this on forums.");
			break;
		}
		if (modern && buttonId == 15 || ancient && buttonId == 9 || lunar
				&& buttonId == 11) {
			if (player.getAttribute("sortLevel", true)) {
				return true;
			}
			player.setAttribute("sortLevel", true);
			player.setAttribute("sortCombat", false);
			player.setAttribute("sortTeleport", false);
			return ancient ? refreshAncient(player)
					: modern ? refreshModern(player) : refreshLunar(player);
		} else if (modern && buttonId == 16 || ancient && buttonId == 10
				|| lunar && buttonId == 12) {
			if (player.getAttribute("sortCombat", false)) {
				return true;
			}
			player.setAttribute("sortCombat", true);
			player.setAttribute("sortLevel", false);
			player.setAttribute("sortTeleport", false);
			return ancient ? refreshAncient(player)
					: modern ? refreshModern(player) : refreshLunar(player);
		} else if (modern && buttonId == 17 || ancient && buttonId == 11
				|| lunar && buttonId == 13) {
			if (player.getAttribute("sortTeleport", false)) {
				return true;
			}
			player.setAttribute("sortTeleport", true);
			player.setAttribute("sortCombat", false);
			player.setAttribute("sortLevel", false);
			return ancient ? refreshAncient(player)
					: modern ? refreshModern(player) : refreshLunar(player);
		} else if (modern && buttonId == 7 || (lunar || ancient)
				&& buttonId == 5) {
			player.setAttribute("showCombat",
					!player.getAttribute("showCombat", true));
			return ancient ? refreshAncient(player)
					: modern ? refreshModern(player) : refreshLunar(player);
		} else if (modern && buttonId == 9 || (lunar || ancient)
				&& buttonId == 7) {
			player.setAttribute("showTeleport",
					!player.getAttribute("showTeleport", true));
			return ancient ? refreshAncient(player)
					: modern ? refreshModern(player) : refreshLunar(player);
		} else if (modern && buttonId == 11 || lunar && buttonId == 9) {
			player.setAttribute("showMisc",
					!player.getAttribute("showMisc", true));
			return modern ? refreshModern(player) : refreshLunar(player);
		} else if (modern && buttonId == 13) {
			player.setAttribute("showSkill",
					!player.getAttribute("showSkill", true));
			return refreshModern(player);
		} else if (modern && buttonId == 2 || ancient && buttonId == 18
				|| lunar && buttonId == 20) {
			player.setAttribute("defensiveCast",
					!player.getAttribute("defensiveCast", false));
			player.getIOSession().write(
					new ConfigPacket(player, 439, player.getAttribute(
							"defensiveCast", false) ? (ancient ? 257
									: (lunar ? 258 : 256)) : (ancient ? 1 : (lunar ? 2
											: 0))));
			return true;
		}
		player.getCombatAction().reset();
		int interfaceHash = modern ? 0 : ancient ? 1 : 2;
		if (player.getAttribute("autocastId", -1) == (buttonId | (interfaceHash << 16))) {
			player.getIOSession().write(
					new MessagePacket(player, "Autocast spell cleared."));
			player.getIOSession().write(new ConfigPacket(player, 108, -1));
			player.removeAttribute("autocastId");
			player.removeAttribute("spellId");
			player.getEquipment().refresh(); // TODO: Better way.
			return true;
		}
		MagicSpell spell = EventManager.getSpellEvent(buttonId
				| (interfaceHash << 16));
		if (spell == null
				|| !MagicHandler.checkRunes(player, spell.getRunes(), false)) {
			player.getIOSession().write(new MessagePacket(player, "You do not have enough runes to cast this spell."));
			if (player.getCredentials().getRights() == 2)
				player.getIOSession().write(new MessagePacket(player, 99, "Magic spell id: "+ (buttonId | interfaceHash << 16) + "."));
			return true;
		}
		player.getIOSession().write(new ConfigPacket(player, 43, 4));
		player.getIOSession().write(
				new ConfigPacket(player, 108, spell.getAutocastConfig()));
		player.getIOSession().write(
				new MessagePacket(player, "Autocast spell selected."));
		player.setAttribute("autocastId", buttonId | (interfaceHash << 16));
		player.removeAttribute("spellId");
		player.getCombatAction().setType(CombatType.MAGIC);
		return true;
	}

	/**
	 * Refreshes the player's modern spellbook interface.
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True}.
	 */
	public static boolean refreshModern(Player player) {
		int sortId = player.getAttribute("sortLevel", true) ? 0 : player
				.getAttribute("sortCombat", false) ? 1 : 2;
		player.getIOSession()
		.write(new ConfigPacket(
				player,
				1376,
				(player.getAttribute("showSkill", true) ? 0 : 2) << 9
				| (player.getAttribute("showTeleport", true) ? 0
						: 2) << 11
						| (player.getAttribute("showMisc", true) ? 0
								: 2) << 10
								| (player.getAttribute("showCombat", true) ? 0
										: 2) << 8 | sortId));
		return true;
	}

	/**
	 * Refreshes the player's ancient spellbook interface.
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True}.
	 */
	public static boolean refreshAncient(Player player) {
		int sortId = player.getAttribute("sortLevel", true) ? 0 : player
				.getAttribute("sortCombat", false) ? 1 : 2;
		player.getIOSession()
		.write(new ConfigPacket(
				player,
				1376,
				1 << 9
				| (player.getAttribute("showCombat", true) ? 0
						: 2) << 15
						| (player.getAttribute("showTeleport", true) ? 0
								: 2) << 16 | sortId << 3));
		return true;
	}

	/**
	 * Refreshes the player's lunar spellbook interface.
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True}.
	 */
	public static boolean refreshLunar(Player player) {
		int sortId = player.getAttribute("sortLevel", true) ? 0 : player
				.getAttribute("sortCombat", false) ? 1 : 2;
		player.getIOSession()
		.write(new ConfigPacket(
				player,
				1376,
				2 << 9
				| (player.getAttribute("showCombat", true) ? 0
						: 2) << 12
						| (player.getAttribute("showMisc", true) ? 0
								: 2) << 13
								| (player.getAttribute("showTeleport", true) ? 0
										: 2) << 14 | sortId << 6));
		return true;
	}

	/**
	 * Handles miscelanious Spells, eg. Vengeance, Teleports, ...
	 * 
	 * @param player
	 *            The player.
	 * @param interfaceId
	 *            The interface id.
	 * @param buttonId
	 *            The button id.
	 * @return {@code True} if an action got handled.
	 */
	private static boolean handleMiscSpell(Player player, int interfaceId,
			int buttonId) {
		switch (interfaceId) {
		case 192:
			switch (buttonId) {
			case 72:
				GroundItem item = player.getAttribute("gatestone");
				if (item != null) {
					player.getActionManager().register(
							new TeleportAction(player, item.getLocation(),
									TeleportAction.MODERN_ANIM,
									TeleportAction.MODERN_GRAPHIC,
									TeleportAction.MODERN_END_ANIM,
									TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4,
									item.getItem()));
					return true;
				}
				player.getPacketSender().sendMessage(
						"You do not have a gatestone dropped to teleport to.");
				return true;
			}
		case 430:
			switch (buttonId) {
			case 37:
				castVengeance(player);
				return true;
			case 39:
				player.getActionManager().register(
						new TeleportAction(player, Player.DEFAULT_LOCATION,
								TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				return true;
			default:
				if (Constants.isWindows())
					Main.getLogger().info("" + buttonId + " not handled for lunar spellbook.");
				return false;
			}
		}
		return false;
	}

	/**
	 * Casts the vengeance spell.
	 * 
	 * @param player
	 *            The player.
	 */
	public static void castVengeance(Player player) {
		if (!player.getInventory().contains(560, 2)
				|| !player.getInventory().contains(557, 10)
				|| !player.getInventory().contains(9075, 4)) {
			player.getPacketSender().sendMessage(
					"You don't have enough runes to cast vengeance.");
			return;
		}
		if (player.getSkills().getLevel(Skills.MAGIC) < 94) {
			player.getPacketSender().sendMessage(
					"You need a level of 94 magic to cast vengeance.");
			return;
		}
		if (player.getAttribute("vengeanceDelay", -1) > MajorUpdateWorker
				.getTicks()) {
			player.getPacketSender().sendMessage(
					"You can only cast vengeance Spells every 30 seconds.");
			return;
		}
		player.getInventory().remove(new Item(560, 2), new Item(557, 10),
				new Item(9075, 4));
		player.setAttribute("vengeanceDelay", MajorUpdateWorker.getTicks() + 50);
		player.setAttribute("vengeance", Boolean.TRUE);
		player.getUpdateMasks().register(
				new Animation(4410, 0, false, Priority.HIGH));
		player.getUpdateMasks().register(new Graphic(726, 96, 0, false));
	}

}
