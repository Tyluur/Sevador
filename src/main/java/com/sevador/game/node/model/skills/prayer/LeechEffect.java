package com.sevador.game.node.model.skills.prayer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic4;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.MessagePacket;

/**
 * Handles a leech type prayer effect.
 * @author Emperor
 *
 */
public class LeechEffect implements PrayerEffect {

	/**
	 * The animation.
	 */
	private static final Animation ANIMATION = new Animation(12575, 0, false);

	/**
	 * The leech special attack start graphic.
	 */
	private static final Graphic4 LEECH_SPECIAL_GFX = new Graphic4(2223, 0, 0, false);

	/**
	 * The leech special attack end graphic.
	 */
	private static final Graphic4 LEECH_SPECIAL_END_GFX = new Graphic4(2225, 0, 0, false);
	
	/**
	 * The leech run energy graphic.
	 */
	private static final Graphic4 LEECH_RUN_ENERGY_GFX = new Graphic4(2254, 0, 0, false);
	
	/**
	 * The cache of entities influenced by this type of leech.
	 */
	private final Map<Player, Map<Entity, Double>> leechCache = new HashMap<Player, Map<Entity, Double>>();
	
	/**
	 * The skill id.
	 */
	private final int skillId;
	
	/**
	 * The type.
	 */
	private final int type;
	
	/**
	 * If the leech is a special leech (leech energy/leech special attack).
	 */
	private final boolean special;
	
	/**
	 * Constructs a new {@code LeechEffect} {@code Object}.
	 * @param skillId The skill id to leech (or 0 for run energy; 1 for special attack if special leech).
	 * @param type The leech type;
	 * @param special If the leech is a special leech (leech energy/leech special attack).
	 */
	public LeechEffect(int skillId, int type, boolean special) {
		this.skillId = skillId;
		this.type = type;
		this.special = special;
	}
	
	@Override
	public void handle(Player source, Entity victim) {
		if (special && victim.isNPC() || Math.random() > 0.3) {
			return;
		}
		if (special) {
			handleSpecial(source, victim);
			return;
		}
		Map<Entity, Double> map = leechCache.get(source);
		if (map == null) {
			leechCache.put(source, (map = new HashMap<Entity, Double>()));
		}
		double drain = 0.01;
		Double drained = map.get(victim);
		if (drained == null) {
			drained = 0.0;
		} else if (drained + drain >= 0.26) {
			return;
		}
		if (drained + drain <= 0.05) {
			source.getIOSession().write(new MessagePacket(source, "Your curse drains " + Skills.SKILL_NAMES[skillId] + " from the enemy, boosting your " + Skills.SKILL_NAMES[skillId] + "."));
		}
		if (victim.isPlayer()) {
			victim.getPlayer().getIOSession().write(new MessagePacket(victim.getPlayer(), "Your " + Skills.SKILL_NAMES[skillId] + " has been drained by an enemy curse."));
		}
		source.getProperties().updateCursedModifiers(source, skillId, drain, 0.05);
		victim.getProperties().updateCursedModifiers(victim, skillId, -drain, -0.25);
		int i = type - 9;
		if (i > 1) {
			victim.getUpdateMasks().register(new Graphic4(2230 + i * 4, 0, 0, victim.isNPC()));
			PacketSender.sendProjectiles(Projectile.create(source, victim, 2228 + i * 4, 30, 35, 30, 70, 20));
		} else {
			victim.getUpdateMasks().register(new Graphic4(2232, 0, 0, victim.isNPC()));
			PacketSender.sendProjectiles(Projectile.create(source, victim, 2231, 30, 35, 45, 70, 20));
		}
		source.getUpdateMasks().register(ANIMATION);
		map.put(victim, drained + drain > 0.25 ? 0.25 : drained + drain);
		map.put(source, -(drained + drain) < -0.05 ? -0.05 : -(drained + drain));
	}

	/**
	 * Handles the leech energy & leech special attack curses.
	 * @param source The player using ths leech.
	 * @param victim The cursed victim.
	 */
	private void handleSpecial(Player source, Entity victim) {
		Player p = victim.getPlayer();
		switch (skillId) {
		case 0: //Leech run energy.
			double drain = p.getSettings().getRunEnergy() < 10 ? p.getSettings().getRunEnergy() : 10;
			if (drain < 1) {
				return;
			}
			source.getSettings().increaseRunEnergy(drain);
			p.getSettings().decreaseRunEnergy(drain);
			source.getIOSession().write(new MessagePacket(source, "You leech some run energy from your enemy."));
			p.getIOSession().write(new MessagePacket(p, "Your run energy has been drained by an enemy curse."));
			source.getUpdateMasks().register(ANIMATION);
			victim.getUpdateMasks().register(LEECH_RUN_ENERGY_GFX);
			PacketSender.sendProjectiles(Projectile.create(source, victim, 2252, 30, 35, 30, 70, 20));
			break;
		case 1: //Leech special attack.
			drain = p.getSettings().getSpecialEnergy() < 10 ? p.getSettings().getSpecialEnergy() : 10;
			if (drain < 1) {
				return;
			}
			source.getSettings().updateSpecialEnergy((int) -drain);
			p.getSettings().updateSpecialEnergy((int) drain);
			source.getIOSession().write(new MessagePacket(source, "You leech some special attack energy from your enemy."));
			p.getIOSession().write(new MessagePacket(p, "Your special attack energy has been drained by an enemy curse."));
			source.getUpdateMasks().register(ANIMATION);
			source.getUpdateMasks().register(LEECH_SPECIAL_GFX);
			victim.getUpdateMasks().register(LEECH_SPECIAL_END_GFX);
			PacketSender.sendProjectiles(Projectile.create(source, victim, 2256, 30, 35, 30, 70, 20));
		}
	}

	@Override
	public void reset(Player player) {
		Map<Entity, Double> map = leechCache.get(player);
		if (map == null) {
			return;
		}
		for (Iterator<Entity> it = map.keySet().iterator(); it.hasNext();) {
			Entity e = it.next();
			if (e != null) {
				if (e.isPlayer()) {
					e.getPlayer().getIOSession().write(new MessagePacket(e.getPlayer(), "Your " + Skills.SKILL_NAMES[skillId] + " is now unaffected by sap and leech curses."));
				}
				double amount = map.get(e);
				e.getProperties().updateCursedModifiers(e, skillId, amount, amount > 0 ? 0.25 : -0.25);
			}
		}
		leechCache.remove(player);
	}
}