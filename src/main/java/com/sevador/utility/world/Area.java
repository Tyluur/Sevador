package com.sevador.utility.world;

import java.awt.Point;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;

/**
 * Represents an area in the world.
 *
 * @author Stephen
 */
public class Area {

    public final String name;
    public final int swX, swY, nwX, nwY, radius, centerX, centerY, height;
    public transient int length, width;
    private final boolean isPvpZone, isMulti, canTele;

    public Area(int swX, int swY, int neX, int neY) {
    	this.name = "Unspecified";
    	this.isPvpZone = false;
    	this.isMulti = false;
    	this.canTele = true;
    	this.swX = swX;
    	this.swY = swY;
    	this.nwX = neX;
    	this.nwY = neY;
    	this.radius = 0;
    	this.centerX = swX + ((neX - swX) >> 1);
    	this.centerY = swY + ((neY - swY) >> 1);
    	this.height = 0;
    }
    
    public Area(String name, int[] coords, int radius, int centerX, int centerY, int height, boolean isPvpZone, boolean isMulti, boolean canTele) {//perhaps something like
        this.name = name;
        this.swX = coords[0];
        this.swY = coords[1];
        this.nwX = coords[2];
        this.nwY = coords[3];
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.isPvpZone = isPvpZone;
        this.isMulti = isMulti;
        this.canTele = canTele;
        this.length = nwX - swX;
        this.width = nwY - swY;
        this.height = height;
    }

    public Area() {
        this.name = "";
        this.nwX = -1;
        this.nwY = -1;
        this.swX = -1;
        this.swY = -1;
        this.radius = -1;
        this.centerX = -1;
        this.centerY = -1;
        this.isPvpZone = false;
        this.isMulti = false;
        this.canTele = true;
        this.length = nwX - swX;
        this.width = nwY - swY;
        this.height = 0;
    }

    public boolean contains(Location pos) {
    	return pos.getX() >= swX && pos.getY() >= swY && pos.getX() <= nwX && pos.getY() <= nwY;
    }

    public boolean isPvpZone() {
        return isPvpZone;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public boolean canTele() {
        return canTele;
    }

    public String getName() {
        return name;
    }

    public void teleTo(Player player) {
        player.teleport(new Location(swX + ((nwX - swX) / 2), swY + ((nwY - swY) / 2), height));
    }

    public Point getCenterPoint() {
        return new Point(swX + ((nwX - swX) / 2), swY + ((nwY - swY) / 2));
    }
}
