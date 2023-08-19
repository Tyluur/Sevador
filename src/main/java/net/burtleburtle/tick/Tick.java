package net.burtleburtle.tick;

/**
 * Represents a Tick Task.
 * @author Emperor
 *
 */
public abstract class Tick {

	/**
	 * If the task is still running.
	 */
	private boolean running = true;
	
	/**
	 * The amount of game-ticks to wait before execution.
	 */
	private double delay;
	
	/**
	 * The amount of ticks passed.
	 */
	int ticksPassed;
	
	/**
	 * Constructs a new {@code Tick} {@code Object}.
	 * @param d The delay.
	 */
	public Tick(double d) {
		this.delay = d;
	}
	
	/**
	 * Updates this {@code Tick} task.
	 * @return {@code True} if this {@code Tick} is finished and can be removed, {@code false} if not.
	 */
	public boolean update() {
		if (!running) {
			return true;
		}
		if (++ticksPassed >= delay) {
			ticksPassed = 0;
			return run() || !running;
		}
		return false;
	}
	
	/**
	 * Executes the task.
	 * @return {@code True} if the task is finished, {@code false} if we have to re-schedule.
	 */
	public abstract boolean run();
	
	/**
	 * Manually stop the {@code Tick} task.
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Manually start the {@code Tick} task.
	 */
	public void start() {
		running = true;
	}
	
	/**
	 * Checks if the tick is still running.
	 * @return {@code True}.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Gets the delay of this {@code Tick}.
	 * @return The delay.
	 */
	public double getDelay() {
		return delay;
	}
	
	/**
	 * Sets the delay.
	 * @param delay The delay.
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
}