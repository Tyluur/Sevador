package com.sevador.game.node.player;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.network.out.MessagePacket;
import com.sevador.network.out.ReceivePrivateMessage;
import com.sevador.network.out.SendPrivateMessage;
import com.sevador.network.out.UpdateFriendsList;
import com.sevador.network.out.UpdateIgnoresList;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * Represents the player's contacts.
 * 
 * @author Emperor
 * 
 */
public class Contacts implements Serializable {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -738908783022139254L;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The player's friends.
	 */
	private final List<Contact> friends;

	/**
	 * The player's ignored players.
	 */
	private final List<Contact> ignores;

	/**
	 * Constructs a new {@code Contacts} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 */
	public Contacts(Player player) {
		this.player = player;
		this.friends = new ArrayList<Contact>();
		this.ignores = new ArrayList<Contact>();
	}

	/**
	 * Adds a player to the friend list.
	 * 
	 * @param username
	 *            The player's username.
	 */
	public void addFriend(String username) {
		for (char c : username.toCharArray()) {
			if (!Misc.allowed(c)) {
				return;
			}
		}
		if (friends.size() >= 200) {
			player.getIOSession().write(
					new MessagePacket(player, new StringBuilder(
							"Your friends list is full.").toString()));
			return;
		}
		if (!new File(new StringBuilder(""+Constants.SAVE_PATH+"").append(username)
				.append(PlayerWorldLoader.suffix).toString()).exists()) {
			player.getIOSession()
					.write(new MessagePacket(
							player,
							new StringBuilder(
									"There is no player by the name of "
											+ Misc.formatPlayerNameForDisplay(username))
									.append(".").toString()));
			return;
		}
		Contact contact = new Contact(username, "null");
		if (friends.contains(contact)) {
			player.getIOSession().write(
					new MessagePacket(player, new StringBuilder(
							"This player is already on your friends list.")
							.toString()));
			return;
		}
		friends.add(contact);
		Player p = NodeWorker.getPlayer(username);
		if (p == null) {
			p = NodeWorker.getLobbyPlayer(username);
		}
		boolean online = p != null;
		player.getIOSession().write(
				new UpdateFriendsList(player).add(contact, online ? 1 : 0,
						online, online ? p.getIOSession().isInLobby() : false));
	}

	/**
	 * Adds a player to the ignores list.
	 * 
	 * @param username
	 *            The player's username.
	 */
	public void addIgnore(String username) {
		for (char c : username.toCharArray()) {
			if (!Misc.allowed(c)) {
				return;
			}
		}
		if (ignores.size() >= 100) {
			player.getIOSession().write(
					new MessagePacket(player, new StringBuilder(
							"Your ignores list is full.").toString()));
			return;
		}
		if (!new File(new StringBuilder(""+Constants.SAVE_PATH+"").append(username)
				.append(PlayerWorldLoader.suffix).toString()).exists()) {
			player.getIOSession()
					.write(new MessagePacket(
							player,
							new StringBuilder(
									"There is no player by the name of "
											+ Misc.formatPlayerNameForDisplay(username))
									.append(".").toString()));
			return;
		}
		Contact contact = new Contact(username, "null");
		if (ignores.contains(contact)) {
			player.getIOSession().write(
					new MessagePacket(player, new StringBuilder(
							"This player is already on your ignores list.")
							.toString()));
			return;
		}
		ignores.add(contact);
		player.getIOSession().write(new UpdateIgnoresList(player, ignores));
	}

	/**
	 * Handles a private message.
	 * 
	 * @param from
	 *            The player sending the private message.
	 * @param to
	 *            The player who's receiving the pm's name.
	 * @param message
	 *            The message.
	 */
	public void handlePrivateMessage(String to, String message) {
		String formattedName = to.toLowerCase();
		/*
		 * for (Contact c : friends) { player.getIOSession().write(new
		 * MessagePacket(player, new
		 * StringBuilder("You do not have ").append(to).append
		 * (" added in your friends list.").toString())); return; }
		 */
		Player p = NodeWorker.getPlayer(formattedName);
		if (p == null) {
			p = NodeWorker.getLobbyPlayer(formattedName);
			if (p == null) {
				player.getIOSession().write(
						new MessagePacket(player, "This player is not online."));
				return;
			}
		}
		message = Misc.optimizeText(message);
		player.getIOSession().write(new SendPrivateMessage(p, to, message));
		p.getIOSession().write(
				new ReceivePrivateMessage(p, Misc.formatPlayerNameForDisplay(player.getCredentials()
						.getDisplayName()), message, player.getCredentials()
						.getRights()));
	}

	/**
	 * Sends the player's friends & ignores lists.
	 */
	public void init() {
		if (friends.size() < 1) {
			player.getIOSession().write(new UpdateFriendsList(player));
		}
		for (Contact c : friends) {
			Player p = NodeWorker.getPlayer(c.getName());
			if (p == null) {
				p = NodeWorker.getLobbyPlayer(c.getName());
			}
			boolean online = p != null;
			player.getIOSession().write(
					new UpdateFriendsList(player).add(c, online ? 1 : 0, false,
							online ? p.getIOSession().isInLobby() : false));
		}
		player.getIOSession().write(new UpdateIgnoresList(player, ignores));
		Contact c = new Contact(player.getCredentials().getUsername(), "null");
		boolean lobby = player.getIOSession().isInLobby();
		for (Player p : NodeWorker.getPlayers()) {
			if (p != null && p.getContacts().getFriends().contains(c)) {
				p.getIOSession().write(
						new UpdateFriendsList(p).add(c, 1, true, lobby));
			}
		}
		for (Player p : NodeWorker.getLobbyPlayers()) {
			if (p != null && p.getContacts().getFriends().contains(c)) {
				p.getIOSession().write(
						new UpdateFriendsList(p).add(c, 1, true, lobby));
			}
		}
	}

	/**
	 * Updates all players that this player has logged out.
	 * 
	 * @param world
	 *            If the player logged out from the world.
	 */
	public void finalization(boolean world) {
		Contact c = new Contact(player.getCredentials().getUsername(), "null");
		boolean lobby = player.getIOSession().isInLobby();
		for (Player p : NodeWorker.getPlayers()) {
			if (p != null && p.getContacts().getFriends().contains(c)) {
				p.getIOSession().write(
						new UpdateFriendsList(p).add(c, 0, world, lobby));
			}
		}
		for (Player p : NodeWorker.getLobbyPlayers()) {
			if (p != null && p.getContacts().getFriends().contains(c)) {
				p.getIOSession().write(
						new UpdateFriendsList(p).add(c, 0, world, lobby));
			}
		}
	}

	/**
	 * @return the friends
	 */
	public List<Contact> getFriends() {
		return friends;
	}

	/**
	 * @return the ignores
	 */
	public List<Contact> getIgnores() {
		return ignores;
	}
}