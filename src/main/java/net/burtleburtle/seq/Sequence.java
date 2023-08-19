package net.burtleburtle.seq;


/**
 * The Sequence interface, used for sequental tasks.
 * @author Emperor
 * @param <T> The argument type.
 *
 */
public interface Sequence<T> {

	/**
	 * The preparation task in the sequence.
	 * @param t The argument.
	 * @return {@code True} if succeeded, and can continue the sequence,
	 * <br>		{@code false} if failed; thus ending the sequence 
	 * 					(without calling the finalization method).
	 * @throws LoggedException When an exception is thrown.
	 */
	public boolean preparation(T t);
	
	/**
	 * The execution task in this sequence, 
	 * this executes the logic if and only if the 
	 * preparation method returned {@code true}.
	 * @param t The argument.
	 * @return {@code True} if the execution task is finished and can move on to the finalization task,
	 * <br>		{@code false} if the execution task could not end yet (and thus keeps being called until it returns {@code true}).
	 * @throws LoggedException When an exception is thrown.
	 */
	public boolean execution(T t);
	
	/**
	 * The finalization task is used to end the sequence, 
	 * though this will only be called if both the preparation and the execution method returned {@code true}.
	 * Depending on the handling of this sequence, this would end the sequence task.
	 * @param t The argument.
	 * @return {@code True} if the sequence can end, 
	 * <br>			{@code false} if the sequence has to go back to the execution method.
	 * @throws LoggedException When an exception is thrown.
	 */
	public boolean finalization(T t);
	
}