package com.sevador.game.node.model.container.impl;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.impl.combat.AttackTabAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.form.RangeWeapon;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.ContainerListener;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ContainerPacket;
import com.sevador.utility.Constants;
import com.sevador.utility.Priority;

/**
 * The equipment container listener, handles the refreshing of the equipment container.
 * @author Emperor
 *
 */
public class EquipmentContainerListener implements ContainerListener {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Constructs a new {@code EquipmentContainerListener} {@code Object}.
	 * @param player The player.
	 */
	public EquipmentContainerListener(Player player) {
		this.player = player;
	}

	@Override
	public boolean itemChanged(Container container, int slot) {
		return refresh(container);
	}

	@Override
	public boolean itemsAdded(Container container, Item... items) {
		return refresh(container);
	}

	@Override
	public boolean itemsRemoved(Container container, Item... items) {
		return refresh(container);
	}

	@Override
	public boolean refresh(Container container) {
		if (player.getUpdateMasks() != null)
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		if (player.getIOSession() != null)
			player.getIOSession().write(new ContainerPacket(player, 94, container, false));
		int[] bonus = new int[18];
		int slot = 0;
		int increasedLifepoints = 0;
		for (Item item : container.toArray()) {
			if (item != null) {
				if (item.getDefinition() == null)
					item.setDefinition(ItemDefinition.forId(item.getId()));
				increasedLifepoints += getLifepointsBoost(slot, item);
				for (int j = 0; j < 15; j++) {
					if (j == 12) {
						if (bonus[j] == 0) {
							bonus[j] = item.getDefinition().getBonus()[j];
						}
					} else {
						bonus[j] += item.getDefinition().getBonus()[j];
					}
				}
				for (int i = 0; i < item.getDefinition().getAbsorb().length; i++) {
					bonus[15 + i] += item.getDefinition().getAbsorb()[i];
				}
			}
			slot++;
		}
		if (player.getSkills() != null)
			player.getSkills().setLifepointsIncrease(increasedLifepoints);
		if (player.getSkills() != null)
			player.getSkills().heal(0);
		if (player.getProperties() != null)
			player.getProperties().setStats(bonus);
		if (player.getAttribute("autocastId", -1) == -1) {
			if (player.getCombatAction() != null)
				player.getCombatAction().setType(getCombatType(player));
		}
		Animation attackAnim = new Animation(getAttackAnimation(player), 0, false, Priority.HIGHEST);
		if (player.getProperties() != null)
			player.getProperties().setAttackAnimation(attackAnim);
		Animation defenceAnim = new Animation(getDefenceEmote(player), 0, false, Priority.NORMAL);
		if (player.getProperties() != null)
			player.getProperties().setDefenceAnimation(defenceAnim);
		if (player.getProperties() != null)
			player.getProperties().setAttackSpeed(container.getNew(3).getDefinition().getAttackSpeed());
		AttackTabAction.calculateType(player);
		setEquipmentSets(player, container);
		return true;
	}

	/**
	 * Gets the lifepoints boost for an item.
	 * @param slot The item slot.
	 * @param item The item.
	 * @return The amount to boost lifepoints with.
	 */
	private int getLifepointsBoost(int slot, Item item) {
		if (item == null) {
			return 0;
		}
		boolean canHeal = item.getId() > 20134 && item.getId() < 20170;
		if (!canHeal) {
			return 0;
		}
		switch (slot) {
		case Constants.SLOT_HAT:
			return 66;
		case Constants.SLOT_CHEST:
			return 200;
		case Constants.SLOT_LEGS:
			return 134;
		}
		return 0;
	}

	/**
	 * Gets the current combat type used.
	 * @param player The player.
	 * @return The combat type.
	 */
	public static CombatType getCombatType(Player player) {
		if (RangeWeapon.get(player.getEquipment().getNew(3).getId()) != null) {
			return CombatType.RANGE;
		}
		return CombatType.MELEE;
	}

	/**
	 * Gets the player's melee-based attack animation.
	 * @param player The player.
	 * @return The animation id.
	 */
	private static int getAttackAnimation(Player player) {
		int style = player.getSettings().getAttackBox();
		Item item = player.getEquipment().getNew(Constants.SLOT_WEAPON);
		if (item.getId() == 1) {
			return style == 1 ? 423 : 422;
		}
		String itemName = ItemDefinition.forId(item.getId()).getName();
		if (itemName.contains("whip")) {
			return 1658;
		} else if (itemName.contains("scimitar") || itemName.contains("korasi")) {
			return style == 2 ? 15072 : 15071;
		} else if (itemName.contains("rapier")) {
			return 12310;
		} else if (itemName.contains("longsword")) {
			return style == 2 ? 12310 : 12029;
		} else if (itemName.contains("claws")) {
			return style == 1 ? 395 : 393;
		} else if (itemName.contains("dharok")) {
			return style == 2 ? 2067 : 2066;
		} else if (itemName.contains("verac")) {
			return 2062;
		} else if (itemName.contains("granite ma")) {
			return 1665;
		} else if (itemName.contains("guthan")) {
			return style == 2 ? 2081 : 2080;
		} else if (itemName.contains("torag")) {
			return 2068;
		} else if (itemName.contains("godsword") || itemName.contains("2h sword") || itemName.contains("saradomin swor")) {
			return style == 2 ? 11980 : style == 3 ? 11981 : 11979;
		} else if (itemName.equals("keris") || itemName.contains("dagger") || itemName.endsWith("pickaxe")) {
			return style == 2 ? 395 : 396;
		} else if (itemName.contains("halberd")) {
			return 440;
		} else if (itemName.contains("tzhaar-ket-o") || itemName.contains(" maul")) {
			return 13055;
		} else if (itemName.contains("battleaxe") || itemName.contains("hatchet")) {
			return 1833;
		} else if (itemName.contains("anchor")) {
			return 5865;
		} else if (itemName.contains("spear")) {
			return style == 1 ? 12009 : style == 2 ? 12005 : 12006;
		}
		return 12029;
	}

	/**
	 * Gets the player's defence animation.
	 * @param player The player.
	 * @return The defence animation id.
	 */
	private static int getDefenceEmote(Player player) {
		Item weapon = player.getEquipment().getNew(Constants.SLOT_WEAPON);
		Item shield = player.getEquipment().getNew(Constants.SLOT_SHIELD);
		if (weapon.getId() == 1 && shield.getId() == 1) {
			return 424;
		}
		String weaponName = weapon.getDefinition().name.toLowerCase();
		String shieldName = shield.getDefinition().name.toLowerCase();
		if (shieldName.contains("defender")) {
			return 4177;
		} else if (shieldName.contains("shield") || shieldName.contains("toktz-ket-xil")) {
			return 1156;
		} else if (weaponName.contains("godsword") || weaponName.contains("2h sword") || weaponName.contains("saradomin sword")) {
			return 7050;
		} else if (weaponName.contains("keris") || weaponName.contains("dagger")) {
			return 403;
		} else if (weaponName.contains("staff of light")) {
			return shield.getId() == 1 ? 12806 : 13038;
		} else if (weaponName.contains("claws")) {
			return 12004;
		} else if (weaponName.contains("spear")) {
			return 12008;
		} else if (weaponName.contains("maul")) {
			return 13054;
		}
		return 424;
	}

	/**
	 * Sets the equipment set attributes.
	 * @param player The player.
	 * @param c The container.
	 */
	private static void setEquipmentSets(Player player, Container c) {
		player.removeAttribute("set-Dharok");
		player.removeAttribute("set-Verac");
		player.removeAttribute("set-Guthan");
		player.removeAttribute("set-Void:melee");
		player.removeAttribute("set-Void:range");
		player.removeAttribute("set-Void:magic");
		String gloves = c.getNew(Constants.SLOT_HANDS).getDefinition().name.toLowerCase();
		String hat = c.getNew(Constants.SLOT_HAT).getDefinition().name.toLowerCase();
		String body = c.getNew(Constants.SLOT_CHEST).getDefinition().name.toLowerCase();
		String legs = c.getNew(Constants.SLOT_LEGS).getDefinition().name.toLowerCase();
		String weapon = c.getNew(Constants.SLOT_WEAPON).getDefinition().name.toLowerCase();
		if (gloves.contains("void") && body.contains("void") && legs.contains("void") && hat.contains("void")) {
			if (hat.contains("melee")) {
				player.setAttribute("set-Void:melee", true);
			} else if (hat.contains("rang")) {
				player.setAttribute("set-Void:range", true);
			} else if (hat.contains("magic")) {
				player.setAttribute("set-Void:magic", true);
			}
		} else if (weapon.contains("dharok") && body.contains("dharok") && legs.contains("dharok") && hat.contains("dharok")) {
			player.setAttribute("set-Dharok", true);
		} else if (weapon.contains("verac") && body.contains("verac") && legs.contains("verac") && hat.contains("verac")) {
			player.setAttribute("set-Verac", true);
		} else if (weapon.contains("guthan") && body.contains("guthan") && legs.contains("guthan") && hat.contains("guthan")) {
			player.setAttribute("set-Guthan", true);
		}
	}
}
