package com.sevador.game.region;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;

/**
 * Loads and handles region tiles.
 * 
 * @author Emperor
 * @author Dementhium development team (mainly).
 *
 */
public class RegionManager {

	/**
	 * The region size.
	 */
	public static final int REGION_SIZE = 128;
	
	/**
	 * The region mapping.
	 */
	private static final Map<Integer, Region> REGION_CACHE = new ConcurrentHashMap<Integer, Region>();

	/**
	 * Gets a list of NPCs within reach.
	 * @param tile The location.
	 * @return The list of NPCs.
	 */
	public synchronized static List<NPC> getLocalNPCs(Location tile) {
		return getLocalNPCs(tile, 16);
	}

	/**
	 * Gets a list of NPCs within a distance of the {@code depth} parameter.
	 * @param tile The location.
	 * @param depth The maximum distance.
	 * @return A list of NPCs.
	 */
	public synchronized static List<NPC> getLocalNPCs(Location tile, int depth) {
		int baseX = tile.getX();
		int baseY = tile.getY();
		int z = tile.getZ();
		int lastRegionId = hash(baseX, baseY);
		Region region = forCoords(lastRegionId);
		List<Region> regions = new ArrayList<Region>();
		regions.add(region);
		for (int x = -depth; x < depth + 1; x++) {
			for (int y = -depth; y < depth + 1; y++) {
				int currentRegionId = hash(baseX + x, baseY + y);
				if (currentRegionId != lastRegionId) {
					lastRegionId = currentRegionId;
					if (!regions.contains(region)) {
						regions.add(forCoords(lastRegionId));
					}
				}
			}
		}
		List<NPC> list = new ArrayList<NPC>();
		for (Region r : regions) {
			synchronized(r.getNpcs()) {
				for (NPC n : r.getNpcs()[z]) {
					if (n.getLocation().getDistance(tile) <= depth) {
						list.add(n);
					}
				}
			}
		}
		return list;
	}

	/**
	 * Gets a list of players within reach.
	 * @param tile The location.
	 * @return The list of players.
	 */
	public synchronized static List<Player> getLocalPlayers(Location tile) {
		return getLocalPlayers(tile, 16);
	}

	/**
	 * Gets a list of players within a distance of the {@code depth} parameter.
	 * @param tile The location.
	 * @param depth The maximum distance.
	 * @return A list of players.
	 */
	public synchronized static List<Player> getLocalPlayers(Location tile, int depth) {
		int baseX = tile.getX();
		int baseY = tile.getY();
		int z = tile.getZ();
		int lastRegionId = hash(baseX, baseY);
		Region region = forCoords(lastRegionId);
		List<Region> regions = new ArrayList<Region>();
		regions.add(region);
		for (int x = -depth; x < depth; x++) {
			for (int y = -depth; y < depth; y++) {
				int currentRegionId = hash(baseX + x, baseY + y);
				if (currentRegionId != lastRegionId) {
					lastRegionId = currentRegionId;
					if (!regions.contains(region)) {
						regions.add(forCoords(lastRegionId));
					}
				}
			}
		}
		List<Player> list = new ArrayList<Player>();
		for (Region r : regions) {
			synchronized(r.getPlayers()) {
				for (Player p : r.getPlayers()[z]) {
					if (p.getLocation().getDistance(tile) <= depth) {
						list.add(p);
					}
				}
			}
		}
		return list;
	}

	/**
	 * Gets a hash of the given x & y-coordinates.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return The hash.
	 */
	public static int hash(int x, int y) {
		return x >> 7 << 8 | y >> 7;
	}
	
	/**
	 * Gets a region.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return The region.
	 */
	public static Region getRegion(int x, int y) {
		return forCoords(x, y);
	}

	/**
	 * Gets a local location from a region.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The height.
	 * @return The Location.
	 */
	public static Location getLocation(int x, int y, int z) {
		Region region = forCoords(x, y);
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		return region.getLocalLocation(localX, localY, z);
	}
	
	/**
	 * Gets a region.
	 * @param hash The hash.
	 * @return The region.
	 */
	public static Region forCoords(int hash) {
		Region region = REGION_CACHE.get(hash);
		if (region == null) {
			region = new Region(hash >> 8, hash & 0xFF, REGION_SIZE);
			REGION_CACHE.put(hash, region);
		}
		return region;
	}

	/**
	 * Gets a region.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return The region.
	 */
	public static Region forCoords(int x, int y) {
		x = x >> 7;
		y = y >> 7;
		int hash = x << 8 | y;
		Region region = REGION_CACHE.get(hash);
		if (region == null) {
			region = new Region(x, y, REGION_SIZE);
			REGION_CACHE.put(hash, region);
		}
		return region;
	}
	
	/**
	 * Gets the clipping mask for the given coordinates.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The height.
	 * @return The clipping mask.
	 */
	public static int getClippingMask(int x, int y, int z) {
		Region region = forCoords(x, y);
		if (region.getClippingMasks()[z] == null) {
			return -1;
		}
		/*TODO: if (region.getClippingMasks()[z] == null || !region.isClipped()) {
			DynamicRegion dynamicRegion = RegionBuilder.getDynamicRegion(x, y);
			if (dynamicRegion != null) {
				int baseLocalX = x - (((x >> 3) >> 3) << 6) ;
				int baseLocalY = y - (((y >> 3) >> 3) << 6);
				return dynamicRegion.getMask(z, baseLocalX, baseLocalY);
			}
			return -1;
		}*/
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		return region.getClippingMasks()[z][localX][localY];
	}
	
	/**
	 * Checks if a location is basic clipped.
	 * @param l The location.
	 * @return {@code true} if so.
	 */
	public static boolean isBasicClipped(Location l) {
		int clippingMask = getClippingMask(l.getX(), l.getY(), l.getZ());
		if (clippingMask == -1) {
			return true; //?
		}
        return (clippingMask & 0x1280180) == 0 && (clippingMask & 0x1280108) == 0
                && (clippingMask & 0x1280120) == 0 && (clippingMask & 0x1280102) == 0;
	}

	/**
	 * Gets a region.
	 * @param other The location.
	 * @return The region.
	 */
	public static Region forLocation(Location other) {
		return forCoords(other.getX(), other.getY());
	}
	
	/**
	 * Gets the regions mapping.
	 * @return The region mapping.
	 */
	public static Map<Integer, Region> getRegions() {
		return REGION_CACHE;
	}

}