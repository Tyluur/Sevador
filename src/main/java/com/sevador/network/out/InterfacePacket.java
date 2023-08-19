package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Creates a new interface packet.
 * @author Emperor
 *
 */
public class InterfacePacket extends OutgoingPacket {
	
	/**
	 * The window id.
	 */
	private final int windowId;
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * If the interface is an overlay.
	 */
	private boolean walkable;
	
	/**
	 * Constructs a new {@code InterfacePacket} packet {@code Object}.
	 * @param player The player.
	 * @param windowId The window id.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param walkable If the interface is an overlay.
	 */
	public InterfacePacket(Player player, int windowId, int interfaceId, int childId, boolean walkable) {
		super(player, 139);
		this.windowId = windowId;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.walkable = walkable;
	}
	
	@Override
	public OutgoingPacket get() {
		//putPacket(139);
		putByteS(walkable ? 1 : 0);
		putShortA(childId);
		putInt(windowId << 16 | interfaceId);
		return this;
	}

}