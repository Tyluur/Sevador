package com.sevador.game.node.npc;

import com.sevador.game.node.model.SkillSkeleton;
import com.sevador.game.node.player.Skills;

/**
 * Represents the NPC's skills.
 * @author Emperor
 *
 */
public class NPCSkills implements SkillSkeleton {

	/**
	 * The NPC.
	 */
	private final NPC npc;
	
	/**
	 * The NPC's current lifepoints.
	 */
	private int lifepoints = 100;
	
	/**
	 * The NPC's maximum lifepoints.
	 */
	private int maximumLifepoints = 100;
	
	/**
	 * The NPC's dynamic levels.
	 */
	private final int[] levels;
	
	/**
	 * The NPC's static levels.
	 */
	private final int[] staticLevels;
	
	/**
	 * Constructs a new {@code NPCSkills} {@code Object}.
	 * @param npc The npc.
	 */
	public NPCSkills(NPC npc) {
		this.npc = npc;
		this.levels = new int[25];
		this.staticLevels = new int[25];
		maximumLifepoints = npc.getDefinition().getLifepoints();
		reset();
	}
	
	/**
	 * Resets the NPC's skills.
	 */
	public void reset() {
		levels[Skills.ATTACK] = staticLevels[Skills.ATTACK] = npc.getDefinition().getAttackLevel();
		levels[Skills.STRENGTH] = staticLevels[Skills.STRENGTH] = npc.getDefinition().getStrengthLevel();
		levels[Skills.DEFENCE] = staticLevels[Skills.DEFENCE] = npc.getDefinition().getDefenceLevel();
		levels[Skills.RANGE] = staticLevels[Skills.RANGE] = npc.getDefinition().getRangeLevel();
		levels[Skills.MAGIC] = staticLevels[Skills.MAGIC] = npc.getDefinition().getMagicLevel();
		lifepoints = maximumLifepoints;
	}
	
	@Override
	public void setLevel(int slot, int level) {
		levels[slot] = level;
	}

	@Override
	public int getLevel(int slot) {
		return levels[slot];
	}

	@Override
	public int getStaticLevel(int slot) {
		return staticLevels[slot];
	}

	@Override
	public int heal(int health) {
		lifepoints += health;
		int left = 0;
		if (lifepoints > maximumLifepoints) {
			left = lifepoints - maximumLifepoints;
			lifepoints = maximumLifepoints;
		}
		return left;		
	}

	@Override
	public int hit(int damage) {
		lifepoints -= damage;
		int left = 0;
		if (lifepoints < 0) {
			left = -lifepoints;
			lifepoints = 0;
		}
		return left;
	}

	@Override
	public void setLifepoints(int lifepoints) {
		this.lifepoints = lifepoints;
	}

	@Override
	public int getLifepoints() {
		return lifepoints;
	}

	@Override
	public int getMaximumLifepoints() {
		return maximumLifepoints;
	}
	
	public void setMaximumLifepoints(int maximumLifepoints) {
		this.maximumLifepoints = maximumLifepoints;
	}

	@Override
	public double getPrayerPoints() {
		return 0;
	}

	@Override
	public int updateLevel(int skill, int amount, int maximum) {
		int left = (levels[skill] + amount) - maximum;
		int level = levels[skill] += amount;
		if (level < 0) {
			levels[skill] = 0;
		} else if (level > maximum) {
			levels[skill] = maximum;
		}
		return left;
	}
	
	@Override
	public void updatePrayerPoints(double amount) {	}

	@Override
	public void setLifepointsIncrease(int amount) { }
	
}