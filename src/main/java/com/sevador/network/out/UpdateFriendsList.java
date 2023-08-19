package com.sevador.network.out;

import com.sevador.game.node.player.Contact;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Represents the update friends list packet.
 * @author Emperor
 *
 */
public class UpdateFriendsList extends OutgoingPacket {

	/**
	 * Constructs a new {@code UpdateFriendsList} {@code Object}.
	 * @param player The player.
	 */
	public UpdateFriendsList(Player player) {
		super(player, 5, PacketType.VAR_SHORT);
	}
	
	/**
	 * Updates a friend.
	 * @param contact The contact to add.
	 * @param world The world id.
	 * @param warn If this player should be warned.
	 * @param lobby If the player is in the lobby.
	 * @return This OutgoingPacket instance.
	 */
    public OutgoingPacket add(Contact contact, int world, boolean warn, boolean lobby) {
        put(warn ? 0 : 1);
        putRS2String(contact.getFormatName());
        if (!contact.getLastName().equals("null")) {
        	putRS2String(contact.getLastName());
        }
        putShort(world);
        put(contact.getClanRank());
        put(0);
        if (world > 0) {
            putRS2String(new StringBuilder(((lobby) ? " Lobby " : " World ")).append(world).toString());
            put(0);
        }
        return this;
    }

}