package com.sevador.game.node.model.combat;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.action.impl.FreezeAction;
import com.sevador.game.node.Node;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.region.RegionManager;
import com.sevador.game.region.path.DefaultPathFinder;
import com.sevador.game.region.path.PathEvent;
import com.sevador.game.region.path.PrimitivePathFinder;
import com.sevador.game.world.World;

/**
 * Handles the movement in combat.
 * @author Emperor
 *
 */
public class Movement {

	/**
	 * The entity.
	 */
	private final Entity entity;
	
	/**
	 * The last location we found a path to.
	 */
	@SuppressWarnings("unused")
	private Location lastLocation = null;
	
	/**
	 * The destination.
	 */
	private Location destination;
	
	/**
	 * Constructs a new {@code Movement} {@code Object}.
	 * @param entity The entity.
	 */
	public Movement(Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * Walks towards the victim, if we can't attack it.
	 * @param victim The victim.
	 * @param type The combat type.
	 * @return {@code true} if the entity can attack the victim.
	 */
	public boolean go(Entity victim, CombatType type) {
		if (victim.getLocation().distance(entity.getLocation()) < 1.5 && victim.getCombatAction().getVictim() == entity) {
			if (victim.getIndex() <= entity.getIndex()) {
				return true; //Fixes the constant following eachother.
			}
		}
		destination = getDestination(entity, victim);
		if (destination != null && type.canInteract(entity, type == CombatType.MELEE ? destination : victim)) {
			entity.getWalkingQueue().reset();
			return true;
		}
		if (entity.getActionManager().contains(FreezeAction.FLAG)) {
			return false;
		}
		if (true) {//lastLocation != victim.getLocation()) {
			lastLocation = victim.getLocation();
			entity.getWalkingQueue().reset();
			if (entity.isPlayer() || entity.isNPC() /*TODO: Decent primitive pathfinder, then remove this*/) {
				World.submit(new PathEvent(new DefaultPathFinder(), entity, destination));
			} else {
				World.submit(new PathEvent(new PrimitivePathFinder(), entity, destination));
			}
		}
		return false;
	}
	
	/**
	 * Gets the location the entity has to move to.
	 * @param entity The entity.
	 * @param victim The victim.
	 * @return The location to walk to.
	 */
	public static Location getDestination(Node entity, Node victim) {
		//If X > 0 - victim > entity, x < 0 victim < entity.
		Location delta = Location.getDelta(entity.getLocation(), victim.getLocation());
		boolean vertical = (delta.getY() < 0 ? -delta.getY() : delta.getY()) > (delta.getX() < 0 ? -delta.getX() : delta.getX());
		List<Location> victimList = null;
		List<Location> entityList = null;
		int z = entity.getLocation().getZ();
		if (vertical) {
			if (delta.getY() > 0) { //Victim has higher Y than entity.
				victimList = getSurrounding(victim, victim.getLocation().getY(), victim.getLocation().getX(), z, -1, true);
				entityList = getSurrounding(entity, entity.getLocation().getY(), entity.getLocation().getX(), z, entity.size(), true);
			} else {
				victimList = getSurrounding(victim, victim.getLocation().getY(), victim.getLocation().getX(), z, victim.size(), true);
				entityList = getSurrounding(entity, entity.getLocation().getY(), entity.getLocation().getX(), z, -1, true);
			}
		} else {
			if (delta.getX() > 0) { //Victim has higher X than entity.
				victimList = getSurrounding(victim, victim.getLocation().getX(), victim.getLocation().getY(), z, -1, false);
				entityList = getSurrounding(entity, entity.getLocation().getX(), entity.getLocation().getY(), z, entity.size(), false);
			} else {
				victimList = getSurrounding(victim, victim.getLocation().getX(), victim.getLocation().getY(), z, victim.size(), false);
				entityList = getSurrounding(entity, entity.getLocation().getX(), entity.getLocation().getY(), z, -1, false);
			}
		}
		double currentDistance = 999; //Random high number so we override first in the loop.
		Location victLoc = victim.getLocation();
		@SuppressWarnings("unused")
		Location entLoc = entity.getLocation();
		for (Location sl : entityList) {
			for (Location vl : victimList) {
				double distance = sl.distance(vl);
				if (distance < currentDistance) {
					currentDistance = distance;
					victLoc = vl;
					entLoc = sl;
				}
			}
		}
		return victLoc;//.transform(Location.getDelta(entity.getLocation(), entLoc));
	}
	
	/**
	 * Gets the tiles specified by {@code i}, {@code j} & {@code size}.
	 * @param n The node who's surrounding tiles we're finding.
	 * @param i The first base coordinate.
	 * @param j The second base coordinate.
	 * @param z The height.
	 * @param size The size.
	 * @param switched
	 * @return The tiles list.
	 */
	private static List<Location> getSurrounding(Node n, int i, int j, int z, int size, boolean switched) {
		List<Location> list = new ArrayList<Location>();
		int x = i + size;
		for (int y = j; y < j + n.size(); y++) {
			Location l = switched ? Location.locate(y, x, z) : Location.locate(x, y, z);
			if (RegionManager.isBasicClipped(l)) {
				list.add(l);
			}
		}
		return list;
	}
	
}