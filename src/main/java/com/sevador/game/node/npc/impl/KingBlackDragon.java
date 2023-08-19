package com.sevador.game.node.npc.impl;

import java.util.List;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.ForceTextUpdate;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionManager;
import com.sevador.utility.Misc;
import com.sevador.utility.Priority;

/**
 * Represents a king black dragon.
 * 
 * @author Emperor
 * 
 */
public class KingBlackDragon extends NPC {

	private static final KingBlackDragonAction COMBAT_ACTION = new KingBlackDragonAction();

	public TypeHandler getHandler(CombatType type) {
		return COMBAT_ACTION;
	}
	
	public static boolean atKBD(Location tile) {
		if ((tile.getX() >= 2250 && tile.getX() <= 2292)
				&& (tile.getY() >= 4675 && tile.getY() <= 4710))
			return true;
		return false;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new {@code KingBlackDragon} {@code Object}.
	 * 
	 * @param npcId
	 *            The NPC id.
	 */

	public KingBlackDragon(int npcId) {
		super(npcId, NodeType.KING_BLACK_DRAGON, true);
	}

	@Override
	public void tick() {
		if (getRandom().nextInt(5) < 2) {
			List<Player> targets = RegionManager.getLocalPlayers(getLocation(),
					50);
			if (targets.size() > 0) {
				getCombatAction().setVictim(
						targets.get(getRandom().nextInt(targets.size())));
				getActionManager().register(getCombatAction());
			}
		}
	}


	/**
	 * @author Tyluur<lethium@hotmail.co.uk>
	 * 
	 */
	public static class KingBlackDragonAction implements TypeHandler {

		public enum FireType {

			ICY_BREATH(83, 300, DamageType.MAGIC, 395), TOXIC_BREATH(82, 100,
					DamageType.MAGIC, 394), SHOCKING_BREATH(84, 300,
							DamageType.MAGIC, 396), FIERY_BREATH(81, 500, DamageType.MAGIC,
									393);

			private int maxDamage;
			private int anim;
			private int projectileId;
			private DamageType type;

			public int getAnim() {
				return anim;
			}

			public int getMaxDamage() {
				return maxDamage;
			}

			public int getProjectileId() {
				return projectileId;
			}

			public DamageType getType() {
				return type;
			}

			FireType(int anim, int maxDamage, DamageType type, int projectileId) {
				this.anim = anim;
				this.maxDamage = maxDamage;
				this.type = type;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.argonite.game.node.model.combat.TypeHandler#init()
		 */
		@Override
		public boolean init() {
			return EventManager.register(50, this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.argonite.game.node.model.combat.TypeHandler#handle(org.argonite.game
		 * .node.model.combat.Interaction)
		 */
		@Override
		public boolean handle(Interaction i) {
			if (i.source.isDead()) return true;
			if (Misc.random(2) == 2 && i.source.getLocation().distance(i.victim.getLocation()) > 3) {
				int speed = (int) (27 + (i.source.getLocation().distance(
						i.victim.getLocation().getLocation()) * 5));
				PacketSender.sendProjectiles((Projectile.create(i.source, i.victim,
						393, 40, 36, 20, speed, 15, 11)));
				i.source.getUpdateMasks().register(new ForceTextUpdate("Burn in flames!", true));
				i.source.getUpdateMasks().register(new Animation(81, 0, true, Priority.HIGHEST));
				double max = (i.victim.getPlayer().protectedFromFire() ? getMaximum(i.source) - 250 - Misc.random(100) : getMaximum(i.source));
				Target t = new Target(i.victim);
				t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC,GaussianGen.getDamage(this, i.source, i.victim, max));
				t.damage.setMaximum((int) max);
				i.targets.add(t);
			} else {
				i.source.getUpdateMasks().register(new Animation(91, 0, true, Priority.HIGHEST));
				double max = getMaximum(i.source);
				Target t = new Target(i.victim);
				t.damage = Damage.getDamage(i.source, t.entity, CombatType.MELEE, GaussianGen.getDamage(this, i.source, i.victim, max));
				t.damage.setMaximum((int) max);
				i.targets.add(t);
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.argonite.game.node.model.combat.TypeHandler#getAccuracy(org.argonite
		 * .game.node.model.Entity, java.lang.Object[])
		 */
		@Override
		public double getAccuracy(Entity e, Object... args) {
			return 150;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.argonite.game.node.model.combat.TypeHandler#getMaximum(org.argonite
		 * .game.node.model.Entity, java.lang.Object[])
		 */
		@Override
		public double getMaximum(Entity e, Object... args) {
			return 500;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.argonite.game.node.model.combat.TypeHandler#getDefence(org.argonite
		 * .game.node.model.Entity, int, java.lang.Object[])
		 */
		@Override
		public double getDefence(Entity e, int attackBonus, Object... args) {
			return 20;
		}

	}

}