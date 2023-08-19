package com.sevador.game.action.impl;

import com.sevador.game.action.Action;

/**
 * Represents a null action.
 * @author Emperor
 *
 */
public class NullAction extends Action {

	/**
	 * The singleton.
	 */
	private static final NullAction SINGLETON = new NullAction();
	
	/**
	 * Constructs a new {@code NullAction} {@code Object}.
	 */
	private NullAction() {
		super(null);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public int getActionType() {
		return 0;
	}

	/**
	 * @return the singleton
	 */
	public static NullAction getSingleton() {
		return SINGLETON;
	}

}
