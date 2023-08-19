package com.sevador.game.action.impl.movement;

import net.burtleburtle.tick.Tick;

import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.FaceLocationUpdate;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.region.path.DefaultPathFinder;
import com.sevador.game.world.World;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.OptionType;

/**
 * Handles an NPC action.
 * @author Emperor
 *
 */
public class NPCAction extends MovementAction {

	/**
	 * The npc.
	 */
	private final NPC npc;

	/**
	 * The option type.
	 */
	private final OptionType type;

	/**
	 * The last location of the NPC.
	 */
	private Location lastLocation;

	/**
	 * Constructs a new {@code NPCAction} {@code Object}.
	 * @param entity The entity.
	 * @param npc The NPC.
	 * @param type The option type.
	 * @param running If we are running (ctrl + click).
	 */
	public NPCAction(Entity entity, NPC npc, OptionType type, boolean running) {
		super(entity, npc.getLocation().getX(), npc.getLocation().getY(), running);
		this.npc = npc;
		this.type = type;
	}

	@Override
	public boolean execute() {
		if (entity.getLocation().getDistance(npc.getLocation()) < 2) {
			entity.getWalkingQueue().reset();
			World.getWorld().submit(new Tick(1) {
				@Override
				public boolean run() {
					entity.getUpdateMasks().register(new FaceLocationUpdate(entity, 
							npc.getLocation().transform(npc.size() >> 1, npc.size() >> 1, 0)));
					if (!EventManager.handleNPCActionEvent(entity.getPlayer(), npc, type)) {
						entity.getPlayer().getPacketSender().sendMessage(""+npc.getDefinition().name + " is not available to talk right now.");
						if (entity.getPlayer().getCredentials().getRights() == 2)
							entity.getPlayer().getIOSession().write(new MessagePacket(entity.getPlayer(), 99, "Unhandled npc action: " + npc.getId() + " - " + type + "."));
					}
					return true;
				}				
			});
			return true;
		}
		if (lastLocation != npc.getLocation()) {
			entity.getWalkingQueue().reset();
			lastLocation = npc.getLocation();
			super.x = lastLocation.getX();
			super.y = lastLocation.getY();
			if (super.execute()) {
				super.pathFinder = new DefaultPathFinder();
			} else {
				lastLocation = null; //Fail-safe.
			}
		}
		return false;
	}

}