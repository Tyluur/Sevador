package com.sevador.game.node.model.skills.runecrafting;

import java.util.HashMap;
import java.util.Map;

import com.sevador.game.node.model.Location;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 *
 */
public enum Talisman {

	AIR_TALISMAN(1438, 5527, 1, 1, Location.locate(3129, 3405, 0), Location.locate(2841, 4829, 0), 2452, 2478,556, 5, false),
	MIND_TALISMAN(1448, 5529, 2, 2, Location.locate(2980, 3513, 0), Location.locate(2792, 4828, 0), 2453, 2479, 558, 5.5, false),
	WATER_TALISMAN(1444, 5531, 4, 5, Location.locate(3187, 3164, 0), Location.locate(3494, 4833, 0), 2454, 2480, 555, 6, false),
	EARTH_TALISMAN(1440, 5535, 8, 9, Location.locate(3304, 3474, 0), Location.locate(2656, 4829, 0), 2455, 2481, 557, 6.5, false),
	FIRE_TALISMAN(1442, 5537, 16, 14, Location.locate(3315, 3254, 0), Location.locate(2576, 4845, 0), 2456, 2482, 554, 7, false),
	BODY_TALISMAN(1446, 5533, 32, 20, Location.locate(3053, 3443, 0), Location.locate(2521, 4834, 0), 2457, 2483, 559, 7.5, false),
	COSMIC_TALISMAN(1454, 5539, 64, 27, Location.locate(2409, 4375, 0), Location.locate(2122, 4833, 0), 2458, 2484, 564, 8, true),
	CHAOS_TALISMAN(1452, 5543, 512, 35, Location.locate(3058, 3591, 0), Location.locate(2281, 4837, 0), 2461, 2487, 562, 8.5, true),
	NATURE_TALISMAN(1462, 5541, 256, 44, Location.locate(2869, 3021, 0), Location.locate(2400, 4835, 0), 2460,2486, 561, 9, true),
	LAW_TALISMAN(1458, 5545, 128, 54, Location.locate(2858, 3379, 0), Location.locate(2464, 4818, 0), 2459, 2485, 563, 9.5, true),
	DEATH_TALISMAN(1456, 5547, 0, 65, Location.locate(3087, 3496, 0), Location.locate(2208, 4830, 0), -1, 2488, 560, 10, true),//No Mapdata for outside alter
	BLOOD_TALISMAN(1450, 5549, 0, 77, Location.locate(3087, 3496, 0), Location.locate(2467, 4888, 1), -1, 30624, 565, 10.5, true),//Don't know coords
	ELEMENTAL_TALISMAN(5516, -1, 0, 1, Location.locate(3222, 3222, 0), Location.locate(3222, 3222, 0), -1, -1, -1, 0, true),//No idea
	OMNI_TALISMAN(13649, 13655, 0, 1, Location.locate(3222, 3222, 0), Location.locate(3222, 3222, 0), -1, -1, -1, 0, true)//No idea
	;
	/**
	 * The id of the Talisman
	 */
	private final short id;
	/**
	 * The id of the Tiara
	 */
	private final short tiaraid;
	
	private final int tiaraConfig;
	/**
	 * The level required to use the talisman
	 */
	private final int level;
	/**
	 * The location of the alter that corresponds to the talisman
	 */
	private final Location outsideLocation;
	/**
	 * The location of the alter once inside
	 */
	private final Location insideLocation;
	/**
	 * The object Id of the outside alter
	 */
	private final int objectId;
	
	private final int alterId;
	
	private final int rewardId;
	
	private final double rewardExp;
	
	private boolean requirePureEss;
	/**
	 * A map of all talismans
	 */
	private static Map<Short, Talisman> talismans = new HashMap<Short, Talisman>();
	/**
	 * Gets a talisman by an item id.
	 *
	 * @param item The item id.
	 * @return The talisman.
	 */
	public static Talisman forId(int item) {
		return talismans.get((short) item);
	}
	
	static{
		for(Talisman talisman : Talisman.values()){
			talismans.put(talisman.id, talisman);
		}
	}

	private Talisman(int id, int tiaraid, int tiaraConfig, int level, Location outsideLocation, Location insideLocation, int objectId, int alterId, int rewardId, double rewardExp, boolean requirePureEss){
		this.id = (short) id;
		this.tiaraid = (short) tiaraid;
		this.tiaraConfig = tiaraConfig;
		this.level = level;
		this.outsideLocation = outsideLocation;
		this.insideLocation = insideLocation;
		this.objectId = objectId;
		this.alterId = alterId;
		this.rewardId = rewardId;
		this.rewardExp = rewardExp;
		this.requirePureEss = requirePureEss;
	}
	
	public static boolean alterIds(int id){
		for(Talisman talisman: talismans.values()){
			if(talisman.getAlterId() == id){
				return true;
			}
		}
		return false;
	}
	
	public static Talisman getTalismanByAlter(int id){
		for(Talisman talisman: talismans.values()){
			if(talisman.getAlterId() == id){
				return talisman;
			}
		}
		return null;
	}
	
	public static Talisman getTalismanByTiara(int id){
		for(Talisman talisman: talismans.values()){
			if(talisman.getTiaraId() == id){
				return talisman;
			}
		}
		return null;
	}

	/**
	 * Gets the id.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}
	
	public int getTiaraId(){
		return tiaraid;
	}
	
	public int getTiaraConfig(){
		return tiaraConfig;
	}

	/**
	 * Gets the required level.
	 *
	 * @return The required level.
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * Gets the outside location of the alter
	 * 
	 * @return the location
	 */
	public Location getOutsideLocation(){
		return outsideLocation;
	}
	/**
	 * Gets the inside location of the alter
	 * 
	 * @return the location
	 */
	public Location getInsideLocation(){
		return insideLocation;
	}
	
	public int getObjectId(){
		return objectId;
	}
	
	public int getAlterId(){
		return alterId;
	}
	
	public int getRewardId(){
		return rewardId;
	}
	
	public double getRewardExp(){
		return rewardExp;
	}
	
	public boolean getRequirePureEss(){
		return requirePureEss;
	}
}
