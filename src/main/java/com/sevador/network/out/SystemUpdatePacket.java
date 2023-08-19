package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * The system update message packet.
 * 
 * @author Jefferson
 * 
 */
public class SystemUpdatePacket extends OutgoingPacket {

	/**
	 * Updating time tick.
	 */
	private int time;
	
	/***
	 * Creates a new system update packet.
	 * 
	 * @param player
	 * @param time
	 */
	public SystemUpdatePacket(Player player, int time) {
		super(player, 10);
	}

	@Override
	public OutgoingPacket get() {
		putShort(125);
		putShort((int) (time * 1.6));
		return this;
	}
	
	/**
	 * @return the time
	 */
	public int getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(int time) {
		this.time = time;
	}

}
