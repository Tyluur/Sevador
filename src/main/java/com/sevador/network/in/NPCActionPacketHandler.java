package com.sevador.network.in;

import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.action.impl.movement.NPCAction;
import com.sevador.game.node.model.mask.FaceEntityUpdate;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;
import com.sevador.utility.OptionType;

/**
 * Handles an incoming NPC action packet.
 * 
 * @author Emperor
 * 
 */
public class NPCActionPacketHandler implements PacketSkeleton {

	/**
	 * The attack NPC packet opcode.
	 */
	private static final int ATTACK_OPCODE = 70;

	/**
	 * The first NPC option.
	 */
	private static final int FIRST_OPTION = 29;

	/**
	 * The second NPC option.
	 */
	private static final int SECOND_OPTION = 10;

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int index = packet.readLEShortA();
		boolean running = packet.readByteC() == 1;
		/*if (index > NodeWorker.getNPCs().capacity()) {
			return false;
		}*/
		NPC npc = NodeWorker.getNPCs().get(index);
		if (npc == null) {
			return false;
		}
		OptionType type = null;
		player.turnTo(npc);	
		if (player.getAttribute("freezeImmunity", -1) > MajorUpdateWorker.getTicks()) {
			player.getPacketSender().sendMessage("You are stunned!");
			return true;
		}
		switch (packet.getOpcode()) {
		case ATTACK_OPCODE:
			player.getActionManager().register(player.getCombatAction());
			player.getCombatAction().setVictim(npc);
			return true;
		case FIRST_OPTION:
			type = OptionType.FIRST;
			break;
		case SECOND_OPTION:
			type = OptionType.SECOND;
			break;
		}
		player.setAttribute("m_o_d", npc.getLocation());
		npc.getUpdateMasks().register(new FaceEntityUpdate(player.getIndex(), true));
		if (type != null) {
			player.getActionManager().register(
					new NPCAction(player, npc, type, running));
		} else {
			System.out.println("Unhandled opcode for NPC action - "
					+ packet.getOpcode() + ".");
		}
		return true;
	}

}