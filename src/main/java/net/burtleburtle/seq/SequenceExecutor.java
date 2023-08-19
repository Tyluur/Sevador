package net.burtleburtle.seq;


/**
 * The default sequence task executor, 
 * this will execute the full sequence immediatly on the current thread.
 * @author Emperor
 * @param <T> The argument type used in the sequence to execute.
 *
 */
public class SequenceExecutor<T> {

	/**
	 * The sequence to execute.
	 */
	private final Sequence<T> sequence;
	
	/**
	 * The argument used to execute the sequence methods.
	 */
	private T argument;
	
	/**
	 * Constructs a new {@code SequenceExecutor} {@code Object}.
	 * @param sequence The sequence to execute.
	 */
	public SequenceExecutor(Sequence<T> sequence) {
		this.sequence = sequence;
	}

	/**
	 * Executes the sequence.
	 */
	public void execute() {
		try {
			if (!sequence.preparation(argument)) {
				return;
			}
			do {
				while (!sequence.execution(argument));
			} while (!sequence.finalization(argument));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the argument
	 */
	public T getArgument() {
		return argument;
	}

	/**
	 * @param argument the argument to set
	 */
	public void setArgument(T argument) {
		this.argument = argument;
	}
	
}