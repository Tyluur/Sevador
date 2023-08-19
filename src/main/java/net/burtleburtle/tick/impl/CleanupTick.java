package net.burtleburtle.tick.impl;

import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.tick.Tick;

import com.sevador.Main;
import com.sevador.game.node.player.Player;

/**
 * Handles the cleaning up of the server.
 * @author Emperor
 *
 */
public final class CleanupTick extends Tick {

	/**
	 * The cleanup tick singleton.
	 */
	private static final CleanupTick SINGLETON = new CleanupTick();
	
	/**
	 * The amount of ticks.
	 */
	private int ticks = 0;
	
	/**
	 * Constructs a new {@code CleanupTick} {@code Object}.
	 */
	private CleanupTick() {
		super(100);
	}

	@Override
	public boolean run() {
		Main.getWorkingSet().submitLogic(new Runnable() {
			@Override
			public void run() {
				for (Player player : NodeWorker.getPlayers()) {
					if (player != null) {
						if (!player.isLoggedIn() 
								|| player.getAttribute("last:ping", MajorUpdateWorker.getTicks()) < MajorUpdateWorker.getTicks() - 15
								|| !player.getIOSession().getChannel().isConnected()) {
							Main.getLogger().info("Removing " + player.getCredentials().getDisplayName() + " through clean-up.");
							Main.getNodeWorker().remove(player);
						}
					}
				}
			}
		});
		/*
		 * We clean up every 5 minutes.
		 */
		if (ticks++ % 5 == 0) {
			Main.getWorkingSet().submitLogic(new Runnable() {
				@Override
				public void run() {
					System.gc();
					System.runFinalization();
				}
			});
		}
		return false;
	}

	/**
	 * Gets the singleton.
	 * @return The singleton.
	 */
	public static CleanupTick getSingleton() {
		return SINGLETON;
	}

}