package com.sevador.game.action.impl;

import net.burtleburtle.cache.format.CacheItemDefinition;

import com.sevador.content.ItemConstant;
import com.sevador.content.quest.impl.CooksAssistant;
import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.utility.Constants;

/**
 * Handles the equipping/unequipping of an item.
 * 
 * @author Emperor
 * 
 */
public class EquipAction extends Action {

	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * The item id.
	 */
	private int itemId;

	/**
	 * The item slot.
	 */
	private final int itemSlot;

	/**
	 * If we have to equip, rather than unequip.
	 */
	private final boolean equip;

	/**
	 * Constructs a new {@code EquipAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 * @param itemId
	 *            The item id to handle.
	 * @param slot
	 *            The item slot to handle.
	 * @param equip
	 *            If the action is an equip action, rather than an unequip
	 *            action.
	 */
	public EquipAction(Entity entity, int itemId, int slot, boolean equip) {
		super(entity);
		addFlag((DEFAULT_RESET & ~ActionFlag.CLOSE_INTERFACE)
				& ~MovementAction.FLAG);
		this.itemId = itemId;
		this.itemSlot = slot;
		this.equip = equip;
	}

	public boolean canWeildCape(String name) {
		name = name.toLowerCase();
		Player p = entity.getPlayer();
		if (name.contains("strength") && p.getSkills().getLevel(Skills.STRENGTH) < 99) {
			p.getPacketSender().sendMessage("You need a Strength level of 99 to weild this item.");
			return false;
		}
		if (name.contains("attack") && p.getSkills().getLevel(Skills.ATTACK) < 99) {
			p.getPacketSender().sendMessage("You need an Attack level of 99 to weild this item.");
			return false;
		}
		if (name.contains("constitution") && p.getSkills().getLevel(Skills.HITPOINTS) < 99) {
			p.getPacketSender().sendMessage("You need a Constitution level of 99 to weild this item.");
			return false;
		}
		if (name.contains("defence") && p.getSkills().getLevel(Skills.DEFENCE) < 99) {
			p.getPacketSender().sendMessage("You need a Defence level of 99 to weild this item.");
			return false;
		}
		if (name.contains("ranging") && p.getSkills().getLevel(Skills.RANGE) < 99) {
			p.getPacketSender().sendMessage("You need a Ranged level of 99 to weild this item.");
			return false;
		}
		if (name.contains("prayer") && p.getSkills().getLevel(Skills.PRAYER) < 99) {
			p.getPacketSender().sendMessage("You need a Prayer level of 99 to weild this item.");
			return false;
		}
		if (name.contains("magic cape") || name.contains("magic hood") && p.getSkills().getLevel(Skills.MAGIC) < 99) {
			p.getPacketSender().sendMessage("You need a Magic level of 99 to weild this item.");
			return false;
		}
		if (name.equals("runecraft cape") && p.getSkills().getLevel(Skills.RUNECRAFTING) < 99) {
			p.getPacketSender().sendMessage("You need a RuneCrafting level of 99 to wield this item.");
			return false;
		}
		if (name.contains("runecrafting hood") && p.getSkills().getLevel(Skills.RUNECRAFTING) < 99) {
			p.getPacketSender().sendMessage("You need a RuneCrafting level of 99 to wield this item.");
			return false;
		}
		if (name.contains("smithing ") && p.getSkills().getLevel(Skills.SMITHING) < 99) {
			p.getPacketSender().sendMessage("You need a Smithing level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("mining ") && p.getSkills().getLevel(Skills.MINING) < 99) {
			p.getPacketSender().sendMessage("You need a Mining level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("slayer ") && p.getSkills().getLevel(Skills.SLAYER) < 99) {
			p.getPacketSender().sendMessage("You need a Slayer level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("firemaking ") && p.getSkills().getLevel(Skills.FIREMAKING) < 99) {
			p.getPacketSender().sendMessage("You need a Firemaking level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("woodcut. ") || name.contains("woodcutting ") && p.getSkills().getLevel(Skills.WOODCUTTING) < 99) {
			p.getPacketSender().sendMessage("You need a Woodcutting level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("fishing ") && p.getSkills().getLevel(Skills.FISHING) < 99) {
			p.getPacketSender().sendMessage("You need a Fishing level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("cooking ") && p.getSkills().getLevel(Skills.COOKING) < 99) {
			p.getPacketSender().sendMessage("You need a Cooking level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("farming ") && p.getSkills().getLevel(Skills.FARMING) < 99) {
			p.getPacketSender().sendMessage("You need a Farming level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("fletching ") && p.getSkills().getLevel(Skills.FLETCHING) < 99) {
			p.getPacketSender().sendMessage("You need a Fletching level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("thieving ") && p.getSkills().getLevel(Skills.THIEVING) < 99) {
			p.getPacketSender().sendMessage("You need a Thieving level of 99 to weild this item.");
			return false;
		}	
		if (name.contains("hunter ") && p.getSkills().getLevel(Skills.HUNTER) < 99) {
			p.getPacketSender().sendMessage("You need a Hunter level of 99 to weild this item.");
			return false;
		}
		if (name.contains("herblore ") && p.getSkills().getLevel(Skills.HERBLORE) < 99) {
			p.getPacketSender().sendMessage("You need a Herblore level of 99 to weild this item.");
			return false;
		}
		return true;
	}

	@Override
	public boolean execute() {
		try {
			Player p = null;
			if (entity.isPlayer()) {
				p = entity.getPlayer();
				if (equip) {
					Item item = new Item(
							p.getInventory().get(itemSlot).getId(), p
							.getInventory().get(itemSlot).getAmount());
					if (!canWeildCape(item.getDefinition().name)) return true;
					if (item == null || item.getId() != itemId
							|| item.getDefinition().getEquipmentSlot() < 0) {
						return true;
					}
					if (item.getId() >= 7454 && item.getId() >= 7462 && item.getDefinition().name.toLowerCase().contains("glove")) {
						if (!p.getQuestListener().get(CooksAssistant.ID).completed()) {
							p.getPacketSender().sendMessage("You must finish the Cook's Assistant quest to equip RFD gloves.");
							return true;
						}
					}
					CacheItemDefinition def = item.getDefinition().getCacheDefinition();
					if (def.levelRequirements != null) {
						for (int skill : def.levelRequirements.keySet()) {
							if (skill < Skills.SKILL_NAMES.length) {
								if (p.getSkills().getStaticLevel(skill) < def.levelRequirements.get(skill)) {
									String prefix = skill == 0 || skill == 16 ? "an " : "a ";
									p.getPacketSender().sendMessage("You are not high enough level to use this item.");
									p.getPacketSender().sendMessage("You need to have " + prefix + Skills.SKILL_NAMES[skill].toLowerCase() + " level of " + def.levelRequirements.get(skill) + " to wear this item.");
									return true;
								}
							}
						}
					}
					if (item.getDefinition().getEquipmentSlot() == Constants.SLOT_WEAPON) {
						p.getSettings().setSpecialEnabled(false);
					}
					Player player = p;
					int newId = ItemConstant.getDegradeItemWhenWear(itemId);
					if (newId != -1) {
						player.getPacketSender().sendMessage("Your " +new Item(itemId, 1).getDefinition().getName() + " degraded.");
						item = new Item(newId, p.getInventory().get(itemSlot).getAmount());
					}
					int freeSlots = p.getInventory().freeSlots();
					Item equipped = p.getEquipment().get(item.getDefinition().getEquipmentSlot());
					Item weapon = p.getEquipment().get(3);
					if (item.getDefinition().getEquipmentSlot() == 3
							&& item.getDefinition().isTwoHanded()
							&& p.getEquipment().get(5) != null) {
						if (freeSlots < 1) {
							return true;
						}
						p.getEquipment().replace(item, 3, false);
						p.getInventory().replace(equipped, itemSlot, false);
						p.getInventory().add(p.getEquipment().get(5), true);
						p.getEquipment().replace(null, 5);
						return true;
					}
					if (item.getDefinition().getEquipmentSlot() == 5
							&& weapon != null
							&& weapon.getDefinition().isTwoHanded()) {
						if (freeSlots < 1) {
							return true;
						}
						p.getInventory().replace(weapon, itemSlot);
						p.getEquipment().replace(null, 3, false);
						p.getEquipment().replace(item, 5);
						return true;
					}
					if (item.getDefinition().isStackable() && equipped != null
							&& equipped.getId() == item.getId()) {
						p.getInventory().replace(null, itemSlot);
						equipped.setAmount(equipped.getAmount()
								+ item.getAmount());
						p.getEquipment().refresh();
						return true;
					} else {
						//WTF ???? I dont understand this code at all
						if (equipped != null) {
							for (int i = 0; i < 28; i++) {
								if (p.getInventory().getItem(i) != null) {
									if (p.getInventory().getItem(i).getId() == equipped.getId() && equipped.getDefinition().isStackable()) {
										p.getInventory().add(equipped);
										p.getInventory().replace(null, itemSlot);
										p.getEquipment().replace(item, item.getDefinition().getEquipmentSlot());
										return true;
									}
								}
							}
						}
						p.getInventory().replace(equipped, itemSlot); 
						p.getEquipment().replace(item, item.getDefinition().getEquipmentSlot());
						//here's the lines. ok whats wrong watch this..
						return true;
					}
				} else {
					Item item = new Item(itemId, 2147483647);
					int slot = p.getEquipment().getSlotById(itemId);
					Item equipped = slot != -1 ? p.getEquipment().get(slot) : null;
					if (slot == -1) {
						return true;
					}
					if (item.getDefinition().getEquipmentSlot() == Constants.SLOT_WEAPON) {
						p.getSettings().setSpecialEnabled(false);
					}
					int invslot = p.getInventory().getSlotById(itemId);
					if (item.getDefinition().isStackable() && invslot != -1) {
						p.getInventory().replace(
								new Item(itemId, equipped.getAmount()
										+ p.getInventory().get(invslot).getAmount()),
										invslot);
						p.getEquipment().replace(null, slot);
						return true;
					}
					if (p.getInventory().freeSlots() < 1) {
						return true;
					}
					p.getEquipment().replace(null, slot);
					p.getInventory().add(equipped);
					return true;
				}
			}
		} catch (Throwable t) {}
		return true;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}