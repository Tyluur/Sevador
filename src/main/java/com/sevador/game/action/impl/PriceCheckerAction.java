package com.sevador.game.action.impl;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.player.Player;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class PriceCheckerAction extends Action {

	/**
	 * The entity doing the price checking action.
	 */

	private Entity entity;

	/**
	 * The container with the items.
	 */

	@SuppressWarnings("unused")
	private Container itemContainer;

	/**
	 * The maximum quantity of items in the container
	 */

	@SuppressWarnings("unused")
	private static final int SIZE = 10;

	/**
	 * 
	 * @param entity
	 *            The entity being set
	 * @param item
	 *            The item being used
	 */

	public PriceCheckerAction(Entity entity) {
		super(entity);
		this.entity = entity;
		addFlag((DEFAULT_RESET & ~ActionFlag.CLOSE_INTERFACE) & ~MovementAction.FLAG);
	}

	@Override
	public boolean execute() {
		entity.getPlayer().getPriceCheck().open();
		return true;
	}

	@Override
	public int getActionType() {
		return ActionFlag.CLOSE_INTERFACE;
	}

	public boolean dispose(Action a) {
		Player p = entity.getPlayer();
		p.getPacketSender().setDefaultInventory();
		p.getPacketSender().sendCloseInterface();
		return true;
	}

}
