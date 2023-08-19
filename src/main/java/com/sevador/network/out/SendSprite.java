package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Handles the sending of a sprite on an interface.
 * @author Vincent (identified the packets)
 * @author Emperor
 *
 */
public class SendSprite extends OutgoingPacket {

	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * The sprite id to send.
	 */
	private final int spriteId;
	
	/**
	 * Constructs a new {@code SendSprite} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param spriteId The sprite id.
	 */
	public SendSprite(Player player, int interfaceId, int childId, int spriteId) {
		super(player, 22);
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.spriteId = spriteId;
	}
	
	@Override
	public OutgoingPacket get() {
		return putInt(interfaceId << 16 | childId).putLEShort(spriteId);
	}

}