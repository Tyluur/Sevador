package com.sevador.game.node.model.gameobject;

import java.util.HashMap;
import java.util.Map;

import net.burtleburtle.cache.format.ObjectDefinition;
import net.burtleburtle.tick.Tick;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionBuilder;
import com.sevador.game.region.RegionManager;
import com.sevador.game.world.World;
import com.sevador.network.out.RemoveObject;

public class ObjectManager {

	private static Map<Location, GameObject> previousObjects = new HashMap<Location, GameObject>();

	public static GameObject add(int id, Location loc, int type, int direction) {
		return add(null, id, loc, type, direction);
	}

	public static GameObject add(Player player, int id, Location loc, int type, int direction) {
		ObjectDefinition def = ObjectDefinition.forId(id);

		if (def == null) return null;

		boolean original = false;
		GameObject originalObject = previousObjects.get(loc);

		GameObject lastObject = loc.getGameObjectType(type);
		if (lastObject != null) {
			lastObject.setExists(false);
			lastObject.setLocation(null);
		}

		if (originalObject != null && originalObject.getId() == id) {
			original = true;
			previousObjects.remove(loc);
		} else {
			originalObject = loc.getGameObjectType(type);
			if (originalObject != null) {
				previousObjects.put(loc, originalObject);
			}
		}

		RegionBuilder.removeObject(loc.getX(), loc.getY(), loc.getZ(), type);
		GameObject obj = new GameObject(id, loc);
		obj.setSpawned(true);
		obj.setOwner(player);
		GameObject object = RegionBuilder.addObject(new GameObject(obj.getId(), loc), false);

		refresh(obj);

		if (original) {
			obj.setExists(false);
			obj.setLocation(null);
		}
		return object;
	}
	

	@SuppressWarnings("unused")
	public static GameObject remove(Location loc, int type) {
		GameObject oldObj = null;
		if (oldObj != null) {
			checkNullObject(oldObj);

			oldObj.setExists(false);
			oldObj.setLocation(null);
		} else {
			oldObj = loc.getGameObjectType(type);
			previousObjects.put(loc, oldObj);
		}
		if (oldObj == null) {
			return null;
		}
		RegionBuilder.removeObject(loc.getX(), loc.getY(), loc.getZ(), oldObj.getType());
		GameObject obj = new GameObject(-1, loc);
		obj.setSpawned(true);
		obj.getLocation().addObject(obj);
		refresh(obj);

		if (oldObj.isSpawned()) {
			obj.setExists(false);
			obj.setLocation(null);
		}
		return oldObj;
	}


	private static void checkNullObject(GameObject obj) {
		if (obj.getId() == -1) {
			GameObject originalObject = previousObjects.get(obj.getLocation());
			if (originalObject != null) {
				Location loc = obj.getLocation();
				RegionBuilder.addObject(new GameObject(originalObject.getId(), loc), false);
				previousObjects.remove(originalObject.getLocation());
			}
		}
	}

	private static void refresh(GameObject obj) {
		for (Player player : RegionManager.getLocalPlayers(obj.getLocation(), 48)) {
			refresh(player, obj);
		}
	}

	public static void refresh(Player player, GameObject obj) {
		if (obj.getId() != -1) {
			if (obj.getOwner() != null) {
				if (player == obj.getOwner()) { 
					player.getPacketSender().sendObject(player, obj);
					player.getPacketSender().sendObject(player, obj);
				}
			} else {
				player.getPacketSender().sendObject(player, obj);
			}
		} else {
			player.getIOSession().write(new RemoveObject(player, new GameObject(-1, obj.getLocation())));
		}
	}

	public static GameObject replaceObject(Location location, int newId) {
		remove(location, 10);
		return add(newId, location, 10, 0);
	}

	public static void replaceObject(final int x, final int y, final int height, final int newId) {
		Location loc = Location.locate(x, y, height);
		remove(loc, 10);
		add(newId, loc, 10, 0);
	}

	public static void replaceObjectTemporarily(Location location, int newId, int delay) {
		replaceObjectTemporarily(location.getX(), location.getY(), location.getZ(), newId, delay);
	}

	public static void replaceObjectTemporarily(final int x, final int y, final int height, final int newId, final int delay) {
		final Location loc = Location.locate(x, y, height);
		final GameObject objectRemoved = remove(loc, 10);
		if (objectRemoved != null) {
			final int oldId = objectRemoved.getId();
			add(newId, loc, 10, objectRemoved.getRotation());
			World.getWorld().submit(new Tick(delay) {

				@Override
				public boolean run() {
					stop();
					remove(loc, 10);
					add(oldId, loc, 10, objectRemoved.getRotation());
					return true;
				}
			});
		}
	}

	public static void load() {
		add(34382, Location.locate(2338, 3686, 0), 10, 2); // stalls
		add(34383, Location.locate(2341, 3686, 0), 10, 2); // for
		add(34384, Location.locate(2344, 3686, 0), 10, 2); // the
		add(34385, Location.locate(2338, 3693, 0), 10, 0); // home
		add(34386, Location.locate(2341, 3693, 0), 10, 0); // we
		add(34387, Location.locate(2344, 3693, 0), 10, 0); // use

		add(12003, Location.locate(2321, 3696, 0), 22, 0); // fairy ring
		
		add(2352, Location.locate(3568, 9677, 0), 10, 0); //climbing rope @ barrows
		add(2352, Location.locate(3568, 9711, 0), 10, 0); //climbing rope @ barrows
		add(2352, Location.locate(3568, 9694, 0), 10, 0);  //climbing rope @ barrows
		add(2352, Location.locate(2352, 9711, 0), 10, 0);  //climbing rope @ barrows

		add(409, Location.locate(2352, 3700, 0), 10, 4);// altar at home
		add(6552, Location.locate(2331, 3701, 0), 10, 4);// ancient altar at home
		
		buildFairyRing(Location.locate(2718, 3499, 0));
		buildFairyRing(Location.locate(1658, 5257, 0));
		buildFairyRing(Location.locate(2969, 3406, 0));
		buildFairyRing(Location.locate(3237, 9524, 2));
	}

	private static void buildFairyRing(Location locate) {
		int[][] info = {
				{12129, -1, 0, 2}, {12129, 0, 1, 3}, {12129, 1, 0, 0}, {12129, 0, -1, 1}, 
				{12130, -1, 1, 2}, {12130, 1, 1, 3}, {12130, 1, -1, 0}, {12130, -1, -1, 1}
		};
		add(12003, locate, 22, 0);
		for (int[] i : info) {
			add(i[0], locate.transform(i[1], i[2], 0), 22, i[3]);
		}

	}
}
