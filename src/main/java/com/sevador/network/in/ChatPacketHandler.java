package com.sevador.network.in;

import java.io.IOException;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.content.Command;
import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;
import com.sevador.network.out.PublicChatMessage;
import com.sevador.utility.Misc;
import com.sevador.utility.punish.UserPunishHandler;
import com.sevador.utility.saving.ChatLogger;

/**
 * Handles chat related packets.
 * 
 * @author Emperor
 * 
 */
public class ChatPacketHandler implements PacketSkeleton {

	/**
	 * The public chat message opcode.
	 */
	private static final int PUBLIC_CHAT = 19;

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		switch (packet.getOpcode()) {
		case 18:
			player.setAttribute("clanMessage", packet.readByte() > 0);
			return true;
		case PUBLIC_CHAT:
			int effects = packet.readShort();
			int length = packet.readByte() & 0xFF;
			if (length < 0) {
				return false;
			}
			String text = Misc.decompressHuffman(packet, length);
			text = Censoring.checkMessage(player, text);
			if (player.getAttribute("clanMessage")) {
				player.removeAttribute("clanMessage");
				FriendChatManager.getFriendChatManager().sendChannelMessage(
						player, text);
				return true;
			}
			/*if (text.startsWith("::") && player.getCredentials().getRights() != 2) {
				player.getPacketSender().sendMessage("<col=FF5666>Use the reverse apostrophe (') key to open the console to enter that command.");
				player.getPacketSender().sendMessage("<col=FF5666>It is usually located under the ESC key.");
				return true;
			}*/
			if (text.startsWith("::")) {
				Command.handleCommand(player, text.replace("::", "")); 
				return true;
			}
			if (UserPunishHandler.isMuted(player)) {
				player.getPacketSender().sendMessage(
						"You are muted and cannot chat.");
				return true;
			}
			if (player.getAttribute("lastTextMessage") != null && ((String) player.getAttribute("lastTextMessage")).equalsIgnoreCase(text)) {
				player.getPacketSender().sendMessage("You have recently sent that exact same message; it was not sent to prevent spamming.");
				return true;
			}
			if (text.equalsIgnoreCase(" ")) return true;
			text = Misc.optimizeText(text);
			int index = player.getIndex();
			int rights = player.getCredentials().isDonator() ? 4 : player.getCredentials().getRights();
			for (Player p : NodeWorker.getPlayers()) {
				if (p == null || p.getLocation().getRegionID() != player.getLocation().getRegionID()) continue;
				p.getIOSession().write(new PublicChatMessage(p, index, rights, text, effects));
			}
			player.setAttribute("lastTextMessage", text);
			try {
				ChatLogger.logMessage(new String[] { player.getCredentials().getDisplayName(), text, Misc.formatIp(player.getIOSession().getChannel().getRemoteAddress().toString())});
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		return true;
	}

	public static class Censoring {

		public static String checkMessage(Player player, String msg) {
			String[] rudeWords = { "nigger", "nigga", "porn", "redtube"};
			String[] censored = { ".com", ".Com", ".Org", ".org", ".C0m",
					".net", ".Net", "ziotic", "runemyth", "dementhium",
					"xaeron", ",c0m", ". Com", "cloudin", "c o m", "zealusion" };
			for (int i = 0; i < rudeWords.length; i++) {
				if (msg.contains(rudeWords[i])) {
					msg = msg.replace(rudeWords[i],
							getStars(rudeWords[i].length()));
				}
			}
			for (int i = 0; i < censored.length; i++) {
				if (msg.contains(censored[i])) {
					msg = msg.replace(censored[i],
							getStars(censored[i].length()));
				}
			}
			return msg;
		}

		private static String getStars(int number) {
			String stars = "";
			for (int i = 0; i < number; i++)
				stars += "*";
			return stars;
		}

	}

}