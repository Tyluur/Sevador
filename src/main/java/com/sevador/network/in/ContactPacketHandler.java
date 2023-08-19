package com.sevador.network.in;

import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;
import com.sevador.utility.Misc;
import com.sevador.utility.punish.UserPunishHandler;

/**
 * Handles incoming contact-related packets. (friends & ignores lists)
 * @author Emperor
 *
 */
public class ContactPacketHandler implements PacketSkeleton {

	/**
	 * The add friend opcode.
	 */
	private static final int ADD_FRIEND_OPCODE = 31;
	
	/**
	 * The add ignore opcode.
	 */
	private static final int ADD_IGNORE_OPCODE = 68;
	
	/**
	 * The send private message opcode.
	 */
	private static final int SEND_PRIVATE_MESSAGE = 13;
	
	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		case ADD_FRIEND_OPCODE:
			player.getContacts().addFriend(packet.readRS2String().toLowerCase());
			return true;
		case ADD_IGNORE_OPCODE:
			player.getContacts().addIgnore(packet.readRS2String().toLowerCase());
			return true;
		case SEND_PRIVATE_MESSAGE:
			String name = packet.readRS2String();
            byte length = packet.readByte();
            String message = Misc.decompressHuffman(packet, length);
			if (UserPunishHandler.isMuted(player)) {
				player.getPacketSender().sendMessage("You are muted and cannot chat.");
				return true;
			}
            player.getContacts().handlePrivateMessage(name, message);
			return true;
		}
		return false;
	}

}
