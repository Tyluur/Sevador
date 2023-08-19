package com.sevador.game.node.model.combat;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.tick.Tick;

import com.sevador.game.action.impl.combat.HitAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.Graphic3;
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.world.World;

/**
 * Represents a combat interaction between 2 entities.
 * 
 * @author Emperor
 * 
 */
public class Interaction {

	/**
	 * The leech and sap curses.
	 */
	private static final PrayerType[] COMBAT_PRAYER_TYPES = new PrayerType[] {
			PrayerType.SAP_WARRIOR, PrayerType.SAP_RANGER, PrayerType.SAP_MAGE,
			PrayerType.SAP_SPIRIT, PrayerType.LEECH_ATTACK,
			PrayerType.LEECH_RANGED, PrayerType.LEECH_MAGIC,
			PrayerType.LEECH_DEFENCE, PrayerType.LEECH_STRENGTH,
			PrayerType.LEECH_ENERGY, PrayerType.LEECH_SPECIAL_ATTACK,
			PrayerType.TURMOIL };

	/**
	 * The source entity.
	 */
	public final Entity source;

	/**
	 * The victim.
	 */
	public Entity victim;

	/**
	 * The start animation.
	 */
	public Animation start;

	/**
	 * The start graphic.
	 */
	public UpdateFlag startGraphic;

	/**
	 * The targets of this attack.
	 */
	public List<Target> targets = new ArrayList<Target>();

	/**
	 * The ticks before the interaction is completed.
	 */
	public int ticks;

	/**
	 * Constructs a new {@code Interaction} {@code Object}.
	 * 
	 * @param source
	 *            The attacking entity.
	 * @param victim
	 *            The entity being attacked.
	 */
	public Interaction(Entity source, Entity victim) {
		this.source = source;
		this.victim = victim;
	}

	/**
	 * Commences the interaction.
	 * 
	 * @return {@code true} if succesful and we can continue our attack,
	 *         {@code false} if not.
	 */
	public boolean commence() {
		if (source.visual(start, startGraphic)) {
			List<Projectile> projectiles = new ArrayList<Projectile>();
			for (Target t : targets) {
				projectiles.addAll(t.projectile);
			}
			PacketSender.sendProjectiles(projectiles
					.toArray(new Projectile[] {}));
		}
		for (Target t : targets) {
			t.entity.setAttribute("tick:combat",
					MajorUpdateWorker.getTicks() + 17);
		}
		boolean soulsplit = source.getPrayer().get(PrayerType.SOUL_SPLIT);
		boolean smite = source.getPrayer().get(PrayerType.SMITE);
		if (smite || soulsplit) {
			Entity e = null;
			for (Target t : targets) {
				if (t.damage != null) {
					if (soulsplit) {
						soulSplit(source, t.entity, t.damage, t.entity != e);
						e = t.entity;
					} else if (smite) {
						t.entity.getSkills().updatePrayerPoints(
								t.damage.getHit() * 0.25);
					}
				}
			}
		}
		// TODO: Add experience if necessary.
		return true;
	}

	/**
	 * Updates the interaction.
	 * 
	 * @return {@code True} if the interaction is finished.
	 */
	public boolean tick() {
		if (ticks < 2) {
			for (Target t : targets) {
				if (t.damage != null && t.damage.getHit() < 0) {
					t.graphic = new Graphic(85, 96, 0, t.entity.isNPC());
				}
				t.entity.visual(t.animation, t.graphic);
				if (t.graphic2 != null) {
					t.entity.getUpdateMasks().register(t.graphic2);
				}
			}
		}
		if (--ticks < 1) {
			for (Target t : targets) {
				if (t.damage != null && t.damage.getHit() > -1) {
					t.entity.getActionManager().register(
							new HitAction(t.entity, source, t.damage, 0));
				}
			}
			try {
				if (source.isPlayer() && source.getRandom().nextInt(50) < 15
						&& source.getPrayer().isCurses()) {
					List<PrayerType> types = new ArrayList<PrayerType>();
					for (final PrayerType type : COMBAT_PRAYER_TYPES) {
						if (source.getPrayer().get(type)) {
							types.add(type);
						}
					}
					if (types.size() < 1) {
						return true;
					}
					final PrayerType type = types.get(source.getRandom()
							.nextInt(types.size()));
					World.getWorld().submit(new Tick(1) {
						@Override
						public boolean run() {
							if (source.getPrayer().get(type)) {
								type.getEffect().handle(source.getPlayer(),
										victim);
							}
							return true;
						}
					});
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * Handles the soul split effect.
	 * 
	 * @param source
	 *            The attacking entity.
	 * @param victim
	 *            The entity being attacked.
	 * @param damage
	 *            The damage.
	 * @param sendProjectile
	 *            If we should send the projectiles.
	 */
	private static void soulSplit(final Entity source, final Entity victim,
			Damage damage, boolean sendProjectile) {
		int ticks = (int) Math.floor(source.getLocation().distance(
				victim.getLocation()) * 0.5) + 1;
		int speed = (int) (46 + source.getLocation().distance(
				victim.getLocation()) * 10);
		if (damage.getHit() > 0) {
			source.getSkills().heal(
					(int) (damage.getHit() * (victim.isNPC() ? 0.2 : 0.4)));
			victim.getSkills().updatePrayerPoints(damage.getHit() * 0.2);
			if (sendProjectile) {
				PacketSender.sendProjectiles(Projectile.create(source, victim,
						2263, 11, 11, 30, speed, 0, 0));
			}
		} else if (victim.isNPC()) {
			return;
		}
		if (sendProjectile) {
			World.getWorld().submit(new Tick(ticks) {
				@Override
				public boolean run() {
					int speed = (int) (46 + source.getLocation().distance(
							victim.getLocation()) * 10);
					victim.getUpdateMasks().register(
							new Graphic3(2264, 0, 0, victim.isNPC()));
					PacketSender.sendProjectiles(Projectile.create(victim,
							source, 2263, 11, 11, 30, speed, 0, 0));
					return true;
				}
			});
		}
	}

}