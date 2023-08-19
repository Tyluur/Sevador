package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Handles the Access mask outgoing packet.
 * 
 * @author Emperor
 *
 */
public class AccessMask extends OutgoingPacket {

	/**
	 * The interface id.
	 */
	private final int interfaceId;
	private final int interfaceId2;
	
	/**
	 * The child id.
	 */
	private final int childId;
	private final int childId2;
	
	/**
	 * The minimum slot.
	 */
	private final int min;
	
	/**
	 * The maximum slot.
	 */
	private final int max;
	
	/**
	 * The value.
	 */
	//private final int value;
	
	/**
	 * Constructs a new {@code AccessMask} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param min The minimum slot.
	 * @param max The maximum slot.
	 * @param value The value.
	 */
	public AccessMask(Player player, int min, int max, int interfaceId, int childId, int interfaceId2, int childId2) {
		super(player);
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.min = min;
		this.max = max;
		this.interfaceId2 = interfaceId2;
		this.childId2 = childId2;
	}
	
	@Override
	public OutgoingPacket get() {
		int value = interfaceId2 << 16 | childId2;
        setOpcode(42);
        putLEShort(min);
        putLEInt(value); //Value
        putInt1(interfaceId << 16 | childId);
        putLEShort(max);
		return this;
	}
	
}
