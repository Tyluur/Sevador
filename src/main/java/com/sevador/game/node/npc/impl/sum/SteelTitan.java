package com.sevador.game.node.npc.impl.sum;

import net.burtleburtle.tick.Tick;

import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.world.World;

/**
 * Represents a steel titan.
 * @author Emperor
 *
 */
@SuppressWarnings("serial")
public class SteelTitan extends CombatFamiliar {

	/**
	 * The animation played when summoned/called.
	 */
	private static final Animation SUMMON_ANIMATION = new Animation(8188, 0, true);
	
	/**
	 * The combat action to use.
	 */
	//private CombatAction combatAction = new SteelTitanAction();
	
	/**
	 * Constructs a new {@code SteelTitan} {@code Object}.
	 * @param id The NPC id.
	 */
	public SteelTitan(int id) {
		super(id, 6400);
	}
	
	@Override
	public NPC init() {
		super.init();
		visual(SUMMON_ANIMATION, LARGE_SUMMON_GRAPHIC);
		return this;
	}
	
	@Override
	public void call() {
		super.call();
		World.getWorld().submit(new Tick(1) {
			@Override
			public boolean run() {
				visual(SUMMON_ANIMATION, LARGE_SUMMON_GRAPHIC);
				return true;
			}			
		});
	}
	
	@Override
	public int getPouchId() {
		return 12790;
	}
	
	@Override
	public int getSpecialCost() {
		return 12;
	}
	
	@Override
	public String getSpecialName() {
		return "Steel of Legends";
	}
	
	@Override
	public String getSpecialDescription() {
		return "The steel titan's next attack will be four powerful ranged attacks";
	}

}