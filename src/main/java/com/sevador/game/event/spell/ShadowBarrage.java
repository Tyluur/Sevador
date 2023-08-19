package com.sevador.game.event.spell;

import java.util.List;

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
import com.sevador.game.node.player.Skills;
import com.sevador.game.region.RegionManager;
import com.sevador.utility.Priority;


/**
 * Handles the Shadow barrage spell event.
 * @author Emperor
 *
 */
public class ShadowBarrage implements MagicSpell {

	/**
	 * The runes required to cast this spell.
	 */
	private static final Item[] RUNES = new Item[] {new Item(556, 4), new Item(566, 3), new Item(565, 2), new Item(560, 4)};
	
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
		return EventManager.register(65571, this);
	}
	
	@Override
	public boolean handle(Interaction i) {
		Player p = i.source.getPlayer();
		i.start = ANIMATION;
		i.startGraphic = GRAPHIC;
		double max = CombatType.MAGIC.getHandler().getMaximum(i.source, this, i.victim);
		int amount = p.getAttribute("area:multi", false) && i.victim.getAttribute("area:multi", false) ? 8 : 0;
		i.targets.add(new Target(i.victim));
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
			if (t.entity.getPrayer().get(PrayerType.DEFLECT_MAGIC)) {
				t.deflected = true;
				t.animation = new Animation(12573, 0, t.entity.isNPC());
				t.graphic2 = new Graphic2(2228, 0, 0, t.entity.isNPC());
			} else {
				t.animation = t.entity.getProperties().getDefenceAnimation();
			}
			t.graphic = new Graphic(383, 0, 0, t.entity.isNPC());
			int hit = GaussianGen.getDamage(CombatType.MAGIC.getHandler(), p, t.entity, max);
			t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC, hit);
			t.damage.setMaximum((int) max);
			if (hit > 0) {
				Experience.appendMagicExp(i.source.getPlayer(), hit, getAutocastConfig());
				if (t.entity.isPlayer()) {
					t.entity.getPlayer().getPacketSender().sendMessage("You have been blinded.");
				}
				int level = t.entity.getSkills().getStaticLevel(Skills.ATTACK);
				t.entity.getSkills().updateLevel(Skills.ATTACK, (int) -(level * 0.15), (int) (level - (level * 0.15)));
			}
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
		return 20;
	}
	
	@Override
	public int getAutocastConfig() {
		return 89;
	}

}