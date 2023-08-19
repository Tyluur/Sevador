package com.sevador.game.node.player;

import java.io.Serializable;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.container.impl.BankContainerListener;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.BlankCS2Script;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.ContainerPacket;
import com.sevador.network.out.MessagePacket;
import com.sevador.network.out.StringPacket;

/**
 * Handles the opening/closing of the bank and adding/removing items.
 * 
 * @author Emperor
 * 
 */
public class BankHandler implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -681496460151109573L;

	/**
	 * The bank size.
	 */
	public static final int SIZE = 438;

	/**
	 * The maximum amount of bank tabs
	 */
	public static final int TAB_SIZE = 11;

	/**
	 * The container.
	 */
	private final Container container;

	/**
	 * The tab start indexes.
	 */
	private final int[] tabStartSlot = new int[TAB_SIZE];

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The last withdraw/deposit x-amount.
	 */
	private int lastAmount;

	/**
	 * Constructs a new {@code BankHandler} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 */
	public BankHandler(Player player) {
		this.container = new Container(Type.ALWAYS_STACK, SIZE,
				new BankContainerListener(player));
		this.player = player;
		this.lastAmount = 50;
	}

	public void openPin() {
		player.getPacketSender().sendInterface(13);
	}

	public void openChoosePin() {
		player.getPacketSender().sendInterface(14);
	}
	
	private transient boolean allowedToOpen = false;

	/**
	 * Opens the bank.
	 */
	public void open() {
		if (player.getBank().isAllowedToOpen()) {
			player.setAttribute("bankOpen", true);
			player.getIOSession().write(
					new ContainerPacket(player, 31, player.getInventory(),
							false));
			player.getIOSession().write(
					new AccessMask(player, 0, SIZE, 762, 93, 40, 1278));
			player.getIOSession().write(
					new AccessMask(player, 0, 27, 763, 0, 37, 1150));
			player.getIOSession().write(new ConfigPacket(player, 563, 4194304));
			player.getIOSession().write(new ConfigPacket(player, 1249, lastAmount));
			player.getIOSession().write(new BlankCS2Script(player, 1451));
			player.getPacketSender().sendInterface(762).sendInventoryInterface(763);
			player.getIOSession().write(
					new StringPacket(player, container.itemCount() + "", 762,
							31));
			player.getIOSession().write(
					new StringPacket(player, SIZE + "", 762, 31));
		} else {
			InputHandler.requestStringInput(player, 2,
					"What is your current bank pin?");
		}
		container.refresh();
	}

	/**
	 * Opens another player's bank, used for checking.
	 * 
	 * @param player
	 *            The player.
	 */
	public void open(Player player) {
		player.setAttribute("bankOpen", true);
	}

	/**
	 * Closes the bank.
	 */
	public void close() {
		if (!player.getAttribute("bankOpen", false)) {
			return;
		}
		player.setAttribute("bankOpen", false);
		player.getPacketSender().sendCloseInterface().setDefaultInventory();
	}

	/**
	 * Adds an item to the bank container and refreshes.
	 * 
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount.
	 */
	public void addItem(int slot, int amount) {
		addItem(slot, amount, true);
	}

	/**
	 * Adds an item to the bank container.
	 * 
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount.
	 * @param refresh
	 *            If we should refresh.
	 */
	public void addItem(int slot, int amount, boolean refresh) {
		if (player.getAttribute("checkingBank", false)) {
			return;
		}
		if (player.getAttribute("bankOpen", false)) {
			Item item = player.getInventory().get(slot);
			if (item == null) {
				return;
			}
			int playerAmount = player.getInventory().getAmount(item);
			int currentTab = (Integer) player.getAttribute("currentTab", 1);
			if (playerAmount < amount) {
				amount = playerAmount;
			}
			if (item.getDefinition() == null) item.setDefinition(ItemDefinition.forId(item.getId()));
			if (item.getDefinition().isNoted()) {
				item = new Item(item.getId() == 10843 ? 10828
						: item.getId() - 1, item.getAmount());
				player.getInventory().remove(
						new Item(item.getId() == 10828 ? 10843
								: item.getId() + 1, amount), slot, refresh);
			} else 
				player.getInventory().remove(new Item(item.getId(), amount), slot, refresh);
			int index = container.getSlotById(item.getId());
			if (index > -1) {
				Item item2 = container.get(index);
				if (item2 != null) {
					if (item2.getId() == item.getId()) {
						container.replace(
								new Item(item.getId(), amount
										+ item2.getAmount()), index, false);
					}
				}
			} else {
				int freeSlot;
				if (currentTab == 10) {
					freeSlot = container.freeSlot();
				} else {
					freeSlot = tabStartSlot[currentTab]
							+ getItemsInTab(currentTab);
				}
				if (item.getAmount() > 0) {
					if (currentTab != 10) {
						insert(container.freeSlot(), freeSlot);
						increaseTabStartSlots(currentTab);
					}
					container.replace(new Item(item.getId(), amount), freeSlot,
							false);
				}
			}
			if (refresh) {
				container.refresh();
			}
		}
	}

	/**
	 * Removes an item from the bank.
	 * 
	 * @param slot
	 *            The item slot.
	 * @param amount
	 *            The amount.
	 */
	public void removeItem(int slot, int amount) {
		if (player.getAttribute("checkingBank", false)) {
			return;
		}
		if (player.getAttribute("bankOpen", false)) {
			player.getPacketSender().sendChatBoxInterface(137);
			if (slot < 0 || slot > SIZE || amount <= 0) {
				return;
			}
			Item item = container.get(slot);
			Item item2 = container.get(slot);
			Item item3 = container.get(slot);
			int tabId = getTabByItemSlot(slot);
			if (item == null) {
				return;
			}
			if (amount > item.getAmount()) {
				item = new Item(item.getId(), item.getAmount());
				item2 = new Item(item.getId() == 10828 ? 10843
						: item.getId() + 1, item.getAmount());
				item3 = new Item(item.getId(), item.getAmount());
				if (player.getAttribute("noted", false)) {
					if (item2.getDefinition().isNoted()
							&& item2.getDefinition().getName().equals(item
									.getDefinition().getName())
							&& !item.getDefinition().isStackable()) {
						item = new Item(item.getId() == 10828 ? 10843
								: item.getId() + 1, item.getAmount());
					} else {
						player.getIOSession()
								.write(new MessagePacket(player,
										"You cannot withdraw this item as a note."));
						item = new Item(item.getId(), item.getAmount());
					}
				}
				amount = item.getAmount();
			} else {
				item = new Item(item.getId(), amount);
				item2 = new Item(item.getId(), amount);
				item3 = new Item(item.getId(), amount);
				if (player.getAttribute("noted", false)) {
					item2 = new Item(item.getId() == 10828 ? 10843
							: item.getId() + 1, item.getAmount());
					if (item2.getDefinition().isNoted()
							&& item2.getDefinition().getName().equals(item
									.getDefinition().getName())
							&& !item.getDefinition().isStackable()) {
						item = new Item(item.getId() == 10828 ? 10843
								: item.getId() + 1, item.getAmount());
					} else {
						player.getIOSession()
								.write(new MessagePacket(player,
										"You cannot withdraw this item as a note."));
						item = new Item(item.getId(), item.getAmount());
						return;
					}
				}
			}
			if (amount > player.getInventory().freeSlots()
					&& !item3.getDefinition().isStackable()
					&& !player.getAttribute("noted", false)) {
				item = new Item(item.getId(), player.getInventory().freeSlots());
				item2 = new Item(item2.getId(), player.getInventory()
						.freeSlots());
				item3 = new Item(item3.getId(), player.getInventory()
						.freeSlots());
			}
			if (container.getSlotById(item3.getId()) > -1) {
				if (player.getInventory().freeSlots() <= 0) {
					player.getIOSession().write(
							new MessagePacket(player,
									"Not enough space in your inventory."));
				} else {
					if (player.getAttribute("noted", false)
							&& !item.getDefinition().isNoted()) {
						player.getInventory().add(item);
						container.remove(item3);
					} else {
						player.getInventory().add(item);
						container.remove(item3);
					}
				}
			}
			if (container.get(slot) == null) {
				decreaseTabStartSlots(tabId);
			}
			container.shift();
			container.refresh();
		}
	}

	/**
	 * Gets the tab the item slot is in.
	 * 
	 * @param itemSlot
	 *            The item slot.
	 * @return The tab index.
	 */
	public int getTabByItemSlot(int itemSlot) {
		int tabId = 0;
		for (int i = 0; i < tabStartSlot.length; i++) {
			if (itemSlot >= tabStartSlot[i]) {
				tabId = i;
			}
		}
		return tabId;
	}

	/**
	 * Increases a tab's start slot.
	 * 
	 * @param startId
	 *            The start id.
	 */
	public void increaseTabStartSlots(int startId) {
		for (int i = startId + 1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]++;
		}
	}

	/**
	 * Decreases a tab's start slot.
	 * 
	 * @param startId
	 *            The start id.
	 */
	public void decreaseTabStartSlots(int startId) {
		if (startId == 10)
			return;
		for (int i = startId + 1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]--;
		}
		if (getItemsInTab(startId) == 0) {
			collapseTab(startId);
		}
	}

	/**
	 * Inserts an item.
	 * 
	 * @param fromId
	 *            The current slot.
	 * @param toId
	 *            The required slot.
	 */
	public void insert(int fromId, int toId) {
		Item temp = container.toArray()[fromId];
		if (toId > fromId) {
			for (int i = fromId; i < toId; i++) {
				container.replace(container.get(i + 1), i, false);
			}
		} else if (fromId > toId) {
			for (int i = fromId; i > toId; i--) {
				container.replace(container.get(i - 1), i, false);
			}
		}
		container.replace(temp, toId, false);
	}

	/**
	 * Gets the array index for a tab.
	 * 
	 * @param tabId
	 *            The tab id.
	 * @return The array index.
	 */
	public static int getArrayIndex(int tabId) {
		if (tabId == 62 || tabId == 74) {
			return 10;
		}
		int base = 60;
		for (int i = 2; i < 10; i++) {
			if (tabId == base) {
				return i;
			}
			base -= 2;
		}
		base = 74;
		for (int i = 2; i < 10; i++) {
			if (tabId == base) {
				return i;
			}
			base++;
		}
		return -1;
	}

	/**
	 * Collapses a tab.
	 * 
	 * @param tabId
	 *            The tab index.
	 */
	public void collapseTab(int tabId) {
		if (player.getAttribute("checkingBank", false)) {
			return;
		}
		int size = getItemsInTab(tabId);
		Item[] tempTabItems = new Item[size];
		for (int i = 0; i < size; i++) {
			tempTabItems[i] = container.get(tabStartSlot[tabId] + i);
			container.replace(null, tabStartSlot[tabId] + i, false);
		}
		container.shift();
		for (int i = tabId; i < tabStartSlot.length - 1; i++) {
			tabStartSlot[i] = tabStartSlot[i + 1] - size;
		}
		tabStartSlot[10] = tabStartSlot[10] - size;
		for (int i = 0; i < size; i++) {
			int slot = container.freeSlot();
			container.replace(tempTabItems[i], slot, false);
		}
		container.refresh(); // We only refresh once.
	}

	/**
	 * Gets the amount of items in one tab.
	 * 
	 * @param tabId
	 *            The tab index.
	 * @return The amount of items in this tab.
	 */
	public int getItemsInTab(int tabId) {
		return tabStartSlot[tabId + 1] - tabStartSlot[tabId];
	}

	/**
	 * @return the container
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * @return the tabStartSlot
	 */
	public int[] getTabStartSlot() {
		return tabStartSlot;
	}

	/**
	 * @return the lastAmount
	 */
	public int getLastAmount() {
		return lastAmount;
	}

	/**
	 * @param lastAmount
	 *            the lastAmount to set
	 */
	public void setLastAmount(int lastAmount) {
		this.lastAmount = lastAmount;
	}

	/**
	 * Banks all the items from a certain container.
	 * 
	 * @param container
	 *            The container.
	 * @return {@code True}.
	 */
	private boolean bankItems(Container container) {
		int currentTab = (Integer) player.getAttribute("currentTab", 1);
		for (int i = 0; i < container.size(); i++) {
			Item item = container.get(i);
			if (item == null) {
				continue;
			}
			Item toBank = item;
			if (item.getDefinition().isNoted()) {
				toBank = new Item(item.getId() == 10843 ? 10828
						: item.getId() - 1, item.getAmount());
			}
			container.replace(null, i);
			int index = this.container.getSlotById(item.getId());
			if (index > -1) {
				Item item2 = this.container.get(index);
				if (item2 != null) {
					if (item2.getId() == item.getId()) {
						this.container.replace(
								new Item(toBank.getId(), item.getAmount()
										+ item2.getAmount()), index);
					}
				}
			} else {
				int freeSlot;
				if (currentTab == 10) {
					freeSlot = this.container.freeSlot();
				} else {
					freeSlot = tabStartSlot[currentTab]
							+ getItemsInTab(currentTab);
				}
				if (item.getAmount() > 0) {
					if (currentTab != 10) {
						insert(this.container.freeSlot(), freeSlot);
						increaseTabStartSlots(currentTab);
					}
					this.container.replace(
							new Item(toBank.getId(), item.getAmount()),
							freeSlot);
				}
			}
		}
		return true;
	}

	/**
	 * Banks the inventory.
	 */
	public void bankInventory() {
		if (player.getInventory().itemCount() < 1) {
			player.getPacketSender().sendMessage(
					"You don't have any items to bank.", true);
			return;
		}
		for (int i = 0; i < 28; i++) {
			Item item = player.getInventory().get(i);
			if (item != null) {
				addItem(i, item.getAmount(), false);
			}
		}
		container.refresh();
		player.getInventory().refresh();
	}

	/**
	 * Banks the player's equipment.
	 */
	public void bankEquipment() {
		if (player.getEquipment().itemCount() < 1) {
			player.getPacketSender().sendMessage(
					"You're not wearing anything to bank.", true);
			return;
		}
		bankItems(player.getEquipment());
		container.refresh();
		player.getEquipment().refresh();
	}

	/**
	 * @return the allowedToOpen
	 */
	public boolean isAllowedToOpen() {
		if (player.getCredentials().getBankPin() == null) return true;
		return allowedToOpen;
	}

	/**
	 * @param allowedToOpen the allowedToOpen to set
	 */
	public void setAllowedToOpen(boolean allowedToOpen) {
		this.allowedToOpen = allowedToOpen;
	}
}