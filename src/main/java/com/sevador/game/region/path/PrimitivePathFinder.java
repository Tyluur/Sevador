package com.sevador.game.region.path;

import java.awt.Point;

import com.sevador.game.node.model.Directions;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.region.RegionManager;
import com.sevador.game.region.path.PathState.Position;

public class PrimitivePathFinder implements PathFinder {

    public static PrimitivePathFinder INSTANCE = new PrimitivePathFinder();

    public static boolean canMove(Location source, Directions.WalkingDirection dir, boolean diagonalCheck) {
        return canMove(source, dir, diagonalCheck, false);
    }

    public static Point getNextStep(Location source, int toX, int toY, int height, int xLength, int yLength) {
        int baseX = source.getViewportX(0), baseY = source.getViewportY(0);
        int moveX = 0;
        int moveY = 0;
        if (baseX - toX > 0) {
            moveX--;
        } else if (baseX - toX < 0) {
            moveX++;
        }
        if (baseY - toY > 0) {
            moveY--;
        } else if (baseY - toY < 0) {
            moveY++;
        }
        if (canMove(source, baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
            return new Point(baseX + moveX, baseY + moveY);
        } else if (moveX != 0 && canMove(source, baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
            return new Point(baseX + moveX, baseY);
        } else if (moveY != 0 && canMove(source, baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
            return new Point(baseX, baseY + moveY);
        }
        return null;
    }

    public static boolean canMove(Location base, int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
        Location location = Location.locate((base.getRegionX() - 6) << 3, (base.getRegionY() - 6) << 3, base.getZ());
        int diffX = endX - startX;
        int diffY = endY - startY;
        int max = Math.max(Math.abs(diffX), Math.abs(diffY));
        for (int ii = 0; ii < max; ii++) {
            int currentX = location.getX() + (endX - diffX);
            int currentY = location.getY() + (endY - diffY);
            for (int i = 0; i < xLength; i++) {
                for (int i2 = 0; i2 < yLength; i2++) {
                    if (diffX < 0 && diffY < 0) {
                        if ((RegionManager.getClippingMask(currentX + i - 1, currentY
                                + i2 - 1, height) & 0x128010e) != 0
                                || (RegionManager.getClippingMask(currentX + i - 1,
                                currentY + i2, height) & 0x1280108) != 0
                                || (RegionManager.getClippingMask(currentX + i,
                                currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY > 0) {
                        if ((RegionManager.getClippingMask(currentX + i + 1, currentY
                                + i2 + 1, height) & 0x12801e0) != 0
                                || (RegionManager.getClippingMask(currentX + i + 1,
                                currentY + i2, height) & 0x1280180) != 0
                                || (RegionManager.getClippingMask(currentX + i,
                                currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY > 0) {
                        if ((RegionManager.getClippingMask(currentX + i - 1, currentY
                                + i2 + 1, height) & 0x1280138) != 0
                                || (RegionManager.getClippingMask(currentX + i - 1,
                                currentY + i2, height) & 0x1280108) != 0
                                || (RegionManager.getClippingMask(currentX + i,
                                currentY + i2 + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY < 0) {
                        if ((RegionManager.getClippingMask(currentX + i + 1, currentY
                                + i2 - 1, height) & 0x1280183) != 0
                                || (RegionManager.getClippingMask(currentX + i + 1,
                                currentY + i2, height) & 0x1280180) != 0
                                || (RegionManager.getClippingMask(currentX + i,
                                currentY + i2 - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    } else if (diffX > 0 && diffY == 0) {
                        if ((RegionManager.getClippingMask(currentX + i + 1, currentY
                                + i2, height) & 0x1280180) != 0) {
                            return false;
                        }
                    } else if (diffX < 0 && diffY == 0) {
                        if ((RegionManager.getClippingMask(currentX + i - 1, currentY
                                + i2, height) & 0x1280108) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY > 0) {
                        if ((RegionManager.getClippingMask(currentX + i, currentY + i2
                                + 1, height) & 0x1280120) != 0) {
                            return false;
                        }
                    } else if (diffX == 0 && diffY < 0) {
                        if ((RegionManager.getClippingMask(currentX + i, currentY + i2
                                - 1, height) & 0x1280102) != 0) {
                            return false;
                        }
                    }
                }
            }
            if (diffX < 0) {
                diffX++;
            } else if (diffX > 0) {
                diffX--;
            }
            if (diffY < 0) {
                diffY++;
            } else if (diffY > 0) {
                diffY--;
            }
        }
        return true;
    }

    public static boolean canMove(Location source, Directions.WalkingDirection dir, boolean diagonalCheck, boolean npcCheck) {
        if (dir == null) {
            return true;
        }
        // TODO
        // if(!diagonalCheck)
        // Make it so they only get stuck on the following directions: NORTH,
        // SOUTH, EAST, WEST
        int absX = source.getX();
        int absY = source.getY();
        int z = source.getZ();
        switch (dir) {
            case SOUTH:
                if ((RegionManager.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                    return true;
                }
                break;
            case WEST:
                if ((RegionManager.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0) {
                    return true;
                }
                break;
            case NORTH:
                if ((RegionManager.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                    return true;
                }
                break;
            case EAST:
                if ((RegionManager.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0) {
                    return true;
                }
                break;
            case SOUTH_WEST:
                if ((RegionManager.getClippingMask(absX - 1, absY - 1, z) & 0x43a40000) == 0 && (RegionManager.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0 && (RegionManager.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                    return true;
                }
                break;
            case NORTH_WEST:
                if ((RegionManager.getClippingMask(absX - 1, absY + 1, z) & 0x4e240000) == 0 && (RegionManager.getClippingMask(absX - 1, absY, z) & 0x42240000) == 0 && (RegionManager.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                    return true;
                }
                break;
            case SOUTH_EAST:
                if ((RegionManager.getClippingMask(absX + 1, absY - 1, z) & 0x60e40000) == 0 && (RegionManager.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0 && (RegionManager.getClippingMask(absX, absY - 1, z) & 0x40a40000) == 0) {
                    return true;
                }
                break;
            case NORTH_EAST:
                if ((RegionManager.getClippingMask(absX + 1, absY + 1, z) & 0x78240000) == 0 && (RegionManager.getClippingMask(absX + 1, absY, z) & 0x60240000) == 0 && (RegionManager.getClippingMask(absX, absY + 1, z) & 0x48240000) == 0) {
                    return true;
                }
                break;
        }
        return false;
    }

    public PathState findPath(Entity mob, Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear) {
        return findPath(mob, base, srcX, srcY, dstX, dstY, z, radius, running, ignoreLastStep, moveNear, false);
    }

    public PathState findPath(Entity mob, Location base, int srcX, int srcY, int dstX, int dstY, int z, int radius, boolean running, boolean ignoreLastStep, boolean moveNear, boolean nullOnFail) {
        if (srcX < 0 || srcY < 0 || srcX >= 104 || srcY >= 104 || dstX < 0 || dstY < 0 || srcX >= 104 || srcY >= 104) {
            return null;
        }
        if (srcX == dstX && srcY == dstY) {
            return null;
        }
        Location location = Location.locate((base.getRegionX() - 6) << 3, (base.getRegionY() - 6) << 3, base.getZ());
        PathState state = new PathState();
        int curX = srcX;
        int curY = srcY;
        final int stepCount = running ? 6 : 3;
        for (int i = 0; i < stepCount; i++) {
            Location curLoc = Location.locate(location.getX() + curX, location.getY() + curY, location.getZ());
            Directions.WalkingDirection dstDir = Directions.directionFor(curX, curY, dstX, dstY);
            if (dstDir != null && canMove(curLoc, dstDir, false)) {
                Location step = curLoc.transform(Directions.DIRECTION_DELTA_X[dstDir.intValue()], Directions.DIRECTION_DELTA_Y[dstDir.intValue()], 0);
                curX = step.getViewportX(base, 0);
                curY = step.getViewportY(base, 0);
                if (!ignoreLastStep || curX != dstX || curY != dstY) {
                    state.getPoints().add(new Position(step.getX(), step.getY(), step.getZ()));
                } else {
                    break;
                }
            } else if (nullOnFail) {
                return null;
            } else {
                break;
            }
        }
        return state;
    }
}
