package com.sevador.game.node.player;

import java.io.Serializable;

import com.sevador.game.action.impl.combat.HitAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.network.out.ConfigPacket;
import com.sevador.utility.Misc;

public final class Poison implements Serializable {

	private static final long serialVersionUID = -6324477860776313690L;

	private transient Entity entity;
	private int poisonDamage;
	private int poisonCount;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

	public void makePoisoned(int startDamage) {
		if (startDamage == 0) {
			poisonCount = 0;
			poisonDamage = 0;
			refresh();
			return;
		}
		if (poisonDamage > startDamage)
			return;
		if (entity instanceof Player) {
			Player player = ((Player) entity);
			if (player.getPoisonImmune() > Misc.currentTimeMillis())
				return;
			if (poisonDamage == 0)
				player.getPacketSender().sendMessage("You have been poisoned.");
		}
		poisonDamage = startDamage;
		refresh();
	}

	public void processPoison() {
		if (!entity.isDead() && isPoisoned()) {
			if (poisonCount > 0) {
				poisonCount--;
				return;
			}
			Damage d = new Damage(poisonDamage).setType(DamageType.POISON);
			d.setMaximum(9999);
			entity.getActionManager().register(new HitAction(entity, entity, d, 0));
			poisonDamage -= 10;
			if (isPoisoned()) {
				poisonCount = 30;
				return;
			}
			reset();
		}
	}

	public void reset() {
		poisonDamage = 0;
		poisonCount = 0;
		refresh();
	}

	public void refresh() {
		if (entity instanceof Player) {
			Player player = ((Player) entity);
			player.getIOSession().write(new ConfigPacket(player, 102, isPoisoned() ? 1 : 0));
		}
	}

	public boolean isPoisoned() {
		return poisonDamage >= 1;
	}
}
