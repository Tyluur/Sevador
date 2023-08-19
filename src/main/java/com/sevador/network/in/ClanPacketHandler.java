package com.sevador.network.in;

import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class ClanPacketHandler implements PacketSkeleton {

	public static final int JOIN = 81, RANK = 51, MESSAGE = 18;

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		case JOIN:
			String owner = "";
			if (packet.remaining() > 0) {
				owner = packet.readRS2String();
			}
			if (owner.length() > 0) {
				FriendChatManager.getFriendChatManager().joinFriendChat(player, owner);
			} else {
				FriendChatManager.getFriendChatManager().leaveChannel(player);
				player.getSettings().setCurrentFriendChat(null);
			}
			break;
		case RANK:
			handleRank(player, packet);
			break;
		case MESSAGE:
			boolean clanMessage = packet.readByte() > 0;
			player.setAttribute("sendingClanMessage", clanMessage);
			break;
		}
		return true;
	}

	private void handleRank(Player player, IncomingPacket packet) {
		//something is sent before the string i think, ignore it though
		String playerName = packet.readRS2String();
		int rank = packet.readByteA();
		FriendChatManager.getFriendChatManager().rankMember(player, playerName, rank);
	}

}
