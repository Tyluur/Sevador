package com.sevador.game.action.impl;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.packetactions.EmoteAction;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.OptionType;

/**
 * Handles an item clicked in the inventory.
 * @author Emperor
 *
 */
public class ItemAction extends Action {

	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * The option type.
	 */
	private final OptionType type;
	
	/**
	 * The item id.
	 */
	private final int itemId;
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The item slot.
	 */
	private final int slot;
	
	/**
	 * Constructs a new {@code ItemAction} {@code Object}.
	 * @param player The player.
	 * @param type The option type.
	 * @param itemId The item id.
	 * @param interfaceId The interface id.
	 * @param slot The item slot.
	 */
	public ItemAction(Player player, OptionType type, int itemId, int interfaceId, int slot) {
		super(player);
		addFlag((DEFAULT_RESET & ~MovementAction.FLAG) | EmoteAction.FLAG | FLAG);
		this.type = type;
		this.itemId = itemId;
		this.interfaceId = interfaceId;
		this.slot = slot;
	}

	@Override
	public boolean execute() {
		Item item = null;
		switch (interfaceId) {
		case 679:
			item = entity.getPlayer().getInventory().get(slot);
			break;
		case 387:
			item = entity.getPlayer().getEquipment().get(entity.getPlayer().getEquipment().getSlotById(itemId));
			break;
		}
		if (item == null || item.getId() != itemId) {
			return true;
		}
		if (item.getDefinition() == null) 
			item.setDefinition(ItemDefinition.forId(item.getId()));
		if (!EventManager.handleItemEvent(entity.getPlayer(), item, interfaceId, slot, type)) {
			if (entity.getPlayer().getCredentials().getRights() == 2)
				entity.getPlayer().getIOSession().write(new MessagePacket(entity.getPlayer(), 99, "Unhandled item action - [id=" + itemId + ", slot=" + slot + ", interface=" + interfaceId + ", type=" + type + "]."));
		}
		return true;
	}
	
	@Override
	public boolean dispose(Action a) {
		if (a.getActionType() == FLAG) {
			return false;
		}
		return true;
	}
	
	@Override
	public int getActionType() {
		return FLAG;
	}

}