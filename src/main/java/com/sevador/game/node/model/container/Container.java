package com.sevador.game.node.model.container;

import java.io.Serializable;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.impl.DefaultListener;
import com.sevador.utility.Constants;

/**
 * The container holding a collection of {@link Item}s.
 * 
 * @author Emperor
 * @author Graham
 * 
 */
public class Container implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 3205463001404223065L;
	
	/**
	 * The type of container.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	public static enum Type {

		/**
		 * A standard container such as inventory.
		 */
		STANDARD,

		/**
		 * A container which always stacks, e.g. the bank, regardless of the
		 * item.
		 */
		ALWAYS_STACK,

		/**
		 * A container which never stacks, used mainly for death containers.
		 */
		NEVER_STACK,

		/**
		 * A container used for shops, this will disable removing items
		 * completely for {@link Container#replace}.
		 */
		SHOP;

	}

	/**
	 * The capacity of this container.
	 */
	private int capacity;

	/**
	 * The container type.
	 */
	private Type type;

	/**
	 * The items in this container.
	 */
	private Item[] items;

	/**
	 * The container listener used.
	 */
	private transient ContainerListener listener;

	/**
	 * Creates the container with the specified capacity.
	 * 
	 * @param type
	 *            The type of this container.
	 * @param capacity
	 *            The capacity of this container.
	 */
	public Container(Type type, int capacity) {
		this(type, capacity, DefaultListener.getSingleton());
	}

	/**
	 * Creates the container with the specified capacity.
	 * 
	 * @param type
	 *            The type of this container.
	 * @param capacity
	 *            The capacity of this container.
	 * @param listener
	 *            The container listener used.
	 */
	public Container(Type type, int capacity, ContainerListener listener) {
		if (listener == null) {
			throw new IllegalStateException("No listener given!");
		}
		this.type = type;
		this.capacity = capacity;
		this.items = new Item[capacity];
		this.listener = listener;
	}

	/**
	 * Replaces the item on the given slot with the argued item.
	 * 
	 * @param item
	 *            The item.
	 * @param slot
	 *            The slot.
	 * @return The old item.
	 */
	public Item replace(Item item, int slot) {
		return replace(item, slot, true);
	}

	/**
	 * Replaces the item on the given slot with the argued item.
	 * 
	 * @param item
	 *            The item.
	 * @param slot
	 *            The slot.
	 * @param fireListener
	 *            If the listener should be "notified".
	 * @return The old item.
	 */
	public Item replace(Item item, int slot, boolean fireListener) {
		if (item != null && item.getAmount() < 1 && type != Type.SHOP) {
			item = null;
		}
		Item oldItem = items[slot];
		items[slot] = item;
		if (fireListener) {
			listener.itemChanged(this, slot);
		}
		return oldItem;
	}

	/**
	 * Adds a set of items to this container.
	 * 
	 * @param items
	 *            The set of items.
	 */
	public void add(Item... items) {
		for (Item item : items) {
			if (item != null) {
				add(item, false);
			}
		}
		listener.itemsAdded(this, items);
	}

	/**
	 * Adds an item on the first free slot.
	 * 
	 * @param item
	 *            The item.
	 */
	public boolean add(Item item) {
		return add(item, true);
	}

	/**
	 * Adds an item on the first free slot.
	 * 
	 * @param item
	 *            The item.
	 * @param fireListener
	 *            If the listener should be "notified".
	 */
	public boolean add(Item item, boolean fireListener) {
		if (item.getDefinition() == null) item.setDefinition(ItemDefinition.forId(item.getId()));
		if (type != Type.NEVER_STACK
				&& (item.getDefinition().isStackable()
						|| type.equals(Type.ALWAYS_STACK) || type == Type.SHOP)) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null && items[i].getId() == item.getId()) {
					int totalCount = item.getAmount() + items[i].getAmount();
					items[i] = new Item(items[i].getId(), totalCount);
					if (fireListener) {
						listener.itemsAdded(this, item);
					}
					return true;
				}
			}
			int slot = freeSlot();
			if (slot == -1) {
				return false;
			}
			items[slot] = item;
			if (fireListener) {
				listener.itemsAdded(this, item);
			}
			return true;
		}
		int slots = freeSlots();
		if (slots >= item.getAmount()) {
			for (int i = 0; i < item.getAmount(); i++) {
				items[freeSlot()] = new Item(item.getId(), 1);
			}
			if (fireListener) {
				listener.itemsAdded(this, item);
			}
			return true;
		}
		return false;
	}

	/**
	 * Removes a set of items.
	 * 
	 * @param items
	 *            The set of items.
	 */
	public void remove(Item... items) {
		for (Item item : items) {
			remove(item, false);
		}
		listener.itemsRemoved(this, items);
	}

	/**
	 * Removes an item.
	 * 
	 * @param item
	 *            The item.
	 * @return {@code True} if the item got removed, {@code false} if not.
	 */
	public boolean remove(Item item) {
		return remove(item, true);
	}

	/**
	 * Removes an item.
	 * 
	 * @param item
	 *            The item to remove.
	 * @param fireListener
	 *            If the fire listener should be "notified".
	 * @return {@code True} if the item got removed, <br>
	 *         {@code false} if not.
	 */
	public boolean remove(Item item, boolean fireListener) {
		int slot = getSlotById(item.getId());
		if (slot != -1) {
			return remove(item, slot, fireListener);
		}
		return false;
	}

	/**
	 * Removes an item from this container.
	 * 
	 * @param item
	 *            The item.
	 * @param slot
	 *            The item slot.
	 * @param fireListener
	 *            If the fire listener should be "notified".
	 * @return {@code True} if the item got removed, <br>
	 *         {@code false} if the item on the slot was null or the ids didn't
	 *         match.
	 */
	public boolean remove(Item item, int slot, boolean fireListener) {
		Item oldItem = items[slot];
		if (oldItem == null || oldItem.getId() != item.getId()) {
			return false;
		}
		if (oldItem.getDefinition() == null) oldItem.setDefinition(ItemDefinition.forId(oldItem.getId()));
		if (oldItem.getDefinition().isStackable()
				|| type.equals(Type.ALWAYS_STACK) || type == Type.SHOP) {
			if (item.getAmount() >= oldItem.getAmount()) {
				items[slot] = null;
				if (fireListener) {
					listener.itemsRemoved(this, item);
				}
				return true;
			}
			items[slot] = new Item(item.getId(), oldItem.getAmount()
					- item.getAmount());
			if (fireListener) {
				listener.itemsRemoved(this, item);
			}
			return true;
		}
		items[slot] = null;
		int removed = 1;
		for (int i = removed; i < item.getAmount(); i++) {
			slot = getSlotById(item.getId());
			if (slot != -1) {
				items[slot] = null;
			} else {
				break;
			}
		}
		if (fireListener) {
			listener.itemsRemoved(this, item);
		}
		return true;
	}

	/**
	 * Gets a slot by id.
	 * 
	 * @param id
	 *            The id.
	 * @return The slot, or <code>-1</code> if it could not be found.
	 */
	public int getSlotById(int id) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null)
				continue;
			if (items[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Sorts the container so there are no null slots between the items.
	 */
	public void shift() {
		Item[] oldData = items;
		items = new Item[oldData.length];
		int ptr = 0;
		for (int i = 0; i < items.length; i++) {
			if (oldData[i] != null) {
				items[ptr++] = oldData[i];
			}
		}
	}

	/**
	 * Gets the next free slot.
	 * 
	 * @return The slot, or <code>-1</code> if there are no available slots.
	 */
	public int freeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the number of free slots.
	 * 
	 * @return The number of free slots.
	 */
	public int freeSlots() {
		return capacity - itemCount();
	}

	/**
	 * Gets the size of this container.
	 * 
	 * @return The size of this container.
	 */
	public int itemCount() {
		int size = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				size++;
			}
		}
		return size;
	}

	/**
	 * Forces the listener to refresh.
	 */
	public void refresh() {
		listener.refresh(this);
	}

	/**
	 * Gets the amount of an item.
	 * 
	 * @param item
	 *            The item.
	 * @return The amount of this item in this container.
	 */
	public int getAmount(Item item) {
		if (item == null) {
			return 0;
		}
		int count = 0;
		for (Item i : items) {
			if (i != null && i.getId() == item.getId()) {
				count += i.getAmount();
			}
		}
		return count;
	}
	
	
	public int getAmount(int id) {
		return getAmount(new Item(id));
	}

	/**
	 * Gets an item from this container.
	 * 
	 * @param slot
	 *            The item slot.
	 * @return The item stored on the slot given.
	 */
	public Item get(int slot) {
		return items[slot];
	}

	public Item getItem(int slot) {
		return items[slot];
	}

	public int getWeaponId() {
		return get(Constants.SLOT_WEAPON).getId();
	}	
	
	public int getShieldId() {
		return get(Constants.SLOT_SHIELD).getId();
	}	
	
	public int getAmmoId() {
		return get(Constants.SLOT_ARROWS).getId();
	}
	
	public int getAuraId() {
		return get(Constants.SLOT_AURA).getId();
	}

	/**
	 * Gets an item from this container, or a new item (with id 1 and amount 0) <br>
	 * when the item on the slot was {@code null}.
	 * 
	 * @param slot
	 *            The item slot.
	 * @return The item on the slot, or a new item.
	 */
	public Item getNew(int slot) {
		Item item = items[slot];
		return item != null ? item : new Item(1, 0);
	}

	/**
	 * Returns an array representing this container.
	 * 
	 * @return The array.
	 */
	public Item[] toArray() {
		return items;
	}

	/**
	 * Sets the container listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void setListener(ContainerListener listener) {
		this.listener = listener;
	}

	/**
	 * Adds a container to this container.
	 * 
	 * @param container
	 *            The container.
	 */
	public void addAll(Container container) {
		add(container.items);
	}

	/**
	 * Checks if this container has space to add the other container.
	 * 
	 * @param c
	 *            The other container.
	 * @return {@code True} if so.
	 */
	public boolean hasSpaceFor(Container c) {
		if (c == null) {
			return false;
		}
		Container check = new Container(type, capacity,
				DefaultListener.getSingleton());
		check.addAll(this);
		for (Item item : c.items) {
			if (item != null) {
				if (!check.add(item, false)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks if the container contains an item.
	 * 
	 * @param itemId
	 *            The item id.
	 * @param amount
	 *            The amount.
	 * @return {@code True} if so.
	 */
	public boolean contains(int itemId, int amount) {
		int count = 0;
		for (Item item : items) {
			if (item != null && item.getId() == itemId) {
				count += item.getAmount();
			}
		}
		return count >= amount;
	}
	
	public boolean containsOneItem(int i) {
		return contains(i, 1);
	}

	/**
	 * Clears the container.
	 */
	public void clear() {
		items = new Item[capacity];
		listener.refresh(this);
	}

	/**
	 * Gets the container size.
	 * 
	 * @return The capacity of this container.
	 */
	public int size() {
		return capacity;
	}

	public boolean hasFreeSlots() {
		return freeSlots() != -1;
	}

	public boolean remove(int item, int amount) {
		return remove(new Item(item, amount));
	}

	public void addItem(int item, int amount) {
		add(new Item(item, amount));
	}

	public static boolean isTwoHandedWeapon(Item item) {
		int itemId = item.getId();
		if (itemId == 4212)
			return true;
		else if (itemId == 4214)
			return true;
		else if (itemId == 20281)
			return true;
		String wepEquiped = item.getDefinition().getName().toLowerCase();
		if (wepEquiped == null)
			return false;
		else if (wepEquiped.endsWith("claws"))
			return true;
		else if (wepEquiped.endsWith("anchor"))
			return true;
		else if (wepEquiped.contains("2h sword"))
			return true;
		else if (wepEquiped.contains("katana"))
			return true;
		else if (wepEquiped.equals("seercull"))
			return true;
		else if (wepEquiped.contains("shortbow"))
			return true;
		else if (wepEquiped.contains("longbow"))
			return true;
		else if (wepEquiped.contains("shortbow"))
			return true;
		else if (wepEquiped.contains("bow full"))
			return true;
		else if (wepEquiped.equals("zaryte bow"))
			return true;
		else if (wepEquiped.equals("dark bow"))
			return true;
		else if (wepEquiped.endsWith("halberd"))
			return true;
		else if (wepEquiped.contains("maul"))
			return true;
		else if (wepEquiped.equals("karil's crossbow"))
			return true;
		else if (wepEquiped.equals("torag's hammers"))
			return true;
		else if (wepEquiped.equals("verac's flail"))
			return true;
		else if (wepEquiped.contains("greataxe"))
			return true;
		else if (wepEquiped.contains("spear"))
			return true;
		else if (wepEquiped.equals("tzhaar-ket-om"))
			return true;
		else if (wepEquiped.contains("godsword"))
			return true;
		else if (wepEquiped.equals("saradomin sword"))
			return true;
		else if (wepEquiped.equals("hand cannon"))
			return true;
		return false;
	}


}
