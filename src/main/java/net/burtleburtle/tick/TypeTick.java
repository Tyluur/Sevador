package net.burtleburtle.tick;

/**
 * Represents a tick that can be executed with an argument.
 * @author Emperor
 * @param <T> The type argument.
 *
 */
public abstract class TypeTick<T> extends Tick {

	/**
	 * The argument for this tick.
	 */
	private T arg;
	
	/**
	 * Constructs a new {@code TypeTick} {@code Object}.
	 * @param delay The delay.
	 */
	public TypeTick(int delay) {
		super(delay);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean run() {
		return run(arg);
	}
	
	/**
	 * Executes the task.
	 * @return {@code True} if the task is finished, {@code false} if we have to re-schedule.
	 */
	public abstract boolean run(T... arg);

	/**
	 * @return the arg
	 */
	public T getArg() {
		return arg;
	}

	/**
	 * @param arg the arg to set
	 */
	public void setArg(T arg) {
		this.arg = arg;
	}

}