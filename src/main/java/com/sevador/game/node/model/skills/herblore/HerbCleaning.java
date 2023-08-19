package com.sevador.game.node.model.skills.herblore;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Contains various data related to cleaning/identifying herbs; please
 * note that on RuneScape you originally "identified" the herbs, but it
 * was changed because of massive scamming (people sold guams for ranarr
 * price and so forth). This update happened on 10th of September 2007,
 * so I suggest 317 servers to have identification, and NOT cleaning.
 *
 * Also note that some of the herbs didn't exist back in 2007, and is
 * therefore not in the 317 cache.
 *
 * Credits to the following sites for level requirement, experience
 * and herb item ID's;
 *
 * http://itemdb.biz/
 * http://runescape.wikia.com/wiki/Calculators/Herbs
 * http://runescape.wikia.com/wiki/Dungeoneering/HerbloreData
 * http://services.runescape.com/m=rswiki/en/Herblore_-_The_Herbs
 * http://pastebin.com/WXK7gCPd
 *
 * @author Nouish <admin@nouish.com>
 *
 */
public enum HerbCleaning {

	/* Herbs in RuneScape. */
	GUAM(199, 249, 3, 2.5D),
	MARRENTILL(201, 251, 5, 3.8D),
	TARROMIN(203, 253, 11, 5.0D),
	HARRALANDER(205, 255, 20, 6.3D),
	RANARR(207, 257, 25, 7.5D),
	TOADFLAX(3049, 2998, 30, 8.0D),
	IRIT(209, 259, 40, 8.8D),
	WERGALI(14836, 14854, 41, 9.5D),
	AVANTOE(211, 261, 48, 10.0D),
	KWUARM(213, 263, 54, 11.3D),
	SNAPDRAGON(3051, 3000, 59, 11.8D),
	CADANTINE(215, 265, 65, 12.5D),
	LANTADYME(2485, 2481, 67, 13.1D),
	DWARFWEED(217, 267, 70, 13.8D),
	SPIRITWEED(12174, 12172, 35, 7.8D),
	TORSTOL(219, 269, 75, 15.0D),
	FELLSTALK(21626, 21625, 91, 16.8D),

	/* Herbs in Daemonheim. */
	SAGEWORT(17494, 17512, 3, 2.1D),
	VALERIAN(17496, 17514, 4, 3.2D),
	ALOE(17498, 17516, 8, 4.0D),
	WORMWOOD(17500, 17518, 34, 7.2D),
	MAGEBANE(17502, 17520, 37, 7.7D),
	FEATHERFOIL(17504, 17522, 41, 8.6D),
	WINTERSGRIP(17506, 17524, 67, 12.7D),
	LYCOPUS(17508, 17526, 70, 13.1D),
	BUCKTHORN(17510, 17510, 74, 13.8D),

	/* Herbs in the HerbloreData Habitat. */
	ERZILLE(19984, 19989, 54, 10.0D),
	UGUNE(19986, 19991, 56, 11.5D),
	ARGWAY(19985, 19990, 57, 11.6D),
	SHENGO(19987, 19992, 58, 11.7D),
	SAMADEN(19988, 19993, 59, 11.7D);

	/**
	 * Construct a new {@link HerbCleaning} object with the given parameters.
	 * These are all required to successfully lose the grimy herb,
	 * obtain the clean version and then obtain some experience if
	 * your HerbloreData level is high enough.
	 *
	 * @param unidentifiedId
	 *		The ID of the unidentified/grimy version of the herb.
	 * @param identifiedId
	 *		The ID of the identified/clean version of the herb.
	 * @param levelRequirement
	 *		The level required to identify/clean the herb.
	 * @param experience
	 *		The experience obtained by identifying/cleaning the herb.
	 */
	private HerbCleaning(int unidentifiedId, int identifiedId, int levelRequirement,
			double experience) {
		this.unidentifiedId = (short) unidentifiedId;
		this.identifiedId = (short) identifiedId;
		this.levelRequirement = (short) levelRequirement;
		this.experience = experience;
	}

	/**
	 * The unidentified (grimy) ID of the herb.
	 */
	private final short unidentifiedId;

	/**
	 * The identified (clean) ID of the herb.
	 */
	private final short identifiedId;

	/**
	 * The level required to identify/clean the herb.
	 */
	private final short levelRequirement;

	/**
	 * The experience obtained by identifying/cleaning the herb.
	 */
	private final double experience;

	/**
	 * Returns the represented herbs' unidentified/grimy ID.
	 *
	 * @return
	 *		The unidentified/grimy ID.
	 */
	public final short getUnidentifiedID() {
		return this.unidentifiedId;
	}

	/**
	 * Returns the represented herbs' identified/clean ID.
	 *
	 * @return
	 *		The identified/clean ID.
	 */
	public final short getIdentifiedID() {
		return this.identifiedId;
	}

	/**
	 * Returns the level required to identify/clean the represented
	 * herb.
	 *
	 * @return
	 *		The level required to identify/clean the represented
	 *		herb.
	 */
	public final short getLevelRequirement() {
		return this.levelRequirement;
	}

	/**
	 * Returns the experience obtained by identifying/cleaning the
	 * represented herb.
	 *
	 * @return
	 *		The experience obtained by the represented herb.
	 */
	public final double getExperience() {
		return this.experience;
	}

	/**
	 * A {@link Map} of the {@link HerbCleaning}s in this enum.
	 */
	private static final Map<Short, HerbCleaning> herbs = new HashMap<Short, HerbCleaning>();

	/**
	 * Returns the {@link HerbCleaning} for an unidentified ID.
	 *
	 * @param unidentifiedId
	 *		The ID of the unidentified/grimy version of the herb.
	 * @return
	 *		The {@link HerbCleaning} object which has an unidentified/grimy
	 *		version which equals {@link unidentifiedId}.
	 */
	public static HerbCleaning forId(int unidentifiedId) {
		return herbs.get((short) unidentifiedId);
	}

	/**
	 * We'd like to fill in the values in the {@link herbs} {@link HashMap} to
	 * make it easier to find back the values (instead of iterating over the values()
	 * checking for an matching ID).
	 */
	static {
		for (HerbCleaning herb : HerbCleaning.values()) {
			herbs.put(herb.getUnidentifiedID(), herb);
		}
	}

}