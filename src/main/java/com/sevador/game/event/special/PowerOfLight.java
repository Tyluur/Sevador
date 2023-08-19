package com.sevador.game.event.special;

import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Staff of light's special attack event.
 * @author Emperor
 *
 */
public final class PowerOfLight implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 100;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(12804, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(2319, 0, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(15486, this);
		return EventManager.register(15502, this);
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
		p.setAttribute("staffOfLightEffect", MajorUpdateWorker.getTicks() + 100);
		p.getCombatAction().increaseTicks(3, true);
		p.visual(ANIMATION, GRAPHIC);
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return 0;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return 0;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return 0;
	}

}