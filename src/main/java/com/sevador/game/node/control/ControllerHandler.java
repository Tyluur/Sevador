package com.sevador.game.node.control;

/**
 *
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public abstract class ControllerHandler {
	
	/**
	 * Whenever you die, you this method will be referenced.
	 */
	
	public abstract void onDeath();
	
	/**
	 * The logout method that will be called upon login
	 */
	
	public abstract void onLogin();
	
	/**
	 * The action that will be handled once a player has logged out.
	 */
	
	public abstract void onLogout();
	
	public boolean onWalk() {
		return true;
	}
	
}
