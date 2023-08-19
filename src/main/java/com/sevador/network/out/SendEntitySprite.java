package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Handles the sending an entity on an interface.
 * @author Emperor
 * @author Vincent (identified the packets)
 *
 */
public class SendEntitySprite extends OutgoingPacket {

	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * The entity's id to send.
	 */
	private final int entityId;
	
	/**
	 * Constructs a new {@code SendEntitySprite} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param entityId The entity id.
	 */
	public SendEntitySprite(Player player, int interfaceId, int childId, int entityId) {
		super(player, entityId == -1 ? 48 : 136);
		this.entityId = entityId;
		this.interfaceId = interfaceId;
		this.childId = childId;
	}
	
	@Override
	public OutgoingPacket get() {
		if (entityId == -1) {
			return putInt1(interfaceId << 16 | childId);
		}
		return putInt(interfaceId << 16 | childId).putLEShortA(entityId);
	}

}