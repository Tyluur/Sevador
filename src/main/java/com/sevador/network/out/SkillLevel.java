package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.OutgoingPacket;

/**
 * Creates a new {@code skillLevel} packet.
 * @author Emperor
 *
 */
public class SkillLevel extends OutgoingPacket {

	/**
	 * The skill slot id.
	 */
	private final int slot;
	
	/**
	 * Creates a new {@code SkillLevel} {@code Object}.
	 * @param player The player.
	 * @param slot The skill level id to update.
	 */
	public SkillLevel(Player player, int slot) {
		super(player, 8);
		this.slot = slot;
	}
	
	@Override
	public OutgoingPacket get() {
		getPlayer().getIOSession().write(new ConfigPacket(getPlayer(), 1801, 
				((Skills) getPlayer().getSkills()).getExperienceGained() * 10));
		putByteC(getPlayer().getSkills().getLevel(slot));
		put(slot);
		putInt2((int) ((Skills) getPlayer().getSkills()).getExperience(slot));
		return this;
	}

}
