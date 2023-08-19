package com.sevador.network.in;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.action.impl.TradeAction;
import com.sevador.game.action.impl.movement.Following;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles incoming player action packets.
 * 
 * @author Emperor
 * 
 */
public class PlayerActionPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int index = packet.readShort();
		boolean running = packet.readByte() == 1;
		if (index > 2047 || index < 1) {
			return false;
		}
		player.getSkillAction().forceStop();
		Player p = NodeWorker.getPlayers().get(index);
		if (p == null) {
			return false;
		}
		switch (packet.getOpcode()) {
		case 43:
			if (World.getWorld().getAreaManager().getAreaByName("Duel").contains(player.getLocation())) {
				if (p.getAttribute("didRequestDuel") == Boolean.TRUE) {
//					player.setAttribute("duelSession", new DuelArena(p, player, (Boolean) p.getAttribute("isStaking")));
					p.setAttribute("duelSession", player.getAttribute("duelSession"));
				} else {
					player.getPacketSender().sendInterface(640).sendConfig(283, 67108864);
					player.setAttribute("isStaking", Boolean.FALSE);
					player.setAttribute("duelWithIndex", p.getIndex());
				}
			}
			player.getWalkingQueue().setRunning(running);
			player.getActionManager().register(player.getCombatAction());
			player.getCombatAction().setVictim(p);
			break;
		case 44:
			player.getActionManager().register(new Following(player, p, running));
			break;
		case 90:
			player.getActionManager().register(new TradeAction(player, p));
			break;
		}
		return true;
	}

}
