package com.sevador.network.in;

import com.sevador.Main;
import com.sevador.game.action.impl.packetactions.ItemOnItemAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class ItemOnItemHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int itemUsed = packet.readLEShortA();
		int value2 = packet.readLEShortA();
		int value3 = packet.readLEShortA();
		int value4 = packet.readLEInt();
		int value5 = packet.readLEInt();
		int itemUsedWith = packet.readShortA();
		Item firstItem = new Item(itemUsed);
		Item secondItem = new Item(itemUsedWith);
		player.getSkillAction().forceStop();
		if (Main.DEBUG)
			System.err.println(value2 + " " + value3 + "  " + value4 + " " + value5 + " " + itemUsed + " " + itemUsedWith);
		player.getActionManager().register(new ItemOnItemAction(player, firstItem, secondItem));
		return true;
	}

}
