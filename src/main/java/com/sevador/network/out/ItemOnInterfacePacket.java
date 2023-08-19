package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class ItemOnInterfacePacket extends OutgoingPacket {
	
	private int interfaceId, child, amount, id;

	public ItemOnInterfacePacket(Player player, int interfaceId, int child, int amount, int id) {
		super(player, 64);
		this.interfaceId = interfaceId;
		this.child = child;
		this.amount = amount;
		this.id = id;
	}
	
	@Override
	public OutgoingPacket get() {
		putInt(amount);
		putShortA(id);
		putInt1(interfaceId << 16 | child);
        return this;
	}

}
