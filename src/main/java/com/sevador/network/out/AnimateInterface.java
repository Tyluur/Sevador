package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Handles the animating of an interface.
 * 
 * @author Vincent (identified the packet)
 * @author Emperor
 *
 */
public class AnimateInterface extends OutgoingPacket {

	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * The animation id.
	 */
	private final int animationId;
	
	/**
	 * Constructs a new {@code AnimateInterface} {@code Object}.
	 * @param player The player.
	 * @param interfaceId The interface id.
	 * @param childId The child id.
	 * @param animationId The animation id.
	 */
	public AnimateInterface(Player player, int interfaceId, int childId, int animationId) {
		super(player, 131);
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.animationId = animationId;
	}
	
	@Override
	public OutgoingPacket get() {
		return putShort(animationId).putInt(interfaceId << 16 | childId);
	}

}