package com.sevador.content.areas.impl;

import com.sevador.game.node.model.Location;
import com.sevador.utility.world.Area;

/**
 * Represents a circular area in the world.
 *
 * @author Stephen
 */
public class CircularArea extends Area {

    private Location center;

    public CircularArea(String name, int[] coords, int radius, int centerX, int centerY, int height, boolean isPvpZone,
                        boolean isPlusOneZone, boolean canTele) {
        super(name, coords, radius, centerX, centerY, height, isPvpZone, isPlusOneZone, canTele);
        center = Location.locate(centerX, centerY, 0);
    }

    CircularArea() {

    }

    /**
     * Not sure about this, i think it should work though.
     */
    @Override
    public boolean contains(Location pos) {
        if (center == null || radius == 0) {
            center = Location.locate(centerX, centerY, 0);
        }
        return (center.getDistance(pos) < radius);

    }

    public void spawnItemsOnPoints() {
        /*RectangularArea rect = new RectangularArea("bob", new int[] { 3145, 3172, 3185, 3506 }, -1, -1, -1, false, false, true);
          for (Location pos : rect.generatePointsWithin()) {
              if (contains(pos)) {
                  World.getWorld().getGroundItemManager().sendGlobalGroundItem(World.getWorld().getGroundItemManager().create(null, new Item(995, 100000), pos), false);
              }
          }*/
    }
}
