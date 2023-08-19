package com.sevador.game.event.spell;

import com.sevador.game.action.impl.FreezeAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
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
import com.sevador.utility.Priority;

/**
 * Handles the Ice blitz spell event.
 * 
 * @author Emperor
 * 
 */
public final class IceBlitz implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] { new Item(555, 3),
			new Item(565, 2), new Item(560, 2) };

	/**
	 * The start animation.
	 */
	private static final Animation ANIMATION = new Animation(1978, 0, false,
			Priority.HIGH);

	/**
	 * The start graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(366, 124, 0, false);

	@Override
	public boolean init() {
		return EventManager.register(65557, this);
	}

	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		i.ticks += 2;
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this,
				i.victim);
		Target t = new Target(i.victim);
		if (t.entity.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
			t.deflected = true;
			t.animation = new Animation(12573, 0, t.entity.isNPC());
			t.graphic2 = new Graphic2(2228, 0, 0, t.entity.isNPC());
		} else {
			t.animation = t.entity.getProperties().getDefenceAnimation();
		}
		boolean wasFrozen = t.entity.getActionManager().contains(
				FreezeAction.FLAG);
		t.graphic = new Graphic(367, 0, 0, t.entity.isNPC());
		int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(), p,
				t.entity, max);
		t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
		t.damage.setMaximum((int) max);
		if (hit > -1) {
			t.entity.getActionManager()
					.register(new FreezeAction(t.entity, 25));
			if (t.entity.isPlayer() && !wasFrozen)
				t.entity.getPlayer().getPacketSender().sendMessage("You have been frozen!");
		}
		i.targets.add(t);
		Experience.appendMagicExp(i.source.getPlayer(), hit,
				getAutocastConfig());
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
		return 40;
	}

	@Override
	public int getAutocastConfig() {
		return 85;
	}

}