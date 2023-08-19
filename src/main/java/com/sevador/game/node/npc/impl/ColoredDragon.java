package com.sevador.game.node.npc.impl;

import java.util.List;

import com.sevador.game.event.EventManager;
import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Target;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.NPCCombatDefinitions;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionManager;
import com.sevador.utility.Misc;
import com.sevador.utility.Priority;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class ColoredDragon extends NPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4552247826034381131L;
	
	private static final ChromaticDragonAction COMBAT_ACTION = new ChromaticDragonAction();

	public TypeHandler getHandler(CombatType type) {
		return COMBAT_ACTION;
	}

	public ColoredDragon(int npcId) {
		super(npcId, NodeType.DRAGON, true);
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


	public static class ChromaticDragonAction implements TypeHandler {

		@Override
		public boolean init() {
			return EventManager.register(941, this);
		}

		@Override
		public boolean handle(Interaction i) {
			NPC npc = (NPC) i.source;
			Entity target = i.victim;
			final NPCCombatDefinitions defs = npc.getNPCCombatDefinitions();
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			int size = npc.getSize();
			if (distanceX > size || distanceX < -1 || distanceY > size
					|| distanceY < -1)
				return true;
			if (i.source.isDead()) return true;
			if (Misc.random(3) == 0) {
				npc.getUpdateMasks().register(new Animation(defs.getAttackEmote(), 0, true, Priority.HIGHEST));
				double max = getMaximum(i.source);
				Target t = new Target(i.victim);
				t.damage = Damage.getDamage(i.source, t.entity, CombatType.MELEE, GaussianGen.getDamage(this, i.source, i.victim, max));
				t.damage.setMaximum((int) max);
				i.targets.add(t);
			} else {
				int damage = Misc.random(650);
				npc.getUpdateMasks().register(new Animation(12259, 0, true, Priority.HIGHEST));
				npc.getUpdateMasks().register(new Graphic(1, 0, 100, false));
				if (target.getPlayer().protectedFromFire()) {
					damage = 0;
					target.getPlayer().getPacketSender().sendMessage("Your protection absorbs most of the dragon's breath!", true);
				}
				Target t = new Target(i.victim);
				t.damage = Damage.getDamage(i.source, t.entity, CombatType.MAGIC,GaussianGen.getDamage(this, i.source, i.victim, damage));
				t.damage.setMaximum((int) damage);
				t.damage.setDelay(defs.getAttackDelay());
				i.targets.add(t);
			}
			return true;
		}

		@Override
		public double getAccuracy(Entity e, Object... args) {
			return 100;
		}

		@Override
		public double getMaximum(Entity e, Object... args) {
			return 300;
		}

		@Override
		public double getDefence(Entity e, int attackBonus, Object... args) {
			return 10;
		}

	}

}
