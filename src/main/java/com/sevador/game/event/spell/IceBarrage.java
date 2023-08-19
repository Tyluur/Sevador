package com.sevador.game.event.spell;

import java.util.List;

import com.sevador.game.action.impl.FreezeAction;
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
import com.sevador.game.region.RegionManager;
import com.sevador.utility.Priority;


/**
 * Handles the ice barrage spell event.
 * @author Emperor
 *
 */
public final class IceBarrage implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] {new Item(555, 6), new Item(565, 2), new Item(560, 4)};

	/**
	 * The start animation.
	 */
	private static final Animation ANIMATION = new Animation(1979, 0, false, Priority.HIGH);

	/**
	 * The start graphic.
	 */
	private static final Graphic GRAPHIC = new Graphic(-1, 0, 0, false);

	@Override
	public boolean init() {
		return EventManager.register(65559, this);
	}

	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this, i.victim);
		int amount = p.getAttribute("area:multi", false) && i.victim.getAttribute("area:multi", false) ? 8 : 0;
		i.targets.add(new Target(i.victim));
		i.targets.get(0).projectile.add(Projectile.create(p, i.victim, 368, 43, 0, 51, 120, 16, 64));
		List<? extends Entity> possibleVictims = i.victim.isNPC() ? RegionManager.getLocalNPCs(i.victim.getLocation(), 1) : RegionManager.getLocalPlayers(i.victim.getLocation(), 1);
		for (Entity e : possibleVictims) {
			if (e == p) {
				continue;
			}
			Target t = new Target(e);
			if (!i.targets.contains(t) && e.isAttackable(p) && --amount > -1) {
				i.targets.add(t);
			}
		}
		for (Target t : i.targets) {
			boolean wasFrozen = t.entity.getActionManager().contains(FreezeAction.FLAG);
			if (t.entity.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
				t.deflected = true;
				t.animation = new Animation(12573, 0, t.entity.isNPC());
				t.graphic2 = new Graphic2(2228, 0, 0, t.entity.isNPC());
			} else {
				t.animation = t.entity.getProperties().getDefenceAnimation();
			}
			t.graphic = new Graphic(wasFrozen ? 1677 : 369, wasFrozen ? 96 : 0, 0, t.entity.isNPC());
			int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(), p, t.entity, max);
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
			t.damage.setMaximum((int) max);
			if (hit > -1) {
				t.entity.getActionManager().register(new FreezeAction(t.entity, 33));
				if (t.entity.isPlayer() && !wasFrozen)
					t.entity.getPlayer().getPacketSender().sendMessage("You have been frozen!");
			}
			Experience.appendMagicExp(i.source.getPlayer(), hit, getAutocastConfig());
		}
		return true;
	}

	@Override
	public Item[] getRunes() {
		return RUNES;
	}

	@Override
	public int getStartDamage(Entity e, Entity victim) {
		return 260 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 24;
	}

	@Override
	public int getBaseDamage() {
		return 40;
	}

	@Override
	public int getAutocastConfig() {
		return 93;
	}

}