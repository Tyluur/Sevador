package com.sevador.game.action.impl.combat;

import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.tick.Tick;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.packetactions.EmoteAction;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Movement;
import com.sevador.game.world.World;
import com.sevador.network.out.MessagePacket;

/**
 * Represents a combat action.
 * @author Emperor
 *
 */
public class CombatAction extends Action {

	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * Add to the default reset flag.
	 */
	static {
		DEFAULT_RESET |= FLAG;
	}
	
	/**
	 * The combat movement used.
	 */
	private Movement movement;
		
	/**
	 * The amount of ticks left before next attack.
	 */
	private int ticks = 0;
	
	/**
	 * The handler used for this combat system.
	 */
	private CombatType type = CombatType.MELEE;
	
	/**
	 * The current victim.
	 */
	private Entity victim;
	
	/**
	 * Constructs a new {@code CombatAction} {@code Object}.
	 * @param entity The entity.
	 */
	public CombatAction(Entity entity) {
		super(entity);
		addFlag(DEFAULT_RESET | EmoteAction.FLAG);
		this.movement = new Movement(entity);
	}

	@Override
	public boolean execute() {
		entity.turnTo(victim);
		if (victim == null) {
			return true;
		} else if (entity.getActionManager().contains(DeathAction.FLAG) || victim.getActionManager().contains(DeathAction.FLAG)) {
			reset();
			return true;
		} else if (!movement.go(victim, type)) {
			return false;
		} else if (ticks <= MajorUpdateWorker.getTicks()) {
			if (!victim.isAttackable(entity)) {
				reset();
				return true;
			}
			ticks = MajorUpdateWorker.getTicks();
			increaseTicks(entity.getProperties().getAttackSpeed(), type == CombatType.MAGIC);
			final Interaction interaction = new Interaction(entity, victim);
			if (entity.getHandler(type).handle(interaction) && interaction.commence()) {
				if (victim != null) {
					victim.setAttribute("tick:combat", MajorUpdateWorker.getTicks() + 17);
					victim.setAttribute("entity:last", entity);
				}
				World.getWorld().submit(new Tick(1) {
					@Override
					public boolean run() {
						try {
							return interaction.tick();
						} catch (Throwable t) {
							t.printStackTrace();
						}
						return true;
					}
				});
			} else {
				reset();
			}
		}
		return false;
	}
	
	@Override
	public boolean dispose(Action action) {
		if (action.getActionType() == FLAG) {
			return true;
		}
		if (action instanceof EmoteAction) {
			entity.getPlayer().getIOSession().write(new MessagePacket(entity.getPlayer(), "You can't do emotes while you're under attack."));
			return false;
		}
		reset();
		return true;
	}

	/**
	 * Resets the combat action.
	 */
	public void reset() {
		victim = null;
		entity.getActionManager().unregister(this, MovementAction.FLAG);
		entity.turnTo(null);
	}
	
	/**
	 * Increases the amount of combat ticks.
	 * @param amount The amount of ticks.
	 * @param magic If the combat type used is magic.
	 */
	public void increaseTicks(int amount, boolean magic) {
		if (!magic && entity.getAttribute("miasmicTime", -1) > MajorUpdateWorker.getTicks()) {
			amount *= 1.5;
		}
		ticks += amount;
	}
	
	/**
	 * Gets the amount of ticks before next attack.
	 * @return The amount of ticks.
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * Gets the movement object.
	 * @return The movement.
	 */
	public Movement getMovement() {
		return movement;
	}
	
	/**
	 * @return the type
	 */
	public CombatType getCombatType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(CombatType type) {
		this.type = type;
	}

	/**
	 * @return the victim
	 */
	public Entity getVictim() {
		return victim;
	}

	/**
	 * @param victim the victim to set
	 */
	public void setVictim(Entity victim) {
		this.victim = victim;
	}
	
	/**
	 * Gets the last attacking entity.
	 * @return The last attacking entity.
	 */
	public Entity getLastAttacker() {
		if (entity.getAttribute("tick:combat", -1) <= MajorUpdateWorker.getTicks()) {
			entity.removeAttribute("entity:last");
		}
		return entity.getAttribute("entity:last");
	}
	
	@Override
	public int getActionType() {
		return FLAG;
	}

}