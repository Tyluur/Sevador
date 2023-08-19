package com.sevador.content.friendChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.sevador.game.node.player.Player;
import com.sevador.network.out.ConfigPacket;
import com.sevador.utility.Misc;

/**
 * 
 * 
 * @author 'Mystic Flow
 * 
 */
public class FriendChat {

	private String roomName;
	private String roomOwner;
	private int joinReq = 0;
	private int talkReq = 0;
	private int kickReq = 7;
	private HashMap<String, Byte> ranks;
	private transient List<Player> members;
	private transient boolean lootsharing;

	public FriendChat(String owner, String name) {
		this.roomName = name;
		this.roomOwner = owner;
		setTransient();
	}

	FriendChat() {

	}

	public void setTransient() {
		setLootsharing(false);
		if (kickReq == 0) {
			kickReq = 7;
		}
		if (members == null) {
			this.members = new ArrayList<Player>();
		}
		if (ranks == null) {
			this.ranks = new HashMap<String, Byte>();
		}
	}

	public String getName() {
		return roomName;
	}

	public String getOwner() {
		return roomOwner;
	}

	public void rankUser(String name, int rank) {
		ranks.put(name, (byte) rank);
	}

	/**
	 * Gets a player's rank.
	 *
	 * @param username The username.
	 * @return The rank.
	 */
	 public int getRank(String username) {
		if (ranks.containsKey(username)) {
			return ranks.get(username);
		}
		return 0;
	 }

	 public Byte getRank(Player player) {
		 if (Misc.formatPlayerNameForProtocol(player.getCredentials().getUsername()).equals(roomOwner)) {
			 return 7;
		 } else if (player.getCredentials().getRights() == 2) {
			 return 127;
		 } else if (ranks.containsKey(Misc.formatPlayerNameForProtocol(player.getCredentials().getUsername()))) {
			 return ranks.get(Misc.formatPlayerNameForProtocol(player.getCredentials().getUsername()));
		 } else {
		 }
		 return -1;
	 }

	 public boolean canJoin(Player player) {
		 byte rank = 0;
		 if (ranks.containsKey(player.getCredentials().getUsername())) {
			 rank = ranks.get(player.getCredentials().getUsername());
		 }
		 return rank >= joinReq;
	 }

	 public boolean canTalk(Player player) {
		 byte rank = 0;
		 if (ranks.containsKey(player.getCredentials().getUsername())) {
			 rank = ranks.get(player.getCredentials().getUsername());
		 }
		 return rank >= talkReq;
	 }

	 public void toggleLootshare() {
		 lootsharing = !lootsharing;
		 String message = "";
		 if (lootsharing) {
			 message = "Lootshare has been enabled.";
		 } else {
			 message = "Lootshare has been disabled.";
		 }
		 for (Player pl : members) {
			 pl.getPacketSender().sendMessage(message);
			 pl.getIOSession().write(new ConfigPacket(pl, 1083, lootsharing ? 1 : 0));
		 }
	 }

	 public void addMember(Player member) {
		 members.add(member);
	 }

	 public void setName(String name) {
		 this.roomName = name;
	 }

	 public List<Player> getMembers() {
		 return members;
	 }

	 public void removeMember(Player player) {
		 members.remove(player);
	 }

	 public HashMap<String, Byte> getRanks() {
		 return ranks;
	 }

	 public void setLootsharing(boolean lootsharing) {
		 this.lootsharing = lootsharing;
	 }

	 public boolean isLootsharing() {
		 return lootsharing;
	 }

	 public void setTalkReq(int talkReq) {
		 this.talkReq = talkReq;
	 }

	 public int getTalkReq() {
		 return talkReq;
	 }

	 public void setJoinReq(int joinReq) {
		 this.joinReq = joinReq;
	 }

	 public int getJoinReq() {
		 return joinReq;
	 }

	 public int getKickReq() {
		 return kickReq;
	 }

	 public void ensureMembers() {
		 //TODO
		 Iterator<Player> players = members.iterator();
		 while (players.hasNext()) {
			 Player player = players.next();
			 if (player == null || !player.getIOSession().getChannel().isConnected()) {
				 players.remove();
				 return;
			 }
		 }
	 }
}
