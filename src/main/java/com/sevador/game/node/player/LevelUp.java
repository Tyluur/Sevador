package com.sevador.game.node.player;

import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.MessagePacket;
import com.sevador.network.out.SkillLevel;
import com.sevador.network.out.StringPacket;

/**
 * Sends the level up interface, flashing icons, etc.
 * 
 * @author Emperor
 * 
 */
public class LevelUp {

	/**
	 * The skill icons send on the level up chat box interface.
	 */
	public static final int[] SKILL_ICON = { 67108864, 335544320, 134217728,
			402653184, 201326592, 469762048, 268435456, 1073741824, 1207959552,
			1275068416, 1006632960, 1140850688, 738197504, 939524096,
			872415232, 603979776, 536870912, 671088640, 1342177280, 1409286144,
			805306368, 1543503872, 1476395008, 1610612736, 1677721600 };

	/**
	 * The flashing icons on level up.
	 */
	public static final int[] FLASH_ICONS = { 1, 4, 2, 64, 8, 16, 32, 32768,
			131072, 2048, 16384, 65536, 1024, 8192, 4096, 256, 128, 512,
			524288, 1048576, 262144, 4194304, 2097152, 8388608, 16777216 };

	/**
	 * The level up gfx.
	 */
	private static final Graphic LEVEL_UP_GFX = new Graphic(199, 96, 0, false);
	
	/**
	 * Sends the level up interfaces, flashing icons, graphics, etc.
	 * 
	 * @param player
	 *            The player.
	 * @param slot
	 *            The skill slot.
	 */
	public static void sendLevelUp(Player player, int slot) {
		player.getSkillAction().forceStop();
		player.getUpdateMasks().register(LEVEL_UP_GFX);
		player.getIOSession().write(new SkillLevel(player, slot));
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		player.getIOSession().write(new MessagePacket(player, "You've just advanced a " + Skills.SKILL_NAMES[slot] + " level! You have reached level " + ((Skills) player.getSkills()).getStaticLevel(slot) + "."));
		player.getIOSession().write(new StringPacket(player, "Congratulations, you have just advanced a " + Skills.SKILL_NAMES[slot] + " level!", 740, 1));
		player.getIOSession().write(new StringPacket(player, "You have now reached level " + ((Skills) player.getSkills()).getStaticLevel(slot) + ".", 740, 0));
		player.setAttribute(Skills.SKILL_NAMES[slot], true);
		sendFlashIcons(player, slot);
		player.getPacketSender().sendChatBoxInterface(740);
	}

	/**
	 * Sends the flash icons with the level up chat box interface.
	 * 
	 * @param player
	 *            The player.
	 * @param slot
	 *            The skill slot.
	 */
	private static void sendFlashIcons(Player player, int slot) {
		int value = 0;
		for (int i = 0; i < 25; i++) {
			if ((Boolean) player.getAttribute(Skills.SKILL_NAMES[i], false)) {
				value += FLASH_ICONS[i];
			}
		}
		player.getIOSession().write(new ConfigPacket(player, 1179, SKILL_ICON[slot] + value));
	}

	/**
	 * Sends the flash icons without the level up interface.
	 * 
	 * @param player
	 *            The player.
	 */
	public static void sendFlashIcons(Player player) {
		int value = 0;
		for (int i = 0; i < 25; i++) {
			if ((Boolean) player.getAttribute(Skills.SKILL_NAMES[i], false)) {
				value += FLASH_ICONS[i];
			}
		}
		player.getIOSession().write(new ConfigPacket(player, 1179, value));
	}
}
