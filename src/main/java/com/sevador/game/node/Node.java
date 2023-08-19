package com.sevador.game.node;

import com.sevador.game.node.model.Location;

/**
 * An interface implemented by all the players, NPCs, game objects, ...
 * @author Emperor
 *
 */
public interface Node {

	/**
	 * Gets the node type of this node.
	 * @return The node type.
	 */
	public NodeType getNodeType();
	
	/**
	 * Sets the node type of this node.
	 * @param n The node type.
	 */
	public void setNodeType(NodeType n);
	
	/**
	 * Gets the node's location on the world-map.
	 * @return The location.
	 */
	public Location getLocation();
	
	/**
	 * The size of this node.
	 * @return The size.
	 */
	public int size();
	
}