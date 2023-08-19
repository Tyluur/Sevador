package com.sevador.game.action.impl.combat;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;

/**
 * Handles the overload effect action.
 * @author Emperor
 *
 */
public final class OverloadAction extends Action {

	/**
	 * The animaiton.
	 */
	private static final Animation ANIMATION = new Animation(3170, 0, false);
	
	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * Constructs a new {@code OverloadAction} {@code Object}.
	 * @param entity The entity.
	 * @param ticks The amount of ticks left.
	 */
	public OverloadAction(Entity entity, int ticks) {
		super(entity);
		addFlag(FLAG);
		update(entity.getPlayer());
		entity.getPlayer().getCredentials().setOverloadTicks(ticks);
	}

	@Override
	public boolean execute() {
		int ticks = entity.getPlayer().getCredentials().getOverloadTicks();
		if (ticks == 1) {
			entity.getSkills().heal(500);
			for (int i = 0; i < 7; i++) {
				if (i != 3 && i != 5) {
					entity.getSkills().setLevel(i, entity.getSkills().getStaticLevel(i));
				}
			}
			entity.getPlayer().getPacketSender().sendMessage("<col=FF0000>The effects of overload have worn off, and you feel normal again.</col>");
			entity.getPlayer().getCredentials().setOverloadTicks(-1);
			return true;
		} else if (ticks % 25 == 0) {
			update(entity.getPlayer());
		} else {
			switch (ticks) {
			case 497:
			case 495:
			case 493:
			case 491:
			case 489:
				entity.getUpdateMasks().register(ANIMATION);
				entity.getActionManager().register(new HitAction(entity, entity, 
						new Damage(100).setType(DamageType.RED_DAMAGE), 0));
				break;
			}
		}
		entity.getPlayer().getCredentials().setOverloadTicks(ticks - 1);
		return false;
	}
	
	/**
	 * Updates the effect for the player.
	 * @param p The player.
	 */
	private static void update(Player p) {
		for (int i = 0; i < 3; i++) {
			int staticLevel = p.getSkills().getStaticLevel(i);
			int amount = (int) (5 + (staticLevel * 0.23));
			p.getSkills().updateLevel(i, amount, staticLevel + amount);
		}
		int staticLevel = p.getSkills().getStaticLevel(Skills.RANGE);
		int amount = (int) (4 + (staticLevel * 0.1923076923076923));
		p.getSkills().updateLevel(Skills.RANGE, amount, staticLevel + amount);
		staticLevel = p.getSkills().getStaticLevel(Skills.MAGIC);
		p.getSkills().updateLevel(Skills.MAGIC, 7, staticLevel + 7);
	}
	
	@Override
	public int getActionType() {
		return FLAG;
	}

}