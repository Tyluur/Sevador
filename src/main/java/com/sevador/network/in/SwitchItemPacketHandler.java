package com.sevador.network.in;

import com.sevador.Main;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.BankHandler;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles the switch item packet handler incoming packet.
 * @author Emperor
 *
 */
public class SwitchItemPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int fromItemId = packet.readShort();
		int toHash = packet.readInt2();
		int fromSlot = packet.readShort();
		int toSlot = packet.readShort();
		int toItemId = packet.readShort();
		int interfaceHash = packet.readInt();
		int interfaceId = interfaceHash >> 16;
		int fromChild = interfaceHash & 0xff;
		int toChild = toHash & 0xFFFF;
		int toInterface = toHash >> 16;
		if (Main.DEBUG)
		System.out.println(toChild + ", " + toInterface + ", " + fromItemId + ", " + fromSlot + ", " + toItemId + ", " + toSlot + ", " + interfaceId + ", " + fromChild);
        switch (interfaceId) {
        case 762:
            if (player.getAttribute("checkingBank", false)) {
            	return true;
            }
            if (toChild == 93) {
                if (fromSlot < 0 || fromSlot >= BankHandler.SIZE || toSlot < 0 || toSlot >= BankHandler.SIZE) {
                    break;
                }
                if (!player.getAttribute("inserting", false)) {
                    Item temp = player.getBank().getContainer().get(fromSlot);
                    Item temp2 = player.getBank().getContainer().get(toSlot);
                    player.getBank().getContainer().replace(temp2, fromSlot, false);
                    player.getBank().getContainer().replace(temp, toSlot);
                } else {
                    if (toSlot > fromSlot) {
                        player.getBank().insert(fromSlot, toSlot - 1);
                    } else if (fromSlot > toSlot) {
                        player.getBank().insert(fromSlot, toSlot);
                    }
                    player.getBank().getContainer().refresh();
                }
                break;
            } else {
				int tabIndex = BankHandler.getArrayIndex(toChild);
				System.out.println(tabIndex + ", " + toChild + ", " + fromSlot);
                if (tabIndex > -1) {
                    toSlot = tabIndex == 10 ? player.getBank().getContainer().freeSlot() : player.getBank().getTabStartSlot()[tabIndex] + player.getBank().getItemsInTab(tabIndex);
                    fromChild = player.getBank().getTabByItemSlot(fromSlot);
                    if (toSlot > fromSlot) {
                        player.getBank().insert(fromSlot, toSlot - 1);
                    } else if (fromSlot > toSlot) {
                        player.getBank().insert(fromSlot, toSlot);
                    }
                    player.getBank().increaseTabStartSlots(tabIndex);
                    player.getBank().decreaseTabStartSlots(fromChild);
                    player.getBank().getContainer().refresh();
                    break;
                }
            }
            break;
        case 679:
        case 763:
        	if (interfaceId == 679) {
        		toSlot -= 28;
        	}
			if (fromSlot < 0 || fromSlot >= 28 || player.getInventory().get(fromSlot) == null) {
				return true;
			}
			if (toSlot < 0 || toSlot >= 28) {
				return true;
			}
			Item toSlotItem = player.getInventory().get(toSlot);
			player.getInventory().replace(player.getInventory().get(fromSlot), toSlot, false);
			player.getInventory().replace(toSlotItem, fromSlot);
			break;
		}
		return true;
	}

}