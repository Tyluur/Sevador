package com.sevador.network;

import org.jboss.netty.buffer.ChannelBuffer;

import com.sevador.game.node.player.Player;

/**
 * The interface implemented by the incoming and outgoing packets.
 * 
 * @author Emperor
 *
 */
public interface Packet {

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer();
	
	/**
	 * Gets the opcode of this packet.
	 * @return The opcode.
	 */
	public int getOpcode();

	/**
	 * Gets the size of this packet.
	 * @return The packet size.
	 */
	public int getSize();

	/**
	 * If the packet is raw.
	 * @return {@code True} if the opcode is {@code -1}.
	 */
	public boolean isRaw();

	/**
	 * Gets the packet type.
	 * @return The packet type.
	 */
	public PacketType getType();

	/**
	 * Gets the channel buffer.
	 * @return The channel buffer.
	 */
	public ChannelBuffer getBuffer();

}
