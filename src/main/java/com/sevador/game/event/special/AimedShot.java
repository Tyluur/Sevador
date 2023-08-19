package com.sevador.game.event.special;

import net.burtleburtle.tick.Tick;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.combat.form.RangeData;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.network.out.MessagePacket;

/**
 * Handles the Hand cannon's special attack event.
 * @author Emperor
 *
 */
public final class AimedShot implements TypeHandler {
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(12175, 0, false);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(2141, 96, 0, false);
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 50;
	
	@Override
	public boolean init() {
		return EventManager.register(15241, this);
	}
	
	@Override
	public boolean handle(final Interaction i) {
		Player p = i.source.getPlayer();
		RangeData data = p.getRangeData(i);
		if (data == null) {
			return false;
		}
		p.getSettings().setSpecialEnabled(false);
		if (p.getSettings().getSpecialEnergy() < ENERGY_REQUIRED) {
			p.getIOSession().write(new MessagePacket(p, "You do not have enough special attack energy left."));
			return false;
		}
		p.getSettings().updateSpecialEnergy(ENERGY_REQUIRED);
		i.start = new Animation(-1, 0, false);
		i.startGraphic = new Graphic(-1, 0, 0, false);
		i.ticks = 4 + (int) Math.ceil(i.source.getLocation().distance(i.victim.getLocation()) * 0.3);
		double max = getMaximum(p);
		Target t = new Target(i.victim);
		if (i.victim.getPrayer().get(PrayerType.DEFLECT_MISSILES)) {
			t.deflected = true;
			t.animation = new Animation(12573, 0, i.victim.isNPC());
			t.graphic2 = new Graphic2(2229, 0, 0, i.victim.isNPC());
		} else {
			t.animation = i.victim.getProperties().getDefenceAnimation();
		}
		t.graphic = new Graphic(-1, 0, 0, i.victim.isNPC());
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.RANGE, GaussianGen.getDamage(this, p, i.victim, max));
		t.damage.setMaximum((int) max);
		i.targets.add(t);
		World.getWorld().submit(new Tick(3) {
			@Override
			public boolean run() {
				i.source.getUpdateMasks().register(ANIMATION);
				i.source.getUpdateMasks().register(GRAPHIC);
				int speed = (int) (32 + (i.source.getLocation().distance(i.victim.getLocation()) * 5));
				PacketSender.sendProjectiles(Projectile.create(i.source, i.victim, 2143, 31, 36, 45, speed, 0));
				return true;
			}			
		});
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getAccuracy(e, args) * 1.499;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.RANGE.getHandler().getMaximum(e, args);
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.RANGE.getHandler().getDefence(e, attackBonus, args) * 0.978;
	}

}