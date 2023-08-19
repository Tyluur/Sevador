package com.sevador.game.region.path;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;

/**
 * The pathfinder interface.
 * 
 * @author Graham
 * 
 */
public interface PathFinder {

	public static final int SOUTH_FLAG = 0x1, WEST_FLAG = 0x2, NORTH_FLAG = 0x4, EAST_FLAG = 0x8;

	public static final int SOUTH_WEST_FLAG = SOUTH_FLAG | WEST_FLAG;
	public static final int NORTH_WEST_FLAG = NORTH_FLAG | WEST_FLAG;
	public static final int SOUTH_EAST_FLAG = SOUTH_FLAG | EAST_FLAG;
	public static final int NORTH_EAST_FLAG = NORTH_FLAG | EAST_FLAG;

	public static final int SOLID_FLAG = 0x20000;
	public static final int UNKNOWN_FLAG = 0x40000000;

	/**
	 * Finds a path for the entity.
	 * @param mob The entity.
	 * @param base The base location.
	 * @param srcX The source x-coordinate.
	 * @param srcY The source y-coordinate.
	 * @param dstX The destination x-coordinate.
	 * @param dstY The destination y-coordinate.
	 * @param z The height.
	 * @param radius The radius.
	 * @param running If the entity should run.
	 * @param ignoreLastStep If we should ignore the last step.
	 * @param moveNear If we should move near if we can't find a path to the destination.
	 * @return The path state.
	 */
	public PathState findPath(Entity mob, Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear);

}