package com.sevador.game.node.model.mask;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.utility.Constants;
import com.sevador.utility.Priority;

/**
 * Represents an Entity's update masks.
 * @author Emperor
 *
 */
public class UpdateMasks {

	/**
	 * The mask data.
	 */
	private int maskData = 0;

	/**
	 * If we're updating this Entity.
	 */
	private boolean updating;

	/**
	 * The current animation priority.
	 */
	private Priority animationPriority;

	/**
	 * Our priority queue used.
	 */
	public final PriorityQueue<UpdateFlag> flagQueue = new PriorityQueue<UpdateFlag>();

	/**
	 * A queue holding all update flags 
	 */
	private final List<UpdateFlag> queuedUpdates = new LinkedList<UpdateFlag>();

	/**
	 * Registers an update flag.
	 * @param updateFlag The update flag.
	 */
	public void register(UpdateFlag updateFlag) {
		if (updating) {
			queuedUpdates.add(updateFlag);
			return;
		}
		if (!updateFlag.canRegister(this)) {
			return;
		}
		if ((maskData & updateFlag.getMaskData()) != 0) {
			flagQueue.remove(updateFlag);
		}
		maskData |= updateFlag.getMaskData();
		flagQueue.add(updateFlag);
	}

	/**
	 * Prepares the outgoing packet for updating.
	 * @param e The entity who's using this update mask instance.
	 */
	public void prepare(Entity e) {
		if (e.isPlayer()) {
			if (e.getPlayer().getCredentials().getAppearance() != null)
				e.getPlayer().getCredentials().getAppearance().prepareBodyData(e.getPlayer());
			if (e.getWalkingQueue().getWalkDir() != -1 || e.getWalkingQueue().getRunDir() != -1) {
				register(new MovementUpdate(e.getPlayer()));
			}
		}
		if (e.getDamageMap().getDamageList().size() > 0) {
			register(new HitUpdate(e));
		}
		updating = true;
	}

	/**
	 * Finishes the updating.
	 */
	public void finish() {
		animationPriority = Priority.LOWEST;
		maskData = 0;
		flagQueue.clear();
		updating = false;
		for (UpdateFlag flag : queuedUpdates) {
			register(flag);
		}
		queuedUpdates.clear();
	}

	public boolean equip(Player player, int itemId, int itemSlot, boolean equip) {
		try {
			Player p = null;
			if (player.isPlayer()) {
				p = player.getPlayer();
				if (equip) {
					Item item = new Item(
							p.getInventory().get(itemSlot).getId(), p
							.getInventory().get(itemSlot).getAmount());
					if (item == null || item.getId() != itemId
							|| item.getDefinition().getEquipmentSlot() < 0) {
						return true;
					}
					HashMap<Integer, Integer> requiriments = item.getDefinition().getWearingSkillRequiriments();
					boolean hasRequiriments = true;
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0)
								continue;
							int level = requiriments.get(skillId);
							if (level < 0 || level > 120)
								continue;
							if (p.getSkills().getLevel(skillId) < level) {
								if (hasRequiriments) {
									p.getPacketSender()
									.sendMessage(
											"You are not high enough level to use this item.",
											true);
								}
								hasRequiriments = false;
								String name = Skills.SKILL_NAMES[skillId]
										.toLowerCase();
								p.getPacketSender().sendMessage(
										"You need to have a"
												+ (name.startsWith("a") ? "n"
														: "") + " " + name
														+ " level of " + level + ".",
														true);
							}

						}
					}
					if (!hasRequiriments)
						return true;
					if (item.getDefinition().getEquipmentSlot() == Constants.SLOT_WEAPON) {
						p.getSettings().setSpecialEnabled(false);
					}
					int freeSlots = p.getInventory().freeSlots();
					Item equipped = p.getEquipment().get(
							item.getDefinition().getEquipmentSlot());
					Item weapon = p.getEquipment().get(3);
					if (item.getDefinition().getEquipmentSlot() == 3
							&& item.getDefinition().isTwoHanded()
							&& p.getEquipment().get(5) != null) {
						if (freeSlots < 1) {
							return true;
						}
						p.getEquipment().replace(item, 3, false);
						p.getInventory().replace(equipped, itemSlot, false);
						p.getInventory().add(p.getEquipment().get((byte) 5), true);
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
						p.getInventory().replace(equipped, itemSlot);
						p.getEquipment().replace(item,
								item.getDefinition().getEquipmentSlot());
						return true;
					}
				} else {
					Item item = new Item(itemId, 2147483647);
					int slot = p.getEquipment().getSlotById(itemId);
					Item equipped = slot != -1 ? p.getEquipment().get(slot)
							: null;
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
										+ p.getInventory().get(invslot)
										.getAmount()), invslot);
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

	/**
	 * Checks if an update is required.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isUpdateRequired() {
		return maskData != 0;
	}

	/**
	 * Checks if an update flag was registered.
	 * @param data The mask data of the update flag.
	 * @return {@code True} if the update flag was registered, {@code false} if not.
	 */
	public boolean get(int data) {
		return (maskData & data) != 0;
	}

	/**
	 * Gets the mask data.
	 * @return The mask data.
	 */
	public int getMaskData() {
		return maskData;
	}

	/**
	 * @return the animationPriority
	 */
	public Priority getAnimationPriority() {
		return animationPriority;
	}

	/**
	 * @param animationPriority the animationPriority to set
	 */
	public void setAnimationPriority(Priority animationPriority) {
		this.animationPriority = animationPriority;
	}
}