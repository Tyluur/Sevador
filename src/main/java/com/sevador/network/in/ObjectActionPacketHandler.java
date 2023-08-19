package com.sevador.network.in;

import com.sevador.game.action.impl.movement.ObjectAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.OptionType;

/**
 * Handles incoming object action packets.
 * 
 * @author Emperor
 * 
 */
public class ObjectActionPacketHandler implements PacketSkeleton {

	/**
	 * The first object option opcode.
	 */
	private static final int FIRST_OPTION = 1;

	/**
	 * The second object option opcode.
	 */
	private static final int SECOND_OPTION = 39;

	/**
	 * The third object option opcode.
	 */
	private static final int THIRD_OPTION = 86;

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int y = packet.readShortA();
		int x = packet.readLEShortA();
		int objectId = packet.readLEShort();
		boolean running = packet.readByte() == 1;
		final Location location = Location.locate(x, y, player.getLocation()
				.getZ());
		GameObject object = location.getGameObject(objectId);
		if (object == null) {
			if (player.getCredentials().getRights() == 2)
				player.getIOSession().write(new MessagePacket(player, 99, "Object " + objectId+ " did not exist!"));
			return true;
		}
		player.getSkillAction().forceStop();
		if (player.getAttribute("inTutorial") != null && player.getAttribute("inTutorial").equals(true)) {
			player.getDialogueManager().startDialogue("StartDialogue");
			return true;
		}
		OptionType type = null;
		switch (packet.getOpcode()) {
		case FIRST_OPTION:
			type = OptionType.FIRST;
			break;
		case SECOND_OPTION:
			type = OptionType.SECOND;
			break;
		case THIRD_OPTION:
			type = OptionType.THIRD;
			break;
		}
		player.removeAttribute("m_o_d"); // m_o_d = manual-object-destination.
		if (type != null) {
			ObjectEvent event = EventManager.getObjectActionEvent(object
					.getId());
			if (event != null) {
				event.setDestination(player, object);
			}
			player.getActionManager().register(
					new ObjectAction(player, object, type, running));
		}
		return true;
	}

}
