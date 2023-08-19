package com.sevador.game.region.path;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.region.RegionManager;

/**
 * The projectile path finder.
 * @author Emperial
 *
 */
public class ProjectilePathFinder {
	
	/**
	 * The solid tile flag.
	 */
	public static final int SOLID_FLAG = 0x100;
	
	/**
	 * The north clipped tile flag.
	 */
	public static final int NORTH_FLAG = 0x48240000;
	
	/**
	 * The south clipped tile flag.
	 */
	public static final int SOUTH_FLAG = 0x40a40000;
	
	/**
	 * The east clipped tile flag.
	 */
	public static final int EAST_FLAG = 0x60240000;
	
	/**
	 * The west clipped tile flag.
	 */
	public static final int WEST_FLAG = 0x42240000;
	
	/**
	 * Attempts to find a projectile path to the opponent.
	 * @param e the entity.
	 * @param opponent the opponent.
	 * @return {@code true} if found, {@code false} if not.
	 */
	public static boolean findPath(Entity e, Entity opponent) {
		if(e == null || opponent == null) {
			return false;
		}
		Location start = e.getLocation();
		Location dest = opponent.getLocation();
		if(start.getZ() != dest.getZ()) {
			return false;
		}
		if(start == dest) {
			return true;
		}
		Location lastTile = null; //The last tile in the path.
		for(Location l : getPath(start, dest, start.getZ())) {
			int clippingMask = RegionManager.getClippingMask(l.getX(), l.getY(), l.getZ());
			if((clippingMask & SOLID_FLAG) != 0) {
				//Tile is solid.
				return false;
			}
			if(lastTile != null) {
				if(l.getY() > lastTile.getY() && (clippingMask & NORTH_FLAG) != 0) {
					//Tile y is higher than the last tile.
					return false;
				}
				if(l.getY() < lastTile.getY() && (clippingMask & SOUTH_FLAG) != 0) {
					//Tile y is lower than the last tile.
					return false;
				}
				if(l.getX() > lastTile.getX() && (clippingMask & EAST_FLAG) != 0) {
					//Tile x is higher than the last tile.
					return false;
				}
				if(l.getX() < lastTile.getX() && (clippingMask & WEST_FLAG) != 0) {
					//Tile x is lower than the last tile.
					return false;
				}
			}
			lastTile = l;
		}
		return true;
	}
	
	/**
	 * @return a list of tiles to the destination (the path).
	 */
	public static List<Location> getPath(Location start, Location dest, int z) {
		List<Location> tiles = new ArrayList<Location>();
		int startX = start.getX();
        int startY = start.getY();
        int endX = dest.getX();
        int endY = dest.getY();
        int diffX = endX - startX;
        int diffY = endY - startY;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int i = 0; i < max; i++) {
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
            tiles.add(Location.locate((endX - diffX), (endY - diffY), z));
        }
        return tiles;
	}

}
