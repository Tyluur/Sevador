package com.sevador.game.action.impl.movement;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.cache.format.ObjectDefinition;
import net.burtleburtle.tick.Tick;

import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.FaceLocationUpdate;
import com.sevador.game.node.model.skills.agility.Agility;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionManager;
import com.sevador.game.region.path.DefaultPathFinder;
import com.sevador.game.world.World;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.OptionType;

/**
 * Handles the walking towards an object.
 * @author Emperor
 *
 */
public class ObjectAction extends MovementAction {

	/**
	 * The game object to handle.
	 */
	private final GameObject object;
	
	/**
	 * The option type.
	 */
	private final OptionType type;
	
	/**
	 * If we have found a path yet.
	 */
	private boolean foundPath = false;
	
	/**
	 * The destination.
	 */
	private Location destination;
	
	/**
	 * Constructs a new {@code ObjectAction} {@code Object}.
	 * @param entity The entity.
	 * @param object The object.
	 * @param type The option type.
	 * @param running If the entity is running (ctrl + mouseclick)
	 */
	public ObjectAction(Entity entity, GameObject object, OptionType type, boolean running) {
		super(entity, object.getLocation().getX(), object.getLocation().getY(), running);
		addFlag(FLAG);
		this.object = object;
		this.type = type;
		this.destination = object.getLocation();
	}

	@Override
	public boolean execute() {
		if (entity.getLocation() == destination || entity.getLocation().getX() == destination.getX() || entity.getLocation().getY() == destination.getY()) {
			World.getWorld().submit(new Tick(1) {
				@Override
				public boolean run() {
					ObjectDefinition def = object.getDefinition();
					entity.getUpdateMasks().register(new FaceLocationUpdate(entity, 
							object.getLocation().transform(def.sizeX >> 1, def.sizeY >> 1, 0)));
					if (!entity.getPlayer().getControlerManager().processObjectClick(object)) return true;
					if (Agility.handleObject(entity.getPlayer(), object)) {
						stop();
						return true;
					}
					if (!EventManager.handleObjectEvent(entity.getPlayer(), object, type)) {
						if (entity.getPlayer().getCredentials().getRights() == 2)
							entity.getPlayer().getIOSession().write(new MessagePacket(entity.getPlayer(), 99, "Unhandled object action: " + object + " - " + object.getDefinition().name + " - " + type + "."));
					}
					return true;
				}				
			});
			return true;
		}
		if (!foundPath) {
			destination = entity.getAttribute("m_o_d");
			if (destination == null) {
				destination = getDestination(entity.getPlayer(), object);
			}
			super.x = destination.getX();
			super.y = destination.getY();
			entity.getWalkingQueue().reset();
			foundPath = super.execute();
			pathFinder = new DefaultPathFinder();
		} else {
			foundPath = false;
		}
		return false;
	}
	
	/**
	 * Gets the destination location.
	 * @param p The player.
	 * @param object The object to walk to.
	 * @return The destination.
	 */
	public static Location getDestination(Player p, GameObject object) {
		Location delta = Location.getDelta(p.getLocation(), object.getLocation());
		boolean vertical = (delta.getY() < 0 ? -delta.getY() : delta.getY()) > (delta.getX() < 0 ? -delta.getX() : delta.getX());
		List<Location> victimList = null;
		Location pl = p.getLocation();
		ObjectDefinition d = object.getDefinition();
		int z = p.getLocation().getZ();
		if (vertical) {
			if (delta.getY() > 0) { //Victim has higher Y than entity.
				victimList = getLocations(object.getLocation().getY(), object.getLocation().getX(), -1, d.sizeX, z, true);
				pl = p.getLocation().transform(0, 1, 0);
			} else {
				victimList = getLocations(object.getLocation().getY(), object.getLocation().getX(), d.sizeY, d.sizeX, z, true);
				pl = p.getLocation().transform(0, -1, 0);
			}
		} else {
			if (delta.getX() > 0) { //Victim has higher X than entity.
				victimList = getLocations(object.getLocation().getX(), object.getLocation().getY(), -1, d.sizeY, z, false);
				pl = p.getLocation().transform(1, 0, 0);
			} else {
				victimList = getLocations(object.getLocation().getX(), object.getLocation().getY(), d.sizeX, d.sizeY, z, false);
				pl = p.getLocation().transform(-1, 0, 0);
			}
		}
		double currentDistance = 999; //Random high number so we override first in the loop.
		Location victLoc = object.getLocation();
		for (Location vl : victimList) {
			double distance = pl.distance(vl);
			if (distance < currentDistance) {
				currentDistance = distance;
				victLoc = vl;
			}
		}
		return victLoc;
	}
	

	/**
	 * Gets the possible locations that can be a destination.
	 * @param i The x-based coordinate.
	 * @param j The y-based coordinate.
	 * @param sizeX The x-based size.
	 * @param sizeY The y-based size.
	 * @param z The height.
	 * @param switched If the x & y coordinates should be switched.
	 * @return The list of possible locations.
	 */
	private static List<Location> getLocations(int i, int j, int sizeX, int sizeY, int z, boolean switched) {
		List<Location> list = new ArrayList<Location>();
		int x = i + sizeX;
		for (int y = j; y < j + sizeY; y++) {
			Location l = switched ? Location.locate(y, x, z) : Location.locate(x, y, z);
			if (RegionManager.isBasicClipped(l)) {
				list.add(l);
			}
		}
		return list;
	}
}