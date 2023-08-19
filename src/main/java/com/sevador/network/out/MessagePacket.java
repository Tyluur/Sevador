package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Creates a new {@code MessagePacket} {@code packet}.
 * @author Emperor
 *
 */
public class MessagePacket extends OutgoingPacket {

	/**
	 * The id of this message.
	 */
	private final int id;
	
	/**
	 * The identifier.
	 */
	private final byte identifier;
	
	/**
	 * The actual message to send.
	 */
	private final String message;
	
	/**
	 * The username.
	 */
	private final String username;
	
	/**
	 * Constructs a new {@code MessagePacket} {@code Object} with {@code 0} as message id (normal game message).
	 * @param player The player.
	 * @param message The message.
	 */
	public MessagePacket(Player player, String message) {
		this(player, 0, message);
	}
	
	/**
	 * Constructs a new {@code MessagePacket} {@code Object}.
	 * @param player The player.
	 * @param id The message id.
	 * @param message The message.
	 */
	public MessagePacket(Player player, int id, String message) {
		super(player, 98, PacketType.VAR_BYTE);
		this.id = id;
		this.identifier = 0;
		this.message = message;
		this.username = null;
	}
	
	/**
	 * Constructs a new {@code MessagePacket} {@code Object} used for sending a duel or trade request.
	 * @param player The player.
	 * @param id The text id (100 = trade, 101 = duel request).
	 * @param message The message.
	 * @param username The username.
	 */
	public MessagePacket(Player player, int id, String message, String username) {
		super(player, 98, PacketType.VAR_BYTE);
		this.id = id;
		this.identifier = 0x1;
		this.message = message;
		this.username = username;
	}
	
	@Override
	public OutgoingPacket get() {
		putSmart(id);
		putInt(0);
		put(identifier);
		if (username != null) {
			putRS2String(username);
		}
		putRS2String(message);
		return this;
	}

}
