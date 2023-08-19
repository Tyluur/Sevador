package com.sevador.game.node.npc;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.game.node.model.skills.prayer.PrayerSkeleton;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;

/**
 * Handles the NPC's prayer.
 * @author Emperor
 *
 */
public class NPCPrayer implements PrayerSkeleton {

	/**
	 * The NPC's used prayers.
	 */
	private final List<PrayerType> prayers = new ArrayList<PrayerType>();
	
	/**
	 * The NPC's headicon.
	 */
	private final int headIcon;
	
	/**
	 * Constructs a new {@code NPCPrayer} {@code Object}.
	 * @param npc The NPC.
	 */
	public NPCPrayer(NPC npc) {
		this.headIcon = npc.getDefinition().getHeadIcon();
		NPCDefinition def = npc.getDefinition();
		if (def.getHeadIcon() == 0 || def.getHeadIcon() == 8 || def.getHeadIcon() == 12 || def.getHeadIcon() == 16) {
			prayers.add(PrayerType.PROTECT_FROM_MELEE);
		}
		if (def.getHeadIcon() == 1 || def.getHeadIcon() == 6 || def.getHeadIcon() == 9 || def.getHeadIcon() == 14 || def.getHeadIcon() == 17) {
			prayers.add(PrayerType.PROTECT_FROM_MISSILES);
		}
		if (def.getHeadIcon() == 2 || def.getHeadIcon() == 6 || def.getHeadIcon() == 10 || def.getHeadIcon() == 13 || def.getHeadIcon() == 18) {
			prayers.add(PrayerType.PROTECT_FROM_MAGIC);
		}
	}
	
	@Override
	public boolean isCurses() {
		return false;
	}

	@Override
	public boolean get(PrayerType prayer) {
		return prayers.contains(prayer);
	}

	@Override
	public double getMod(int skill) {
		return 0;
	}

	@Override
	public int getHeadIcon() {
		return headIcon;
	}

	@Override
	public double getDrainRate() {
		return 0;
	}
	
	@Override
	public void reset(Player p) { }

	@Override
	public boolean activate(Player player, PrayerType prayerType) {
		return false;
	}

}
