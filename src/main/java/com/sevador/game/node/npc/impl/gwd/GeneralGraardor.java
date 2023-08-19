package com.sevador.game.node.npc.impl.gwd;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.ForceTextUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.node.player.Player;

/**
 * Represents the General graardor NPC.
 * @author Emperor
 *
 */
@SuppressWarnings("serial")
public final class GeneralGraardor extends NPC {

	/**
	 * The combat action to use.
	 */
	private static final CombatAction COMBAT_ACTION = new CombatAction();

	/**
	 * The force text update messages.
	 */
	private static final String[] MESSAGES = {
		"Death to our enemies!",
		"Brargh!",
		"Break their bones!",
		"For the glory of Bandos!",
		"Split their skulls!",
		"We feast on the bones of our enemies tonight!",
		"CHAAARGE!",
		"Crush them underfoot!",
		"All glory to Bandos!",
		"GRAAAAAAAAAR!",
		"FOR THE GLORY OF THE BIG HIGH WAR GOD!",
	};

	/**
	 * Constructs a new {@code TzTokJad} {@code Object}.
	 * 
	 * @param npcId
	 *            The NPC id.
	 */
	public GeneralGraardor(int npcId) {
		super(npcId);
	}

	@Override
	public void tick() {
		for (Player targets : NodeWorker.getPlayers()) {
			if (targets.getLocation().getRegionID() == this.getLocation().getRegionID()) {
				getCombatAction().setVictim(targets);
				getActionManager().register(getCombatAction());
				getCombatAction().execute();
				if (getRandom().nextInt(100) < 3) {
					this.getUpdateMasks().register(new ForceTextUpdate(MESSAGES[getRandom().nextInt(MESSAGES.length)], true));
				}
			}
		}
	} 

	@Override
	public TypeHandler getHandler(CombatType type) {
		return COMBAT_ACTION;
	}

	/**
	 * Handles General graardor's combat action.
	 * @author Emperor
	 *
	 */
	private static final class CombatAction implements TypeHandler {

		/**
		 * The melee animation.
		 */
		private static final Animation MELEE_ANIMATION = new Animation(7060, 0, true);

		/**
		 * The range animation.
		 */
		private static final Animation RANGE_ANIMATION = new Animation(7063, 0, true);

		/**
		 * The range start gfx.
		 */
		private static final Graphic RANGE_START_GFX = new Graphic(1219, 0, 0, true);

		/**
		 * The projectile to use.
		 */
		private static final Projectile PROJECTILE = Projectile.create(null, null, 1200, 5, 5, 41, 46, 1, 5 << 6);

		@Override
		public boolean handle(Interaction i) {
			final GeneralGraardor n = (GeneralGraardor) i.source;
			if (n.isDead()) return true;
			CombatType type = n.getCombatAction().getCombatType();
			n.getCombatAction().setType(n.getRandom().nextInt(3) < 2 ? CombatType.RANGE : CombatType.MELEE);
			switch (type) {
			case MELEE:
				n.getProperties().setAttackAnimation(MELEE_ANIMATION);
				return CombatType.MELEE.getHandler().handle(i);
			case RANGE:
				i.start = RANGE_ANIMATION;
				i.startGraphic = RANGE_START_GFX;
				i.ticks = 1 + (int) Math.ceil(n.getLocation().distance(i.victim.getLocation()) * 0.3);
				boolean deflect = i.victim.getPrayer().get(PrayerType.DEFLECT_MISSILES);
				Animation anim = deflect ? Animation.create(12573) : i.victim.getProperties().getDefenceAnimation();
				Graphic graphic = deflect ? Graphic.create(2229) : Graphic.create(-1);
				double maximum = CombatType.RANGE.getHandler().getMaximum(n);
				for (Player p : NodeWorker.getPlayers()) {
					if (p.getLocation().getRegionID() != n.getLocation().getRegionID()) continue;
					Target t = new Target(p);
					t.deflected = deflect;
					t.animation = anim;
					t.graphic = graphic;
					t.projectile.add(PROJECTILE.copy(n, i.victim, 5));
					final int hit = GaussianGen.getDamage(CombatType.RANGE.getHandler(), n, t.entity, maximum);
					t.damage = Damage.getDamage(n, t.entity, CombatType.RANGE, hit);
					t.damage.setMaximum((int) maximum);
					i.targets.add(t);
					PacketSender.sendProjectiles(PROJECTILE.copy(n, p, 5));
					final int currentHit = GaussianGen.getDamage(CombatType.RANGE.getHandler(), n, p, maximum);
					final Damage d = Damage.getDamage(n, p, CombatType.RANGE, currentHit);
					d.setMaximum((int) maximum);
				}
				return true;
			case MAGIC:
				break;
			default:
				break;
			}
			return false;
		}

		@Override
		public boolean init() {
			return false;
		}

		@Override
		public double getAccuracy(Entity e, Object... args) {
			return 0;
		}

		@Override
		public double getMaximum(Entity e, Object... args) {
			return 0;
		}

		@Override
		public double getDefence(Entity e, int attackBonus, Object... args) {
			return 0;
		}

	}
}