package com.sevador.game.action;

/**
 * A class used for getting a unique flag.
 * @author Emperor
 *
 */
public class ActionFlag {

	/**
	 * If an interface should be closed.
	 */
	public static final int CLOSE_INTERFACE = 0x2;

	/**
	 * If the chatbox interface should be closed.
	 */
	public static final int CLOSE_CHATBOX = 0x1;

	/**
	 * The current flag.
	 */
	private static int flag = 0x2;
	
	/**
	 * Gets the next unique flag.
	 * @return The unique flag.
	 */
	public static int nextFlag() {
		return flag <<= 1;
	}
	
	/**
	 * Constructs a new {@code ActionFlag} {@code Object}.
	 */
	private ActionFlag() {
		/*
		 * empty.
		 */
	}
}