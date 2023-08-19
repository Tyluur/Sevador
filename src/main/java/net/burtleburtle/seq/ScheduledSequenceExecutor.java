package net.burtleburtle.seq;

import net.burtleburtle.tick.Tick;

import com.sevador.game.world.World;

/**
 * A sequence executor that uses a {@link Tick} task to execute the sequence.
 * @author Emperor
 *
 */
public class ScheduledSequenceExecutor<T> {

	/**
	 * The default amount of ticks.
	 */
	public static final int DEFAULT_TICKS = 1;
	
	/**
	 * Constructs a new {@code ScheduledSequenceExecutor} {@code Object}.
	 */
	public ScheduledSequenceExecutor() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Offers a new sequence to execute with a 1 tick delay.
	 * @param sequence The sequence to execute.
	 * @param argument The argument to execute the {@code Sequence} methods with.
	 * @see ScheduledSequenceExecutor#offer(Sequence, T, int)
	 */
	public void offer(Sequence<T> sequence, T argument) {
		offer(sequence, argument, DEFAULT_TICKS);
	}
	
	/**
	 * Offers a new sequence to execute, delayed by the amount of ticks given.
	 * <br>The {@link Sequence#preparation} method will be called instantly, after the first delay the
	 * <br>{@link Sequence#execution} method will be called continuing with a delay equal to 
	 * <br>the given amount of {@code ticks} until it returns {@code true}.
	 * <br>When the {@link Sequence#execution} method returns {@code true}, the {@link Sequence#finalization}
	 * <br>method gets called. 
	 * <br>If this returns {@code false} we return to executing after the delay given has passed again.
	 * <br>If this returns {@code true} the sequence will end.
	 * @param sequence The sequence to execute.
	 * @param argument The argument to execute the {@code Sequence} methods with.
	 * @param ticks The amount of delay in game-ticks.
	 */
	public void offer(final Sequence<T> sequence, final T argument, int ticks) {
		try {
			if (!sequence.preparation(argument)) {
				return;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		World.getWorld().submit(new Tick(ticks) {
			
			/**
			 * If the sequence can be finalized.
			 */
			private boolean finalize = false;
	
			@Override
			public boolean run() {
				if (finalize) {
					finalize = false;
					try {
						return sequence.finalization(argument);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				try {
					finalize = sequence.execution(argument);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return false;
			}			
		});
	}
}
