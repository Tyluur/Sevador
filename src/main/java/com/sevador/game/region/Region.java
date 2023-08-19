package com.sevador.game.region;

import java.util.LinkedList;
import java.util.List;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;

/**
 * Represents a region tile.
 * 
 * @author Emperor
 * @author Dementhium Development team.
 * 
 */
public class Region {

	/**
	 * The clipping masks of this region.
	 */
	private int[][][] clippingMasks = new int[4][][];

	/**
	 * The locations in this region.
	 */
	private Location[][][] tiles = new Location[4][][];

	/**
	 * The region size.
	 */
	private int size;

	/**
	 * The base x-coordinate of this region.
	 */
	private int x;

	/**
	 * The base y-coordinate of this region.
	 */
	private int y;

	/**
	 * The player synchronization lock.
	 */
	private final Object playerLock = new Object();

	/**
	 * The npc synchronization lock.
	 */
	private final Object npcLock = new Object();

	/**
	 * A list of players in this region.
	 */
	@SuppressWarnings("unchecked")
	private final List<Player>[] players = new LinkedList[4];

	/**
	 * A list of NPCs in this region.
	 */
	@SuppressWarnings("unchecked")
	private final List<NPC>[] npcs = new LinkedList[4];

	/**
	 * The clipped flag.
	 */
	private boolean clipped;

	/**
	 * Constructs a new {@code Region} {@code Object}.
	 * 
	 * @param x
	 *            The base x-coordinate.
	 * @param y
	 *            The base y-coordinate.
	 * @param size
	 *            The size of the region.
	 */
	public Region(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		for (int i = 0; i < 4; i++) {
			players[i] = new LinkedList<Player>();
			npcs[i] = new LinkedList<NPC>();
		}
	}

	/**
	 * Gets a local location.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param z
	 *            The height.
	 * @return The location.
	 */
	public Location getLocalLocation(int x, int y, int z) {
		if (tiles[z] == null) {
			tiles[z] = new Location[size][size];
		}
		Location tile = tiles[z][x][y];
		if (tile == null) {
			tile = new Location(this.x << 7 | x, this.y << 7 | y, z, this);
			tiles[z][x][y] = tile;
		}
		return tile;
	}

	/**
	 * Adds an entity to this region.
	 * 
	 * @param entity
	 *            The entity to add.
	 */
	public void addEntity(Entity entity) {
		int z = entity.getLocation().getZ();
		if (entity.isPlayer()) {
			synchronized (playerLock) {
				if (!players[z].contains(entity)) {
					players[z].add(entity.getPlayer());
				}
			}
		} else if (entity.isNPC()) {
			synchronized (npcLock) {
				if (!npcs[z].contains(entity)) {
					npcs[z].add(entity.getNPC());
				}
			}
		}
	}

	/**
	 * Removes an entity from this region.
	 * 
	 * @param entity
	 *            The entity to remove.
	 */
	public void removeEntity(Entity entity) {
		int z = entity.getLocation().getZ();
		if (entity.isPlayer()) {
			synchronized (playerLock) {
				players[z].remove(entity.getPlayer());
			}
		} else if (entity.isNPC()) {
			synchronized (npcLock) {
				npcs[z].remove(entity.getNPC());
			}
		}
	}

	/**
	 * Moves all pending entities to the entity sets, clears the removed entity
	 * sets.
	 */
	public void finishUpdate() {

	}

	/**
	 * Gets the region size.
	 * 
	 * @return The size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the region base x-coordinate..
	 * 
	 * @return The region base x-coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the region base y-coordinate.
	 * 
	 * @return The base y-coordinate.
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x << 8 | y;
	}

	/**
	 * Sets the clipped flag.
	 * 
	 * @param clipped
	 *            The flag to set.
	 */
	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	/**
	 * Gets the clipped flag.
	 * 
	 * @return The clipping flag.
	 */
	public boolean isClipped() {
		return clipped;
	}

	/**
	 * Sets the clipping flag.
	 * 
	 * @param x
	 *            The local x coordinate.
	 * @param y
	 *            The local y coordinate.
	 * @param z
	 *            The height.
	 * @param flag
	 *            The flag to set.
	 */
	public void setClippingFlag(int x, int y, int z, int flag) {
		clippingMasks[z][x][y] = flag;
	}

	/**
	 * Gets the clipping masks in this region.
	 * 
	 * @return The clipping masks.
	 */
	public synchronized int[][][] getClippingMasks() {
		return clippingMasks;
	}

	/**
	 * @return the players
	 */
	public List<Player>[] getPlayers() {
		synchronized (playerLock) {
			return players;
		}
	}

	/**
	 * @return the npcs
	 */
	public List<NPC>[] getNpcs() {
		synchronized (npcLock) {
			return npcs;
		}
	}

	public static int getClippingMask(int x, int y, int z) {
		Region region = forCoords(x, y);
		if (region.clippingMasks[z] == null || !region.clipped) {
			return -1;
		}
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		return region.clippingMasks[z][localX][localY];
	}

	public static final int PLAYERS = 0, NPCS = 1;

	public static final int REGION_SIZE = 128;

	public static final int MAX_MAP_X = 16383, MAX_MAP_Y = 16383;

	private static Region[][] regions = new Region[(MAX_MAP_X + 1) / REGION_SIZE][(MAX_MAP_Y + 1) / REGION_SIZE];

	public static Region forCoords(int x, int y) {
		int regionX = x >> 7, regionY = y >> 7;
			Region r = regions[regionX][regionY];
			if (r == null) {
				r = regions[regionX][regionY] = new Region(regionX, regionY, REGION_SIZE);
			}
			return r;
	}
	
	public static boolean isPassable(Location l) {
		int clippingMask = getClippingMask(l.getX(), l.getY(), l.getZ());
		if (clippingMask == -1) {
			return true; //?
		}
		return clippingMask < 1;
	}

}