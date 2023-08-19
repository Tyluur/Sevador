/*package com.sevador.game.region.path;

import com.sevador.game.node.model.Directions.NormalDirection;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;

*//**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *//*
public class SizedPathFinder implements PathFinder {

	private int[] queueX = new int[4096];
	private int[] queueY = new int[4096];
	private int[][] via = new int[104][104];
	private int[][] cost = new int[104][104];
	private int writePathPosition = 0;

	public boolean checkNPCs, checkPlayers;

	public SizedPathFinder() {
		this(false);
	}

	public SizedPathFinder(boolean checkNPCs) {
		this(checkNPCs, false);
	}

	public SizedPathFinder(boolean checkNPCs, boolean checkPlayers) {
		this.checkNPCs = checkNPCs;
		this.checkPlayers = checkPlayers;
	}

	public void check(Entity mob, int x, int y, int viaDir, int thisCost) {
		queueX[writePathPosition] = x;
		queueY[writePathPosition] = y;
		via[x][y] = viaDir;
		cost[x][y] = thisCost;
		writePathPosition = writePathPosition + 1 & 0xfff;
	}

	@Override
	public PathState findPath(Entity mob, Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear) {
		PathState state = new PathState();
		writePathPosition = 0;
		if (srcX < 0 || srcY < 0 || srcX >= 104 || srcY >= 104 || dstX < 0 || dstY < 0 || dstX >= 104 || dstY >= 104) {
			state.routeFailed();
			return state;
		}
		if (srcX == dstX && srcY == dstY) {
			return state;
		}
		Location location = Location.locate((base.getRegionX() - 6) << 3, (base.getRegionY() - 6) << 3, base.getZ());

		boolean foundPath = false;
		int size = mob.size();
		int flags = 0;
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				via[xx][yy] = 0;
				cost[xx][yy] = 99999999;
			}
		}
		if (checkNPCs)
			flags |= 0x1;
		if (checkPlayers)
			flags |= 0x2;

		int curX = srcX;
		int curY = srcY;
		int attempts = 0;
		int readPosition = 0;
		check(mob, curX, curY, 99, 0);

		while (writePathPosition != readPosition) {
			curX = queueX[readPosition];
			curY = queueY[readPosition];
			readPosition = readPosition + 1 & 0xfff;
			if (curX == dstX && curY == dstY) {
				foundPath = true;
				break;
			}
			int absX = location.getX() + curX, absY = location.getY() + curY;
			int thisCost = cost[curX][curY] + 1;
			if (curX > size - 1) {
				if (via[curX - 1][curY] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.WEST, size, flags)) {
					check(mob, curX - 1, curY, WEST_FLAG, thisCost);
				}
			}
			if (curX < 104 - size) {
				if (via[curX + 1][curY] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.EAST, size, flags)) {
					check(mob, curX + 1, curY, EAST_FLAG, thisCost);
				}
			}
			if (curY > size - 1) {
				if (via[curX][curY - 1] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.SOUTH, size, flags)) {
					check(mob, curX, curY - 1, SOUTH_FLAG, thisCost);
				}
			}
			if (curY < 104 - size) {
				if (via[curX][curY + 1] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.NORTH, size, flags)) {
					check(mob, curX, curY + 1, NORTH_FLAG, thisCost);
				}
			}
			if (curX > size - 1 && curY > size - 1) {
				if (via[curX - 1][curY - 1] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.SOUTH_WEST, size, flags)) {
					check(mob, curX - 1, curY - 1, SOUTH_WEST_FLAG, thisCost);
				}
			}
			if (curX < 104 - size && curY > size - 1) {
				if (via[curX + 1][curY - 1] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.SOUTH_EAST, size, flags)) {
					check(mob, curX + 1, curY - 1, SOUTH_EAST_FLAG, thisCost);
				}
			}
			if (curX > size - 1 && curY < 104 - size) {
				if (via[curX - 1][curY + 1] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.NORTH_WEST, size, flags)) {
					check(mob, curX - 1, curY + 1, NORTH_WEST_FLAG, thisCost);
				}
			}
			if (curX < 104 - size && curY < 104 - size) {
				if (via[curX + 1][curY + 1] == 0 && PrimitivePathFinder.canMove(absX, absY, z, NormalDirection.NORTH_EAST, size, flags)) {
					check(mob, curX + 1, curY + 1, NORTH_EAST_FLAG, thisCost);
				}
			}
		}
		if (!foundPath) {
			state.routeFailed();
			if (moveNear) {
				int fullCost = 1000;
				int thisCost = 100;
				int depth = 10;
				int xLength = mob.size();
				int yLength = mob.size();
				for (int x = dstX - depth; x <= dstX + depth; x++) {
					for (int y = dstY - depth; y <= dstY + depth; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
							int diffX = 0;
							if (x < dstX)
								diffX = dstX - x;
							else if (x > dstX + xLength - 1)
								diffX = x - (dstX + xLength - 1);
							int diffY = 0;
							if (y < dstY)
								diffY = dstY - y;
							else if (y > dstY + yLength - 1)
								diffY = y - (dstY + yLength - 1);
							int totalCost = diffX * diffX + diffY * diffY;
							if (totalCost < fullCost || (totalCost == fullCost && (cost[x][y] < thisCost))) {
								fullCost = totalCost;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (fullCost == 1000)
					return state;
			}
		}
		readPosition = 0;
		queueX[readPosition] = curX;
		queueY[readPosition++] = curY;
		int l5;
		attempts = 0;
		for (int j5 = l5 = via[curX][curY]; curX != srcX || curY != srcY; j5 = via[curX][curY]) {
			if (attempts++ >= queueX.length) {
				state.routeFailed();
				return state;
			}
			if (j5 != l5) {
				l5 = j5;
				queueX[readPosition] = curX;
				queueY[readPosition++] = curY;
			}
			if ((j5 & WEST_FLAG) != 0) {
				curX++;
			} else if ((j5 & EAST_FLAG) != 0) {
				curX--;
			}
			if ((j5 & SOUTH_FLAG) != 0) {
				curY++;
			} else if ((j5 & NORTH_FLAG) != 0) {
				curY--;
			}
		}
		int readSize = readPosition--;
		int absX = location.getX() + queueX[readPosition];
		int absY = location.getY() + queueY[readPosition];
		state.getPoints().add(BasicPoint.create(absX, absY, z));
		for (int i = 1; i < readSize; i++) {
			readPosition--;
			absX = location.getX() + queueX[readPosition];
			absY = location.getY() + queueY[readPosition];
			state.getPoints().add(BasicPoint.create(absX, absY, z));
		}
		return state;
	}
}
*/