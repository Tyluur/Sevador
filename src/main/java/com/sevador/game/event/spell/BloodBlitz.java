package com.sevador.game.event.spell;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.combat.form.MagicSpell;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic2;
import com.sevador.game.node.model.skills.Experience;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Priority;

/**
 * Handles the Blood blitz spell event.
 * @author Emperor
 *
 */
public final class BloodBlitz implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] {new Item(565, 4), new Item(560, 2)};
	
	/**
	 * The start animation.
	 */
	private static final Animation ANIMATION = new Animation(1978, 0, false, Priority.HIGH);
	
	/**
	 * The start graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(-1, 0, 0, false);
	
	@Override
	public boolean init() {
		return EventManager.register(65561, this);
	}
	
	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		i.ticks += 2;
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this, i.victim);
		Target t = new Target(i.victim);		
		int speed = (int) (46 + p.getLocation().distance(i.victim.getLocation()) * 13);
		t.projectile.add(Projectile.create(p, i.victim, 374, 43, 0, 51, speed, 16, 64));
		if (t.entity.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
			t.deflected = true;
			t.animation = new Animation(12573, 0, t.entity.isNPC());
			t.graphic2 = new Graphic2(2228, 0, 0, t.entity.isNPC());
		} else {
			t.animation = t.entity.getProperties().getDefenceAnimation();
		}
		t.graphic = new Graphic(375, 0, 0, t.entity.isNPC());
		int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(), p, t.entity, max);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
		t.damage.setMaximum((int) max);
		if (hit > 0) {
			if (t.entity.isPlayer()) {
				t.entity.getPlayer().getPacketSender().sendMessage("Your lifepoints have been drained.", true);
			}
			p.getSkills().heal((int) (t.damage.getHit() * 0.25));
			p.getIOSession().write(new MessagePacket(p, "You drain some of your opponents' lifepoints."));
		}
		i.targets.add(t);
		Experience.appendMagicExp(i.source.getPlayer(), hit, getAutocastConfig());
		return true;
	}

	@Override
	public Item[] getRunes() {
		return RUNES;
	}

	@Override
	public int getStartDamage(Entity e, Entity victim) {
		return 220 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 18;
	}

	@Override
	public int getBaseDamage() {
		return 30;
	}
	
	@Override
	public int getAutocastConfig() {
		return 83;
	}

}