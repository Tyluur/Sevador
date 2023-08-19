package com.sevador.game.node.npc.impl.sum;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.npc.impl.Familiar;
import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ContainerPacket;

/**
 * Represents a beast of burden familiar.
 * @author Emperor
 *
 */
@SuppressWarnings("serial")
public class BurdenBeast extends Familiar {

	/**
	 * The deposit options client scrip arguments.
	 */
	private static final Object[] DEPOSIT_OPTIONS = new Object[] { "", "", "", "", "Store-X", "Store-All", "Store-10", "Store-5", "Store-1", -1, 0, 7, 4, 90, 665 << 16 };
	
	/**
	 * The withdraw options client script arguments.
	 */
	private static final Object[] WITHDRAW_OPTIONS = new Object[] { "", "", "", "", "Withdraw-X", "Withdraw-All", "Withdraw-10", "Withdraw-5", "Withdraw-1", -1, 0, 5, 6, 30, 671 << 16 | 27 };

	/**
	 * The animation the player does when he stores an item.
	 */
	private static final Animation STORE_ANIMATION = new Animation(827, 0, false);
	
	/**
	 * The maximum amount of items.
	 */
	private final int size;

	/**
	 * The container.
	 */
	private final Container container;
	
	/**
	 * Constructs a new {@code BurdenBeast} {@code Object}.
	 * @param id The npc id.
	 * @param ticks The amount of ticks.
	 * @param size The maximum amount of items in the inventory.
	 */
	public BurdenBeast(int id, int ticks, int size) {
		super(id, ticks);
		this.size = size;
		this.container = new Container(Type.STANDARD, size);
	}
	
	@Override
	public boolean isBurdenBeast() {
		return true;
	}
	
	@Override
	public BurdenBeast getBurdenBeast() {
		return this;
	}
	
	@Override
	public void onDeath() {
		super.onDeath();
		dropAllItems();
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		dropAllItems();
	}
	
	/**
	 * Drops all the items.
	 */
	private void dropAllItems() {
		for (Item item : container.toArray()) {
			if (item != null) {
				GroundItemManager.createGroundItem(new GroundItem(owner, item, getLocation(), false));
			}
		}
		container.clear();
	}

	/**
	 * Stores items on the container.
	 * @param item The item.
	 * @param slot The item slot.
	 * @param amount The amount to store.
	 */
	public void store(int itemId, int slot, int amount) {
		Item inventoryItem = owner.getInventory().get(slot);
		int freeSlots = container.freeSlots();
		if (amount > freeSlots && !inventoryItem.getDefinition().isStackable()) {
			if (freeSlots == 0) {
				owner.getPacketSender().sendMessage("Your familiar can't hold any more items.");
				return;
			}
			amount = freeSlots;
		}
		int inventoryAmount = owner.getInventory().getAmount(new Item(itemId));
		if (amount > inventoryAmount) {
			amount = inventoryAmount;
		}
		if (inventoryItem == null || inventoryItem.getId() != itemId || amount < 1
				|| (freeSlots < 1 && !inventoryItem.getDefinition().isStackable())) {
			return;
		}
		owner.getUpdateMasks().register(STORE_ANIMATION);
		if (amount == 1 || inventoryItem.getDefinition().isStackable()) {
			inventoryItem.setAmount(inventoryItem.getAmount() - amount);
			if (inventoryItem.getAmount() < 1) {
				owner.getInventory().replace(null, slot);
			}
			container.add(new Item(itemId, amount));
			owner.getInventory().refresh();
			owner.getIOSession().write(new ContainerPacket(owner, 90, owner.getInventory(), false));
			owner.getIOSession().write(new ContainerPacket(owner, 30, container, false));
			return;
		}
		for (int i = 0; i < amount; i++) {
			if (owner.getInventory().contains(itemId, 1) && container.itemCount() < size) {
				owner.getInventory().remove(new Item(itemId));
				container.add(new Item(itemId, 1));
			} else {
				break;
			}
		}
		owner.getInventory().refresh();
		owner.getIOSession().write(new ContainerPacket(owner, 90, owner.getInventory(), false));
		owner.getIOSession().write(new ContainerPacket(owner, 30, container, false));
	}
	
	/**
	 * Withdraws an item from the beast of burden.
	 * @param itemId The item id.
	 * @param slot The item slot.
	 * @param amount The amount to withdraw.
	 */
	public void withdraw(int itemId, int slot, int amount) {
		Item item = container.get(slot);
		int freeSlots = owner.getInventory().freeSlots();
		if (amount > freeSlots && !item.getDefinition().isStackable()) {
			if (freeSlots == 0) {
				owner.getPacketSender().sendMessage("Your familiar can't hold any more items.");
				return;
			}
			amount = freeSlots;
		}
		int bobAmount = container.getAmount(new Item(itemId));
		if (amount > bobAmount) {
			amount = bobAmount;
		}
		if (item == null || item.getId() != itemId || amount < 1
				|| (freeSlots < 1 && !item.getDefinition().isStackable())) {
			return;
		}
		if (amount == 1 || item.getDefinition().isStackable()) {
			item.setAmount(item.getAmount() - amount);
			if (item.getAmount() < 1) {
				container.replace(null, slot);
			}
			Container c = new Container(Type.STANDARD, size);
			c.addAll(container);
			container.clear();
			container.addAll(c);
			owner.getInventory().add(new Item(itemId, amount));
			owner.getIOSession().write(new ContainerPacket(owner, 90, owner.getInventory(), false));
			owner.getIOSession().write(new ContainerPacket(owner, 30, container, false));
			return;
		}
		for (int i = 0; i < amount; i++) {
			if (container.contains(item.getId(), 1)) {
				container.remove(new Item(itemId, 1), false);
				owner.getInventory().add(new Item(itemId, 1), false);
			} else {
				break;
			}
		}
		Container c = new Container(Type.STANDARD, size);
		c.addAll(container);
		container.clear();
		container.addAll(c);
		owner.getInventory().refresh();
		owner.getIOSession().write(new ContainerPacket(owner, 90, owner.getInventory(), false));
		owner.getIOSession().write(new ContainerPacket(owner, 30, container, false));
	}
	
	/**
	 * Opens the container.
	 */
	public void open() {
		owner.getPacketSender().sendInterface(671);
		owner.getPacketSender().sendInventoryInterface(665);
		owner.getIOSession().write(new CS2Script(owner, 150, "IviiiIsssssssss", WITHDRAW_OPTIONS));
		owner.getIOSession().write(new AccessMask(owner, 0, 30, 671, 27, 0, 1150));
		owner.getIOSession().write(new CS2Script(owner, 150, "IviiiIsssssssss", DEPOSIT_OPTIONS));
		owner.getIOSession().write(new AccessMask(owner, 0, 28, 665, 0, 0, 1150));
		owner.getIOSession().write(new ContainerPacket(owner, 90, owner.getInventory(), false));
		owner.getIOSession().write(new ContainerPacket(owner, 30, container, false));
	}
	
	/**
	 * Withdraws all of the familiar's items.
	 */
	public void withdrawAll() {
		if (container.itemCount() < 1) {
			owner.getPacketSender().sendMessage("Your beast of burden is not wearing any items.");
			return;
		}
		for (Item item : container.toArray()) {
			if (item != null) {
				if (!owner.getInventory().add(item, false)) {
					break;
				} else {
					container.remove(item);
				}
			}
		}
		owner.getInventory().refresh();
		Container c = new Container(Type.STANDARD, size);
		c.addAll(container);
		container.clear();
		container.addAll(c);
		owner.getIOSession().write(new ContainerPacket(owner, 90, owner.getInventory(), false));
		owner.getIOSession().write(new ContainerPacket(owner, 30, container, false));
	}

	/**
	 * Gets the container.
	 * @return The container.
	 */
	public Container getContainer() {
		return container;
	}

}