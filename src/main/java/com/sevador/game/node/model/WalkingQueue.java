package com.sevador.game.node.model;

import java.util.Deque;
import java.util.LinkedList;

import net.burtleburtle.tick.Tick;
import net.burtleburtle.tick.impl.AreaUpdateTick;

import com.sevador.Main;
import com.sevador.game.node.model.Directions.WalkingDirection;
import com.sevador.game.node.model.mask.TeleportUpdate;
import com.sevador.game.world.World;
import com.sevador.utility.Misc;

/**
 * The entity's walking queue.
 * 
 * @author Emperor
 * @author Graham
 * @author Mystic flow'
 */
public class WalkingQueue {

	/**
	 * Represents a single point to walk.
	 * 
	 * @author Mystic Flow
	 * @author Graham
	 * 
	 */
	public static class Point {

		/**
		 * The x-coordinate.
		 */
		private final int x;

		/**
		 * The y-coordinate.
		 */
		private final int y;

		/**
		 * The direction.
		 */
		private final WalkingDirection direction;

		/**
		 * Constructs a new {@code Point} {@code Object}.
		 * 
		 * @param x
		 *            The x-coordinate.
		 * @param y
		 *            The y-coordinate.
		 * @param direction
		 *            The walking direction.
		 */
		private Point(int x, int y, WalkingDirection direction) {
			this.x = x;
			this.y = y;
			this.direction = direction;
		}

		/**
		 * Gets the x-coordinate of this point.
		 * 
		 * @return The x-coordinate.
		 */
		public int getX() {
			return x;
		}

		/**
		 * Gets the y-coordinate of this point.
		 * 
		 * @return The y-coordinate.
		 */
		public int getY() {
			return y;
		}
	}

	/**
	 * The walking queue.
	 */
	private final Deque<Point> walkingQueue = new LinkedList<Point>();

	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * The current walking direction.
	 */
	private int walkDir = -1;

	/**
	 * The current running direction.
	 */
	private int runDir = -1;

	/**
	 * If the entity is running (set to true when holding the ctrl button +
	 * click).
	 */
	private boolean running = false;

	/**
	 * The last location this entity walked on.
	 */
	private Location footPrint;

	/**
	 * Constructs a new {@code WalkingQueue} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 */
	public WalkingQueue(Entity entity) {
		this.entity = entity;
		this.footPrint = entity.getLocation();
	}

	/**
	 * Updates the walking queue.
	 */
	public void updateMovement() {
		boolean isPlayer = entity.isPlayer();
		if (updateTeleport()) {
			return;
		}
		this.walkDir = -1;
		this.runDir = -1;
		Point walkPoint = walkingQueue.poll();
		Point runPoint = null;
		if (walkPoint == null) {
			if (entity.isPlayer()) {
				entity.getPlayer().getSettings().increaseRunEnergy(0.264);
			}
			return;
		}
		if (walkPoint.direction == null) {
			walkPoint = walkingQueue.poll();
		}
		int walkDirection = -1;
		int runDirection = -1;
		if (isRunningBoth()) {
			runPoint = walkingQueue.poll();
		}
		if (walkPoint != null) {
			walkDirection = isPlayer ? walkPoint.direction.intValue()
					: walkPoint.direction.npcIntValue();
		}
		if (runPoint != null) {
			runDirection = isPlayer ? runPoint.direction.intValue()
					: runPoint.direction.npcIntValue();
		}
		if (isPlayer && updateRegion(walkPoint, runPoint)) {
			return;
		}
		int diffX = 0;
		int diffY = 0;
		if (walkDirection != -1) {
			diffX += Misc.DIRECTION_DELTA_X[walkPoint.direction.intValue()];
			diffY += Misc.DIRECTION_DELTA_Y[walkPoint.direction.intValue()];
		}
		if (runDirection != -1) {
			int nextXDiff = Misc.DIRECTION_DELTA_X[runDirection];
			int nextYDiff = Misc.DIRECTION_DELTA_Y[runDirection];
			if (isPlayer) {
				runDirection = Misc.getRunningDirection(diffX + nextXDiff,
						diffY + nextYDiff);
			}
			if (runDirection != -1) {
				walkDirection = -1;
				diffX += nextXDiff;
				diffY += nextYDiff;
			} else if (walkDirection == -1) {
				walkDirection = (byte) Misc.getWalkDirection(nextXDiff,
						nextYDiff);
				diffX += nextXDiff;
				diffY += nextYDiff;
			}
			if (entity.isPlayer()) {
				entity.getPlayer().getSettings().decreaseRunEnergy(1.5);
				// entity.getPlayer().getSettings().decreaseRunEnergy(getEnergyDrainRate(entity.getPlayer())
				// <= 0 ? 1.5 : getEnergyDrainRate(entity.getPlayer()));
			}
		} else if (entity.isPlayer()) {
			entity.getPlayer().getSettings().increaseRunEnergy(0.264);
		}
		if (diffX != 0 || diffY != 0) {
			footPrint = entity.getLocation();
			entity.setLocation(entity.getLocation().transform(diffX, diffY, 0));
			Main.getWorkingSet().submitLogic(new AreaUpdateTick(entity));
		}
		this.walkDir = walkDirection;
		this.runDir = runDirection;
	}

	/*
	 * public double getEnergyDrainRate(Player player) { return
	 * player.getProperties().getCarriedWeight(player) == 0 ? 0 : (int)
	 * Math.ceil(7.6 - ((player.getSkills() .getLevel(Skills.AGILITY) / 99D) *
	 * (int) player.getProperties() .getCarriedWeight(player))); }
	 */

	/**
	 * Checks if the player is teleporting, if so does the teleporting and
	 * returns true.
	 * 
	 * @return {@code True} if the player is teleporting, {@code false} if not.
	 */
	private boolean updateTeleport() {
		if (entity.getProperties().getTeleportLocation() != null) {
			reset(false);
			Location lastRegion = entity.getLocation();
			footPrint = entity.getProperties().getTeleportLocation();
			entity.setLocation(entity.getProperties().getTeleportLocation());
			entity.getProperties().setTeleportLocation(null);
			if (entity.isNPC()) {
				entity.getNPC().setVisible(false);
				World.getWorld().submit(new Tick(1) {
					@Override
					public boolean run() {
						entity.getNPC().setVisible(true);
						return true;
					}
				});
				return true;
			}
			entity.getUpdateMasks().register(new TeleportUpdate());
			if ((lastRegion.getRegionX() - entity.getLocation().getRegionX()) >= 4
					|| (lastRegion.getRegionX() - entity.getLocation()
							.getRegionX()) <= -4) {
				entity.getPlayer().getPlayerFlags().setMapRegionChanged(true);
			}
			if ((lastRegion.getRegionY() - entity.getLocation().getRegionY()) >= 4
					|| (lastRegion.getRegionY() - entity.getLocation()
							.getRegionY()) <= -4) {
				entity.getPlayer().getPlayerFlags().setMapRegionChanged(true);
			}
			entity.getPlayer().getPlayerFlags().setTeleported(true);
			Main.getWorkingSet().submitLogic(new AreaUpdateTick(entity));
			return true;
		}
		return false;
	}

	/**
	 * Checks if the region should be updated, if so we set the update flag and
	 * return true.
	 * 
	 * @param runPoint
	 *            a run point
	 * @param walkPoint
	 *            a walk point (y)(y)(y)
	 * @return {@code True} if the region updated, {@code false} if not.
	 */
	private boolean updateRegion(Point walkPoint, Point runPoint) {
		Location lastRegion = entity.getPlayer().getRegion() != null ? entity
				.getPlayer().getRegion() : entity.getLocation();
		int rx = lastRegion.getRegionX();
		int ry = lastRegion.getRegionY();
		int cx = entity.getLocation().getRegionX();
		int cy = entity.getLocation().getRegionY();
		if ((rx - cx) >= 4) {
			entity.getPlayer().getPlayerFlags().setMapRegionChanged(true);
		} else if ((rx - cx) <= -4) {
			entity.getPlayer().getPlayerFlags().setMapRegionChanged(true);
		}
		if ((ry - cy) >= 4) {
			entity.getPlayer().getPlayerFlags().setMapRegionChanged(true);
		} else if ((ry - cy) <= -4) {
			entity.getPlayer().getPlayerFlags().setMapRegionChanged(true);
		}
		if (entity.getPlayer().getPlayerFlags().isMapRegionChanged()) {
			if (walkPoint != null) {
				walkingQueue.addFirst(walkPoint);
				walkDir = -1;
			}
			if (runPoint != null) {
				walkingQueue.addFirst(runPoint);
				runDir = -1;
			}
			return true;
		}
		return false;
	}

	/**
	 * Adds a path to the walking queue.
	 * 
	 * @param x
	 *            The last x-coordinate of the path.
	 * @param y
	 *            The last y-coordinate of the path.
	 */
	public void addPath(int x, int y) {
		Point point = walkingQueue.peekLast();
		int diffX = 0, diffY = 0;
		if (point != null)
			if (point.x != -1 && x != -1) {
				diffX = x - point.x;
				diffY = y - point.y;
			}
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < max; i++) {
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
			addPoint(x - diffX, y - diffY);
		}
	}

	/**
	 * Adds a point to the walking queue.
	 * 
	 * @param x
	 *            The x-coordinate of the point.
	 * @param y
	 *            The y-coordinate of the point.
	 */
	public void addPoint(int x, int y) {
		Point point = walkingQueue.peekLast();
		int diffX = x - point.x, diffY = y - point.y;
		WalkingDirection direction = Directions.directionFor(diffX, diffY);
		if (direction != null) {
			walkingQueue.add(new Point(x, y, direction));
		}
	}

	/**
	 * Checks if the entity is running.
	 * 
	 * @return {@code True} if a ctrl + click action was performed, <br>
	 *         the player has the run option enabled or the NPC is a familiar,
	 *         <p>
	 *         {@code false} if not.
	 */
	public boolean isRunningBoth() {
		return running || entity.isRunning();
	}

	/**
	 * Resets the walking queue.
	 */
	public void reset() {
		reset(running);
	}

	/**
	 * Resets the walking queue.
	 * 
	 * @param running
	 *            The running flag (ctrl + click action).
	 */
	public void reset(boolean running) {
		walkingQueue.clear();
		walkingQueue.add(new Point(entity.getLocation().getX(), entity
				.getLocation().getY(), null));
		this.running = running;
	}

	/**
	 * Gets the current walking direction.
	 * 
	 * @return The walk direction.
	 */
	public int getWalkDir() {
		return walkDir;
	}

	/**
	 * Gets the current run direction.
	 * 
	 * @return The run direction.
	 */
	public int getRunDir() {
		return runDir;
	}

	/**
	 * Sets the running flag (when ctrl + click action performed).
	 * 
	 * @param running
	 *            If the player will be running this path.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Checks if the player is running.
	 * 
	 * @return {@code True} if so.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return the footPrint
	 */
	public Location getFootPrint() {
		return footPrint;
	}

	/**
	 * @param footPrint
	 *            the footPrint to set
	 */
	public void setFootPrint(Location footPrint) {
		this.footPrint = footPrint;
	}

}