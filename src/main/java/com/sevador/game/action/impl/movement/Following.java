package com.sevador.game.action.impl.movement;

import com.sevador.game.action.Action;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.region.path.DefaultPathFinder;

/**
 * Handles the following of an entity.
 * @author Emperor
 *
 */
public class Following extends MovementAction {

	/**
	 * The entity to follow.
	 */
	private final Entity f;
	
	/**
	 * If we are running (ctrl + click).
	 */
	private final boolean running;
	
	/**
	 * The last location.
	 */
	private Location lastLocation;
	
	/**
	 * Constructs a new {@code Following} {@code Object}.
	 * @param entity The entity.
	 * @param followable The entity to follow.
	 * @param running If we are running (ctrl + click).
	 */
	public Following(Entity entity, Entity followable, boolean running) {
		super(entity, followable.getLocation().getX(), followable.getLocation().getY(), running);
		this.f = followable;
		this.running = running;
	}
	
	@Override
	public boolean execute() {
		entity.turnTo(f);
		lastLocation = f.getWalkingQueue().getFootPrint();
		if (entity.getLocation() == lastLocation) {
			return false;
		} else {
			entity.getWalkingQueue().reset(running);
			super.x = lastLocation.getX();
			super.y = lastLocation.getY();
			if (super.execute()) {
				super.pathFinder = new DefaultPathFinder();
			} else {
				lastLocation = null; //Fail-safe.
			}
		}
		return false;
	}
	
	@Override
	public boolean dispose(Action a) {
		entity.turnTo(null);
		return super.dispose(a);
	}

}