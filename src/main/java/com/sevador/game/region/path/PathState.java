package com.sevador.game.region.path;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class PathState {

	/**
	 * Represents a position.
	 * @author Emperor
	 *
	 */
	public static class Position {
		final int x, y, z;
		
		public Position(int x, int y, int z) {
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
	}
	
    private Deque<Position> points = new ArrayDeque<Position>();
    private boolean routeFound = true;

    public Deque<Position> getPoints() {
        return points;
    }

    public void routeFailed() {
        this.routeFound = false;
    }

    public boolean isRouteFound() {
        return routeFound;
    }

}
