package com.sevador.game.region.path;

import com.sevador.game.node.Node;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.region.path.PathState.Position;

/**
 * Handles the pathfinding of a path.
 * @author Emperor
 *
 */
public class PathEvent implements Runnable {

	/**
	 * The pathfinder to use.
	 */
	private final PathFinder pathFinder;
	
	/**
	 * The entity walking this path.
	 */
	private final Entity entity;
	 
	/**
	 * The destination node.
	 */
	private final Node destination;
	
	/**
	 * If we should ignore the last step.
	 */
	private final boolean ignoreLast;
	
	/**
	 * If we should move near the destination if we can't find a path.
	 */
	private final boolean moveNear;
	
	/**
	 * Constructs a new {@code PathEvent} {@code Object}.
	 * @param pathFinder The path finder to use.
	 * @param entity The entity walking.
	 * @param destination The destination node.
	 */
	public PathEvent(PathFinder pathFinder, Entity entity, Node destination) {
		this.pathFinder = pathFinder;
		this.entity = entity;
		this.destination = destination;
		this.ignoreLast = false;
		this.moveNear = true;
	}
	
	/**
	 * Constructs a new {@code PathEvent} {@code Object}.
	 * @param pathFinder The path finder to use.
	 * @param entity The entity walking.
	 * @param destination The destination node.
	 * @param ignoreLast If we should ignore the last step.
	 */
	public PathEvent(PathFinder pathFinder, Entity entity, Node destination, boolean ignoreLast) {
		this.pathFinder = pathFinder;
		this.entity = entity;
		this.destination = destination;
		this.ignoreLast = ignoreLast;
		this.moveNear = true;
	}
	
	/**
	 * Constructs a new {@code PathEvent} {@code Object}.
	 * @param pathFinder The path finder to use.
	 * @param entity The entity walking.
	 * @param destination The destination node.
	 * @param ignoreLast If we should ignore the last step.
	 * @param moveNear If we should move near the destination if we can't find a path.
	 */
	public PathEvent(PathFinder pathFinder, Entity entity, Node destination, boolean ignoreLast, boolean moveNear) {
		this.pathFinder = pathFinder;
		this.entity = entity;
		this.destination = destination;
		this.ignoreLast = ignoreLast;
		this.moveNear = moveNear;
	}
	
	@Override
	public void run() {
		try {
			Location dest = destination.getLocation();
			Location base = entity.getLocation();
			int srcX = entity.getLocation().getViewportX(0);
			int srcY = entity.getLocation().getViewportY(0);
			int destX = dest.getViewportX(base, 0);
			int destY = dest.getViewportY(base, 0);
			PathState state = pathFinder.findPath(entity, entity.getLocation(), srcX, srcY, destX, destY, entity.getLocation().getZ(), 0, entity.getWalkingQueue().isRunning(), ignoreLast, moveNear);
			if (state != null) {
				for (Position step : state.getPoints()) {
					if (step == null || step.getX() == -1) continue;
			        entity.getWalkingQueue().addPath(step.getX(), step.getY());
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}