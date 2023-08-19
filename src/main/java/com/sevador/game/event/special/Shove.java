package com.sevador.game.event.special;

import net.burtleburtle.tick.Tick;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Dragon spear special attack event.
 * @author Emperor
 *
 */
public final class Shove implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 25;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(12017, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(2116, 96, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(1249, this);
		EventManager.register(1263, this);
		EventManager.register(3176, this);
		EventManager.register(5716, this);
		EventManager.register(5730, this);
		EventManager.register(13770, this);
		EventManager.register(13772, this);
		EventManager.register(13774, this);
		EventManager.register(13776, this);
		EventManager.register(11716, this);
		return EventManager.register(13454, this);
	}

	@Override
	public boolean handle(final Interaction i) {
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
		int vx = i.victim.getLocation().getX();
		int vy = i.victim.getLocation().getY();
		int sx = p.getLocation().getX();
		int sy = p.getLocation().getY();
		int x = 0;
		int y = 0;
		if (vx == sx && vy > sy) {
			y++;
		} else if (vx == sx && vy < sy) {
			y--;
		} else if (vx > sx && vy == sy) {
			x++;
		} else if (vx < sx && vy == sy) {
			x--;
		} else if (vx > sx && vy > sy) {
			x++;
			y++;
		} else if (vx < sx && vy > sy) {
			x--;
			y++;
		} else if (vx > sx && vy < sy) {
			x++;
			y--;
		} else if (vx < sx && vy < sy) {
			x--;
			y--;
		}
		i.source.getCombatAction().reset();
		i.victim.getCombatAction().reset();
		i.victim.getWalkingQueue().reset();
		i.source.turnTo(i.victim);
		i.victim.turnTo(i.source);
		i.victim.getWalkingQueue().addPath(vx + x, vy + y);
		World.getWorld().submit(new Tick(1) {
			@Override
			public boolean run() {
				i.victim.turnTo(null);
				return true;
			}			
		});
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args) * 1.124;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getMaximum(e, args);
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.MELEE.getHandler().getDefence(e, attackBonus, args);
	}

}