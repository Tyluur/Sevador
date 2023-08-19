package com.sevador.game.node.model.gameobject;

import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Location;

/**
 * Represents a removed object
 * @author Emperor
 *
 */
public class Removed extends GameObject {

	/**
	 * Constructs a new Removed object.
	 * @param id The object id.
	 * @param x The object x-coordinate.
	 * @param y The object y-coordinate.
	 * @param z The object z-coordinate.
	 */
	public Removed(int id, int x, int y, int z) {
		super(id, NodeType.GAME_OBJECT, Location.locate(x, y, z), 10, 0);
	}

	/**
	 * Constructs a new Removed object.
	 * @param id The object id.
	 * @param location The object's location.
	 */
	public Removed(int id, Location location) {
		super(id, NodeType.GAME_OBJECT, location, 10, 0);
	}
	
	/**
	 * Constructs a new Removed object.
	 * @param id The object id.
	 * @param location The object's location.
	 * @param rotation The object's rotation.
	 */
	public Removed(int id, Location location, int rotation) {
		super(id, NodeType.GAME_OBJECT, location, 10, rotation);
	}

	/**
	 * Constructs a new {@code Removed} {@code Object}.
	 * @param id The object id.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param z The z-coordinate.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Removed(int id, int x, int y, int z, int type, int rotation) {
		super(id, NodeType.GAME_OBJECT, Location.locate(x, y, z), type, rotation);
	}
	
	/**
	 * Constructs a new {@code Removed} {@code Object}.
	 * @param id The object id.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Removed(int id, int type, int rotation) {
		super(id, NodeType.GAME_OBJECT, Location.locate(0, 0, 0), type, rotation);
	}
	
	/**
	 * Constructs a new {@code Removed} {@code Object}.
	 * @param id The object id.
	 * @param nodeType The nodeType.
	 * @param location The location.
	 * @param type The object type.
	 * @param rotation The rotation.
	 */
	public Removed(int id, NodeType nodeType, Location location, int type, int rotation) {
		super(id, nodeType, location, type, rotation);
	}
	
	@Override
	public boolean isRemoved() {
		return true;
	}

}