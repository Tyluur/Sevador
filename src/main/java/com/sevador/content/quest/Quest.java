package com.sevador.content.quest;

import java.io.Serializable;
import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

import com.sevador.content.quest.impl.CooksAssistant;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ItemOnInterfacePacket;
import com.sevador.network.out.StringPacket;

/**
 * Represents a quest.
 * @author Tyluur
 */
public abstract class Quest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3715117454761784404L;

	/**
	 * The quest id.
	 */
	private final int id;
	
	/**
	 * The quest name.
	 */
	private final String name;
	
	/**
	 * The current quest progress.
	 */
	protected int progress = -1;
	
	/**
	 * The quest points you get rewarded with.
	 */
	private final int questPoints;
	
	/**
	 * Constructs a new {@code Quest} {@code Object}.
	 * @param id The quest id.
	 * @param name The quest name.
	 */
	public Quest(int id, int questPoints, String name) {
		this.id = id;
		this.questPoints = questPoints;
		this.name = name;
	}
	
	/**
	 * Creates a new instance of this quest.
	 * @return The constructed Quest object.
	 */
	public abstract Quest create();

	/**
	 * Passes an event to this quest.
	 * @return {@code True} if the event got handled.
	 */
	public abstract boolean passEvent(QuestEvent event);
	
	/**
	 * Initializes the quest for the player.
	 * @param player The player.
	 */
	public abstract void initialize(Player player);
	
	/**
	 * Called when the player has finished the quest.
	 * @param player The player.
	 */
	public abstract void finish(Player player);
	
	/**
	 * If the quest is completed.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public abstract boolean completed();

	/**
	 * Sends the quest guide for a player.
	 * @param player The player.
	 */
	public abstract void sendQuestGuide(Player player);
	
	/**
	 * Sends the quest guide data.
	 * @param player The player.
	 * @param stages The stages.
	 */
	public final void sendGuide(Player player, String[]... stages) {
		player.getIOSession().write(new StringPacket(player, name, 275, 2));
		int count = 0;
		int child = 16;
		for (int i = 0; i < stages.length; i++) {
			player.getIOSession().write(new StringPacket(player, "", 275, child++));
			for (String s : stages[i]) {
				if (i == stages.length - 1) {
					player.getIOSession().write(new StringPacket(player, s, 275, child++));
				} else {
					player.getIOSession().write(new StringPacket(player, new StringBuilder("<str>").append(s).append("</str>").toString(), 275, child++));
				}
			}
			count += stages[i].length;
		}
		while (child < 27) {
			player.getIOSession().write(new StringPacket(player, "", 275, child++));
		}
		player.getIOSession().write(new CS2Script(player, 1207, "i", new Object[]{count + (stages.length)}));
		player.getPacketSender().sendInterface(275);
	}
	
	/**
	 * Loads this quest's settings from the byte buffer.
	 * @param player The player we're loading for.
	 * @param buf The byte buffer to parse.
	 * @warning Always call this method, even when overriding.
	 */
	public void load(Player player, ByteBuffer buf) {
		progress = buf.getShort();
	}

	/**
	 * Saves the quest settings on the byte buffer.
	 * @param player The player we're saving for.
	 * @param buf The channel buffer to write on.
	 * @warning Always call this method, even when overriding.
	 */
	public void save(Player player, ChannelBuffer buf) {
		buf.writeShort(progress);
	}
	
	/**
	 * Refreshes this quest for the player.
	 * @param player The player.
	 */
	public void refresh(Player player) {
		player.getPacketSender().sendConfig(29, (player.getQuestListener().start(CooksAssistant.ID).completed() ? 2 : (player.getQuestListener().start(CooksAssistant.ID).getProgress() == -1 ? 0 : -1)));//Cook's Assistant
	}
	
	/**
	 * Sends the you have completed this quest interface.
	 * @param player The player.
	 */
	public final void sendCompletionInterface(Player player, int itemId, String...rewards) {
		player.getIOSession().write(new StringPacket(player, new StringBuilder("You have completed the ").append(name).append(" Quest!").toString(), 277, 4));
		player.getIOSession().write(new StringPacket(player, new StringBuilder().append(player.getQuestListener().getQuestPoints()).toString(), 277, 7));
		player.getIOSession().write(new StringPacket(player, new StringBuilder().append("You are awarded:").toString(), 277, 9));
		player.getIOSession().write(new StringPacket(player, new StringBuilder().append(questPoints).append(" Quest Points").toString(), 277, 10));
		for (int i = 0; i < 7; i++) {
			if (i >= rewards.length) {
				player.getIOSession().write(new StringPacket(player, "", 277, 11));
			} else {
				player.getIOSession().write(new StringPacket(player, rewards[i], 277, 11 + i));
			}
		}
		player.getIOSession().write(new ItemOnInterfacePacket(player, 277, 5, 175, itemId));
		player.getPacketSender().sendInterface(277);
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * @return the questPoints
	 */
	public int getQuestPoints() {
		return questPoints;
	}
	
}