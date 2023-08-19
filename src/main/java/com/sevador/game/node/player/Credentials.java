package com.sevador.game.node.player;

import java.io.Serializable;
import java.util.Date;

import com.sevador.utility.Misc;
import com.sevador.utility.Tools;

/**
 * Holds the player's credentials
 * 
 * @author Emperor
 * 
 */
public class Credentials implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 6572795170702934208L;
	
	/**
	 * The player's real username.
	 */
	private final String username;

	/**
	 * Whether or not your experience is locked
	 */

	private boolean experienceLocked;

	/**
	 * The player's bank pin
	 */

	private String bankPin;

	/**
	 * The player's current display name.
	 */
	private String displayName;

	/**
	 * The player's password.
	 */
	private String password;

	/**
	 * The player's rights.
	 */
	private byte rights;
	
	/**
	 * The amount of vote points the player has accumulated.
	 */
	private int votePoints;
	
	/**
	 * Whether or not you're a helper.
	 */
	private boolean helper;
	
	/**
	 * The variables used for the donator status.
	 */

	private boolean donator;
	
	public long donatorTill;

	/**
	 * The variables for donator rank
	 */

	public boolean isDonator() {
		return donator || donatorTill > Misc.currentTimeMillis();
	}

	@SuppressWarnings("deprecation")
	public void makeDonator(int months) {
		if (donatorTill < Misc.currentTimeMillis())
			donatorTill = Misc.currentTimeMillis();
		Date date = new Date(donatorTill);
		date.setMonth(date.getMonth() + months);
		donatorTill = date.getTime();
	}

	@SuppressWarnings("deprecation")
	public String getDonatorTill() {
		return (donator ? "1 Jan 2012 1:00:00 GMT" : new Date(donatorTill)
				.toGMTString()) + ".";
	}

	public void setDonator(boolean donator) {
		this.donator = donator;
	}

	/**
	 * The player's appearance.
	 */
	private Appearance appearance;

	/**
	 * The player's recoil damage.
	 */
	private int recoilDamage = 400;

	/**
	 * The player's overload ticks left.
	 */
	private int overloadTicks;

	/**
	 * The last ip-address this player used to connect.
	 */
	private String lastAddress;

	/**
	 * The player's pest control points.
	 */

	private int pestPoints;
	
	/**
	 * The player's display title.
	 */
	
	private int displayTitle;

	/**
	 * Constructs a new {@code Credentials} {@code Object}.
	 * 
	 * @param username
	 *            The username.
	 * @param password
	 *            The password.
	 */
	public Credentials(String username, String password) {
		this.username = username;
		this.displayName = Tools.formatPlayerNameForDisplay(username);
		this.password = password;
		this.rights = 0;
		this.appearance = new Appearance();
	}

	/**
	 * Gets the player's username.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the player's display name.
	 * 
	 * @param displayName
	 *            The display name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the player's display name.
	 * 
	 * @return The display name.
	 */
	public String getDisplayName() {
		return Misc.formatPlayerNameForDisplay(displayName);
	}
	
	/**
	 * @return Whether you have an active display name
	 */
	public boolean hasDisplayName() {
		return displayName != null;
	}


	/**
	 * Sets the player's password.
	 * 
	 * @param password
	 *            The password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the player's password.
	 * 
	 * @return The password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the player's current rights.
	 * 
	 * @param rights
	 *            The rights to set.
	 */
	public void setRights(byte rights) {
		this.rights = rights;
	}

	/**
	 * Gets the player's rights.
	 * 
	 * @return The rights.
	 */
	public byte getRights() {
		return rights;
	}

	/**
	 * @return the appearance
	 */
	public Appearance getAppearance() {
		return appearance;
	}
	
	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
	}

	/**
	 * @param recoilDamage
	 *            the recoilDamage to set
	 */
	public void setRecoilDamage(int recoilDamage) {
		this.recoilDamage = recoilDamage;
	}

	/**
	 * @return the recoilDamage
	 */
	public int getRecoilDamage() {
		return recoilDamage;
	}

	/**
	 * @return the overloadTicks
	 */
	public int getOverloadTicks() {
		return overloadTicks;
	}

	/**
	 * @param overloadTicks
	 *            the overloadTicks to set
	 */
	public void setOverloadTicks(int overloadTicks) {
		this.overloadTicks = overloadTicks;
	}

	/**
	 * @return the lastAddress
	 */
	public String getLastAddress() {
		return lastAddress;
	}

	/**
	 * @param lastAddress
	 *            the lastAddress to set
	 */
	public void setLastAddress(String lastAddress) {
		this.lastAddress = lastAddress;
	}

	/**
	 * @return the bankPin
	 */
	public String getBankPin() {
		return bankPin;
	}

	/**
	 * @param bankPin
	 *            the bankPin to set
	 */
	public void setBankPin(String bankPin) {
		this.bankPin = bankPin;
	}

	/**
	 * @return the pestPoints
	 */
	public int getPestPoints() {
		return pestPoints;
	}

	/**
	 * @param pestPoints
	 *            the pestPoints to set
	 */
	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	public boolean isExperienceLocked() {
		return experienceLocked;
	}

	public void setExperienceLocked(boolean b) {
		this.experienceLocked = b;
	}

	/**
	 * @return the displayTitle
	 */
	public int getDisplayTitle() {
		return displayTitle;
	}

	/**
	 * @param displayTitle the displayTitle to set
	 */
	public void setDisplayTitle(int displayTitle) {
		this.displayTitle = displayTitle;
	}

	/**
	 * @return the votePoints
	 */
	public int getVotePoints() {
		return votePoints;
	}

	/**
	 * @param votePoints the votePoints to set
	 */
	public void setVotePoints(int votePoints) {
		this.votePoints = votePoints;
	}

	/**
	 * @return the helper
	 */
	public boolean isHelper() {
		if (getRights() > 0) return true;
		return helper;
	}

	/**
	 * @param helper the helper to set
	 */
	public void setHelper(boolean helper) {
		this.helper = helper;
	}
}
