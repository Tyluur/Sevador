package com.sevador.game.node.model;

/**
 * Represents a positioned graphic.
 * @author Emperor
 *
 */
public final class LocationGraphic {

	/**
	 * The graphic's location.
	 */
	private final Location location;
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * Constructs a new {@code LocationGraphic} {@code Object}.
	 * @param id The graphic id.
	 * @param location The location.
	 */
	public LocationGraphic(int id, Location location) {
		this.location = location;
		this.id = id;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}