package com.sevador.game.node.model.gameobject;

import net.burtleburtle.cache.format.ObjectDefinition;

import com.sevador.game.node.Node;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;

/**
 * Represents a game object.
 * 
 * @author Emperor
 * 
 */
public class GameObject implements Node {

	/**
	 * The object id.
	 */
	private final int id;

	private int health;
	private boolean healthSet;

	private Player owner;

	private boolean exists = true;
	private boolean spawned;

	/**
	 * Object's rotation and type.
	 */
	private final int rotation, type;

	/**
	 * The life of the object, used for mining.
	 */
	private int life;

	/**
	 * The object's location.
	 */
	private Location location;

	/**
	 * The object's definition.
	 */
	private final ObjectDefinition definition;

	/**
	 * The node type.
	 */
	private NodeType nodeType;

	/**
	 * Constructs a new game object.
	 * 
	 * @param id
	 *            The object id.
	 * @param x
	 *            The object x-coordinate.
	 * @param y
	 *            The object y-coordinate.
	 * @param z
	 *            The object z-coordinate.
	 */
	public GameObject(int id, int x, int y, int z) {
		this(id, NodeType.GAME_OBJECT, Location.locate(x, y, z), 10, 0);
	}

	/**
	 * Constructs a new game object.
	 * 
	 * @param id
	 *            The object id.
	 * @param location
	 *            The object's location.
	 */
	public GameObject(int id, Location location) {
		this(id, NodeType.GAME_OBJECT, location, 10, 0);
	}

	/**
	 * Constructs a new game object.
	 * 
	 * @param id
	 *            The object id.
	 * @param location
	 *            The object's location.
	 * @param rotation
	 *            The object's rotation.
	 */
	public GameObject(int id, Location location, int rotation) {
		this(id, NodeType.GAME_OBJECT, location, 10, rotation);
	}

	/**
	 * Constructs a new {@code GameObject} {@code Object}.
	 * 
	 * @param id
	 *            The object id.
	 * @param x
	 *            The x-coordinate.
	 * @param y
	 *            The y-coordinate.
	 * @param z
	 *            The z-coordinate.
	 * @param type
	 *            The object type.
	 * @param rotation
	 *            The rotation.
	 */
	public GameObject(int id, int x, int y, int z, int type, int rotation) {
		this(id, NodeType.GAME_OBJECT, Location.locate(x, y, z), type, rotation);
	}

	/**
	 * Constructs a new {@code GameObject} {@code Object}.
	 * 
	 * @param id
	 *            The object id.
	 * @param type
	 *            The object type.
	 * @param rotation
	 *            The rotation.
	 */
	public GameObject(int id, int type, int rotation) {
		this(id, NodeType.GAME_OBJECT, Location.locate(0, 0, 0), type, rotation);
	}

	/**
	 * Constructs a new {@code GameObject} {@code Object}.
	 * 
	 * @param id
	 *            The object id.
	 * @param nodeType
	 *            The nodeType.
	 * @param location
	 *            The location.
	 * @param type
	 *            The object type.
	 * @param rotation
	 *            The rotation.
	 */
	public GameObject(int id, NodeType nodeType, Location location, int type,
			int rotation) {
		this.id = id;
		this.nodeType = nodeType;
		this.location = location;
		this.type = type;
		this.rotation = rotation;
		this.definition = ObjectDefinition.forId(id);
	}

	/**
	 * Gets a transformed object of this object.
	 * 
	 * @param id
	 *            The new object id.
	 * @return The constructed game object.
	 */
	public GameObject transform(int id) {
		return new GameObject(id, nodeType, location, type, rotation);
	}

	/**
	 * Gets the constructed instance of this object, or the constructed object
	 * if this instance is {@link Removed}.
	 * 
	 * @return The {@code Constructed} instance.
	 */
	public Constructed getConstructed() {
		return new Constructed(id, nodeType, location, type, rotation);
	}

	/**
	 * Gets the Removed instance of this object, or gets the removed object if
	 * this instance is {@link Constructed}.
	 * 
	 * @return The {@code Removed} instance.
	 */
	public Removed getRemoved() {
		return new Removed(id, nodeType, location, type, rotation);
	}

	/**
	 * Checks if this game object is a Constructed game object.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isConstructed() {
		return false;
	}

	/**
	 * Checks if this game object is a Removed game object.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isRemoved() {
		return false;
	}

	/**
	 * Checks if this instance holds a removed object.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean hasRemoved() {
		return false;
	}

	/**
	 * Checks if this instance holds a constructed object.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean hasConstructed() {
		return false;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Gets the definition.
	 * 
	 * @return The definition.
	 */
	public ObjectDefinition getDefinition() {
		return definition;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof GameObject)) {
			return false;
		}
		GameObject other = (GameObject) obj;
		return other.getId() == id && other.getLocation().equals(location);
	}

	@Override
	public String toString() {
		return "[GameObject " + id + ", " + location + ", " + type + ", "
				+ rotation + "]";
	}

	@Override
	public NodeType getNodeType() {
		return nodeType;
	}

	@Override
	public void setNodeType(NodeType n) {
		this.nodeType = n;
	}

	@Override
	public int size() {
		return definition.sizeX; // TODO;
	}

	/**
	 * @return the life
	 */
	public int getLife() {
		return life;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(int life) {
		this.life = life;
	}

	public void decrementObjectLife() {
		this.life--;
	}

	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * @return the spawned
	 */
	public boolean isSpawned() {
		return spawned;
	}

	/**
	 * @param spawned the spawned to set
	 */
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}

	/**
	 * @return the exists
	 */
	public boolean isExists() {
		return exists;
	}

	/**
	 * @param exists the exists to set
	 */
	public void setExists(boolean exists) {
		this.exists = exists;
	}

	/**
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/**
	 * @return the healthSet
	 */
	public boolean isHealthSet() {
		return healthSet;
	}

	/**
	 * @param healthSet the healthSet to set
	 */
	public void setHealthSet(boolean healthSet) {
		this.healthSet = healthSet;
	}
	
	public void setLocation(Location loc) {
		this.location = loc;
	}

}