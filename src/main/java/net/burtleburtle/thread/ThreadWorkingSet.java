package net.burtleburtle.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A working set containing all the main threads, and thread-related factories.
 * @author Emperor
 *
 */
public class ThreadWorkingSet {
	
	/**
	 * The executor used to execute the js5 tasks.
	 */
	private final Executor js5worker = Executors.newSingleThreadExecutor(new EmperialThreadFactory("Js5Worker"));
	
	/**
	 * The logic worker.
	 */
	private final Executor logic = Executors.newSingleThreadExecutor(new EmperialThreadFactory("GameLogic"));
	
	/**
	 * Constructs a new {@code ThreadWorkingSet} {@code Object}.
	 */
	public ThreadWorkingSet() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Submits a new js5 task to execute.
	 * @param runnable The js5 task.
	 */
	public void submitJs5Work(Runnable runnable) {
		js5worker.execute(runnable);
	}
	
	/**
	 * Submits a new task to execute.
	 * @param runnable The logic task.
	 */
	public void submitLogic(Runnable runnable) {
		logic.execute(runnable);
	}

}