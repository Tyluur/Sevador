package com.sevador.network.in;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.action.impl.FreezeAction;
import com.sevador.game.action.impl.combat.CombatAction;
import com.sevador.game.action.impl.movement.Following;
import com.sevador.game.action.impl.packetactions.EmoteAction;
import com.sevador.game.action.impl.packetactions.RestAction;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.FaceEntityUpdate;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.path.DefaultPathFinder;
import com.sevador.game.region.path.PathFinder;
import com.sevador.game.region.path.PathState;
import com.sevador.game.region.path.PathState.Position;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

public class WalkPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int steps = (packet.getSize() - 5) >> 1;
		if (steps > 25) {
			return false;
		}
		int y = packet.readLEShort();
		boolean running = packet.readByteC() == 1;
		int x = packet.readLEShortA();
		if (player.getAttribute("inTutorial") != null && player.getAttribute("inTutorial").equals(true)) {
			player.getDialogueManager().startDialogue("StartDialogue");
			return true;
		}
		player.getWalkingQueue().reset();
        walk(player, x, y, running);
        player.getSkillAction().forceStop();
        player.getDialogueManager().finishDialogue();
		return true;
	}
	
	public static void walk(Player player, int x, int y, boolean running) {
		if(executeWalk(player, x, y, running, new DefaultPathFinder())) {
			//Add movement action that does not execute here.
		}
	}
	
	private static boolean executeWalk(Player entity, int x, int y, boolean running, PathFinder pathFinder) {
		if(entity.getActionManager().contains(FreezeAction.FLAG)) {
			entity.getPlayer().getPacketSender().sendMessage("A magical force stops you from moving.");
			return false;
		}
		if (entity.getAttribute("freezeImmunity", -1) > MajorUpdateWorker.getTicks()) {
			entity.getPlayer().getPacketSender().sendMessage("You are stunned!");
			return true;
		}
		if(entity.getActionManager().contains(EmoteAction.FLAG)) {
			return false;
		}
		if(entity.getActionManager().contains(CombatAction.FLAG))
			entity.getActionManager().unregister(CombatAction.FLAG);
		if(entity.getActionManager().contains(Following.FLAG))
			entity.getActionManager().unregister(Following.FLAG);
		if(entity.getActionManager().contains(RestAction.FLAG))
			entity.getActionManager().unregister(RestAction.FLAG);
		entity.getUpdateMasks().register(new FaceEntityUpdate(-1, entity.isNPC()));
		entity.getWalkingQueue().reset(running);
		entity.getPacketSender().sendChatBoxInterface(137).setDefaultInventory().sendCloseInterface();
		try {
			Location dest = Location.locate(x, y, entity.getLocation().getZ());
			Location base = entity.getLocation();
			int srcX = entity.getLocation().getViewportX(0);
			int srcY = entity.getLocation().getViewportY(0);
			int destX = dest.getViewportX(base, 0);
			int destY = dest.getViewportY(base, 0);
			PathState state = pathFinder.findPath(entity, entity.getLocation(), srcX, srcY, destX, destY, entity.getLocation().getZ(), 0, entity.getWalkingQueue().isRunning(), false, true);
			if (state != null) {
				for (Position step : state.getPoints()) {
					System.out.println("Step{" + step.getX() + ", " + step.getY() + "}");
			        entity.getWalkingQueue().addPath(step.getX(), step.getY());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
