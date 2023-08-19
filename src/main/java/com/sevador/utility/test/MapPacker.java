package com.sevador.utility.test;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.sevador.utility.MapData;

/**
 * Packs the mapdata viewing files.
 * @author Emperor
 *
 */
public class MapPacker {
	
	/**
	 * The main method.
	 * @param arg The arguments on runtime.
	 * @throws Throwable When an exception occurs.
	 */
	public static void main(String...arg) throws Throwable {
		MapData.init();
		int count = 0;
		for (int xtea : MapData.getMapData().keySet()) {
			int regionX = xtea >> 8;
			int regionY = xtea & 0xFF;
			count++;
			if (count % 20 == 0) {
				System.out.println("Finished packing " + count + " regions.");
			}
			try {
				File newFile = new File("./data/mapdata/viewer p/m" + regionX + "_" + regionY + ".dat");
				if (newFile.exists()) {
					continue;
				}
				File f = new File("./data/mapdata/viewer/m" + regionX + "_" + regionY + ".dat");
				if (!f.exists()) {
					continue;
				}
				DataInputStream in = new DataInputStream(new FileInputStream(f));
				int sectX = in.readShort();
				int sectY = in.readShort();
				boolean loadedLandscapes = false;
				int x = -1;
				int y = -1;
				Position[][][] map = new Position[4][200][200];
				while (in.available() > 0) {
					if (loadedLandscapes) {
						//Load the object clipping flags & types.
						int type = in.readByte();
						int localx = in.readByte();
						int localy = in.readByte();
						int z = in.readByte();
						if (localx > x) {
							x = localx;
						}
						if (localy > y) {
							y = localy;
						}
						if (localx < 0 || localy < 0) {
							in.readInt();
							continue;
						}
						if (map[z][localx][localy] == null) {
							map[z][localx][localy] = new Position();
						}
						map[z][localx][localy].tile = false;
						map[z][localx][localy].type = type;
						map[z][localx][localy].flag |= in.readInt();
						continue;
					}
					//Load the landscapes.
					int localx = in.readByte();
					if (localx == -5) {
						loadedLandscapes = true;
						continue;
					}
					int localy = in.readByte();
					if (localx > x) {
						x = localx;
					}
					if (localy > y) {
						y = localy;
					}
					int z = in.readByte() % 4;
					if (localx < 0 || localy < 0) {
						in.readInt();
						continue;
					}
					if (map[z][localx][localy] == null) {
						map[z][localx][localy] = new Position();
					}
					map[z][localx][localy].flag |= in.readInt();
				}
				if (x != -1 && y != -1) {
					DataOutputStream out = new DataOutputStream(new FileOutputStream(newFile));
					for (int z = 0; z < 4; z++) {
						for (int i = 0; i < x; i++) {
							for (int j = 0; j < y; j++) {
								if (map[z][i][j] == null) {
									continue;
								}
								out.writeInt(getHash(z, i + sectX, j + sectY));
								out.writeInt(getColorCode(map[z][i][j]).getRGB());
							}
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Gets the hash.
	 * @param z The z-coordinate.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @return The hash.
	 */
	private static int getHash(int z, int x, int y) {
		return x << 14 | y & 0x3fff | z << 28;
	}
	
	private static Color getColorCode(Position p) {
		if (p.type == 22 || p.tile) {
			if (!p.tile && p.flag != 0) {
				return Color.BLUE;
			}
			return Color.CYAN;
		} else if (p.type >= 0 && p.type <= 3) {
			return Color.GRAY;
		} else if (p.type >= 9 && p.type <= 11) {
			Color c = Color.GREEN;
			if ((p.flag & 0x20000) != 0) {
				c = Color.RED;
			}
			if ((p.flag & 0x40000000) != 0) {
				if (c == Color.RED) {
					return Color.pink;
				} else
					return Color.ORANGE;
			}
		}
		return Color.GREEN;
	}

	private static class Position {
		int flag;
		boolean tile = true;
		int type;
	}
}