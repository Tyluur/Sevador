package com.sevador.game.node.player;

import java.io.Serializable;
import java.util.HashMap;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.content.ItemConstant;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.utility.Misc;

public class ChargeManager implements Serializable {

	private static final long serialVersionUID = -5978513415281726450L;

	private transient Player player;

	private HashMap<Integer, Integer> charges;

	public ChargeManager() {
		charges = new HashMap<Integer, Integer>();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void process() {
		Item[] items = player.getEquipment().toArray();
		for (int slot = 0; slot < items.length; slot++) {
			Item item = items[slot];
			if (item == null)
				continue;
			if (player.getAttackedByDelay() > Misc.currentTimeMillis()) {
				int newId = ItemConstant.getDegradeItemWhenCombating(item
						.getId());
				if (newId != -1) {
					item.setId((short) newId);
					player.getEquipment().refresh();
					player.getPacketSender().sendMessage("Your " + item.getDefinition().getName()+ " degraded.");
				}
			}
			int defaultCharges = ItemConstant.getItemDefaultCharges(item.getId());
			if (defaultCharges == -1)
				continue;
			if (ItemConstant.itemDegradesWhileWearing(item.getId()))
				degrade(item.getId(), defaultCharges, slot);
			else if (player.getAttackedByDelay() > Misc.currentTimeMillis())
				degrade(item.getId(), defaultCharges, slot);
		}
	}

	public void die() {
		Item[] equipItems = player.getEquipment().toArray();
		for (int slot = 0; slot < equipItems.length; slot++) {
			if (equipItems[slot] != null && degradeCompletly(equipItems[slot]))
				player.getEquipment().replace(null, slot);
		}
		Item[] invItems = player.getInventory().toArray();
		for (int slot = 0; slot < invItems.length; slot++) {
			if (invItems[slot] != null && degradeCompletly(invItems[slot]))
				player.getInventory().replace(null, slot);
		}
	}

	/*
	 * return disapear;
	 */
	public boolean degradeCompletly(Item item) {
		int defaultCharges = ItemConstant.getItemDefaultCharges(item.getId());
		if (defaultCharges == -1)
			return false;
		while (true) {
			if (ItemConstant.itemDegradesWhileWearing(item.getId())
					|| ItemConstant.itemDegradesWhileCombating(item.getId())) {
				charges.remove(item.getId());
				int newId = ItemConstant.getItemDegrade(item.getId());
				if (newId == -1)
					return ItemConstant.getItemDefaultCharges(item.getId()) == -1 ? false
							: true;
				item.setId((short) newId);
			} else {
				int newId = ItemConstant.getItemDegrade(item.getId());
				if (newId != -1) {
					charges.remove(item.getId());
					item.setId((short) newId);
				}
				break;
			}
		}
		return false;
	}

	private void degrade(int itemId, int defaultCharges, int slot) {
		Integer c = charges.remove(itemId);
		if (c == null)
			c = defaultCharges;
		else {
			c--;
			if (c == 0) {
				int newId = ItemConstant.getItemDegrade(itemId);
				player.getEquipment().remove(new Item(itemId));
				if (newId == -1)
					player.getPacketSender().sendMessage("Your "+ ItemDefinition.forId(itemId).getName() + " became into dust.");
				else
					player.getPacketSender().sendMessage("Your "+ ItemDefinition.forId(itemId).getName() + " degraded.");
				player.getEquipment().refresh();
				player.getUpdateMasks().register(new AppearanceUpdate(player));
				return;
			}
		}
		charges.put(itemId, c);
	}

}
