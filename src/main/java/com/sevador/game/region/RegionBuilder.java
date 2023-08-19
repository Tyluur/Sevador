package com.sevador.game.region;

import net.burtleburtle.cache.format.ObjectDefinition;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;

/**
 * A region constructing aid class.
 * 
 * @author Emperor
 * @author Dementhium development team.
 *
 */
public class RegionBuilder {

	/**
	 * Adds an object on the map (clipping & instance on location).
	 * @param object The object to add.
	 * @param ignoreObjects If the object should be ignored.
	 * @return The old game object.
	 */
	public static GameObject addObject(GameObject object, boolean ignoreObjects) {
		ObjectDefinition def = object.getDefinition();
		if (def == null) {
			return null;
		}
		int xLength;
		int yLength;
		if (object.getRotation() == 1 || object.getRotation() == 3) {
			xLength = def.getSizeX();
			yLength = def.getSizeY();
		} else {
			xLength = def.getSizeY();
			yLength = def.getSizeX();
		}
		if (object.getType() == 22) {
			if (def.getActionCount() == 1) {
				addClipping(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), 0x200000);
			}
		} else if (object.getType() >= 9 && object.getType() <= 11) {
			if (def.getActionCount() != 0) {
				addClippingForSolidObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), xLength, yLength, def.isSolid(), !def.isClippingFlag());
			}
		} else if (object.getType() >= 0 && object.getType() <= 3) {
			if (def.getActionCount() != 0) {
				addClippingForVariableObject(object.getLocation().getX(), object.getLocation().getY(), 
						object.getLocation().getZ(), object.getType(), object.getRotation(), def.isSolid(), !def.isClippingFlag());
			}
		}
		if (!ignoreObjects) {
			removeObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType());
		}
		addGameObject(object);
		return object;
	}

	/**
	 * Adds a game object.
	 * @param object The game object.
	 */
	private static void addGameObject(GameObject object) {
		GameObject oldObject = object.getLocation().getGameObjectType(object.getType());
		if(oldObject != null && !excluded(object.getId())) {
			return;
		}
		object.getLocation().addObject(object);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static boolean excluded(int id) {
		switch(id) {
		case 36523:
		case 36532:
		case 36521:
		case 36495:
		case 36484:
		case 36480:
		case 36586:
		case 36540:
		case 36579:
		case 36481: //These are CW objects that are excluded because there's an object in their position already
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param type
	 * @return
	 */
	public static GameObject removeObject(int x, int y, int height, int type) {
		Location loc = Location.locate(x, y, height);
		GameObject oldObj = loc.getGameObjectType(type);
		loc.removeObject(oldObj);
		if (oldObj != null) {
			ObjectDefinition def = ObjectDefinition.forId(oldObj.getId());
			int xLength;
			int yLength;
			if (oldObj.getRotation() != 1 && oldObj.getRotation() != 3) {
				xLength = def.getSizeX();
				yLength = def.getSizeY();
			} else {
				xLength = def.getSizeY();
				yLength = def.getSizeX();
			}
			if (oldObj.getType() == 22) {
				if (def.getActionCount() == 1) {
					removeClipping(x, y, height, 0x200000);
				}
			} else if (oldObj.getType() >= 9 && oldObj.getType() <= 11) {
				if (def.getActionCount() != 0) {
					removeClippingForSolidObject(x, y, height, xLength, yLength, def.isSolid(), !def.isClippingFlag());
				}
			} else if (oldObj.getType() >= 0 && oldObj.getType() <= 3) {
				if (def.getActionCount() != 0) {
					removeClippingForVariableObject(x, y, height, oldObj.getType(), oldObj.getRotation(), def.isSolid(), !def.isClippingFlag());
				}
			}
		}
		return oldObj;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param shift
	 */
	public static void addClipping(int x, int y, int z, int shift) {
		Region region = RegionManager.forCoords(x, y);
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		if (region.getClippingMasks()[z] == null) {
			region.getClippingMasks()[z] = new int[region.getSize()][region.getSize()];
		}
		region.setClipped(true);
		region.getClippingMasks()[z][localX][localY] |= shift;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param shift
	 */
	public static void removeClipping(int x, int y, int z, int shift) {
		Region region = RegionManager.forCoords(x, y);
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		if (region.getClippingMasks()[z] == null) {
			region.getClippingMasks()[z] = new int[region.getSize()][region.getSize()];
		}
		region.getClippingMasks()[z][localX][localY] &= ~shift;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param xLength
	 * @param yLength
	 * @param flag
	 * @param flag2
	 */
	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag, boolean flag2) {
		int clipping = 256;
		if(flag) {
			clipping |= 0x20000;
		}
		if(flag2) {
			clipping |= 0x40000000;
		}
		for(int i = x; i < x + xLength; i++) {
			for(int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param height
	 * @param xLength
	 * @param yLength
	 * @param flag
	 * @param flag2
	 */
	private static void removeClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag, boolean flag2) {
		int clipping = 256;
		if(flag) {
			clipping |= 0x20000;
		}
		if(flag2) {
			clipping |= 0x40000000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				removeClipping(i, i2, height, clipping);
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param type
	 * @param direction
	 * @param flag
	 * @param flag2
	 */
	private static void addClippingForVariableObject(int x, int y, int z, int type, int direction, boolean flag, boolean flag2) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, z, 128);
				addClipping(x - 1, y, z, 8);
			} else if (direction == 1) {
				addClipping(x, y, z, 2);
				addClipping(x, y + 1, z, 32);
			} else if (direction == 2) {
				addClipping(x, y, z, 8);
				addClipping(x + 1, y, z, 128);
			} else if (direction == 3) {
				addClipping(x, y, z, 32);
				addClipping(x, y - 1, z, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, z, 1);
				addClipping(x - 1, y + 1, z, 16);
			} else if (direction == 1) {
				addClipping(x, y, z, 4);
				addClipping(x + 1, y + 1, z, 64);
			} else if (direction == 2) {
				addClipping(x, y, z, 16);
				addClipping(x + 1, y - 1, z, 1);
			} else if (direction == 3) {
				addClipping(x, y, z, 64);
				addClipping(x - 1, y - 1, z, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, z, 130);
				addClipping(x - 1, y, z, 8);
				addClipping(x, y + 1, z, 32);
			} else if (direction == 1) {
				addClipping(x, y, z, 10);
				addClipping(x, y + 1, z, 32);
				addClipping(x + 1, y, z, 128);
			} else if (direction == 2) {
				addClipping(x, y, z, 40);
				addClipping(x + 1, y, z, 128);
				addClipping(x, y - 1, z, 2);
			} else if (direction == 3) {
				addClipping(x, y, z, 160);
				addClipping(x, y - 1, z, 2);
				addClipping(x - 1, y, z, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, z, 0x10000);
					addClipping(x - 1, y, z, 4096);
				} else if (direction == 1) {
					addClipping(x, y, z, 1024);
					addClipping(x, y + 1, z, 16384);
				} else if (direction == 2) {
					addClipping(x, y, z, 4096);
					addClipping(x + 1, y, z, 0x10000);
				} else if (direction == 3) {
					addClipping(x, y, z, 16384);
					addClipping(x, y - 1, z, 1024);
				}
			} else if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, z, 512);
					addClipping(x - 1, y + 1, z, 8192);
				} else if (direction == 1) {
					addClipping(x, y, z, 2048);
					addClipping(x + 1, y + 1, z, 32768);
				} else if (direction == 2) {
					addClipping(x, y, z, 8192);
					addClipping(x + 1, y - 1, z, 512);
				} else if (direction == 3) {
					addClipping(x, y, z, 32768);
					addClipping(x - 1, y - 1, z, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, z, 0x10400);
					addClipping(x - 1, y, z, 4096);
					addClipping(x, y + 1, z, 16384);
				} else if (direction == 1) {
					addClipping(x, y, z, 5120);
					addClipping(x, y + 1, z, 16384);
					addClipping(x + 1, y, z, 0x10000);
				} else if (direction == 2) {
					addClipping(x, y, z, 20480);
					addClipping(x + 1, y, z, 0x10000);
					addClipping(x, y - 1, z, 1024);
				} else if (direction == 3) {
					addClipping(x, y, z, 0x14000);
					addClipping(x, y - 1, z, 1024);
					addClipping(x - 1, y, z, 4096);
				}
			}
		}
		if (flag2) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, z, 0x20000000);
					addClipping(x - 1, y, z, 0x2000000);
				} else if (direction == 1) {
					addClipping(x, y, z, 0x800000);
					addClipping(x, y + 1, z, 0x8000000);
				} else if (direction == 2) {
					addClipping(x, y, z, 0x2000000);
					addClipping(x + 1, y, z, 0x20000000);
				} else if (direction == 3) {
					addClipping(x, y, z, 0x8000000);
					addClipping(x, y - 1, z, 0x800000);
				}
			} else if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, z, 0x400000);
					addClipping(x - 1, y + 1, z, 0x4000000);
				} else if (direction == 1) {
					addClipping(x, y, z, 0x1000000);
					addClipping(1 + x, 1 + y, z, 0x10000000);
				} else if (direction == 2) {
					addClipping(x, y, z, 0x4000000);
					addClipping(x + 1, -1 + y, z, 0x400000);
				} else if (direction == 3) {
					addClipping(x, y, z, 0x10000000);
					addClipping(-1 + x, y - 1, z, 0x1000000);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, z, 0x20800000);
					addClipping(-1 + x, y, z, 0x2000000);
					addClipping(x, 1 + y, z, 0x8000000);
				} else if (direction == 1) {
					addClipping(x, y, z, 0x2800000);
					addClipping(x, 1 + y, z, 0x8000000);
					addClipping(x + 1, y, z, 0x20000000);
				} else if (direction == 2) {
					addClipping(x, y, z, 0xa000000);
					addClipping(1 + x, y, z, 0x20000000);
					addClipping(x, y - 1, z, 0x800000);
				} else if (direction == 3) {
					addClipping(x, y, z, 0x28000000);
					addClipping(x, y - 1, z, 0x800000);
					addClipping(-1 + x, y, z, 0x2000000);
				}
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param type
	 * @param direction
	 * @param flag
	 * @param flag2
	 */
	public static void removeClippingForVariableObject(int x, int y, int z, int type, int direction, boolean flag, boolean flag2) {
		if (type == 0) {
			if (direction == 0) {
				removeClipping(x, y, z, 128);
				removeClipping(x - 1, y, z, 8);
			}
			if (direction == 1) {
				removeClipping(x, y, z, 2);
				removeClipping(x, 1 + y, z, 32);
			}
			if (direction == 2) {
				removeClipping(x, y, z, 8);
				removeClipping(1 + x, y, z, 128);
			}
			if (direction == 3) {
				removeClipping(x, y, z, 32);
				removeClipping(x, y - 1, z, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (direction == 0) {
				removeClipping(x, y, z, 1);
				removeClipping(x - 1, 1 + y, z, 16);
			}
			if (direction == 1) {
				removeClipping(x, y, z, 4);
				removeClipping(1 + x, y + 1, z, 64);
			}
			if (direction == 2) {
				removeClipping(x, y, z, 16);
				removeClipping(x + 1, -1 + y, z, 1);
			}
			if (direction == 3) {
				removeClipping(x, y, z, 64);
				removeClipping(-1 + x, -1 + y, z, 4);
			}
		}
		if (type == 2) {
			if (direction == 0) {
				removeClipping(x, y, z, 130);
				removeClipping(x - 1, y, z, 8);
				removeClipping(x, 1 + y, z, 32);
			}
			if (direction == 1) {
				removeClipping(x, y, z, 10);
				removeClipping(x, 1 + y, z, 32);
				removeClipping(1 + x, y, z, 128);
			}
			if (direction == 2) {
				removeClipping(x, y, z, 40);
				removeClipping(x + 1, y, z, 128);
				removeClipping(x, -1 + y, z, 2);
			}
			if (direction == 3) {
				removeClipping(x, y, z, 160);
				removeClipping(x, y - 1, z, 2);
				removeClipping(-1 + x, y, z, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x10000);
					removeClipping(-1 + x, y, z, 4096);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 1024);
					removeClipping(x, 1 + y, z, 16384);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 4096);
					removeClipping(x + 1, y, z, 0x10000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 16384);
					removeClipping(x, y - 1, z, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					removeClipping(x, y, z, 512);
					removeClipping(-1 + x, 1 + y, z, 8192);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 2048);
					removeClipping(1 + x, 1 + y, z, 32768);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 8192);
					removeClipping(x + 1, -1 + y, z, 512);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 32768);
					removeClipping(x - 1, -1 + y, z, 2048);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x10400);
					removeClipping(-1 + x, y, z, 4096);
					removeClipping(x, y + 1, z, 16384);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 5120);
					removeClipping(x, 1 + y, z, 16384);
					removeClipping(x + 1, y, z, 0x10000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 20480);
					removeClipping(1 + x, y, z, 0x10000);
					removeClipping(x, -1 + y, z, 1024);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x14000);
					removeClipping(x, -1 + y, z, 1024);
					removeClipping(-1 + x, y, z, 4096);
				}
			}
		}
		if (flag2) {
			if (type == 0) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x20000000);
					removeClipping(-1 + x, y, z, 0x2000000);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 0x800000);
					removeClipping(x, 1 + y, z, 0x8000000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 0x2000000);
					removeClipping(x + 1, y, z, 0x20000000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x8000000);
					removeClipping(x, -1 + y, z, 0x800000);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x400000);
					removeClipping(x - 1, y + 1, z, 0x4000000);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 0x1000000);
					removeClipping(1 + x, 1 + y, z, 0x10000000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 0x4000000);
					removeClipping(x + 1, -1 + y, z, 0x400000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x10000000);
					removeClipping(-1 + x, y - 1, z, 0x1000000);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x20800000);
					removeClipping(-1 + x, y, z, 0x2000000);
					removeClipping(x, y + 1, z, 0x8000000);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 0x2800000);
					removeClipping(x, y + 1, z, 0x8000000);
					removeClipping(1 + x, y, z, 0x20000000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 0xa000000);
					removeClipping(x + 1, y, z, 0x20000000);
					removeClipping(x, y - 1, z, 0x800000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x28000000);
					removeClipping(x, -1 + y, z, 0x800000);
					removeClipping(x - 1, y, z, 0x2000000);
				}
			}
		}
	}
}