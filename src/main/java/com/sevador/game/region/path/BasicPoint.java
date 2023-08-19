package com.sevador.game.region.path;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class BasicPoint {

    public static BasicPoint create(int x, int y, int z) {
        return new BasicPoint(x, y, z);
    }

    private int x;
    private int y;
    private int z;

    private BasicPoint(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof BasicPoint) {
            BasicPoint p = (BasicPoint) other;
            return p.x == x && p.y == y && p.z == z;
        }
        return false;
    }
}
