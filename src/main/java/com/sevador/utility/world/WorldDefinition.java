package com.sevador.utility.world;

/**
 * Represents a world's definition.
 * @author Dementhium development team.
 *
 */
public class WorldDefinition {

	/**
	 * The activity for this world.
	 */
    private final String activity;
    
    /**
     * The coutry flag.
     */
    private final int country;
    
    /**
     * If the world is members.
     */
    private final int flag;
    
    /**
     * The ip-address for this world.
     */
    private final String ip;
    
    /**
     * The location.
     */
    private final int location;
    
    /**
     * The region.
     */
    private final String region;
    
    /**
     * The world's id.
     */
    private final int worldId;

    /**
     * Constructs a new {@code WorldDefinition} {@code Object}.
     * @param worldId The world's id.
     * @param location The location.
     * @param flag If the world is members.
     * @param activity The activity for this world.
     * @param ip The IP-address.
     * @param region The region.
     * @param country The country flag.
     */
    public WorldDefinition(int worldId, int location, int flag, String activity, String ip, String region, int country) {
        this.worldId = worldId;
        this.location = location;
        this.flag = flag;
        this.activity = activity;
        this.ip = ip;
        this.region = region;
        this.country = country;
    }

	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * @return the country
	 */
	public int getCountry() {
		return country;
	}

	/**
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the location
	 */
	public int getLocation() {
		return location;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @return the worldId
	 */
	public int getWorldId() {
		return worldId;
	}


}