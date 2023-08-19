package com.sevador.game.node.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.player.Player;

/**
 * The entity's damage map. This holds the last 2 damages done to this player, a
 * record of which players hit what on this entity, ...
 * 
 * @author Emperor
 * 
 */
public class DamageMap {

	/**
	 * The entity.
	 */
	private final Entity entity;

	/**
	 * Holds all the hit data.
	 */
	private final Map<Player, Integer> hitRecord;

	/**
	 * A list of damages to deal.
	 */
	private final List<Damage> damageList = new LinkedList<Damage>();

	/**
	 * The damage constructor.
	 */
	public DamageMap(Entity entity) {
		this.entity = entity;
		this.hitRecord = new HashMap<Player, Integer>();
	}

	/**
	 * Submits damage to the hit record.
	 * 
	 * @param attacker
	 *            The attacking entity.
	 * @param damage
	 *            The amount of damage.
	 */
	public void submitDamage(Entity attacker, int damage) {
		if (attacker == null || !attacker.isPlayer()) {
			return;
		}
		Player dealer = (Player) attacker;
		Integer totalDamage = hitRecord.get(dealer);
		if (totalDamage == null) {
			totalDamage = 0;
		}
		hitRecord.put(dealer, damage + totalDamage);
	}

	/**
	 * Gets the entity with the most damage.
	 * 
	 * @return The Player with the most damage.
	 */
	public Entity getMostDamageEntity() {
		int currentMaxDamage = 0;
		Entity e = (entity instanceof Player ? (Player) entity : (entity));
		for (Player p : hitRecord.keySet()) {
			boolean bool = NodeWorker.getPlayer(p.getCredentials().getUsername()) != null;
			if (bool && hitRecord.get(p) > currentMaxDamage) {
				currentMaxDamage = hitRecord.get(p);
				e = p;
			} else if (!bool) {
				hitRecord.remove(p);
			}
		}
		return e;
	}

	/**
	 * Clears the hit record.
	 */
	public void clear() {
		hitRecord.clear();
	}

	/**
	 * @return the damageList
	 */
	public List<Damage> getDamageList() {
		return damageList;
	}
}
