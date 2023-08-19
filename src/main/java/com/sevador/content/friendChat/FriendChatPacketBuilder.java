package com.sevador.content.friendChat;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.utility.Misc;


/**
 * @author 'Mystic Flow
 */
public class FriendChatPacketBuilder {

	private static int counter = 1;

	public static OutgoingPacket buildFriendMessage(Player from, String roomName, String user, String message) {
		message = Misc.fixChatMessage(message);
		user = Misc.formatPlayerNameForDisplay(user);
		int id = nextIdentifier();
		byte[] bytes = new byte[256];
		bytes[0] = (byte) message.length();
		int length = 1 + Misc.huffmanCompress(message, bytes, 1);
		OutgoingPacket bldr = new OutgoingPacket(null, 17, PacketType.VAR_BYTE);
		bldr.put(0);
		bldr.putRS2String(user);
		bldr.putLong(Misc.stringToLong(roomName));
		bldr.putShort(id >> 32);
		bldr.putMedium(id - ((id >> 32) << 32));
		bldr.put(from.getCredentials().getRights());
		bldr.putBytes(bytes, 0, length);
		return bldr;
	}

	public static int nextIdentifier() {
		counter++;
		int random = Misc.random(Integer.MIN_VALUE, Integer.MAX_VALUE);
		return random + counter;
	}

	public static OutgoingPacket buildFriendChannel(FriendChat friendChannel) {
		OutgoingPacket bldr = new OutgoingPacket(null, 86, PacketType.VAR_SHORT);
		if (friendChannel != null) {
			bldr.putRS2String(friendChannel.getOwner());
			bldr.put(0);
			bldr.putLong(Misc.stringToLong(friendChannel.getName()));
			bldr.put(friendChannel.getKickReq());
			bldr.put(friendChannel.getMembers().size());
			for (Player pl : friendChannel.getMembers()) {
				bldr.putRS2String(pl.getCredentials().getUsername());
				bldr.put(0); // Need to figure this out
				bldr.putShort(1); // idk tbh ;s
				bldr.put(friendChannel.getRank(pl)); //?
				bldr.putRS2String(pl.getIOSession().isInLobby() ? "Lobby" : "Sevador");
			}
		}
		return bldr;
	}

}
