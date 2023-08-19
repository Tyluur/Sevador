package com.sevador.content.areas.impl;

import com.sevador.game.node.model.Location;
import com.sevador.utility.world.Area;

/**
 * Represents an Irregular Area.
 *
 * @author Stephen
 */
public class IrregularArea extends Area {

    Location[] locations;

    public IrregularArea(String name, int[] coords, int radius, int centerX, int centerY, int height, boolean isPvpZone,
                         boolean isPlusOneZone, boolean canTele) {
        super(name, coords, radius, centerX, centerY, height, isPvpZone, isPlusOneZone, canTele);
    }

    @Override
    public boolean contains(Location pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
