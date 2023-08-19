package net.burtleburtle.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The thread factory implementation used for naming and creating threads in the thread working set.
 * @author Emperor
 *
 */
public class EmperialThreadFactory implements ThreadFactory {

	/**
	 * The atomic integer holding count of the amount of threads created by this factory.
	 */
	private final AtomicInteger threadCount = new AtomicInteger();
	
	/**
	 * The name
	 */
	private final String name;
	
	/**
	 * The thread priority.
	 */
	private int priority;
	
	/**
	 * Constructs a new {@code EmperialThreadFactory} {@code Object}.
	 * @param name The thread pool name.
	 */
	public EmperialThreadFactory(String name) {
		this.name = name;
		this.priority = Thread.NORM_PRIORITY;
	}
	
	@Override
	public Thread newThread(Runnable runnable) {
		Thread t = new Thread(runnable);
		t.setName(new StringBuilder(name).append(" ").append(threadCount.getAndIncrement()).toString());
		t.setPriority(priority);
		return t;
	}

	/**
	 * Sets the thread priority for this factory.
	 * @param priority The priority to set.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Gets the current thread priority.
	 * @return The priority.
	 */
	public int getPriority() {
		return priority;
	}

}
