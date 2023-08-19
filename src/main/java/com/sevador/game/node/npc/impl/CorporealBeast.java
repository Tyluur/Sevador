package com.sevador.game.node.npc.impl;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Misc;
import com.sevador.utility.Priority;

/**
 * Represents a corporeal beast custom action.
 * 
 * @author Tyluur
 * 
 */
@SuppressWarnings("serial")
public final class CorporealBeast extends NPC {

	/**
	 * The combat action.
	 */
	private static final CombatAction COMBAT_ACTION = new CombatAction();

	/**
	 * Constructs a new {@code TzTokJad} {@code Object}.
	 * 
	 * @param npcId
	 *            The NPC id.
	 */
	public CorporealBeast(int npcId) {
		super(npcId);
	}

	@Override
	public void tick() {
		for (Player targets : NodeWorker.getPlayers()) {
			if (targets.getLocation().getRegionID() != this.getLocation().getRegionID()) continue;
			getCombatAction().setVictim(targets);
			getActionManager().register(getCombatAction());
		}
	}

	@Override
	public TypeHandler getHandler(CombatType type) {
		return COMBAT_ACTION;
	}

	/**
	 * Handles the corpreal beast combat
	 * 
	 * @author Tyluur
	 * 
	 */
	private static final class CombatAction implements TypeHandler {

		@Override
		public boolean init() {
			return true;
		}

		@Override
		public boolean handle(Interaction i) {
			if (i.source.isDead()) return true;
			int damage;
			i.source.getNPC().getDefinition().setAggressive(true);
			if (Misc.random(3) == 1) { // MELEE
				damage = 513 - Misc.random(350);
				for (Player v : NodeWorker.getPlayers()) {
					if (v.isDead() || v.getLocation().getRegionID() != i.source.getNPC().getLocation().getRegionID()) continue;
					i.ticks = 6;
					Target t = new Target(v);
					int hit = damage;
					t.damage = Damage.getDamage(i.source, v, CombatType.MELEE, hit);
					t.damage.setMaximum((int) hit);
					i.targets.add(t);
				}
				i.source.getUpdateMasks().register(new Animation(10057, 0, true, Priority.HIGHEST));
			} else { // MAGIC
				damage = 650 - Misc.random(450);
				for (Player v : NodeWorker.getPlayers()) {
					if (v.isDead() || v.getLocation().getRegionID() != i.source.getNPC().getLocation().getRegionID()) continue;
					PacketSender.sendProjectiles((Projectile.create(i.source,v, 1826, 40, 36, 20, (int) (27 + (i.source.getLocation().distance(v.getLocation().getLocation()) * 5)), 15, 11)));
					i.ticks = 6;
					Target t = new Target(v);
					t.damage = Damage.getDamage(i.source, v, CombatType.MAGIC, damage);
					t.damage.setMaximum((int) damage);
					i.targets.add(t);
				}
				i.source.getUpdateMasks().register(new Animation(10058, 0, true, Priority.HIGHEST));
			}
			return true;
		}

		@Override
		public double getAccuracy(Entity e, Object... args) {
			return 100;
		}

		@Override
		public double getMaximum(Entity e, Object... args) {
			return 990;
		}

		@Override
		public double getDefence(Entity e, int attackBonus, Object... args) {
			return 20;
		}

	}

}