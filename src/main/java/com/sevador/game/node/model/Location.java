package com.sevador.game.node.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.cache.format.CS2ScriptSettings;

import com.sevador.Main;
import com.sevador.game.node.Node;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.control.impl.DuelArenaControler;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.region.Region;
import com.sevador.game.region.RegionManager;
import com.sevador.utility.Misc;

/**
 * Represents a single location on the world-map.
 * 
 * @author Emperor
 * 
 */
public class Location implements Serializable, Node {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -1557602170262234102L;

	/**
	 * Represents the viewport sizes.
	 */
	public final static int[] VIEWPORT_SIZES = { 104, 120, 136, 168 };

	/**
	 * The amount of sectors per region.
	 */
	public static final int SECTORS_PER_REGION = 8;

	/**
	 * The sector length.
	 */
	public static final int SECTOR_LENGTH = 4;

	/**
	 * The x-coordinate.
	 */
	private final int x;

	/**
	 * The y-coordinate.
	 */
	private final int y;

	/**
	 * The z-coordinate.
	 */
	private final int z;

	/**
	 * The region this location is in.
	 */
	private final transient Region region;

	/**
	 * A list of game objects on this region.
	 */
	private transient List<GameObject> objects;

	/**
	 * Constructs a new {@code Location} {@code Object}.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param z
	 *            The z-coordinate.
	 */
	public Location(int x, int y, int z, Region region) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.region = region;
	}

	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.region = RegionManager.getLocation(x, y, z).getRegion();
	}

	/**
	 * Gets a location from the RegionManager.
	 * 
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param height
	 *            The height.
	 * @return The location.
	 */
	public static Location locate(int x, int y, int height) {
		return RegionManager.getLocation(x, y, height);
	}

	/**
	 * Returns a location.
	 * 
	 * @param diffX
	 *            The amount to increase the current x-coordinate with.
	 * @param diffY
	 *            The amount to increase the current y-coordinate with.
	 * @param diffZ
	 *            The amount to increase the current height with.
	 * @return The location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return locate(x + diffX, y + diffY, z + diffZ);
	}

	/**
	 * Returns a location calculated by increasing this coordinates with the
	 * given location's coordinates..
	 * 
	 * @param l
	 *            The delta location.
	 * @return The location.
	 */
	public Location transform(Location l) {
		return locate(x + l.x, y + l.y, z + l.z);
	}

	/**
	 * Gets a game object from the objects list.
	 * 
	 * @param id
	 *            The object id.
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public GameObject getGameObject(int id) {
		if (objects == null) {
			return null;
		}
		for (GameObject object : objects) {
			if (object.getId() == id) {
				return object;
			}
		}
		return null;
	}

	/**
	 * Gets a game object from the objects list.
	 * 
	 * @param type
	 *            The object type.
	 * @return The game object, or null if the list didn't contain this object
	 *         type.
	 */
	public GameObject getGameObjectType(int type) {
		if (objects == null) {
			return null;
		}
		for (GameObject object : objects) {
			if (object.getType() == type) {
				return object;
			}
		}
		return null;
	}

	/**
	 * Checks if the location contains any objects.
	 * 
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean hasObjects() {
		return objects != null && objects.size() > 0;
	}

	/**
	 * Adds a game object.
	 * 
	 * @param object
	 *            The object to add.
	 */
	public void addObject(GameObject object) {
		if (objects == null) {
			objects = new ArrayList<GameObject>();
		}
		objects.add(object);
	}

	/**
	 * Removes a game object.
	 * 
	 * @param oldObj
	 *            The object to remove.
	 */
	public void removeObject(GameObject oldObj) {
		if (objects != null) {
			objects.remove(oldObj);
		}
	}

	/**
	 * Gets the viewport x.
	 * 
	 * @param depth
	 *            The depth.
	 * @return The viewport x.
	 */
	public int getViewportX(int depth) {
		return getViewportX(this, depth);
	}

	/**
	 * Gets the viewport y.
	 * 
	 * @param depth
	 *            The depth.
	 * @return The viewport y.
	 */
	public int getViewportY(int depth) {
		return getViewportY(this, depth);
	}

	/**
	 * Gets the viewport x.
	 * 
	 * @param base
	 *            The base location.
	 * @param depth
	 *            The depth.
	 * @return The viewport x.
	 */
	public int getViewportX(Location base, int depth) {
		depth = VIEWPORT_SIZES[depth];
		return x
				- (SECTORS_PER_REGION * (base.getRegionX() - (depth >> SECTOR_LENGTH)));
	}

	/**
	 * Gets the viewport y.
	 * 
	 * @param base
	 *            The base location.
	 * @param depth
	 *            The depth.
	 * @return The viewport y.
	 */
	public int getViewportY(Location base, int depth) {
		depth = VIEWPORT_SIZES[depth];
		return y
				- (SECTORS_PER_REGION * (base.getRegionY() - (depth >> SECTOR_LENGTH)));
	}

	/**
	 * Gets the x-coordinate.
	 * 
	 * @return The x-coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y-coordinate.
	 * 
	 * @return The y-coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the z-coordinate.
	 * 
	 * @return The z-coordinate.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Gets the region x-coordinate.
	 * 
	 * @return The region x-coordinate.
	 */
	public int getRegionX() {
		return x >> 3;
	}

	/**
	 * Gets the region y-coordinate.
	 * 
	 * @return The region y-coordinate.
	 */
	public int getRegionY() {
		return y >> 3;
	}

	private int[] musicIds;

	public int getMusicId() {
		if (musicIds == null)
			return -1;
		if (musicIds.length == 1)
			return musicIds[0];
		return musicIds[Misc.random(musicIds.length - 1)];
	}

	public void loadMusicIds() {
		int musicId1 = getMusicId(getMusicName1(getRegionID()));
		if (musicId1 != -1) {
			int musicId2 = getMusicId(getMusicName2(getRegionID()));
			if (musicId2 != -1) {
				int musicId3 = getMusicId(getMusicName3(getRegionID()));
				if (musicId3 != -1)
					musicIds = new int[] { musicId1, musicId2, musicId3 };
				else
					musicIds = new int[] { musicId1, musicId2 };
			} else
				musicIds = new int[] { musicId1 };
		}
		if (musicIds != null)
			Main.getLogger().info("MusicPacket loaded for region " + getRegionID() + " " + musicIds[0]);
	}

	private static final int getMusicId(String musicName) {
		if (musicName == null)
			return -1;
		if (musicName.equals(""))
			return -2;
		int musicIndex = (int) CS2ScriptSettings.forId(1345).getKey(musicName);
		return CS2ScriptSettings.forId(1351).getKey(musicIndex);
	}

	public static final String getMusicName3(int regionId) {
		switch (regionId) {
		// towers pk lobby musics
		case 40348:
		case 40349:
		case 40092:
		case 40093:
			return "Godslayer";
		case 14948:
			return "Dominion Lobby III";
		default:
			return null;
		}
	}

	public static final String getMusicName2(int regionId) {
		switch (regionId) {
		// towers pk lobby musics
		case 40348:
		case 40349:
		case 40092:
		case 40093:
			return "The Task at Hand";
		case 14948:
			return "Dominion Lobby II";
		default:
			return null;
		}
	}

	public static final String getMusicName1(int regionId) {
		switch (regionId) {
		case 6992:
		case 6993: // mole lair
			return "The Mad Mole";
			// towers pk lobby musics
		case 40348:
		case 40349:
		case 40092:
		case 40093:
			return "Freshwater";
		case 9776: // castle wars
			return "Melodrama";
		case 10029:
		case 10285:
			return "Jungle Hunt";
		case 14231: // barrows under
			return "Dangerous Way";
		case 12856: // chaos temple
			return "Faithless";
		case 13104:
		case 12847: // arround desert camp
		case 13359:
		case 13102:
			return "Desert Voyage";
		case 13103:
			return "Lonesome";
		case 12589: // granite mine
			return "The Desert";
		case 13360: // dominion tower outside
			return "";
		case 14948:
			return "Dominion Lobby I";
		case 11836: // lava maze near kbd entrance
			return "Attack3";
		case 12091: // lava maze west
			return "Wilderness2";
		case 12092: // lava maze north
			return "Wild Side";
		case 9781:
			return "Gnome Village";
		case 11339: // air altar
			return "Serene";
		case 11083: // mind altar
			return "Miracle Dance";
		case 10827: // water altar
			return "Zealot";
		case 10571: // earth altar
			return "Down to Earth";
		case 10315: // fire altar
			return "Quest";
		case 8523: // cosmic altar
			return "Stratosphere";
		case 9035: // chaos altar
			return "Complication";
		case 8779: // death altar
			return "La Mort";
		case 10059: // body altar
			return "Heart and Mind";
		case 9803: // law altar
			return "Righteousness";
		case 9547: // nature altar
			return "Understanding";
		case 9804: // blood altar
			return "Bloodbath";
		case 13107:
			return "Arabian2";
		case 13105:
			return "Al Kharid";
		case 12342:
			return "Forever";
		case 10806:
			return "Overtude";
		case 10899:
			return "Karamja Jam";
		case 13623:
			return "The Terrible Tower";
		case 12374:
			return "The Route of All Evil";
		case 9802:
			return "Undead Dungeon";
		case 10809: // east rellekka
			return "Borderland";
		case 10553: // Rellekka
			return "Rellekka";
		case 10552: // south
			return "Saga";
		case 10296: // south west
			return "Lullaby";
		case 10828: // south east
			return "Legend";
		case 9275:
			return "Volcanic Vikings";
		case 11061:
		case 11317:
			return "Fishing";
		case 9551:
			return "TzHaar!";
		case 12345:
			return "Eruption";
		case 12089:
			return "Dark";
		case 12446:
		case 12445:
			return "Wilderness";
		case 12343:
			return "Dangerous";
		case 14131:
			return "Dance of the Undead";
		case 11844:
		case 11588:
			return "The Vacant Abyss";
		case 13363: // duel arena hospital
			return "Shine";
		case 13362: // duel arena
			return "Duel Arena";
		case 12082: // port sarim
			return "Sea Shanty2";
		case 12081: // port sarim south
			return "Tomorrow";
		case 11602:
			return "Strength of Saradomin";
		case 12590:
			return "Bandit Camp";
		case 10329:
			return "The Sound of Guthix";
		case 9033:
			return "Attack5";
			// godwars
		case 11603:
			return "Zamorak Zoo";
		case 11346:
			return "Armadyl Alliance";
		case 11347:
			return "Armageddon";
		case 13114:
			return "Wilderness";
			// black kngihts fortess
		case 12086:
			return "Knightmare";
			// tzaar
		case 9552:
			return "Fire and Brimstone";
			// kq
		case 13972:
			return "Insect Queen";
			// clan wars free for all:
		case 11094:
			return "Clan Wars";
			/*
			 * tutorial island
			 */
		case 12336:
			return "Newbie Melody";
			/*
			 * darkmeyer
			 */
		case 14644:
			return "Darkmeyer";
			/*
			 * kalaboss
			 */
		case 13626:
		case 13627:
		case 13882:
			return "Born to Do This";
			/*
			 * Lumbridge, falador and region.
			 */
		case 11574: // heroes guild
			return "Splendour";
		case 12851:
			return "Autumn Voyage";
		case 12338: // draynor and market
			return "Unknown Land";
		case 12339: // draynor up
			return "Start";
		case 12340: // draynor mansion
			return "Spooky";
		case 12850: // lumbry castle
			return "Harmony";
		case 12849: // east lumbridge swamp
			return "Yesteryear";
		case 12593: // at Lumbridge Swamp.
			return "Book of Spells";
		case 12594: // on the path between Lumbridge and Draynor.
			return "Dream";
		case 12595: // at the Lumbridge windmill area.
			return "Flute Salad";
		case 12854: // at Varrock Palace.
			return "Adventure";
		case 12853: // at varrock center
			return "Garden";
		case 12852: // varock mages
			return "Expanse";
		case 13108:
			return "Still Night";
		case 12083:
			return "Wander";
		case 11828:
			return "Fanfare";
		case 11829:
			return "Scape Soft";
		case 11577:
			return "Mad Eadgar";
		case 10293: // at the Fishing Guild.
			return "Mellow";
		case 11573:
		case 11575:
		case 11823:
			return "Mudskipper Melody";
		case 11824:
			return "Sea Shanty2";
		case 11570:
			return "Wandar";
		case 12341:
			return "Barbarianims";
		case 12855:
			return "Crystal Sword";
		case 12344:
			return "Dark";
		case 12599:
			return "Doorways";
		case 12598:
			return "The TradeHandler Parade";
		case 11318:
			return "Ice Melody";
		case 12600:
			return "Scape Wild";
		case 10032: // west yannile:
			return "Big Chords";
		case 10288: // east yanille
			return "Magic Dance";
		case 11826: // Rimmington
			return "Long Way Home";
		case 11825: // rimmigton coast
			return "Attention";
		case 11827: // north rimmigton
			return "Nightfall";
			/*
			 * Camelot and region.
			 */
		case 11062:
		case 10805:
			return "Camelot";
		case 10550:
			return "Talking Forest";
		case 10549:
			return "Lasting";
		case 10548:
			return "Wonderous";
		case 10547:
			return "Baroque";
		case 10291:
		case 10292:
			return "Knightly";
		case 11571: // crafting guild
			return "Miles Away";
		case 11595: // ess mine
			return "Rune Essence";
		case 10294:
			return "Theme";
		case 12349:
			return "Mage Arena";
		case 13365: // digsite
			return "Venture";
		case 13364: // exams center
			return "Medieval";
		case 13878: // canifis
			return "Village";
		case 13877: // canafis south
			return "Waterlogged";
			/*
			 * Mobilies Armies.
			 */
		case 9516:
			return "CommandAction Centre";
		case 12596: // champions guild
			return "Greatness";
		case 10804: // legends guild
			return "Trinity";
		case 11601:
			return "Zaros Zeitgeist"; // zaros godwars
		default:
			return null;
		}
	}

	/**
	 * The region ID of the location you're in.
	 */

	public int getRegionID() {
		return (getRegionY() >> 3) | ((getRegionX() >> 3) << 8);
	}

	/**
	 * Gets the local x-coordinate of this location.
	 * 
	 * @return The local x-coordinate.
	 */
	public int getLocalX() {
		return x - ((getRegionX() - 6) << 3);
	}

	/**
	 * Gets the local y-coordinate of this location.
	 * 
	 * @return The local y-coordinate.
	 */
	public int getLocalY() {
		return y - ((getRegionY() - 6) << 3);
	}

	/**
	 * Gets the region location.
	 * 
	 * @return The region location.
	 */
	public Location getRegionLocation() {
		return new Location(x >> 3 + (x - ((x >> 3) << 3)),
		y >> 3 + (y - ((y >> 3) << 3)), z, null);
	}

	@Override
	public Location getLocation() {
		return this;
	}

	/**
	 * Gets the distance between this location and the given location.
	 * 
	 * @param location
	 *            The location argued.
	 * @return The distance.
	 */
	public int getDistance(Location location) {
		return (int) Math.sqrt(Math.pow(location.x - x, 2)
				+ Math.pow(location.y - y, 2));
	}

	/**
	 * Gets the distance between this location and the given location.
	 * 
	 * @param l
	 *            The given location.
	 * @return The distance in double format.
	 */
	public double distance(Location l) {
		return Math.sqrt(Math.pow(l.x - x, 2) + Math.pow(l.y - y, 2));
	}

	/**
	 * Gets the 12 bits hash of this location.
	 * 
	 * @return The hash.
	 */
	public int get12BitsHash() {
		return (0x1f & getLocalY()) | (getZ() << 10)
				| (0x3e5 & ((getLocalX() << 5)));
	}

	/**
	 * Gets the 18 bits hash of this location.
	 * 
	 * @return The hash.
	 */
	public int get18BitsHash() {
		int regionId = ((getRegionX() / 8) << 8) + (getRegionY() / 8);
		return (((regionId & 0xff) * 64) >> 6) | (getZ() << 16)
				| ((((regionId >> 8) * 64) >> 6) << 8);
	}

	/**
	 * Gets the 30 bits hash of this location.
	 * 
	 * @return The hash.
	 */
	public int get30BitsHash() {
		return y | z << 28 | x << 14;
	}

	@Override
	public String toString() {
		return new StringBuilder("x: ").append(x).append(", y: ").append(y)
				.append(", z: ").append(z).toString();
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * Checks if the location is within distance of the other location.
	 * 
	 * @param location
	 *            The location.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Location location) {
		if (z != location.z) {
			return false;
		}
		int deltaX = location.x - x, deltaY = location.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.LOCATION;
	}

	@Override
	public void setNodeType(NodeType n) {
		/*
		 * empty.
		 */
	}

	/**
	 * Gets a delta location.
	 * 
	 * @param l
	 *            The location.
	 * @param o
	 *            The other location.
	 * @return The delta location.
	 */
	public static Location getDelta(Location l, Location o) {
		return new Location(o.x - l.x, o.y - l.y, o.z - l.z, null);
	}

	@Override
	public int size() {
		return 1;
	}

	public boolean containsNPCs() {
		int npcs = 0;
		for (int i = 0; i < 4; i++) 
			npcs += getRegion().getNpcs()[i].size();
		return npcs > 0;
	}

	public boolean isAtDuelArena() {
		return DuelArenaControler.isAtDuelArena(this);
	}

	public boolean containsPlayers() {
		int players = 0;
		for (int i = 0; i < 4; i++) {
			players += getRegion().getPlayers()[i].size();
		}
		return players > 0;
	}

}