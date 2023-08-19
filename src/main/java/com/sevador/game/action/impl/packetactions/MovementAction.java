package com.sevador.game.action.impl.packetactions;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.ActionType;
import com.sevador.game.action.impl.FreezeAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.FaceEntityUpdate;
import com.sevador.game.region.path.DefaultPathFinder;
import com.sevador.game.region.path.PathFinder;
import com.sevador.game.region.path.PathState;
import com.sevador.game.region.path.PathState.Position;

/**
 * Represents a normal movement action.
 * 
 * @author Emperor
 * 
 */
public class MovementAction extends Action {

	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * Add to the default reset flag.
	 */
	static {
		DEFAULT_RESET |= FLAG;
	}

	/**
	 * If the player is running (when holding ctrl and clicking to move).
	 */
	private final boolean running;

	/**
	 * The x-coordinate of the destination.
	 */
	protected int x;

	/**
	 * The y-coordinate of the destination.
	 */
	protected int y;

	/**
	 * The pathfinder used.
	 */
	protected PathFinder pathFinder;

	/**
	 * Constructs a new {@code MovementAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 * @param x
	 *            The x-coordinate of the destination.
	 * @param y
	 *            The y-coordinate of the destination.
	 * @param running
	 *            If the entity should run this path.
	 */
	public MovementAction(Entity entity, int x, int y, boolean running) {
		this(entity, x, y, running, new DefaultPathFinder());
	}

	/**
	 * Constructs a new {@code MovementAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 * @param x
	 *            The x-coordinate of the destination.
	 * @param y
	 *            The y-coordinate of the destination.
	 * @param running
	 *            If the entity should run this path.
	 * @param pathFinder
	 *            The path finder to use.
	 */
	public MovementAction(Entity entity, int x, int y, boolean running,
			PathFinder pathFinder) {
		super(entity, ActionType.MOVEMENT);
		addFlag(DEFAULT_RESET | EmoteAction.FLAG | FreezeAction.FLAG);
		this.running = running;
		this.x = x;
		this.y = y;
		this.pathFinder = pathFinder;
	}

	@Override
	public boolean execute() {
		entity.getUpdateMasks().register(
				new FaceEntityUpdate(-1, entity.isNPC()));
		entity.getWalkingQueue().reset(running);
		if (entity.isPlayer()) {
			entity.getPlayer().getPacketSender().sendChatBoxInterface(137);
		}
		try {
			Location dest = Location.locate(x, y, entity.getLocation().getZ());
			Location base = entity.getLocation();
			int srcX = entity.getLocation().getViewportX(0);
			int srcY = entity.getLocation().getViewportY(0);
			int destX = dest.getViewportX(base, 0);
			int destY = dest.getViewportY(base, 0);
			PathState state = pathFinder.findPath(entity, entity.getLocation(),
					srcX, srcY, destX, destY, entity.getLocation().getZ(), 0,
					entity.getWalkingQueue().isRunning(), false, true);
			if (state != null) {
				for (Position step : state.getPoints()) {
					entity.getWalkingQueue().addPath(step.getX(), step.getY());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean dispose(Action action) {
		entity.getWalkingQueue().reset();
		return true;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}