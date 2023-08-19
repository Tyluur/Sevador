package com.sevador.game.event;

import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Represents an NPC action event.
 * @author Emperor
 *
 */
public interface NPCActionEvent {

	/**
	 * Initializes the event.
	 * @return {@code True} if succesful.
	 */
	public boolean init();
	
	/**
	 * Handles the event.
	 * @param p The player we're handling for.
	 * @param npc The npc we're handling.
	 * @param option The option type.
	 * @return {@code True} if succesful.
	 */
	public boolean handle(Player p, NPC npc, OptionType option);
	
}