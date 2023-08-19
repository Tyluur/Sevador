package net.burtleburtle.thread;

import com.sevador.game.node.activity.ActivityManager;

/**
 * The major update handling thread.
 * 
 * @author Emperor
 * 
 */
public final class MajorUpdateWorker implements Runnable {

	/**
	 * The amount of cycle ticks.
	 */
	private static int ticks = 0;

	/**
	 * The start of the major update procedure.
	 */
	private long start;

	@Override
	public void run() {
		Thread.currentThread().setName("MajorUpdateWorker");
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		while (true) {
			start = System.currentTimeMillis();
			ticks++;
			sleep();
		}
	}

	/**
	 * Lets the current thread sleep.
	 */
	private void sleep() {
		ActivityManager.getActivityManger().tick();
		try {
			long duration = 600 - (System.currentTimeMillis() - start);
			if (duration > 0) {
				Thread.sleep(duration);
			} else {
				System.out.println("Updating cycle duration took " + -duration + "ms too long!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the amount of ticks.
	 * 
	 * @return The amount of ticks.
	 */
	public static int getTicks() {
		return ticks;
	}

}