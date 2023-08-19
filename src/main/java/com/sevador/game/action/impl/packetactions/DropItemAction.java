package com.sevador.game.action.impl.packetactions;

import java.io.File;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.combat.DeathAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.network.out.ItemOnInterfacePacket;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Constants;


/**
 * The action used to drop an item.
 * 
 * @author Emperor
 * 
 */
public class DropItemAction extends Action {

	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * The item slot.
	 */
	private final int slot;

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * Constructs a new {@code DropItemAction} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 * @param slot
	 *            The item slot.
	 * @param itemId
	 *            The item id.
	 */
	public DropItemAction(Player player, int slot, int itemId) {
		super(player);
		addFlag(DEFAULT_RESET | FLAG);
		this.slot = slot;
		this.itemId = itemId;
	}

	@Override
	public boolean execute() {
		Item item = entity.getPlayer().getInventory().getNew(slot);
		if (item.getId() != itemId) {
			return true;
		}
		if (itemId == 6105 || itemId == 20428) {
			// Giant heads...
			return true;
		}
		boolean dropable = true;
		for (String action : ItemDefinition.forId(itemId).inventoryOptions) {
			if (action != null && action.equals("Destroy")) {
				dropable = false;
				break;
			}
		}
		if (item.getDefinition() == null) item.setDefinition(ItemDefinition.forId(item.getId()));
		if (itemId != 4045 && dropable && item.getDefinition().isTradable()) {
			GroundItem groundItem = new GroundItem(entity.getPlayer(), item,
					entity.getLocation(), false);
			int ticks = 100;
			if (itemId == 17489) { // Gatestone.
				entity.setAttribute("gatestone", groundItem);
				ticks = Integer.MAX_VALUE - 1;
			}
			GroundItemManager.createGroundItem(groundItem, ticks);
			if (entity.isPlayer())
				PlayerWorldLoader.store(entity.getPlayer(), new File("" + Constants.SAVE_PATH + ""
						+ entity.getPlayer().getCredentials().getUsername() + "" + PlayerWorldLoader.suffix + ""));
		} else {
			if (!entity.isNPC()) {
				entity.getPlayer().getPacketSender().sendChatBoxInterface(94);
				for(int i = 0; i < 4; i++) {//wha t u looking for		

				}
				entity.getPlayer().getIOSession().write(new StringPacket(entity.getPlayer(), "Are you sure you want to destroy this object?", 94, 2));
				entity.getPlayer().getIOSession().write(new StringPacket(entity.getPlayer(), ItemDefinition.forId(itemId).name, 94, 8));
				entity.getPlayer().getIOSession().write(new StringPacket(entity.getPlayer(), "<br>The item is undroppable, and if dropped could possibly not be obtained again.", 94, 7));
				entity.getPlayer().getIOSession().write(new ItemOnInterfacePacket(entity.getPlayer(), 94, 9, -1, itemId));
				entity.setAttribute("destroyItem", item);
				entity.setAttribute("destroyItemSlot", slot);
				entity.setAttribute("isDestroying", true);
				return true;
			}
		}
		entity.getPlayer().getInventory().remove(item, slot, false);
		entity.getPlayer().getInventory().refresh();
		return true;
	}

	@Override
	public boolean dispose(Action action) {
		return action.getFlag() == DeathAction.FLAG;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}
