package com.sevador.game.action.impl;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.ForceMovement;

/**
 * Handles the force movement.
 * @author Emperor
 *
 */
public final class ForceMovementAction extends Action {

	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * Add the flag to as a default reset.
	 */
	static {
		DEFAULT_RESET |= FLAG;
	}
	
	/**
	 * The animation to use when moving.
	 */
	private final Animation animation;
	
	/**
	 * The duration.
	 */
	private final int duration;
	
	/**
	 * The x-coordinate.
	 */
	private final int x;
	
	/**
	 * The y-coordinate.
	 */
	private final int y;
	
	/**
	 * The speed.
	 */
	private final int speed;
	
	/**
	 * The second speed.
	 */
	private final int speed1;
	
	/**
	 * The direction.
	 */
	private int dir;
	
	/**
	 * The amount of cycles.
	 */
	private int cycles;
	
	/**
	 * 
	 * Constructs a new {@code ForceMovementAction} {@code Object}.
	 * @param entity The entity.
	 * @param animation The animation to use.
	 * @param x The destination x-coordinate.
	 * @param y The destination y-coordinate.
	 * @param speed The speed.
	 * @param speed1 The second speed.
	 * @param dir The direction.
	 * @param duration The duration (in game ticks).
	 */
	public ForceMovementAction(Entity entity, Animation animation, int x, int y, int speed, int speed1, int dir, int duration) {
		super(entity);
		addFlag(DEFAULT_RESET);
		this.animation = animation;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.speed1 = speed1;
		this.dir = dir;
		this.duration = duration;
	}
    
	@Override
	public boolean execute() {
		if (cycles++ == 0) {
			if (dir == -1) {
				if (entity.getLocation().getX() > x)
					dir = 3;
				if (entity.getLocation().getX() < x)
					dir = 4;
				if (entity.getLocation().getY() > y)
					dir = 2;
				if (entity.getLocation().getY() < y)
					dir = 0;
	        }
			entity.getUpdateMasks().register(animation);
			entity.getUpdateMasks().register(new ForceMovement(entity, new int[] {x, y, speed, speed1, dir}));
		} else if (cycles == duration) {
			entity.getProperties().setTeleportLocation(Location.locate(x, y, entity.getLocation().getZ()));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean dispose(Action a) {
		return false;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}