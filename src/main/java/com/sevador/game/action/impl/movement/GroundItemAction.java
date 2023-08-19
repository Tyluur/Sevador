package com.sevador.game.action.impl.movement;

import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;
import com.sevador.game.region.path.DefaultPathFinder;
import com.sevador.network.out.MessagePacket;

/**
 * Handles the moving towards and handling a ground item action.
 * @author Emperor
 *
 */
public class GroundItemAction extends MovementAction {

	/**
	 * The ground item id.
	 */
	private final GroundItem groundItem;
	
	/**
	 * If we have found a path yet.
	 */
	private boolean foundPath = false;
	
	/**
	 * Constructs a new {@code GroundItemAction} {@code Object}.
	 * @param entity The entity.
	 * @param x The x-coordinate.
	 * @param y The y-coordinate.
	 * @param itemId The item id.
	 * @param running If we are running (ctrl + click).
	 */
	public GroundItemAction(Entity entity, int x, int y, int itemId, boolean running) {
		super(entity, x, y, running);
		this.groundItem = GroundItemManager.getGroundItem(itemId, Location.locate(x, y, entity.getLocation().getZ()));
	}
	

	@Override
	public boolean execute() {
		if (groundItem == null || !GroundItemManager.getGroundItems().contains(groundItem)) {
			return true;
		}
		if (entity.getLocation() == groundItem.getLocation()) {
	        if (entity.getPlayer().getInventory().add(groundItem.getItem())) {
	        	if (groundItem.getItem().getId() == 17489) { //Gatestone.
	        		entity.removeAttribute("gatestone");
	        	}
	            GroundItemManager.removeGroundItem(groundItem);
	        } else {
				entity.getPlayer().getIOSession().write(new MessagePacket(entity.getPlayer(), "Not enough space in your inventory."));
	        }
			return true;
		}
		if (!foundPath) {
			entity.getWalkingQueue().reset();
			foundPath = super.execute();
			pathFinder = new DefaultPathFinder();
		}
		return false;
	}

}