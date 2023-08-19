package com.sevador.game.action.impl.combat;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.model.Entity;
import com.sevador.network.out.ConfigPacket;
import com.sevador.utility.Misc;

/**
 * Handles the poison action.
 * 
 * @author Emperor
 * 
 */
public final class PoisonAction extends Action {

	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * The entity poisoning this entity.
	 */
	private final Entity source;

	/**
	 * Constructs a new {@code PoisonAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 * @param source
	 *            The entity who poisoned this entity.
	 * @param poisonDamage
	 *            The start amount.
	 */
	public PoisonAction(Entity entity, Entity source, int poisonDamage) {
		super(entity);
		addFlag(FLAG);
		this.source = source;
	}

	@Override
	public boolean execute() {
		if (entity == null || source == null) {
			return true;
		}
		if (Misc.random(8) != 8)
			entity.getPoison().makePoisoned(48);
		return true;
	}

	@Override
	public boolean dispose(Action a) {
		if (entity.isPlayer()) {
			entity.getPlayer().getIOSession()
					.write(new ConfigPacket(entity.getPlayer(), 102, 0));
		}
		return true;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}