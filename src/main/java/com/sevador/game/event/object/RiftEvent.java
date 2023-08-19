package com.sevador.game.event.object;

import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.skills.runecrafting.Talisman;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 * 
 */
public class RiftEvent implements ObjectEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.event.ObjectEvent#init()
	 */
	@Override
	public boolean init() {
		EventManager.register(7129, this);
		EventManager.register(7130, this);
		EventManager.register(7131, this);
		EventManager.register(7132, this);
		EventManager.register(7133, this);
		EventManager.register(7134, this);
		EventManager.register(7135, this);
		EventManager.register(7136, this);
		EventManager.register(7138, this);
		EventManager.register(7139, this);
		EventManager.register(7140, this);
		EventManager.register(7141, this);
		EventManager.register(2156, this);// Magic Portal
		EventManager.register(2157, this);// Magic Portal
		EventManager.register(2158, this);// Magic Portal
		EventManager.register(2273, this);// Portal
		EventManager.register(2274, this);// Portal
		EventManager.register(2465, this);// Portal
		EventManager.register(2466, this);// Portal
		EventManager.register(2467, this);// Portal
		EventManager.register(2468, this);// Portal
		EventManager.register(2469, this);// Portal
		EventManager.register(2470, this);// Portal
		EventManager.register(2471, this);// Portal
		EventManager.register(2472, this);// Portal
		EventManager.register(2473, this);// Portal
		EventManager.register(2474, this);// Portal
		EventManager.register(2475, this);// Portal
		EventManager.register(2476, this);// Portal
		EventManager.register(2477, this);// Portal
		EventManager.register(2503, this);// Portal
		EventManager.register(2504, this);// Portal
		EventManager.register(2505, this);// Portal
		EventManager.register(2506, this);// Portal
		EventManager.register(2507, this);// Portal
		EventManager.register(3078, this);// Portal
		EventManager.register(3826, this);// Portal
		EventManager.register(5138, this);// Portal
		return EventManager.register(7137, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ObjectEvent#handle(org.argonite.game.node.model
	 * .player.Player, org.argonite.game.node.model.gameobject.GameObject,
	 * org.argonite.util.OptionType)
	 */
	@Override
	public boolean handle(Player player, GameObject gameObject, OptionType type) {
		switch (gameObject.getId()) {
		case 2465:
			if (gameObject.getLocation().getX() == 2841
					&& gameObject.getLocation().getY() == 4828) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.AIR_TALISMAN.getOutsideLocation());
			} else {
				player.teleport(Location.locate(2687, 9506, 0));
			}
			break;
		case 2466:
			if (gameObject.getLocation().getX() == 2793
					&& gameObject.getLocation().getY() == 4827) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.MIND_TALISMAN.getOutsideLocation());
			} else {
				player.teleport(Location.locate(2682, 9506, 0));
			}
			break;
		case 2467:
			if (gameObject.getLocation().getX() == 3495
					&& gameObject.getLocation().getY() == 4832) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.WATER_TALISMAN.getOutsideLocation());
			}
			break;
		case 2468:
			if (gameObject.getLocation().getX() == 2655
					&& gameObject.getLocation().getY() == 4829) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.EARTH_TALISMAN.getOutsideLocation());
			}
			break;
		case 2469:
			if (gameObject.getLocation().getX() == 2576
					&& gameObject.getLocation().getY() == 4846) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.FIRE_TALISMAN.getOutsideLocation());
			}
			break;
		case 2470:
			if (gameObject.getLocation().getX() == 2521
					&& gameObject.getLocation().getY() == 4833) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.BODY_TALISMAN.getOutsideLocation());
			}
			break;
		case 2471:
			if (gameObject.getLocation().getX() == 2121
					&& gameObject.getLocation().getY() == 4833) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.COSMIC_TALISMAN.getOutsideLocation());
			}
			break;
		case 2472:
			if (gameObject.getLocation().getX() == 2464
					&& gameObject.getLocation().getY() == 4817) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.LAW_TALISMAN.getOutsideLocation());
			}
			break;
		case 2473:
			if (gameObject.getLocation().getX() == 2400
					&& gameObject.getLocation().getY() == 4834) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.NATURE_TALISMAN.getOutsideLocation());
			}
			break;
		case 2474:
			if (gameObject.getLocation().getX() == 2282
					&& gameObject.getLocation().getY() == 4837) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.CHAOS_TALISMAN.getOutsideLocation());
			}
			break;
		case 2475:
			if (gameObject.getLocation().getX() == 2208
					&& gameObject.getLocation().getY() == 4829) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.DEATH_TALISMAN.getOutsideLocation());
			}
			break;
		case 2477:
			if (gameObject.getLocation().getX() == 2468
					&& gameObject.getLocation().getY() == 4888) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Talisman.BLOOD_TALISMAN.getOutsideLocation());
			}
			break;
		case 2492:
			if (gameObject.getLocation().getX() == 2889
					&& gameObject.getLocation().getY() == 4813) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Location.locate(3253, 3400, 0));
			}
			if (gameObject.getLocation().getX() == 2933
					&& gameObject.getLocation().getY() == 4815) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Location.locate(3253, 3400, 0));
			}
			if (gameObject.getLocation().getX() == 2932
					&& gameObject.getLocation().getY() == 4854) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Location.locate(3253, 3400, 0));
			}
			if (gameObject.getLocation().getX() == 2885
					&& gameObject.getLocation().getY() == 4850) {
				player.getPacketSender().sendMessage("You step through the portal...");
				player.teleport(Location.locate(3253, 3400, 0));
			}
			break;
		case 7129:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.FIRE_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7130:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.EARTH_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7131:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.BODY_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7132:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.COSMIC_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7133:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.NATURE_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7134:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.CHAOS_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7135:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.LAW_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7136:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.DEATH_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7137:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.WATER_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7138:
			break;
		case 7139:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.AIR_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7140:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.MIND_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		case 7141:
			player.getActionManager().register(
					new TeleportAction(player, Talisman.BLOOD_TALISMAN
							.getInsideLocation(), TeleportAction.MODERN_ANIM,
							TeleportAction.MODERN_GRAPHIC,
							TeleportAction.MODERN_END_ANIM,
							TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
			break;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ObjectEvent#setDestination(org.argonite.game.
	 * node.model.player.Player,
	 * org.argonite.game.node.model.gameobject.GameObject)
	 */
	@Override
	public void setDestination(Player p, GameObject obj) {
		obj.getLocation();
	}

}
