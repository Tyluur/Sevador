package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * The packet used to update a string on an interface.
 * @author Emperor
 *
 */
public class StringPacket extends OutgoingPacket {

	//TODO CONVERT TO 666!!!
	/**
	 * The string.
	 */
	private final String string;
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * Constructs a new {@code StringPacket} {@code Object}.
	 * @param player The player.
	 * @param string The string.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 */
	public StringPacket(Player player, String string, int interfaceId, int childId) {
		super(player, 69, PacketType.VAR_SHORT);
		this.string = string;
		this.interfaceId = interfaceId;
		this.childId = childId;
	}
	
	@Override
	public OutgoingPacket get() {
		putInt(interfaceId << 16 | childId);
		putRS2String(string);
		//putShortA(getPlayer().getActionSender().getFrameIndex());
		return this;
	}

}
