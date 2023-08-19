package com.sevador.content.friendChat;

import java.io.File;
import java.util.Map;

import net.burtleburtle.script.XMLHandler;
import net.burtleburtle.tick.Tick;

import com.sevador.Main;
import com.sevador.game.node.player.Contact;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.in.ChatPacketHandler.Censoring;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.UpdateFriendsList;
import com.sevador.utility.Misc;

/**
 * @author 'Mystic Flow
 */
public class FriendChatManager {

	private static final FriendChatManager INSTANCE = new FriendChatManager();

	public static FriendChatManager getFriendChatManager() {
		return INSTANCE;
	}

	private Map<String, FriendChat> friendChats;

	public FriendChatManager() {
		try {
			File file = new File("./data/xml/friend_chats.xml");
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
		}
		try {
			friendChats = XMLHandler.fromXML("./data/xml/friend_chats.xml");
		} catch (Throwable e) {
			// friendChats = new HashMap<String, FriendChat>();
		}
		for (Map.Entry<String, FriendChat> entries : friendChats.entrySet()) {
			entries.getValue().setTransient();
		}
		Main.getLogger().info("Read " + friendChats.size() + " friend chat xml tables.");
	}

	public FriendChat getClans(String s) {
		return friendChats.get(Misc.formatPlayerNameForProtocol(s));
	}

	public Map<String, FriendChat> getClans() {
		return friendChats;
	}

	public void createFriendChat(Player p, String name) {
		if (name.equals("")) {
			return;
		}
		String user = Misc.formatPlayerNameForProtocol(p.getCredentials()
				.getUsername());
		if (!friendChats.containsKey(user)) {
			FriendChat friendChat = new FriendChat(user, name);
			friendChats.put(Misc.formatPlayerNameForProtocol(p.getCredentials()
					.getUsername()), friendChat);
			refresh(friendChat);
			p.getPacketSender().sendMessage("You have successfuly created friend chat " + name
					+ ".");
			try {
				XMLHandler.toXML("./data/xml/friend_chats.xml", FriendChatManager.getFriendChatManager().getClans());
				Main.getLogger().info("Saved clans!");
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			FriendChat friendChat = friendChats.get(user);
			friendChat.setName(name);
			refresh(friendChat);
		}
	}

	public void joinFriendChat(final Player p, final String user) {
		p.getPacketSender().sendMessage("Attempting to join channel...");
		final FriendChat friendChat = friendChats.get(Misc
				.formatPlayerNameForProtocol(user));
		if (friendChat == null) {
			World.getWorld().submit(new Tick(1) {
				@Override
				public boolean run() {
					p.getPacketSender().sendMessage("The channel you tried to join does not exist.");
					return true;
				}
			});
			return;
		}
		if (friendChat.getMembers().contains(p)) {
			p.getPacketSender().sendMessage("You are already in a friends chat channel.");
			return;
		}
		World.getWorld().submit(new Tick(1) {
			@Override
			public boolean run() {
				if (friendChat.canJoin(p)) {
					p.getSettings().setCurrentFriendChat(friendChat);
					friendChat.addMember(p);
					refresh(friendChat);
					p.getIOSession().write(
							new ConfigPacket(p, 1083, friendChat.isLootsharing() ? 1
									: 0));
					p.getPacketSender().sendMessage("Now talking in the channel "
							+ friendChat.getName());
					p.getPacketSender().sendMessage("To talk, start each line of chat with the / symbol.");
				} else {
					p.getPacketSender().sendMessage("You don't have a high enough rank to join this channel.");
				}
				return true;
			}
		});
	}

	public void destroy(Player player, String username) {
		FriendChat c = friendChats.get(Misc
				.formatPlayerNameForProtocol(username));
		if (c != null) {
			OutgoingPacket message = FriendChatPacketBuilder
					.buildFriendChannel(c);
			for (Player p : c.getMembers()) {
				if (p == null) {
					continue;
				}
				p.getIOSession().write(message);
			}
		}
		friendChats.remove(username);
	}

	public void refresh(FriendChat friendChannel) {
		friendChannel.ensureMembers();
		for (Player p : friendChannel.getMembers()) {
			if (p == null) {
				continue;
			}
			p.getIOSession().write(
					FriendChatPacketBuilder.buildFriendChannel(friendChannel));
		}
	}

	public void leaveChannel(Player player) {
		FriendChat c = player.getSettings().getCurrentFriendChat();
		if (c != null) {
			c.removeMember(player);
			refresh(c);
			player.getIOSession().write(
					FriendChatPacketBuilder.buildFriendChannel(null));
			player.getIOSession().write(new ConfigPacket(player, 1083, 0));
			player.getSettings().setCurrentFriendChat(null);
		}
	}

	public void rankMember(Player player, String user, int rank) {
		FriendChat c = friendChats.get(Misc.formatPlayerNameForProtocol(player
				.getCredentials().getUsername()));
		if (c == null) {
			return;
		}
		c.rankUser(user, rank);
		Player friend = World.getWorld().getPlayerInServer(user);
		Contact contact = null;
		for (Contact c_ : player.getContacts().getFriends()) {
			if (c_.getName().equalsIgnoreCase(user)) {
				contact = c_;
				break;
			}
		}
		try {
			if (contact != null) {
				UpdateFriendsList update = new UpdateFriendsList(player);
				player.getIOSession().write(
						update.add(contact, friend == null ? 0 : 1, true,
								friend == null ? false : friend.getIOSession()
										.isInLobby()));
			}
			refresh(c);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public String getChannelName(String user) {
		FriendChat c = friendChats.get(Misc.formatPlayerNameForProtocol(user));
		if (c == null) {
			return "Chat disabled";
		}
		return c.getName();
	}

	public void sendChannelMessage(Player player, String text) {
		text = Censoring.checkMessage(player, text);
		FriendChat c = player.getSettings().getCurrentFriendChat();
		if (c == null) {
			player.getPacketSender().sendMessage("You aren't in a Friends Chat Channel.");
			return;
		}
		for (Player pl : c.getMembers()) {
			pl.getIOSession().write(
					FriendChatPacketBuilder.buildFriendMessage(player,
							c.getName(), player.getCredentials().getUsername(),
							text));
		}
	}

	public void toggleLootshare(Player player) {
		FriendChat c = friendChats.get(Misc.formatPlayerNameForProtocol(player
				.getCredentials().getUsername()));
		if (c != null) {
			c.toggleLootshare();
		} else {
			player.getPacketSender().sendMessage("You don't have a channel to active lootshare with.");
		}
	}
}
