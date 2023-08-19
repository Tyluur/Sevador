package net.burtleburtle.cache.format;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.burtleburtle.cache.CacheContainer;
import net.burtleburtle.cache.CacheManager;
import net.burtleburtle.cache.stream.ByteInputStream;
import net.burtleburtle.cache.stream.RSInputStream;

import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.region.RegionBuilder;
import com.sevador.utility.XTEA;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class LandscapeParser {

	private static Set<Integer> loaded = new HashSet<Integer>();
	private static Set<Integer> broken = new HashSet<Integer>();

	public static boolean parseLandscape(final int area, final int[] keys) {
		if (broken.contains(area)) {
			return false;
		}
		if (loaded.contains(area)) {
			return true;
		}
		loaded.add(area);
		int regionX = area >> 8;
		int regionY = area & 0xFF;
		/*
		 * File f = new File("./data/mapdata/viewer/m" + regionX + "_" + regionY
		 * + ".dat"); if (f.exists()) { return true; }
		 */
		try {
			byte[] landscapeMap = CacheManager.getByName(5, "m" + regionX + "_"
					+ regionY);
			byte[] objectMap = CacheManager.getByName(5, "l" + regionX + "_"
					+ regionY);
			if (landscapeMap == null && objectMap == null) {
				// System.out.println("Map [" + (regionX << 6) + ", " + (regionY
				// << 6) + "] was not found in the cache!");
				return true;
			}
			RSInputStream str2 = null;
			ByteInputStream str1 = null;
			if (landscapeMap != null) {
				str2 = new RSInputStream(new ByteArrayInputStream(
						new CacheContainer(landscapeMap).decompress()));
			}
			if (objectMap != null) {
				if (keys != null) {
					objectMap = XTEA.decrypt(keys, objectMap, 5,
							objectMap.length);
				}
				str1 = new ByteInputStream(
						new CacheContainer(objectMap).decompress());
			}
			// DataOutputStream out = new DataOutputStream(new
			// FileOutputStream(f));
			int x = regionX << 6;
			int y = regionY << 6;
			byte[][][] landscapeData = new byte[4][64][64];
			/*
			 * out.writeShort(x); out.writeShort(y);
			 */
			if (str2 != null) {
				for (int z = 0; z < 4; z++) {
					for (int localX = 0; localX < 64; localX++) {
						for (int localY = 0; localY < 64; localY++) {
							while (true) {
								int v = str2.readByte() & 0xff;
								if (v == 0) {
									break;
								} else if (v == 1) {
									str2.readByte();
									break;
								} else if (v <= 49) {
									str2.readByte();
								} else if (v <= 81) {
									landscapeData[z][localX][localY] = (byte) (v - 49);
								}
							}
						}
					}
				}
				for (int z = 0; z < 4; z++) {
					for (int localX = 0; localX < 64; localX++) {
						for (int localY = 0; localY < 64; localY++) {
							if ((landscapeData[z][localX][localY] & 1) == 1) {
								int height = z;
								if ((landscapeData[1][localX][localY] & 2) == 2) {
									height--;
								}
								if (height >= 0 && height <= 3) {
									/*
									 * out.writeByte(localX);
									 * out.writeByte(localY);
									 * out.writeByte(height);
									 * out.writeInt(0x200000);
									 */
									RegionBuilder.addClipping(x + localX, y
											+ localY, height, 0x200000);
								}
							}
						}
					}
				}
			}
			str2.close();
			// out.writeByte(-5);//End of landscape parsing.
			if (str1 != null) {
				int objectId = -1;
				int incr;
				while ((incr = str1.readSmart2()) != 0) {
					objectId += incr;
					int location = 0;
					int incr2;
					while ((incr2 = str1.readSmart()) != 0) {
						location += incr2 - 1;
						int localX = location >> 6 & 0x3f;
						int localY = location & 0x3f;
						int height = location >> 12;
						int objectData = str1.readUByte();
						int type = objectData >> 2;
						int rotation = objectData & 0x3;
						if (localX < 0 || localX >= 64 || localY < 0
								|| localY >= 64) {
							continue;
						}
						if ((landscapeData[1][localX][localY] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							RegionBuilder.addObject(new GameObject(objectId, x
									+ localX, y + localY, height, type,
									rotation), true);
							// addObject(out, objectId, localX, localY, height,
							// type, rotation);
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while loading region " + area + ", "
					+ e.getMessage() + ", " + e.getCause() + ", "
					+ e.toString());
			broken.add(area);
			return false;
		}
	}

	@SuppressWarnings("unused")
	private static void addObject(DataOutputStream out, int objectId,
			int localx, int localy, int height, int type, int rotation) {
		ObjectDefinition def = ObjectDefinition.forId(objectId);
		if (def == null) {
			return;
		}
		int xLength;
		int yLength;
		if (rotation == 1 || rotation == 3) {
			xLength = def.getSizeX();
			yLength = def.getSizeY();
		} else {
			xLength = def.getSizeY();
			yLength = def.getSizeX();
		}
		if (type == 22) {
			if (def.getActionCount() == 1) {
				addClipping(out, 22, localx, localy, height, 0x200000);
			}
		} else if (type >= 9 && type <= 11) {
			if (def.getActionCount() != 0) {
				for (int x = localx; x < localx + xLength; x++) {
					for (int y = localy; y < localy + yLength; y++) {
						int hash = 256;
						if (def.isSolid()) {
							hash |= 0x20000;
						}
						if (!def.isClippingFlag()) {
							hash |= 0x40000000;
						}
						addClipping(out, type, x, y, height, hash);
					}
				}
			}
		} else if (type >= 0 && type <= 3) {
			int x = localx;
			int y = localy;
			int z = height;
			if (def.getActionCount() != 0) {
				if (rotation == 0) {
					addClipping(out, type, x, y, z, 128);
					addClipping(out, type, x - 1, y, z, 8);
				} else if (rotation == 1) {
					addClipping(out, type, x, y, z, 2);
					addClipping(out, type, x, y + 1, z, 32);
				} else if (rotation == 2) {
					addClipping(out, type, x, y, z, 8);
					addClipping(out, type, x + 1, y, z, 128);
				} else if (rotation == 3) {
					addClipping(out, type, x, y, z, 32);
					addClipping(out, type, x, y - 1, z, 2);
				}
			} else if (type == 1 || type == 3) {
				if (rotation == 0) {
					addClipping(out, type, x, y, z, 1);
					addClipping(out, type, x - 1, y + 1, z, 16);
				} else if (rotation == 1) {
					addClipping(out, type, x, y, z, 4);
					addClipping(out, type, x + 1, y + 1, z, 64);
				} else if (rotation == 2) {
					addClipping(out, type, x, y, z, 16);
					addClipping(out, type, x + 1, y - 1, z, 1);
				} else if (rotation == 3) {
					addClipping(out, type, x, y, z, 64);
					addClipping(out, type, x - 1, y - 1, z, 4);
				}
			} else if (type == 2) {
				if (rotation == 0) {
					addClipping(out, type, x, y, z, 130);
					addClipping(out, type, x - 1, y, z, 8);
					addClipping(out, type, x, y + 1, z, 32);
				} else if (rotation == 1) {
					addClipping(out, type, x, y, z, 10);
					addClipping(out, type, x, y + 1, z, 32);
					addClipping(out, type, x + 1, y, z, 128);
				} else if (rotation == 2) {
					addClipping(out, type, x, y, z, 40);
					addClipping(out, type, x + 1, y, z, 128);
					addClipping(out, type, x, y - 1, z, 2);
				} else if (rotation == 3) {
					addClipping(out, type, x, y, z, 160);
					addClipping(out, type, x, y - 1, z, 2);
					addClipping(out, type, x - 1, y, z, 8);
				}
			}
			if (def.isSolid()) {
				if (type == 0) {
					if (rotation == 0) {
						addClipping(out, type, x, y, z, 0x10000);
						addClipping(out, type, x - 1, y, z, 4096);
					} else if (rotation == 1) {
						addClipping(out, type, x, y, z, 1024);
						addClipping(out, type, x, y + 1, z, 16384);
					} else if (rotation == 2) {
						addClipping(out, type, x, y, z, 4096);
						addClipping(out, type, x + 1, y, z, 0x10000);
					} else if (rotation == 3) {
						addClipping(out, type, x, y, z, 16384);
						addClipping(out, type, x, y - 1, z, 1024);
					}
				} else if (type == 1 || type == 3) {
					if (rotation == 0) {
						addClipping(out, type, x, y, z, 512);
						addClipping(out, type, x - 1, y + 1, z, 8192);
					} else if (rotation == 1) {
						addClipping(out, type, x, y, z, 2048);
						addClipping(out, type, x + 1, y + 1, z, 32768);
					} else if (rotation == 2) {
						addClipping(out, type, x, y, z, 8192);
						addClipping(out, type, x + 1, y - 1, z, 512);
					} else if (rotation == 3) {
						addClipping(out, type, x, y, z, 32768);
						addClipping(out, type, x - 1, y - 1, z, 2048);
					}
				} else if (type == 2) {
					if (rotation == 0) {
						addClipping(out, type, x, y, z, 0x10400);
						addClipping(out, type, x - 1, y, z, 4096);
						addClipping(out, type, x, y + 1, z, 16384);
					} else if (rotation == 1) {
						addClipping(out, type, x, y, z, 5120);
						addClipping(out, type, x, y + 1, z, 16384);
						addClipping(out, type, x + 1, y, z, 0x10000);
					} else if (rotation == 2) {
						addClipping(out, type, x, y, z, 20480);
						addClipping(out, type, x + 1, y, z, 0x10000);
						addClipping(out, type, x, y - 1, z, 1024);
					} else if (rotation == 3) {
						addClipping(out, type, x, y, z, 0x14000);
						addClipping(out, type, x, y - 1, z, 1024);
						addClipping(out, type, x - 1, y, z, 4096);
					}
				}
			}
			if (!def.isClippingFlag()) {
				if (type == 0) {
					if (rotation == 0) {
						addClipping(out, type, x, y, z, 0x20000000);
						addClipping(out, type, x - 1, y, z, 0x2000000);
					} else if (rotation == 1) {
						addClipping(out, type, x, y, z, 0x800000);
						addClipping(out, type, x, y + 1, z, 0x8000000);
					} else if (rotation == 2) {
						addClipping(out, type, x, y, z, 0x2000000);
						addClipping(out, type, x + 1, y, z, 0x20000000);
					} else if (rotation == 3) {
						addClipping(out, type, x, y, z, 0x8000000);
						addClipping(out, type, x, y - 1, z, 0x800000);
					}
				} else if (type == 1 || type == 3) {
					if (rotation == 0) {
						addClipping(out, type, x, y, z, 0x400000);
						addClipping(out, type, x - 1, y + 1, z, 0x4000000);
					} else if (rotation == 1) {
						addClipping(out, type, x, y, z, 0x1000000);
						addClipping(out, type, 1 + x, 1 + y, z, 0x10000000);
					} else if (rotation == 2) {
						addClipping(out, type, x, y, z, 0x4000000);
						addClipping(out, type, x + 1, -1 + y, z, 0x400000);
					} else if (rotation == 3) {
						addClipping(out, type, x, y, z, 0x10000000);
						addClipping(out, type, -1 + x, y - 1, z, 0x1000000);
					}
				} else if (type == 2) {
					if (rotation == 0) {
						addClipping(out, type, x, y, z, 0x20800000);
						addClipping(out, type, -1 + x, y, z, 0x2000000);
						addClipping(out, type, x, 1 + y, z, 0x8000000);
					} else if (rotation == 1) {
						addClipping(out, type, x, y, z, 0x2800000);
						addClipping(out, type, x, 1 + y, z, 0x8000000);
						addClipping(out, type, x + 1, y, z, 0x20000000);
					} else if (rotation == 2) {
						addClipping(out, type, x, y, z, 0xa000000);
						addClipping(out, type, 1 + x, y, z, 0x20000000);
						addClipping(out, type, x, y - 1, z, 0x800000);
					} else if (rotation == 3) {
						addClipping(out, type, x, y, z, 0x28000000);
						addClipping(out, type, x, y - 1, z, 0x800000);
						addClipping(out, type, -1 + x, y, z, 0x2000000);
					}
				}
			}
		}
	}

	public static void addClipping(DataOutputStream out, int type, int x,
			int y, int z, int shift) {
		try {
			out.writeByte(type);
			out.writeByte(x);
			out.writeByte(y);
			out.writeByte(z);
			out.writeInt(shift);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
