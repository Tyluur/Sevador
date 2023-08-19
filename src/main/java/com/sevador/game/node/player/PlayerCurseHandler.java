package com.sevador.game.node.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.skills.prayer.PrayerSkeleton;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.MessagePacket;

/**
 * Handles a player's curses.
 * @author Emperor
 *
 */
public class PlayerCurseHandler implements PrayerSkeleton, Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -3995422682300526855L;
	
	/**
	 * The protect item GFX.
	 */
	private static final Graphic PROTECT_ITEM_GFX = new Graphic(2213, 0, 0, false);
	
	/**
	 * The protect item activation animation.
	 */
	private static final Animation PROTECT_ITEM_ANIMATION = new Animation(12567, 0, false);
	
	/**
	 * The berserker activation GFX.
	 */
	private static final Graphic BERSERKER_GFX = new Graphic(2266, 0, 0, false);
	
	/**
	 * The berserker activation animation.
	 */
	private static final Animation BERSERKER_ANIMATION = new Animation(12589, 0, false);
	
	/**
	 * The turmoil activation GFX.
	 */
	private static final Graphic TURMOIL_GFX = new Graphic(2226, 0, 0, false);
	
	/**
	 * The turmoil activation animation.
	 */
	private static final Animation TURMOIL_ANIMATION = new Animation(12565, 0, false);

	/**
	 * The list of currently activated prayers.
	 */
	private final List<PrayerType> activeCurses = new ArrayList<PrayerType>();
	
	/**
	 * The list of quick curses to use.
	 */
	private final List<PrayerType> quickCurses = new ArrayList<PrayerType>();
	
	/**
	 * If we are modifying quick curses.
	 */
	private transient boolean quickModifying = false;
	
	/**
	 * If we are using quick curses.
	 */
	private transient boolean usingQuick = false;
	
	/**
	 * The modifiers.
	 */
	private double[] modifiers = new double[7];

	/**
	 * The drain rate.
	 */
	private double drainRate = 0;
	
	/**
	 * The headicon.
	 */
	private int headIcon = -1;
	
	@Override
	public boolean activate(Player player, PrayerType type) {
		int defReq = 30;
		if (player.getSkills().getStaticLevel(Skills.DEFENCE) < defReq) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a defence level of " + defReq + " to activate ancient curses.");
			return false;
		}
		if (player.getSkills().getStaticLevel(Skills.PRAYER) < type.getLevel()) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a prayer level of " + type.getLevel() + " to use this curse.");
			return false;
		}
		if (quickModifying) {
			if (quickCurses.contains(type)) {
				quickCurses.remove(type);
				sendConfig(player);
				return true;
			}
			for (PrayerType prayer : type.getRemove()) {
				quickCurses.remove(prayer);
			}
			quickCurses.add(type);
			sendConfig(player);
			return true;
		}
		if (activeCurses.contains(type)) {
			for (int i = 0; i < type.getModifiers().length; i++) {
				modifiers[type.getModifiers()[i][0]] -= type.getModifiers()[i][1] * 0.01;
			}
			activeCurses.remove(type);
			if (type.getHeadIcon() > -1) {
				setHeadIcon(player);
			}
			drainRate -= type.getDrainRate();
			if (type.getEffect() != null) {
				type.getEffect().reset(player);
			}
			sendConfig(player);
			return true;
		}
		if (type.ordinal() > 35 && type.ordinal() < 40 && player.getAttribute("restrict protection", -1) > MajorUpdateWorker.getTicks()) {
			player.getPacketSender().sendMessage("You can't use protection curses.");
			return false;
		}
		for (PrayerType prayer : type.getRemove()) {
			deactivate(player, prayer, false);
		}
		for (int i = 0; i < type.getModifiers().length; i++) {
			modifiers[type.getModifiers()[i][0]] += type.getModifiers()[i][1] * 0.01;
		}
		drainRate += type.getDrainRate();
		activeCurses.add(type);
		if (type.getHeadIcon() > -1) {
			setHeadIcon(player);
		}
		if (player.getSkills().getPrayerPoints() > 0) {
			visualActivation(player, type);
		}
		sendConfig(player);
		return true;
	}
	
	/**
	 * Sends the GFX & animations on activating a curse.
	 * @param player The player.
	 * @param type Te curse activated.
	 */
	private void visualActivation(Player player, PrayerType type) {
		if (type == PrayerType.PROTECT_ITEM_CURSE) {
			player.visual(PROTECT_ITEM_ANIMATION, PROTECT_ITEM_GFX);
		} else if (type == PrayerType.BERSERKER) {
			player.visual(BERSERKER_ANIMATION, BERSERKER_GFX);
		} else if (type == PrayerType.TURMOIL) {
			player.visual(TURMOIL_ANIMATION, TURMOIL_GFX);
		}
	}
	
	/**
	 * Gets the headicon.
	 * @param type The prayer type to activate.
	 * @return The head icon.
	 */
	private void setHeadIcon(Player player) {
		int icon = -1;
		if (activeCurses.contains(PrayerType.DEFLECT_SUMMONING)) {
			icon += PrayerType.DEFLECT_SUMMONING.getHeadIcon();
			if (activeCurses.contains(PrayerType.DEFLECT_MAGIC)) {
				icon += 3;
			} else if (activeCurses.contains(PrayerType.DEFLECT_MISSILES)) {
				icon += 2;
			} else if (activeCurses.contains(PrayerType.DEFLECT_MELEE)) {
				icon += 1;
			}
		} else if (activeCurses.contains(PrayerType.DEFLECT_MAGIC)) {
			icon += PrayerType.DEFLECT_MAGIC.getHeadIcon();
		} else if (activeCurses.contains(PrayerType.DEFLECT_MISSILES)) {
			icon += PrayerType.DEFLECT_MISSILES.getHeadIcon();
		} else if (activeCurses.contains(PrayerType.DEFLECT_MELEE)) {
			icon += PrayerType.DEFLECT_MELEE.getHeadIcon();
		} else if (activeCurses.contains(PrayerType.WRATH)) {
			icon += PrayerType.WRATH.getHeadIcon();
		} else if (activeCurses.contains(PrayerType.SOUL_SPLIT)) {
			icon += PrayerType.SOUL_SPLIT.getHeadIcon();
		}
		if (icon != headIcon) {
			headIcon = icon;
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}
	}

	/**
	 * Deactivates a prayer.
	 * @param player The player.
	 * @param type The prayer type to deactivate.
	 * @return {@code True} if the prayer type got removed, {@code false} if the prayer type wasn't activated.
	 */
	public boolean deactivate(Player player, PrayerType type, boolean refresh) {
		if (activeCurses.contains(type)) {
			for (int i = 0; i < type.getModifiers().length; i++) {
				modifiers[type.getModifiers()[i][0]] -= type.getModifiers()[i][1] * 0.01;
			}
			if (type.getEffect() != null) {
				type.getEffect().reset(player);
			}
			activeCurses.remove(type);
			drainRate -= type.getDrainRate();
			if (refresh) {
				if (type.getHeadIcon() > -1) {
					setHeadIcon(player);
				}
				sendConfig(player);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Switches between using quick curses and normal curses.
	 * @param p The player.
	 */
	public void switchQuick(Player p) {
		boolean quick = usingQuick;
		reset(p);
		if (quick) {
			p.getIOSession().write(new CS2Config(p, 182, 0));
			return;
		}
		if (quickCurses.size() < 1) {
			p.getIOSession().write(new MessagePacket(p, "You haven't selected any quick "+(p.getPrayer().isCurses() ? "curses" : "prayers") + "!"));
			p.getIOSession().write(new CS2Config(p, 182, 0));
			return;
		}
		usingQuick = true;
		p.getIOSession().write(new CS2Config(p, 182, 1));
		for (PrayerType type : quickCurses) {
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
		p.getIOSession().write(new ConfigPacket(p, 1584, 1));
		int value = 0;
		for (PrayerType prayer : quickModifying ? quickCurses : activeCurses) {
			value |= 1 << (prayer.ordinal() - 30);
		}
		p.getIOSession().write(new ConfigPacket(p, quickModifying ? 1587 : 1582, value));
	}
	
	@Override
	public boolean isCurses() {
		return true;
	}

	@Override
	public boolean get(PrayerType prayer) {
		return activeCurses.contains(prayer);
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
		for (PrayerType type : activeCurses) {
			if (type.getEffect() != null) {
				type.getEffect().reset(p);
			}
		}
		activeCurses.clear();
		if (headIcon > -1) {
			headIcon = -1;
			p.getUpdateMasks().register(new AppearanceUpdate(p));
		}
		usingQuick = false;
		modifiers = new double[7];
		drainRate = 0;
		p.getIOSession().write(new CS2Config(p, 182, 0));
		p.getIOSession().write(new ConfigPacket(p, 1582, 0));
	}

}
