package com.sevador.game.world;

import com.sevador.game.node.Node;
import com.sevador.utility.ReturnCodes;

/**
 * An interface which describes the methods for loading persistent world
 * information.
 * 
 * @author Emperor
 * @author Graham Edgecombe
 * 
 */
public interface WorldLoader<N extends Node> {

	/**
	 * Represents the result of a login request.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	public static class LoginResult {

		/**
		 * The return code.
		 */
		private ReturnCodes returnCode;

		/**
		 * The node object.
		 */
		private Node node;

		/**
		 * Creates a login result Object.
		 * 
		 * @param code
		 *            The return code.
		 * @param node
		 *            The node object.
		 */
		public LoginResult(ReturnCodes code, Node node) {
			this.returnCode = code;
			this.node = node;
		}

		/**
		 * Gets the return code.
		 * 
		 * @return The return code.
		 */
		public ReturnCodes getReturnCode() {
			return returnCode;
		}

		/**
		 * Sets the return code.
		 * 
		 * @param value
		 *            The return code.
		 */
		public void setReturnCode(ReturnCodes returnCode) {
			this.returnCode = returnCode;
		}

		/**
		 * Gets the node.
		 * 
		 * @return The node.
		 */
		public Node getNode() {
			return node;
		}

	}

	/**
	 * Checks if a set of login details are correct. If correct, creates and
	 * loads the player object.
	 * 
	 * @param node
	 *            The node.
	 * @return The login result.
	 */
	public LoginResult checkLogin(Node node);

	/**
	 * Loads node information.
	 * 
	 * @param result
	 *            The login result.
	 * @return The loaded {@code node}.
	 */
	public N load(LoginResult result);

	/**
	 * Saves node information.
	 * 
	 * @param node
	 *            The node object.
	 * @return The saved {@code node}.
	 */
	public N save(Node node);

}