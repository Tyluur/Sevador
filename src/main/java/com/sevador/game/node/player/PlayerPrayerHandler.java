package com.sevador.game.node.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.skills.prayer.PrayerSkeleton;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.MessagePacket;

/**
 * The class handling the player's normal prayers.
 * @author Emperor
 *
 */
public class PlayerPrayerHandler implements PrayerSkeleton, Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 1410179330774645882L;
	
	/**
	 * The config values.
	 */
	private final static int[] CONFIG_VALUES = {
		1, 2, 4, 262144, 524288, 8,
		16, 32, 64, 128, 256, 1048576, 2097152, 512, 1024, 2048, 16777216,
		4096, 8192, 16384, 4194304, 8388608, 32768, 65536, 131072,
		33554432, 134217728, 67108864, 536870912, 268435456
	};
	
	/**
	 * The list of currently activated prayers.
	 */
	private final List<PrayerType> activePrayers = new ArrayList<PrayerType>();
	
	/**
	 * The list of quick prayers to use.
	 */
	private final List<PrayerType> quickPrayers = new ArrayList<PrayerType>();
	
	/**
	 * If we are modifying quick prayers.
	 */
	private transient boolean quickModifying = false;
	
	/**
	 * If we are using quick prayers.
	 */
	private transient boolean usingQuick = false;

	/**
	 * The modifiers.
	 */
	private double[] modifiers = new double[7];
	
	/**
	 * The drain rate.
	 */
	private double drainRate;
	
	/**
	 * The headicon.
	 */
	private int headIcon = -1;
	
	@Override
	public boolean activate(Player player, PrayerType type) {
		if (player.getSkills().getStaticLevel(Skills.PRAYER) < type.getLevel()) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a prayer level of " + type.getLevel() + " to use this prayer.");
			return false;
		}
		if (quickModifying) {
			if (quickPrayers.contains(type)) {
				quickPrayers.remove(type);
				sendConfig(player);
				return true;
			}
			for (PrayerType prayer : type.getRemove()) {
				quickPrayers.remove(prayer);
			}
			quickPrayers.add(type);
			sendConfig(player);
			return true;
		}
		if (activePrayers.contains(type)) {
			for (int i = 0; i < type.getModifiers().length; i++) {
				modifiers[type.getModifiers()[i][0]] -= type.getModifiers()[i][1] * 0.01;
			}
			if (type.getHeadIcon() > -1) {
				headIcon -= type.getHeadIcon();
				player.getUpdateMasks().register(new AppearanceUpdate(player));
			}
			activePrayers.remove(type);
			drainRate -= type.getDrainRate();
			sendConfig(player);
			return true;
		}
		if (type.ordinal() > 15 && type.ordinal() < 20 && player.getAttribute("restrict protection", -1) > MajorUpdateWorker.getTicks()) {
			player.getPacketSender().sendMessage("You can't use protection prayers.");
			return false;
		}
		for (PrayerType prayer : type.getRemove()) {
			deactivate(player, prayer, false);
		}
		if (type.getHeadIcon() > -1) {
			headIcon += type.getHeadIcon();
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}
		for (int i = 0; i < type.getModifiers().length; i++) {
			modifiers[type.getModifiers()[i][0]] += type.getModifiers()[i][1] * 0.01;
		}
		activePrayers.add(type);
		drainRate += type.getDrainRate();
		sendConfig(player);
		return true;
	}
	
	/**
	 * Deactivates a prayer.
	 * @param player The player.
	 * @param type The prayer type to deactivate.
	 * @return {@code True} if the prayer type got removed, {@code false} if the prayer type wasn't activated.
	 */
	public boolean deactivate(Player player, PrayerType type, boolean refresh) {
		if (activePrayers.contains(type)) {
			for (int i = 0; i < type.getModifiers().length; i++) {
				modifiers[type.getModifiers()[i][0]] -= type.getModifiers()[i][1] * 0.01;
			}
			if (type.getHeadIcon() > -1) {
				headIcon -= type.getHeadIcon();
				player.getUpdateMasks().register(new AppearanceUpdate(player));
			}
			activePrayers.remove(type);
			drainRate -= type.getDrainRate();
			if (refresh) {
				sendConfig(player);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Switches between using quick prayers and normal prayers.
	 * @param p The player.
	 */
	public void switchQuick(Player p) {
		boolean quick = usingQuick;
		reset(p);
		if (quick) {
			/*for (PrayerType type : quickPrayers) {
				deactivate(p, type, false);
			}
			sendConfig(p);*/
			p.getIOSession().write(new CS2Config(p, 182, 0));
			return;
		}
		if (quickPrayers.size() < 1) {
			p.getIOSession().write(new MessagePacket(p, "You haven't selected any quick "+(p.getPrayer().isCurses() ? "curses" : "prayers") + "!"));
			return;
		}
		usingQuick = true;
		p.getIOSession().write(new CS2Config(p, 182, 1));
		for (PrayerType type : quickPrayers) {
			activate(p, type);
		}
	}
	
	/**
	 * Opens the quick curses/prayers select menu.
	 * @param p The player.
	 */
	public void openQuickMenu(Player p) {
		if (!quickModifying) {
			quickModifying = true;
			p.getIOSession().write(new CS2Config(p, 181, 1));
			p.getIOSession().write(new CS2Config(p, 168, 6));
			p.getIOSession().write(new AccessMask(p, 0, 29, 271, 42, 0, 2));
			sendConfig(p);
		} else {
			quickModifying = false;
			p.getIOSession().write(new CS2Config(p, 181, 0));
			p.getIOSession().write(new CS2Config(p, 149, 6));
		}
	}
	
	/**
	 * Sends the config that sets the activated prayers client side.
	 * @param p The player.
	 */
	public void sendConfig(Player p) {
		int value = 0;
		for (PrayerType prayer : quickModifying ? quickPrayers : activePrayers) {
			value |= CONFIG_VALUES[prayer.ordinal()];
		}
		p.getIOSession().write(new ConfigPacket(p, quickModifying ? 1397 : 1395, value));
	}
	
	@Override
	public boolean isCurses() {
		return false;
	}

	@Override
	public boolean get(PrayerType prayer) {
		return activePrayers.contains(prayer);
	}

	@Override
	public double getMod(int skill) {
		return modifiers[skill];
	}

	@Override
	public int getHeadIcon() {
		return headIcon;
	}

	@Override
	public double getDrainRate() {
		return drainRate;
	}
	
	@Override
	public void reset(Player p) {
		activePrayers.clear();
		if (headIcon > -1) {
			headIcon = -1;
			p.getUpdateMasks().register(new AppearanceUpdate(p));
		}
		usingQuick = false;
		modifiers = new double[7];
		drainRate = 0;
		p.getIOSession().write(new CS2Config(p, 182, 0));
		p.getIOSession().write(new ConfigPacket(p, 1395, 0));
	}

}
