package com.sevador.utility;

/**
 * Holds the return codes that can be sent to the client when attempting to login.
 * @author Emperor
 *
 */
public enum ReturnCodes {

	/**
	 * An unexpected server response occured.
	 */
	UNEXPECTED_RESPONSE(0),
	
	/**
	 * Could not display advertisement video, logging in in x seconds.
	 */
	COULD_NOT_DISPLAY_AD(1),
	
	/**
	 * A succesful login.
	 */
	SUCCESFUL(2),
	
	/**
	 * Invalid username or password has been entered.
	 */
	INVALID_CREDENTIALS(3),
	
	/**
	 * This account is banned.
	 */
	ACCOUNT_DISABLED(4),
	
	/**
	 * This account is already logged in.
	 */
	ALREADY_ONLINE(5),
	
	/**
	 * We have updated and client needs to be reloaded.
	 */
	UPDATED(6),
	
	/**
	 * The world is full.
	 */
	FULL_WORLD(7),
	
	/**
	 * Login server is offline.
	 */
	LOGIN_SERVER_OFFLINE(8),
	
	/**
	 * The login limit has been exceeded.
	 */
	LOGIN_LIMIT_EXCEEDED(9),
	
	/**
	 * The session key was invalid.
	 */
	BAD_SESSION_ID(10),
	
	/**
	 * The password is too weak, and should be improved.
	 */
	WEAK_PASSWORD(11),
	
	/**
	 * When trying to connect to a members world while being f2p.
	 */
	MEMBERS_WORLD(12),
	
	/**
	 * Could not login.
	 */
	COULD_NOT_LOGIN(13),
	
	/**
	 * The server is currently updating.
	 */
	UPDATING(14),
	
	/**
	 * Too many incorrect login attempts from your address.
	 */
	TOO_MANY_INCORRECT_LOGINS(16),
	
	/**
	 * When logging on a free world while standing in members area.
	 */
	STANDING_IN_MEMBER(17),
	
	/**
	 * This account is locked as it might have been stolen.
	 */
	LOCKED(18),
	
	/**
	 * When trying to use fullscreen to login on a free world.
	 */
	FULLSCREEN_MEMBERS_ONLY(19),
	
	/**
	 * The login server connected to is invalid.
	 */
	INVALID_LOGIN_SERVER(20),
	
	/**
	 * When the player's saved file exists, but is unable to be loaded.
	 */
	ERROR_LOADING_PROFILE(24),
	
	/**
	 * This computer address is disabled as it was used to break our rules.
	 */
	IP_BANNED(26);
	
	/**
	 * The value.
	 */
	private final byte value;
	
	/**
	 * Constructs a new {@code ReturnCodes} {@code Object}.
	 * @param value The value.
	 */
	private ReturnCodes(int value) {
		this.value = (byte) value;
	}

	/**
	 * Gets the return code value.
	 * @return The value.
	 */
	public byte getValue() {
		return value;
	}
	
}
