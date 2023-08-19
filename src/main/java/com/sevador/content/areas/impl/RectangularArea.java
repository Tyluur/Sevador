package com.sevador.content.areas.impl;

import java.util.ArrayList;

import com.sevador.game.node.model.Location;
import com.sevador.utility.world.Area;

/**
 * Represents an area in the shape of a rectangle.
 *
 * @author Stephen
 */
public class RectangularArea extends Area {

    public RectangularArea() {

    }
    public RectangularArea(int[] coords) {
        super(null, coords, -1, -1, -1, 0, false, false, false);
    }

    public RectangularArea(String name, int[] coords, int radius, int centerX, int centerY, int height, boolean isPvpZone, boolean isPlusOneZone, boolean canTele) {
        super(name, coords, radius, centerX, centerY, height, isPvpZone, isPlusOneZone, canTele);
    }

    /**
     * This should definitely work.
     */
    @Override
    public boolean contains(Location pos) {
        return pos.getX() >= swX && pos.getX() <= nwX && pos.getY() >= swY && pos.getY() <= nwY;
    }

    public void spawnItems() {

    }

    public ArrayList<Location> generatePointsWithin() {
        ArrayList<Location> locations = new ArrayList<Location>();
        for (int x = 0; x < nwX - swX; x++) {
            for (int y = 0; y < nwY - swY; y++) {
                locations.add(Location.locate(swX + x, swY + y, 0));
            }
        }
        return locations;
    }

    public static RectangularArea create(String name, int x, int y, int x1, int y1, int height, boolean isPvp, boolean isPlusOne, boolean canTele) {
        return new RectangularArea(name, new int[]{x, y, x1, y1}, -1, -1, -1, height, isPvp, isPlusOne, canTele);
    }
}
