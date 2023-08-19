package com.sevador.game.action.impl;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.skills.prayer.PrayerType;

/**
 * Handles the freezing of an entity.
 * @author Emperor
 *
 */
public final class FreezeAction extends Action {

	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * The amount of ticks to be frozen.
	 */
	private int ticks;
	
	/**
	 * Constructs a new {@code FreezeAction} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The amount of ticks.
	 */
	public FreezeAction(Entity entity, int ticks) {
		super(entity);
		addFlag(MovementAction.FLAG | FLAG);
		if (entity.getPrayer().get(PrayerType.PROTECT_FROM_MAGIC) || entity.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
			ticks /= 2;
		}
		this.ticks = ticks;
	}

	@Override
	public boolean execute() {
		if (entity.getAttribute("freezeImmunity", -1) > MajorUpdateWorker.getTicks()) {
			return true;
		}
		if (ticks == 0) {
			entity.setAttribute("freezeImmunity", MajorUpdateWorker.getTicks() + 7); 
		}
		return ticks-- < 1;
	}
	
	@Override
	public boolean dispose(Action a) {
		if (a == NullAction.getSingleton()) {
			return true;
		}
		if (entity.isPlayer() && a.getActionType() != FLAG) {
			entity.getPlayer().getPacketSender().sendMessage("A magical force stops you from moving.");
		}
		return false;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}