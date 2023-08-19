package com.sevador.update;

import com.sevador.game.node.ChatMessage;
import com.sevador.game.node.player.Player;

/**
 * Holds miscellaneous player flags.
 * 
 * @author Emperor
 *
 */
public class PlayerFlags {

	/**
	 * IF the player's map region changed.
	 */
	private boolean mapRegionChanged;
	
	/**
	 * If the player has teleported.
	 */
	private boolean teleported;
	
	/**
	 * The player's public chat message.
	 */
	private ChatMessage chatMessage;
	
	/**
	 * Constructs a new {@code PlayerFlags} {@code Object}.
	 */
	public PlayerFlags() {

	}

	/**
	 * Updates the player's player flags.
	 * @param player The player.
	 */
	public void update(Player player) {
	}
	
	/**
	 * @return the mapRegionChanged
	 */
	public boolean isMapRegionChanged() {
		return mapRegionChanged;
	}

	/**
	 * @param mapRegionChanged the mapRegionChanged to set
	 */
	public void setMapRegionChanged(boolean mapRegionChanged) {
		this.mapRegionChanged = mapRegionChanged;
	}

	/**
	 * @return the teleported
	 */
	public boolean isTeleported() {
		return teleported;
	}

	/**
	 * @param teleported the teleported to set
	 */
	public void setTeleported(boolean teleported) {
		this.teleported = teleported;
	}

	/**
	 * @return the chatMessage
	 */
	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	/**
	 * @param chatMessage the chatMessage to set
	 */
	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}
}