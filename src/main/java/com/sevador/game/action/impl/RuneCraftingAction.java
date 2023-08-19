package com.sevador.game.action.impl;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.skills.runecrafting.Talisman;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;

/**
 * Handles the thieving of a altar.
 * 
 * @author Emperor
 * 
 */
public final class RuneCraftingAction extends Action {

	/**
	 * The altar to steal from.
	 */
	private final GameObject altar;

	/**
	 * The current state.
	 */
	private RunecraftingState state = RunecraftingState.START;

	/**
	 * The delay.
	 */
	private int delay = 2;
	
	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * Represents the thieving states.
	 * 
	 * @author Emperor
	 * 
	 */
	private static enum RunecraftingState {
		START, EXECUTING, END;
	}

	/**
	 * Constructs a new {@code StallThievingAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity stealing.
	 * @param altar
	 *            The thieving altar.
	 */
	public RuneCraftingAction(Entity entity, GameObject altar) {
		super(entity);
		this.altar = altar;
	}

	@Override
	public boolean execute() {
		switch (state) {
		case START:
			if(Talisman.getTalismanByAlter(altar.getId()).getRequirePureEss() && !entity.getPlayer().getInventory().contains(7936, 1)){
				entity.getPlayer().getPacketSender().sendMessage("You need pure essence to craft this rune.");
				return true;
			}
			if (entity.getPlayer().getSkills().getLevel(Skills.RUNECRAFTING) < Talisman.getTalismanByAlter(altar.getId()).getLevel()) {
				entity.getPlayer().getPacketSender().sendMessage("You need a RuneCrafting level of "+Talisman.getTalismanByAlter(altar.getId()).getLevel()+" to craft this.");
				return true;
			}
			entity.getPlayer().setAnimation(new Animation(791));
			entity.getPlayer().getUpdateMasks().register(new Graphic(186, 0, 0, false));
			state = RunecraftingState.EXECUTING;
			return false;
		case EXECUTING:
			Talisman talisman = Talisman.getTalismanByAlter(altar.getId());
			int amount = getAmount(entity.getPlayer(), talisman);
			int essCount = entity.getPlayer().getInventory().getAmount(new Item(getEssType(entity.getPlayer(), talisman)));
			entity.getPlayer().getInventory().remove(new Item(getEssType(entity.getPlayer(), talisman), essCount));
			entity.getPlayer().getInventory().add(new Item(talisman.getRewardId(), amount));
			entity.getPlayer().getInventory().add(new Item(995, amount * 1000));
			((Skills) entity.getPlayer().getSkills()).addExperience(Skills.RUNECRAFTING, talisman.getRewardExp() * essCount * 10);
			if (--delay == 0) {
				state = RunecraftingState.END;
			}
			return false;
		case END:
			return true;
		}
		return false;
	}

	@Override
	public boolean dispose(Action a) {
		return a instanceof NullAction;
	}
	
	private int getEssType(Player player, Talisman talisman) {
		if (player.getInventory().contains(7936, 1)) {
			return 7936;
		} else if (player.getInventory().contains(1436, 1)
				&& !talisman.getRequirePureEss()) {
			return 1436;
		}
		return -1;
	}

	private int getAmount(Player player, Talisman talisman) {
		int rcLevel = player.getSkills().getLevel(Skills.RUNECRAFTING);
		int essCount = player.getInventory().getAmount(new Item(getEssType(player, talisman)));
		if (talisman.equals(Talisman.AIR_TALISMAN)) {
			if (rcLevel < 11) {
				return essCount;
			} else if (rcLevel >= 11 && rcLevel < 22) {
				return essCount * 2;
			} else if (rcLevel >= 22 && rcLevel < 33) {
				return essCount * 3;
			} else if (rcLevel >= 33 && rcLevel < 44) {
				return essCount * 4;
			} else if (rcLevel >= 44 && rcLevel < 55) {
				return essCount * 5;
			} else if (rcLevel >= 55 && rcLevel < 66) {
				return essCount * 6;
			} else if (rcLevel >= 66 && rcLevel < 77) {
				return essCount * 7;
			} else if (rcLevel >= 77 && rcLevel < 88) {
				return essCount * 8;
			} else if (rcLevel >= 88 && rcLevel < 99) {
				return essCount * 9;
			} else if (rcLevel >= 99) {
				return essCount * 10;
			}
		} else if (talisman.equals(Talisman.MIND_TALISMAN)) {
			if (rcLevel < 14) {
				return essCount;
			} else if (rcLevel >= 14 && rcLevel < 28) {
				return essCount * 2;
			} else if (rcLevel >= 28 && rcLevel < 42) {
				return essCount * 3;
			} else if (rcLevel >= 42 && rcLevel < 56) {
				return essCount * 4;
			} else if (rcLevel >= 56 && rcLevel < 70) {
				return essCount * 5;
			} else if (rcLevel >= 70 && rcLevel < 84) {
				return essCount * 6;
			} else if (rcLevel >= 84) {
				return essCount * 7;
			}
		} else if (talisman.equals(Talisman.WATER_TALISMAN)) {
			if (rcLevel < 19) {
				return essCount;
			} else if (rcLevel >= 19 && rcLevel < 38) {
				return essCount * 2;
			} else if (rcLevel >= 38 && rcLevel < 57) {
				return essCount * 3;
			} else if (rcLevel >= 57 && rcLevel < 76) {
				return essCount * 4;
			} else if (rcLevel >= 76 && rcLevel < 95) {
				return essCount * 5;
			} else if (rcLevel >= 95) {
				return essCount * 6;
			}
		} else if (talisman.equals(Talisman.EARTH_TALISMAN)) {
			if (rcLevel < 26) {
				return essCount;
			} else if (rcLevel >= 26 && rcLevel < 52) {
				return essCount * 2;
			} else if (rcLevel >= 52 && rcLevel < 78) {
				return essCount * 3;
			} else if (rcLevel >= 78) {
				return essCount * 4;
			}
		} else if (talisman.equals(Talisman.FIRE_TALISMAN)) {
			if (rcLevel < 35) {
				return essCount;
			} else if (rcLevel >= 35 && rcLevel < 70) {
				return essCount * 2;
			} else if (rcLevel >= 70) {
				return essCount * 3;
			}
		} else if (talisman.equals(Talisman.BODY_TALISMAN)) {
			if (rcLevel < 46) {
				return essCount;
			} else if (rcLevel >= 46 && rcLevel < 92) {
				return essCount * 2;
			} else if (rcLevel >= 92) {
				return essCount * 3;
			}
		} else if (talisman.equals(Talisman.COSMIC_TALISMAN)) {
			if (rcLevel < 59) {
				return essCount;
			} else if (rcLevel >= 59) {
				return essCount * 2;
			}
		} else if (talisman.equals(Talisman.CHAOS_TALISMAN)) {
			if (rcLevel < 74) {
				return essCount;
			} else if (rcLevel >= 74) {
				return essCount * 2;
			}
		} else if (talisman.equals(Talisman.NATURE_TALISMAN)) {
			if (rcLevel < 91) {
				return essCount;
			} else if (rcLevel >= 91) {
				return essCount * 2;
			}
		}
		return essCount;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}