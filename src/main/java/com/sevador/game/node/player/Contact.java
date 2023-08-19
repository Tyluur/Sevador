package com.sevador.game.node.player;

import java.io.Serializable;

import com.sevador.utility.Misc;

/**
 * Represents a single contact.
 * @author Emperor
 *
 */
public class Contact implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -3071470708205741342L;

	/**
	 * The contact's name.
	 */
	private String name;
	
	/**
	 * The formatted name.
	 */
	private String formatName;
	
	/**
	 * The contacts previously used name.
	 */
	private String lastName;
	
	/**
	 * The contact's clan rank.
	 */
	private int clanRank;

	/**
	 * Constructs a new {@code Contact} {@code Object}.
	 * @param name The player's name.
	 * @param lastName The player's previously used name.
	 */
	public Contact(String name, String lastName) {
		this.name = name;
		this.formatName = Misc.formatPlayerNameForDisplay(name);
		this.lastName = lastName;
	}
	
	@Override
	public boolean equals(Object o) {
		Contact c = (Contact) o;
		return name.equals(c.name) && lastName.equals(c.lastName);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the clanRank
	 */
	public int getClanRank() {
		return clanRank;
	}

	/**
	 * @param clanRank the clanRank to set
	 */
	public void setClanRank(int clanRank) {
		this.clanRank = clanRank;
	}

	/**
	 * @return the formatName
	 */
	public String getFormatName() {
		return formatName;
	}

	/**
	 * @param formatName the formatName to set
	 */
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}
}