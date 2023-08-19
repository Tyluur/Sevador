package com.sevador.network.in;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.action.impl.combat.CombatAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles the option on entity interaction packet.
 * 
 * @author Emperor
 * 
 */
public final class OptionEntityInteraction implements PacketSkeleton {

	/**
	 * The option on player packet opcode.
	 */
	private static final int OPTION_ON_PLAYER = 65;

	/**
	 * The option on NPC packet opcode.
	 */
	private static final int OPTION_ON_NPC = 57;

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		case OPTION_ON_PLAYER:
			int index = packet.readLEShort();
			boolean running = packet.readByte() == 1;
			int id = packet.readShortA();
			int interfaceHash = packet.readLEInt();
			int interfaceId = interfaceHash >> 16;
			int componentId = interfaceHash & 0xFFFF;
			int slot = packet.readLEShortA();
			if (index < 0 || index > 2048) {
				return false;
			}
			Player p = NodeWorker.getPlayers().get(index);
			switch (interfaceId) {
			case 192:
			case 193:
			case 430:
				castSpellEvent(player, p, interfaceId, componentId);
				return true;
			case 747:
					if (player.getFamiliar() != null)
						player.getFamiliar().requestCombat(p);
				return true;
			}
			System.out.println("Incoming option on player packet [index=" + index
					+ ", running=" + running + ", interf.id=" + interfaceId
					+ ", comp.id=" + componentId + ", id=" + id + ", slot="
					+ slot + "].");
			return true;
		case OPTION_ON_NPC:
			running = packet.readByteS() == 1;
			slot = packet.readLEShort();
			interfaceHash = packet.readInt1();
			interfaceId = interfaceHash >> 16;
			componentId = interfaceHash & 0xFFFF;
			id = packet.readLEShort();
			index = packet.readLEShort();
			if (index < 0 || index > Short.MAX_VALUE) {
				return false;
			}
			NPC npc = NodeWorker.getNPCs().get(index);
			switch (interfaceId) {
			case 192:
			case 193:
			case 430:
				castSpellEvent(player, npc, interfaceId, componentId);
				return true;
			case 747:
				if (componentId == 14) {
					if (player.getFamiliar() != null) {
						player.getFamiliar().requestCombat(npc);
						npc.getCombatAction().setVictim(player.getFamiliar());
						npc.getActionManager().register(new CombatAction(player.getFamiliar()));
						player.getFamiliar().getActionManager().register(new CombatAction(npc));
					}
				}
				break;
			}
			System.out.println("Incoming option on NPC packet [index=" + index
					+ ", running=" + running + ", interf.id=" + interfaceId
					+ ", comp.id=" + componentId + ", id=" + id + ", slot="
					+ slot + "].");
			return true;
		}
		return false;
	}

	/**
	 * Handles the spell event for an interaction between the player and the
	 * victim.
	 * 
	 * @param player
	 *            The player.
	 * @param victim
	 *            The victim.
	 * @param interfaceId
	 *            The interface id.
	 * @param spellId
	 *            The spell id.
	 */
	private static void castSpellEvent(Player player, Entity victim,
			int interfaceId, int spellId) {
		player.getCombatAction().setType(CombatType.MAGIC);
		int spell = spellId
				| (interfaceId == 192 ? 0 : interfaceId == 193 ? 1 : 2) << 16;
		player.setAttribute("spellId", spell);
		player.getCombatAction().setVictim(victim);
		player.getActionManager().register(player.getCombatAction());
	}
}