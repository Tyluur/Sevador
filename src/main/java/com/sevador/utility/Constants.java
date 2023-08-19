package com.sevador.utility;

import java.io.File;

/**
 * Holds several Constants, which also can be named parameters.
 * 
 * @author Jefferson
 * 
 */
public class Constants {

	/**
	 * Default RuneScape Game Client Port id.
	 */
	public static final int PORT = 43594;

	/**
	 * This Project Name.
	 */
	public static final String SERVER_NAME = "Sevador";

	/**
	 * The variables for the killed monsters in godwars.
	 */

	public static final int ARMADYL_KILLS = 0, BANDOS_KILLS = 1, SARADOMIN_KILLS = 2, ZAMORAK_KILLS = 3;

	/**
	 * The monsters for godwars kill count receiving.
	 */

	public static final int[] BANDOS_NPCS = {6271, 6272, 6273, 6274, 6275, 6268, 122, 6279, 6280, 6281, 6282, 6283, 6269, 6270, 6276, 6277, 6278},
			ARMADYL_NPCS = {6229, 6230, 6231, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240, 6241, 6242, 6243, 6244, 6245, 6246, 275, 274},
			SARADOMIN_NPCS = {6254, 6255, 6256, 6257, 6258, 6259, 96, 97, 111, 125, 913},
			ZAMORAK_NPCS = 	{6210, 6211, 6212, 6214, 6218, 49, 82, 83, 84, 94, 92, 75, 78, 912};
	/**
	 * The amount of godwars kills required to enter the dungeon.
	 */

	public static final int GODWAR_KILL_REQUIREMENT = 20;


	/**
	 * Gets the Current machine Operative System.
	 * 
	 * @return The statement
	 */
	public static boolean isWindows() {
		return System.getProperty("os.name").contains("Windows");
	}

	/**
	 * Account Save location.
	 */
	public static final String SAVE_PATH = getSavePath();

	public static String getSavePath() {
		File file = new File("/root/server/games/");
		if (file.exists()) 
			return "/root/server/games/";
		else
			return "./data/saved/";
	}

	/**
	 * The location of the accounts being backed up on server reboot
	 */
	public static final String BACKUP_LOCATION = Constants.isWindows() ? "./data/backup/"
			: "/root/server/backup/";

	/**
	 * Whether or not you are allowed to create multiple sessions per machine.
	 */
	public static final boolean MULTILOG_ALLOWED = true;

	/**
	 * The server build release version.
	 */
	public static final short REVISION = 666;

	/**
	 * The player chat decryption data.
	 */
	public static final short[] PLAYER_CHAT_DECRYPTION_DATA = { 215, 203, 83,
		158, 104, 101, 93, 84, 107, 103, 109, 95, 94, 98, 89, 86, 70, 41,
		32, 27, 24, 23, -1, -2, 26, -3, -4, 31, 30, -5, -6, -7, 37, 38, 36,
		-8, -9, -10, 40, -11, -12, 55, 48, 46, 47, -13, -14, -15, 52, 51,
		-16, -17, 54, -18, -19, 63, 60, 59, -20, -21, 62, -22, -23, 67, 66,
		-24, -25, 69, -26, -27, 199, 132, 80, 77, 76, -28, -29, 79, -30,
		-31, 87, 85, -32, -33, -34, -35, -36, 197, -37, 91, -38, 134, -39,
		-40, -41, 97, -42, -43, 133, 106, -44, 117, -45, -46, 139, -47,
		-48, 110, -49, -50, 114, 113, -51, -52, 116, -53, -54, 135, 138,
		136, 129, 125, 124, -55, -56, 130, 128, -57, -58, -59, 183, -60,
		-61, -62, -63, -64, 148, -65, -66, 153, 149, 145, 144, -67, -68,
		147, -69, -70, -71, 152, 154, -72, -73, -74, 157, 171, -75, -76,
		207, 184, 174, 167, 166, 165, -77, -78, -79, 172, 170, -80, -81,
		-82, 178, -83, 177, 182, -84, -85, 187, 181, -86, -87, -88, -89,
		206, 221, -90, 189, -91, 198, 254, 262, 195, 196, -92, -93, -94,
		-95, -96, 252, 255, 250, -97, 211, 209, -98, -99, 212, -100, 213,
		-101, -102, -103, 224, -104, 232, 227, 220, 226, -105, -106, 246,
		236, -107, 243, -108, -109, 231, 237, 235, -110, -111, 239, 238,
		-112, -113, -114, -115, -116, 241, -117, 244, -118, -119, 248,
		-120, 249, -121, -122, -123, 253, -124, -125, -126, -127, 259, 258,
		-128, -129, 261, -130, -131, 390, 327, 296, 281, 274, 271, 270,
		-132, -133, 273, -134, -135, 278, 277, -136, -137, 280, -138, -139,
		289, 286, 285, -140, -141, 288, -142, -143, 293, 292, -144, -145,
		295, -146, -147, 312, 305, 302, 301, -148, -149, 304, -150, -151,
		309, 308, -152, -153, 311, -154, -155, 320, 317, 316, -156, -157,
		319, -158, -159, 324, 323, -160, -161, 326, -162, -163, 359, 344,
		337, 334, 333, -164, -165, 336, -166, -167, 341, 340, -168, -169,
		343, -170, -171, 352, 349, 348, -172, -173, 351, -174, -175, 356,
		355, -176, -177, 358, -178, -179, 375, 368, 365, 364, -180, -181,
		367, -182, -183, 372, 371, -184, -185, 374, -186, -187, 383, 380,
		379, -188, -189, 382, -190, -191, 387, 386, -192, -193, 389, -194,
		-195, 454, 423, 408, 401, 398, 397, -196, -197, 400, -198, -199,
		405, 404, -200, -201, 407, -202, -203, 416, 413, 412, -204, -205,
		415, -206, -207, 420, 419, -208, -209, 422, -210, -211, 439, 432,
		429, 428, -212, -213, 431, -214, -215, 436, 435, -216, -217, 438,
		-218, -219, 447, 444, 443, -220, -221, 446, -222, -223, 451, 450,
		-224, -225, 453, -226, -227, 486, 471, 464, 461, 460, -228, -229,
		463, -230, -231, 468, 467, -232, -233, 470, -234, -235, 479, 476,
		475, -236, -237, 478, -238, -239, 483, 482, -240, -241, 485, -242,
		-243, 499, 495, 492, 491, -244, -245, 494, -246, -247, 497, -248,
		502, -249, 506, 503, -250, -251, 505, -252, -253, 508, -254, 510,
		-255, -256, 0 };

	/**
	 * The valid characters for converting long - String.
	 */
	public static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e',
		'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
		's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9' };

	/**
	 * The equipment slots.
	 */
	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2,
			SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7,
			SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13,
			SLOT_AURA = 14;

	/**
	 * Map sizes variable.
	 */
	public static final int[] MAP_SIZES = { 
		104, 120, 136, 168
	};

	/**
	 * Names and rights of staff.
	 */
	public static final String[][] STAFF = {
		{"jefferson", "0"}, {"tyluur", "2"},
	};

	/**
	 * The array of grand exchange items that arent allowed to be sold.
	 */

	public static final String[] GRAND_EXCHANGE_RARES = { "godsword", "partyhat", "santa", "h'ween", "armadyl", "bandos", "steadfast", "ragefire", "glaiven", "karil", "ahrim", "dharok", "torag", "verac", "guthan", "torva", "virtus", "pernix", "dragonf", "dragon claw", "dragon bone", "ourg", "dagannoth bones"};

	/**
	 * This value is the multiplier for all experience given to the player, 1 is
	 * the default RuneScape Experience. If the skill isn't a combat skill, it
	 * will be applied to it.
	 */
	public static final double EXP_MULTIPLIER = 22.5;

	public static final String CHAT_LOG_FILE = Constants.isWindows() ? "data/logs/chat" : "/root/server/logs/chat";

	/**
	 * The maximum players that can be logged in.
	 */
	public static final short MAXIMUM_PLAYERS = 2048;

	/**
	 * Maximum RuneScape Amount.
	 */
	public static int MAX_AMOUNT = 2147483647;

	public static boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
