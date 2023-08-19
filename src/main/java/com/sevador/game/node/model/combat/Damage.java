package com.sevador.game.node.model.combat;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Constants;

/**
 * Represents a damage to hit.
 * 
 * @author Emperor
 */
public class Damage {

	/**
	 * The hit to be dealt to the opponent.
	 */
	private int hit;

	/**
	 * The amount of soaked damage.
	 */
	private int soaked;

	/**
	 * The deflected hit to be dealt to the attacker.
	 */
	private int deflected;

	/**
	 * The venged hit to be dealt to the attacker.
	 */
	private int venged;

	/**
	 * The recoiled hit to be dealt to the attacker.
	 */
	private int recoiled;

	/**
	 * The maximum hit of the mob.
	 */
	private int maximum = 999;

	/**
	 * The delay of the hit.
	 */
	private int delay;

	/**
	 * The current lifepoints value (used for hit update mask).
	 */
	private int lifepoints;

	/**
	 * The damage type.
	 */
	private DamageType type;

	/**
	 * The entity dealing the damage.
	 */
	private Entity source;

	/**
	 * Represents the damage types.
	 * 
	 * @author Emperor
	 * 
	 */
	public static enum DamageType {
		MELEE, RANGE, MAGIC, RED_DAMAGE, DEFLECT, SOAK, POISON, DISEASED, MISS, HEAL;
	}

	/**
	 * Constructs a new {@code Damage} {@code Object}.
	 * 
	 * @param hit
	 *            The standard hit.
	 */
	public Damage(int hit) {
		this.hit = hit;
	}

	private static int appendExperience(Player player, CombatType type, int hit) {
		double combatXp = hit / 2.5;
		switch (type) {
		case MELEE:
			switch (player.getProperties().getAttackStyle()) {
			case 0:
				((Skills) player.getSkills()).addExperience(Skills.ATTACK,
						combatXp);
				break;
			case 1:
				((Skills) player.getSkills()).addExperience(Skills.STRENGTH,
						combatXp);
				break;
			case 2:
				((Skills) player.getSkills()).addExperience(Skills.STRENGTH,
						combatXp);
				break;
			case 3:
				((Skills) player.getSkills()).addExperience(Skills.DEFENCE,
						combatXp);
				break;
			}
			break;
		case RANGE:
			switch (player.getProperties().getAttackStyle()) {
			case 4: // rapid
			case 0: // accurate
				((Skills) player.getSkills()).addExperience(Skills.RANGE,
						combatXp);
				break;
			case 5: // long range
				((Skills) player.getSkills()).addExperience(Skills.RANGE,
						(combatXp / 2));
				((Skills) player.getSkills()).addExperience(Skills.DEFENCE,
						(combatXp / 2));
				break;
			}
			break;
		case MAGIC:
			break;
		default:
			break;
		}
		((Skills) player.getSkills()).addExperience(Skills.HITPOINTS, hit * 0.133);
		return (int) combatXp;
	}

	/**
	 * Returns the damage instance of this hit, keeping damage modifiers in
	 * mind.
	 * 
	 * @param source
	 *            The attacking entity.
	 * @param victim
	 *            The entity being hit.
	 * @param hit
	 *            The hit.
	 * @return The damage.
	 */
	public static Damage getDamage(Entity source, Entity victim, CombatType type, int hit) {
		if (hit < 0) {
			return new Damage(-1);
		}
		Damage damage = victim.updateHit(source, hit, type);
		if (victim.isPlayer() && damage.getHit() > 200) {
			damage.soaked = getSoaked(victim, damage.getHit(), type);
			damage.setHit(damage.getHit() - damage.soaked);
		}
		if (source instanceof Player) {
			appendExperience((Player) source, type, hit);
		}
		damage.setRecoiled(getRecoilDamage(source, victim, damage.getHit()));
		damage.setVenged(getVengDamage(victim, damage.getHit()));
		damage.setType(type.getType());
		return damage;
	}

	/**
	 * Gets the recoiled damage.
	 * 
	 * @param victim
	 *            The victim.
	 * @param hit
	 *            The hit.
	 * @return The recoiled damage if the victim is recoiling, or -1 if not.
	 */
	private static int getRecoilDamage(Entity source, Entity victim, int hit) {
		int recoiled = (int) Math.floor(hit * 0.1);
		if (recoiled < 1) {
			return -1;
		}
		if (victim.isPlayer()) {
			Player p = victim.getPlayer();
			if (p.getEquipment().getNew(Constants.SLOT_RING).getId() != 2550) {
				return -1;
			}
			if (recoiled > p.getCredentials().getRecoilDamage()) {
				recoiled = p.getCredentials().getRecoilDamage();
			}
			int hitpoints = source.getSkills().getLifepoints();
			if (recoiled > hitpoints) {
				recoiled = hitpoints;
			}
			p.getCredentials().setRecoilDamage(
					p.getCredentials().getRecoilDamage() - recoiled);
			if (p.getCredentials().getRecoilDamage() < 1) {
				p.getIOSession().write(
						new MessagePacket(p,
								"Your ring of recoil has turned to dust."));
				p.getEquipment().replace(null, Constants.SLOT_RING);
				p.getCredentials().setRecoilDamage(400);
			}
			return recoiled;
		}
		// TODO: NPC recoiling.
		return -1;
	}

	/**
	 * Gets the vengeance damage.
	 * 
	 * @param victim
	 *            The victim.
	 * @param hit
	 *            The hit.
	 * @return The vengeance damage if the spell is casted and the victim is a
	 *         player, or -1 if not.
	 */
	private static int getVengDamage(Entity victim, int hit) {
		if (victim.isPlayer() && hit > 0
				&& victim.getAttribute("vengeance", false)) {
			return (int) (hit * 0.75);
		}
		return -1;
	}

	/**
	 * Gets the soaked damage amount.
	 * 
	 * @param victim
	 *            The victim.
	 * @param hit
	 *            The hit to soak.
	 * @param type
	 *            The combat type used.
	 * @return The amount of absorbed damage.
	 */
	private static int getSoaked(Entity victim, int hit, CombatType type) {
		if (victim.isNPC() || type != CombatType.MELEE) {// type.getAbsorbtion()
															// < 0 ||
															// type.getAbsorbtion()
															// > 2) {
			return 0;
		}
		int absorptionId = type.ordinal(); // type.getAbsorbtion();
		int excess = hit - 200;
		double bonus = victim.getProperties().getStats()[15 + absorptionId] / 100D;
		return (int) (excess * bonus);
	}

	/**
	 * @param hit
	 *            the hit
	 */
	public void setHit(int hit) {
		this.hit = hit;
	}

	/**
	 * @return the hit
	 */
	public int getHit() {
		return hit;
	}

	/**
	 * @param deflected
	 *            the deflected to set
	 * @return The Damage instance.
	 */
	public Damage setDeflected(int deflected) {
		this.deflected = deflected;
		return this;
	}

	/**
	 * @return the deflected
	 */
	public int getDeflected() {
		return deflected;
	}

	/**
	 * @param venged
	 *            the venged to set
	 */
	public void setVenged(int venged) {
		this.venged = venged;
	}

	/**
	 * @return the venged
	 */
	public int getVenged() {
		return venged;
	}

	/**
	 * @param recoiled
	 *            the recoiled to set
	 */
	public void setRecoiled(int recoiled) {
		this.recoiled = recoiled;
	}

	/**
	 * @return the recoiled
	 */
	public int getRecoiled() {
		return recoiled;
	}

	/**
	 * @return the soaked
	 */
	public int getSoaked() {
		return soaked;
	}

	/**
	 * @param soaked
	 *            the soaked to set
	 */
	public void setSoaked(int soaked) {
		this.soaked = soaked;
	}

	/**
	 * @return the maximum
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum
	 *            the maximum to set
	 */
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	/**
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * @param delay
	 *            the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * @return the type
	 */
	public DamageType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public Damage setType(DamageType type) {
		this.type = type;
		return this;
	}

	/**
	 * @return the source
	 */
	public Entity getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(Entity source) {
		this.source = source;
	}

	/**
	 * @return the lifepoints
	 */
	public int getLifepoints() {
		return lifepoints;
	}

	/**
	 * @param lifepoints
	 *            the lifepoints to set
	 */
	public void setLifepoints(int lifepoints) {
		this.lifepoints = lifepoints;
	}

}