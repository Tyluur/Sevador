package net.burtleburtle.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * A class used for executing 2 runnables parallel.
 * @author Emperor
 *
 */
public class ParallelExecutor {

	/**
	 * Our executor service used.
	 */
	private final ExecutorService executorService;
		
	/**
	 * Constructs a new {@code ParallelExecutor} using the
	 * {@link Executors#newFixedThreadPool} {@code ExecutorService}, <br>
	 * with a maximum amount of threads depending on the available processors 
	 * <br>(determined by {@link Runtime#availableProcessors}).
	 * <br>The {@link ThreadFactory} will be set as a new {@link EmperialThreadFactor} 
	 * <br>using {@code ParallelExecutor}-# as name. 
	 * <br>The '#' will change depending on the amount of threads in the {@code thread pool}
	 */
	public ParallelExecutor() {
		this(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), 
				new EmperialThreadFactory("ParallelExecutor")));
	}
	
	/**
	 * Constructs a new {@code ParallelExecutor} using the given {@link ExecutorService}.
	 * <br>The {@link ThreadFactory} used will be the one used by the executor, 
	 * so using a different {@code ThreadFactory} should be argued when constructing the {@code ExecutorService}. 
	 * @param executor The {@code ExecutorService} used.
	 */
	public ParallelExecutor(ExecutorService executor) {
		this.executorService = executor;
	}
	
	/**
	 * Offers 2 {@link Runnable}s to execute parallel.
	 * @param r The first runnable.
	 * @param r1 The second runnable.
	 */
	public void offer(Runnable r, Runnable r1) {
		executorService.execute(r);
		executorService.execute(r1);
	}
	
}