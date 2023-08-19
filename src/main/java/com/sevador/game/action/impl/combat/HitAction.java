package com.sevador.game.action.impl.combat;

import net.burtleburtle.tick.Tick;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.packetactions.EmoteAction;
import com.sevador.game.action.impl.packetactions.RestAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.game.node.model.mask.ForceTextUpdate;
import com.sevador.game.world.World;
import com.sevador.utility.Misc;

/**
 * Handles receiving a hit.
 * @author Emperor
 *
 */
public class HitAction extends Action {

	/**
	 * The flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * The damage to deal.
	 */
	private final Damage damage;
	
	/**
	 * The entity dealing the hit.
	 */
	private final Entity source;
	
	/**
	 * The amount of ticks delay before hitting.
	 */
	private int delay = 0;
	
	/**
	 * Constructs a new {@code HitAction} {@code Object}.
	 * @param entity The entity being hit.
	 * @param source The entity dealing the damage.
	 * @param damage The damage to deal.
	 * @param delay The delay before dealing the hit.
	 */
	public HitAction(Entity entity, Entity source, Damage damage, int delay) {
		super(entity);
		addFlag(ActionFlag.CLOSE_CHATBOX | ActionFlag.CLOSE_INTERFACE | RestAction.FLAG);
		this.source = source;
		this.damage = damage;
		this.delay = delay;
	}
	
	@Override
	public boolean execute() {
		if (delay-- > 0) {
			return false;
		}
		if (entity.getActionManager().contains(EmoteAction.FLAG)) {
			/*
			 * Queue the hit if we're doing an emote.
			 */
			return false;
		}
		int dealt = damage.getHit() - entity.getSkills().hit(damage.getHit());
		int currentHitpoints = entity.getSkills().getLifepoints();
		int maximumHitpoints = entity.getSkills().getMaximumLifepoints();
		if (currentHitpoints > maximumHitpoints) {
			damage.setLifepoints(255);
		} else {
			damage.setLifepoints(currentHitpoints * 255 / maximumHitpoints);
		}
		damage.setSource(source);
		damage.setHit(dealt);
		entity.getDamageMap().submitDamage(source, dealt);
		entity.getDamageMap().getDamageList().add(damage);
		entity.setAttackedByDelay(Misc.currentTimeMillis() + 8000);
		if (entity.getSkills().getLifepoints() < 1) {
			World.getWorld().submit(new Tick(0) {
				@Override
				public boolean run() {
					entity.getActionManager().register(new DeathAction(entity));
					return true;
				}				
			});
		}
		if (damage.getVenged() > 0 && entity.getAttribute("vengeance", false)) {
			entity.removeAttribute("vengeance");
			World.getWorld().submit(new Tick(1) {
				@Override
				public boolean run() {
					entity.getUpdateMasks().register(new ForceTextUpdate("Taste vengeance!", entity.isNPC()));
					source.getActionManager().register(new HitAction(source, entity, new Damage(damage.getVenged()).setType(DamageType.DEFLECT), 1));
					return true;
				}				
			});
		}
		if (damage.getRecoiled() > 0) {
			source.getActionManager().register(new HitAction(source, entity, new Damage(damage.getRecoiled()).setType(DamageType.DEFLECT), 1));
		}
		if (damage.getDeflected() > 0) {
			source.getActionManager().register(new HitAction(source, entity, new Damage(damage.getDeflected()).setType(DamageType.DEFLECT), 1));
		}
		if (entity != source && entity.getCombatAction().getVictim() == null) {
			if (entity.isNPC() || entity.getPlayer().getSettings().isRetaliating()) {
				entity.getCombatAction().setVictim(source);
				entity.getActionManager().register(entity.getCombatAction());
			}
		}
		return true;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}
