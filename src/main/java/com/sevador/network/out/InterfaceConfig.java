package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Represents the interface config outgoing packet.
 * @author Emperor
 *
 */
public class InterfaceConfig extends OutgoingPacket {

	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * If we should disable the child.
	 */
	private final boolean disable;
	
	/**
	 * Constructs a new {@code InterfaceConfig} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param disable If the child should be disabled.
	 */
	public InterfaceConfig(Player player, int interfaceId, int childId, boolean disable) {
		super(player, 102);
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.disable = disable;
	}
	
	@Override
	public OutgoingPacket get() {
        put(disable ? 1 : 0);
        putInt(interfaceId<< 16 | childId);
        return this;
	}

}