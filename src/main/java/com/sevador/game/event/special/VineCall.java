package com.sevador.game.event.special;

import com.sevador.game.action.impl.combat.VineCallAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Abyssal vine whip special attack event.
 * @author Emperor
 *
 */
public final class VineCall implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 60;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(11971, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic2 GRAPHIC = new Graphic2(476, 0, 4, false);
	
	@Override
	public boolean init() {
		EventManager.register(21371, this);
		EventManager.register(21372, this);
		EventManager.register(21373, this);
		EventManager.register(21374, this);
		return EventManager.register(21375, this);
	}

	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		p.getSettings().setSpecialEnabled(false);
		if (p.getSettings().getSpecialEnergy() < ENERGY_REQUIRED) {
			p.getIOSession().write(new MessagePacket(p, "You do not have enough special attack energy left."));
			return false;
		}
		p.getSettings().updateSpecialEnergy(ENERGY_REQUIRED);
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		i.ticks = 1;
		p.getActionManager().register(new VineCallAction(p, i.victim.getLocation().transform(-1 + p.getRandom().nextInt(3), -1 + p.getRandom().nextInt(3), 0), this));
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args) * 1.25;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getMaximum(e, args) * 0.33;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.MELEE.getHandler().getDefence(e, attackBonus, args);
	}

}