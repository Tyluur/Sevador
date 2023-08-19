package com.sevador.game.node.model.gameobject;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.tick.Tick;

import com.sevador.Main;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionBuilder;
import com.sevador.game.world.World;
import com.sevador.network.out.ConstructObject;
import com.sevador.network.out.RemoveObject;
import com.sevador.utility.Constants;

/**
 * An aiding class for object constructing/removing.
 * 
 * @author Emperor
 * 
 */
public class ObjectBuilder {

	/**
	 * The list of added/removed game objects.
	 */
	private static final List<GameObject> OBJECTS = new ArrayList<GameObject>();

	/**
	 * Adds/removes permanent customizations.
	 */
	static {
		add(new GameObject(47120, Location.locate(3168, 3499, 0), 0));
		add(new GameObject(37985, Location.locate(3167, 3503, 0), 0));
		add(new GameObject(17010, Location.locate(3159, 3499, 0), 0));
		add(new GameObject(4874, Location.locate(3162, 3496, 0), 0));
		add(new GameObject(4875, Location.locate(3163, 3496, 0), 0));
		add(new GameObject(4876, Location.locate(3164, 3496, 0), 0));
		add(new GameObject(4877, Location.locate(3165, 3496, 0), 0));
		add(new GameObject(4878, Location.locate(3166, 3496, 0), 0));
		remove(new GameObject(47150, new Location(3163, 3490, 0)));
	}

	/**
	 * Replaces a game object.
	 * 
	 * @param remove
	 *            The object to remove.
	 * @param construct
	 *            The object to add.
	 * @return {@code True} if succesful.
	 */
	public static boolean replace(GameObject remove, GameObject construct) {
		GameObject current = RegionBuilder.removeObject(remove.getLocation()
				.getX(), remove.getLocation().getY(), remove.getLocation()
				.getZ(), remove.getType());
		if (current == null) {
			if (Constants.isWindows())
				Main.getLogger().info("Object could not be replaced - object to remove is invalid.");
			return false;
		}
		Constructed constructed = construct.getConstructed();
		constructed.setRemoved(remove.getRemoved());
		RegionBuilder.addObject(constructed, false);
		Main.getNodeWorker().offer(constructed);
		OBJECTS.remove(remove); // ?
		OBJECTS.add(constructed);
		return true;
	}

	/**
	 * Replaces a game object temporarily.
	 * 
	 * @param remove
	 *            The object to remove.
	 * @param construct
	 *            The object to add.
	 * @param restoreTicks
	 *            The amount of ticks before the object gets restored.
	 * @return {@code True} if succesful.
	 */
	public static boolean replace(GameObject remove, GameObject construct,
			int restoreTicks) {
		GameObject current = RegionBuilder.removeObject(remove.getLocation()
				.getX(), remove.getLocation().getY(), remove.getLocation()
				.getZ(), remove.getType());
		if (current == null) {
			if (Constants.isWindows())
				Main.getLogger().info("Object could not be replaced - object to remove is invalid.");
			return false;
		}
		final Constructed constructed = construct.getConstructed();
		constructed.setRemoved(remove.getRemoved());
		RegionBuilder.addObject(constructed, false);
		Main.getNodeWorker().offer(constructed);
		OBJECTS.remove(remove); // ?
		OBJECTS.add(constructed);
		World.getWorld().submit(new Tick(restoreTicks) {
			@Override
			public boolean run() {
				return replace(constructed, constructed.getRemoved());
			}
		});
		return true;
	}

	/**
	 * Adds a game object.
	 * 
	 * @param object
	 *            The object to add.
	 * @return {@code True} if succesful.
	 */
	public static boolean add(GameObject object) {
		if (object.getLocation().getGameObjectType(object.getType()) != null) {
			if (Constants.isWindows())
				Main.getLogger().info("Object could not be added - location already contained this object type.");
			return false;
		}
		Constructed constructed = object.getConstructed();
		RegionBuilder.addObject(constructed, false);
		Main.getNodeWorker().offer(constructed);
		OBJECTS.add(constructed);
		return true;
	}

	/**
	 * Adds a game object.
	 * 
	 * @param object
	 *            The object to add.
	 * @return {@code True} if succesful.
	 */
	public static boolean remove(GameObject object) {
		GameObject current = RegionBuilder.removeObject(object.getLocation()
				.getX(), object.getLocation().getY(), object.getLocation()
				.getZ(), object.getType());
		if (current == null) {
			if (Constants.isWindows())
				Main.getLogger().info("Object could not be removed - location did not contain this object.");
			return false;
		}
		Removed removed = current.getRemoved();
		Main.getNodeWorker().remove(removed);
		OBJECTS.add(removed);
		return true;
	}

	/**
	 * Called when a player enters a new region.
	 * 
	 * @param player
	 *            The player.
	 */
	public static void enterRegion(Player player) {
		for (GameObject object : OBJECTS) {
			if (object.isConstructed()) {
				player.getIOSession()
				.write(new ConstructObject(player, object));
			} else {
				player.getIOSession().write(new RemoveObject(player, object));
			}
		}
	}

	/**
	 * @return the objects
	 */
	public static List<GameObject> getObjects() {
		return OBJECTS;
	}
}