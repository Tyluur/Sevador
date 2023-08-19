package com.sevador.game.action.impl.combat;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.ForceMovementAction;
import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Priority;

/**
 * Handles an entity's death.
 * 
 * @author Emperor
 * 
 */
public class DeathAction extends Action {

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
	 * The player's death animation.
	 */
	private static final Animation PLAYER_DEATH_ANIMATION = new Animation(836,
			0, false, Priority.HIGHEST);

	/**
	 * The player's reset animation.
	 */
	private static final Animation PLAYER_RESET = new Animation(-1, 0, false);

	/**
	 * The NPC's reset animation.
	 */
	private static final Animation NPC_RESET = new Animation(-1, 0, true);

	/**
	 * The amount of ticks passed.
	 */
	private int ticks = 0;

	/**
	 * Constructs a new {@code DeathAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 */
	public DeathAction(Entity entity) {
		super(entity);
		addFlag(FLAG | OverloadAction.FLAG
				| (DEFAULT_RESET & ~ForceMovementAction.FLAG));
	}

	@Override
	public boolean execute() {
		if (entity.getActionManager().contains(ForceMovementAction.FLAG)
				|| entity.getActionManager().contains(TeleportAction.FLAG)) {
			return false;
		}
		ticks++;
		switch (ticks) {
		case 2:
			if (entity.isPlayer()) {
				entity.getUpdateMasks().register(PLAYER_DEATH_ANIMATION);
			} else {
				entity.getUpdateMasks()
						.register(
								new Animation(entity.getNPC().getDefinition()
										.getDeathAnimation(), 0, true,
										Priority.HIGHEST));
				if (entity.getNPC().getCombatAction().getLastAttacker() != null)
					entity.getNPC().getCombatAction().getLastAttacker()
							.removeAttribute("entity:last");
			}
			return false;
		case 6:
			entity.onDeath();
			return false;
		case 7:
			if (entity.isPlayer()) {
				entity.getUpdateMasks().register(PLAYER_RESET);
			} else {
				entity.getUpdateMasks().register(NPC_RESET);
			}
			return false;
		case 9:
			return true;
		}
		return false;
	}

	@Override
	public boolean dispose(Action action) {
		return false;
	}

	/**
	 * Gets the player's death containers.
	 * 
	 * @param player
	 *            The player.
	 * @return The containers, index 0 = kept items, index 1 = lost items.
	 */
	public static Container[] getContainers(Player player) {
		Container[] containers = new Container[2];
		Container droppedItems = new Container(Type.ALWAYS_STACK, 42);
		droppedItems.addAll(player.getInventory());
		droppedItems.addAll(player.getEquipment());
		int count = 3;
		if (player.getPrayer().get(PrayerType.PROTECT_ITEM)
				|| player.getPrayer().get(PrayerType.PROTECT_ITEM_CURSE)) {
			count += 1;
		}
		Container keptItems = new Container(Type.NEVER_STACK, count);
		containers[0] = keptItems;
		for (int i = 0; i < count; i++) {
			for (int j = 0; j < 42; j++) {
				Item item = droppedItems.get(j);
				if (item != null) {
					for (int x = 0; x < count; x++) {
						Item kept = keptItems.get(x);
						if (kept == null
								|| kept != null
								&& kept.getDefinition().getHighAlch() <= item
										.getDefinition().getHighAlch()) {
							keptItems.replace(new Item(item.getId(), 1), x);
							x++;
							while (x < count) {
								Item newKept = keptItems.get(x);
								keptItems.replace(kept, x++);
								kept = newKept;
							}
							if (kept != null) {
								droppedItems.add(kept, false);
							}
							item = droppedItems.get(j);
							droppedItems.replace(new Item(item.getId(), item.getAmount() - 1), j);
							break;
						}
					}
				}
			}
		}
		containers[1] = new Container(Type.STANDARD, 42);
		containers[1].addAll(droppedItems);
		return containers;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}