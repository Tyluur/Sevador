package com.sevador.game.event.special;

import com.sevador.game.action.impl.combat.PoisonAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Dragon dagger special attack event.
 * @author Emperor
 *
 */
public final class Puncture implements TypeHandler {
	
	/**
	 * The amount of special attack energy required.
	 */
	private static final int ENERGY_REQUIRED = 25;
	
	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(1062, 0, false, Priority.HIGHEST);
	
	/**
	 * The graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(252, 96, 0, false);
	
	@Override
	public boolean init() {
		EventManager.register(1215, this);
		EventManager.register(1231, this);
		EventManager.register(5680, this);
		EventManager.register(5698, this);
		EventManager.register(13465, this);
		EventManager.register(13466, this);
		EventManager.register(13467, this);
		return EventManager.register(13468, this);
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
		boolean defl = i.victim.getPrayer().get(PrayerType.DEFLECT_MELEE);
		Animation anim = defl ? new Animation(12573, 0, i.victim.isNPC()) : i.victim.getProperties().getDefenceAnimation();
		UpdateFlag gfx = defl ? new Graphic2(2230, 0, 0, i.victim.isNPC()) : new Graphic(-1, 0, 0, i.victim.isNPC());
		i.ticks = 1;
		double max = getMaximum(p);
		int hit = GaussianGen.getDamage(this, p, i.victim, max);
		for (int j = 0; j < 2; j++) {
			Target t = new Target(i.victim);
			t.deflected = defl;
			t.animation = anim;
			t.graphic = gfx;
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.MELEE, hit);
			if (hit > 0) 
				t.entity.getActionManager().register(new PoisonAction(t.entity, i.source, 42));
			if (j == 1)
				t.damage.setDelay(15);
			t.damage.setMaximum((int) max);
			i.targets.add(t);
		}
		return true;
	}

	@Override
	public double getAccuracy(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getAccuracy(e, args) * 1.124;
	}

	@Override
	public double getMaximum(Entity e, Object... args) {
		return CombatType.MELEE.getHandler().getMaximum(e, args) * 1.1306;
	}

	@Override
	public double getDefence(Entity e, int attackBonus, Object... args) {
		return CombatType.MELEE.getHandler().getDefence(e, attackBonus, args);
	}

}