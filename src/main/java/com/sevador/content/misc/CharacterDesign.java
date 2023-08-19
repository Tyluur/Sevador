package com.sevador.content.misc;

import java.util.Random;

import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.player.Appearance;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.AccessMask;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class CharacterDesign {
	
	private static final Random RANDOM = new Random();

	public static void initiate(Player player) {
		Appearance app = player.getCredentials().getAppearance();
		player.getPacketSender().sendWindowPane(player, 1028, 0);
		player.getIOSession().write(new AccessMask(player, 2, 1028, 45, 0, 204, 0));
		player.getIOSession().write(new AccessMask(player, 2, 1028, 111, 0, 204, 0));
		player.getIOSession().write(new AccessMask(player, 2, 1028, 107, 0, 204, 0));
		player.getPacketSender().sendInterfaceScript(player, 4244);
		randomizeLook(player, app);
	}
	
	public static void randomizeLook(Player player, Appearance app) {
		app.setColor(0, HAIR_COLOURS[RANDOM.nextInt(HAIR_COLOURS.length)]);
		app.setColor(1, CLOTH_COLOURS[RANDOM.nextInt(CLOTH_COLOURS.length)]);
		app.setColor(2, CLOTH_COLOURS[RANDOM.nextInt(CLOTH_COLOURS.length)]);
		app.setColor(3, FEET_COLOURS[RANDOM.nextInt(FEET_COLOURS.length)]);
		app.setColor(4, SKIN_COLOURS[RANDOM.nextInt(SKIN_COLOURS.length)]);
		int hairIndex = RANDOM.nextInt(MALE_HAIR_LOOKS.length);
		app.setLook(0, MALE_HAIR_LOOKS[hairIndex]);
		int facialIndex = RANDOM.nextInt(MALE_FACIAL_LOOKS.length);
		app.setLook(1, MALE_FACIAL_LOOKS[facialIndex]);
		int torsoIndex = RANDOM.nextInt(MALE_TORSO_LOOKS.length);
		int[] torso = MALE_TORSO_LOOKS[torsoIndex];
		app.setLook(2, torso[0] == -1 ? 0 : torso[0]);
		int arms = torso[1];
		if (arms != -1)
			app.setLook(3, torso[1]);
		app.setLook(4, torso[2] == -1 ? 0 : torso[2]);
		int legIndex = RANDOM.nextInt(MALE_LEG_LOOKS.length);
		app.setLook(5, MALE_LEG_LOOKS[legIndex]);
		int feetIndex = RANDOM.nextInt(MALE_FEET_LOOKS.length);
		app.setLook(6, MALE_FEET_LOOKS[feetIndex]);
		player.getPacketSender().sendBConfig(player, 1008, hairIndex);
		player.getPacketSender().sendBConfig(player, 1009, facialIndex);
		player.getPacketSender().sendBConfig(player, 1010, torsoIndex);
		player.getPacketSender().sendBConfig(player, 1013, legIndex);
		player.getPacketSender().sendBConfig(player, 1014, feetIndex);
		player.getPacketSender().sendBConfig(player, 1015, app.getColor(0));
		player.getPacketSender().sendBConfig(player, 1016, app.getColor(1));
		player.getPacketSender().sendBConfig(player, 1017, app.getColor(2));
		player.getPacketSender().sendBConfig(player, 1018, app.getColor(3));
		player.getPacketSender().sendBConfig(player, 1019, app.getColor(4));
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
	
	public static final int[] SKIN_COLOURS = new int[]{9, 8, 7, 0, 1, 2, 3, 4, 5, 6, 10, 11};

	public static final int[] HAIR_COLOURS = new int[]{20, 19, 10, 18, 4, 5, 15, 7, 0, 6, 21, 9,
		22, 17, 8, 16, 24, 11, 23, 3, 2, 1, 14, 13, 12};

	public static final int[] CLOTH_COLOURS = new int[]{32, 101, 48, 56, 165, 103, 167, 106, 54,
		198, 199, 200, 225, 35, 39, 53, 42, 46, 29, 91, 57, 90, 34, 102, 104, 105, 107, 173, 137,
		201, 204, 211, 197, 108, 217, 220, 221, 226, 227, 215, 222, 166, 212, 174, 175, 169, 144,
		135, 136, 133, 123, 119, 192, 194, 117, 115, 111, 141, 45, 49, 84, 77, 118, 88, 85, 138,
		51, 92, 112, 145, 179, 143, 149, 151, 153, 44, 154, 155, 86, 89, 72, 66, 33, 206, 109, 110,
		114, 116, 184, 170, 120, 113, 150, 205, 210, 207, 209, 193, 152, 156, 183, 161, 159, 160,
		73, 75, 181, 185, 208, 74, 36, 37, 43, 50, 58, 55, 139, 148, 147, 64, 69, 70, 71, 68, 93,
		94, 95, 124, 182, 96, 97, 219, 63, 228, 79, 82, 98, 99, 100, 125, 126, 127, 40, 128, 129,
		188, 130, 131, 186, 132, 164, 157, 180, 187, 31, 162, 168, 52, 163, 158, 196, 59, 60, 87,
		78, 61, 76, 80, 171, 172, 176, 177, 178, 38, 41, 47, 62, 65, 67, 81, 83, 121, 122, 134,
		140, 142, 146, 189, 190, 191, 195, 202, 203, 213, 214, 216, 218, 223, 224};

	public static final int[] FEET_COLOURS = new int[]{55, 54, 14, 120, 194, 53, 11, 154, 0, 1,
		2, 3, 4, 5, 9, 78, 25, 33, 142, 80, 144, 83, 31, 175, 176, 177, 12, 16, 30, 19, 23, 6, 68,
		34, 67, 11, 79, 81, 82, 84, 150, 114, 178, 181, 188, 174, 85, 194, 197, 198, 203, 204, 192,
		199, 143, 189, 151, 152, 146, 121, 112, 113, 110, 100, 96, 169, 171, 94, 92, 88, 118, 22,
		26, 61, 95, 65, 62, 115, 28, 69, 89, 122, 156, 126, 128, 130, 21, 131, 132, 63, 66, 49, 43,
		10, 183, 86, 87, 91, 93, 161, 147, 97, 90, 127, 182, 187, 184, 186, 170, 129, 133, 160, 138,
		136, 137, 50, 52, 158, 162, 185, 51, 13, 20, 27, 35, 32, 116, 125, 124, 41, 46, 47, 48, 45,
		70, 71, 72, 101, 159, 73, 74, 196, 40, 205, 56, 59, 75, 76, 77, 102, 103, 104, 17, 105, 106,
		165, 107, 108, 163, 109, 141, 134, 157, 164, 8, 139, 145, 29, 140, 135, 173, 36, 37, 64, 38,
		57, 148, 149, 153, 155, 15, 18, 24, 39, 42, 44, 58, 60, 98, 99, 117, 119, 123, 166, 167, 168,
		172, 179, 180, 190, 191, 193, 195, 200, 201};

	public static final int[] MALE_HAIR_LOOKS = new int[]{5, 6, 93, 96, 92, 268, 265, 264, 267, 315,
		94, 263, 312, 313, 311, 314, 261, 310, 1, 0, 97, 95, 262, 316, 309, 3, 91, 4};

	public static final int[][] MALE_TORSO_LOOKS = new int[][]{{457, 588, 364}, {445, -1, 366},
		{459, 591, 367}, {460, 592, 368}, {461, 593, 369}, {462, 594, 370}, {452, -1, 371},
		{463, 596, 372}, {464, 597, 373}, {446, -1, 374}, {465, 599, 375}, {466, 600, 376},
		{467, 601, 377}, {451, -1, 378}, {468, 603, 379}, {453, -1, 380}, {454, -1, 381},
		{455, -1, 382}, {469, 607, 383}, {470, 608, 384}, {450, -1, 385}, {458, 589, 365},
		{447, -1, 386}, {448, -1, 387}, {449, -1, 388}, {471, 613, 389}, {443, -1, 390},
		{472, 615, 391}, {473, 616, 392}, {444, -1, 393}, {474, 618, 394},
		{456, -1, 9}};

	public static final int[] MALE_LEG_LOOKS = new int[]{620, 622, 623, 624, 625, 626, 627, 628,
		629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 621, 642, 643, 644, 645,
		646, 647, 648, 649, 650, 651};

	public static final int[] MALE_FEET_LOOKS = new int[]{427, 428, 429, 430, 431, 432, 433, 434,
		435, 436, 437, 438, 439, 440, 441, 442, 42, 43};

	public static final int[] MALE_FACIAL_LOOKS = new int[]{14, 13, 98, 308, 305, 307, 10, 15, 16,
		100, 12, 11, 102, 306, 99, 101, 104, 17};

	public static final int[] FEMALE_HAIR_LOOKS = new int[]{141, 361, 272, 273, 359, 274, 353, 277,
		280, 360, 356, 269, 358, 270, 275, 357, 145, 271, 354, 355, 45, 52, 49, 47, 48, 46, 143, 362,
		144, 279, 142, 146, 278, 135};

	public static final int[][] FEMALE_TORSO_LOOKS = new int[][]{{565, 395, 507}, {567, 397, 509},
		{568, 398, 510}, {569, 399, 511}, {570, 400, 512}, {571, 401, 513}, {561, -1, 514}, {572, 403, 515},
		{573, 404, 516}, {574, 405, 517}, {575, 406, 518}, {576, 407, 519}, {577, 408, 520}, {560, -1, 521},
		{578, 410, 522}, {562, -1, 523}, {563, -1, 524}, {564, -1, 525}, {579, 414, 526}, {559, -1, 527},
		{580, 416, 528}, {566, 396, 508}, {581, 417, 529}, {582, 418, 530}, {557, -1, 531}, {583, 420, 532},
		{584, 421, 533}, {585, 422, 534}, {586, 423, 535}, {556, -1, 536}, {587, 425, 537}, {558, -1, 538}};

	public static final int[] FEMALE_LEG_LOOKS = new int[]{475, 477, 478, 479, 480, 481, 482, 483, 484,
		485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 476, 497, 498, 499, 500, 501, 502,
		503, 504, 505, 506};

	public static final int[] FEMALE_FEET_LOOKS = new int[]{539, 540, 541, 542, 543, 544, 545, 546,
		547, 548, 549, 550, 551, 552, 553, 554, 555, 79, 80};
	
}
