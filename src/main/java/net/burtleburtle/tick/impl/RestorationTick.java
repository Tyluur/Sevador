package net.burtleburtle.tick.impl;

import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.tick.Tick;

import com.sevador.game.node.player.Player;

/**
 * Handles the restoration tick.
 * @author Emperor
 *
 */
public final class RestorationTick extends Tick {

	/**
	 * The restoration tick singleton.
	 */
	private static final RestorationTick SINGLETON = new RestorationTick();
	
	/**
	 * The delay.
	 */
	private static final int DELAY = 50;
	
	/**
	 * The ticks elapsed.
	 */
	private int ticks = 0;
	
	/**
	 * Constructs a new {@code RestorationTick} {@code Object}.
	 */
	private RestorationTick() {
		super(DELAY);
	}

	@Override
	public boolean run() {
		boolean normalizeSkills = ticks++ % 2 == 0;
		for (Player p : NodeWorker.getPlayers()) {
			if (p != null) {
				p.getSettings().updateSpecialEnergy(-10);
				if (normalizeSkills) {
					for (int i = 0; i < 25; i++) {
						int level = p.getSkills().getLevel(i);
						int staticLevel = p.getSkills().getStaticLevel(i);
						if (level != staticLevel) {
							p.getSkills().updateLevel(i, level > staticLevel ? -1 : 1, level > staticLevel ? 0 : staticLevel);
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * @return the singleton
	 */
	public static RestorationTick getSingleton() {
		return SINGLETON;
	}

}