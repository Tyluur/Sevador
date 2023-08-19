package com.sevador.utility.world;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.node.Node;
import com.sevador.game.node.model.Location;

/**
 * Represents the world.
 * @author Emperor
 *
 */
public final class WorldArea {

	/**
	 * The multi combat areas.
	 */
	private static final List<Area> MULTI_AREAS = new ArrayList<Area>();

	/**
	 * The risk combat areas.
	 */
	private static final List<Area> RISK_AREAS = new ArrayList<Area>();

	/**
	 * The risk combat areas.
	 */
	private static final List<Area> SAFE_AREAS = new ArrayList<Area>();
	
	/**
	 * Load the areas.
	 */
	static {
		RISK_AREAS.add(new Area(2944, 3525, 3400, 3975)); //Wilderness
		SAFE_AREAS.add(new Area(2756, 5512, 2877, 5628)); //Safe pk (clan wars)
		MULTI_AREAS.add(new Area(2756, 5537, 2877, 5628)); //Safe pk (clan wars) multi-area
	}
	
	/**
	 * Checks if a node is in multi-area.
	 * @param node The node.
	 * @return {@code True} if so.
	 */
	public static boolean isMulti(Node node) {
		Location l = node.getLocation();
		for (Area area : MULTI_AREAS) {
			if (area.contains(l)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a node is in risk-combat area.
	 * @param node The node.
	 * @return {@code True} if so.
	 */
	public static boolean isRisk(Node node) {
		Location l = node.getLocation();
		for (Area area : RISK_AREAS) {
			if (area.contains(l)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a node is in safe-combat area.
	 * @param node The node.
	 * @return {@code True} if so.
	 */
	public static boolean isSafe(Node node) {
		Location l = node.getLocation();
		for (Area area : SAFE_AREAS) {
			if (area.contains(l)) {
				return true;
			}
		}
		return false;
	}
}