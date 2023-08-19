package com.sevador.network.out;

import java.util.List;

import com.sevador.game.node.player.Contact;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Represents the update ignores list outgoing packet.
 * @author Emperor
 *
 */
public class UpdateIgnoresList extends OutgoingPacket {

	/**
	 * The list of ignored players.
	 */
	private final List<Contact> contacts;
	
	/**
	 * Constructs a new {@code UpdateIgnoresList} {@code Object}.
	 * @param player The player.
	 * @param contacts The ignored players list.
	 */
	public UpdateIgnoresList(Player player, List<Contact> contacts) {
		super(player, 11, PacketType.VAR_SHORT);
		this.contacts = contacts;
	}

	@Override
	public OutgoingPacket get() {
        put(contacts.size());
        for (Contact c : contacts) {
        	String name = c.getFormatName();
        	String lastName = c.getLastName();
        	putRS2String(name);
        	if (!lastName.equals("null")) {
        		putRS2String(lastName);
        		putRS2String(lastName);
            	putRS2String(name);
        	}
        }
        return this;
	}
}