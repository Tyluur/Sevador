package net.burtleburtle.cores;

import java.util.LinkedList;
import java.util.List;

import net.burtleburtle.cores.tasks.WorldTasksManager;
import net.burtleburtle.seq.impl.EntityUpdateSequence;
import net.burtleburtle.tick.Tick;

import com.sevador.Main;
import com.sevador.game.node.activity.ActivityManager;
import com.sevador.game.world.World;
import com.sevador.utility.Misc;

public final class WorldThread extends Thread {

	protected WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}

	/**
	 * The entity updating sequence.
	 */
	private final EntityUpdateSequence updateSequence = new EntityUpdateSequence();

	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			long currentTime = Misc.currentTimeMillis();
			try {
				WorldTasksManager.processTasks();
				ActivityManager.getActivityManger().tick();
				List<Tick> ticks = new LinkedList<Tick>(World.getTicks());
				for (Tick t : ticks) {
					if (t.update()) {
						World.getTicks().remove(t);
					}
				}
				updateSequence.preparation(null);
				updateSequence.execution(null);
				updateSequence.finalization(null);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			LAST_CYCLE_CTM = Misc.currentTimeMillis();
			long sleepTime = 600 + currentTime - LAST_CYCLE_CTM;
			if (sleepTime > 600)
				Main.getLogger().info("Server took too long to cycle! Time taken: " + sleepTime);
			if (sleepTime <= 0)
				continue;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static long LAST_CYCLE_CTM;

}
