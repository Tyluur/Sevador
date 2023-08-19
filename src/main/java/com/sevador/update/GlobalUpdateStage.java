package com.sevador.update;

import com.sevador.game.node.player.Player;

/**
 * @author Sean
 */
public enum GlobalUpdateStage {

	ADD_PLAYER,
	HEIGHT_UPDATED,
	MAP_REGION_DIRECTION,
	TELEPORTED;

	/**
	 * Gets the global update stages.
	 * @param player The player for the update,
	 * @param otherPlayer The players to update for.
	 * @return The state.
	 */
	public static GlobalUpdateStage getStage(Player player, Player otherPlayer) {
		if (otherPlayer == null || !otherPlayer.isLoggedIn()) {
			return null;
		} else if (player != otherPlayer && player.getLocation().isWithinDistance(otherPlayer.getLocation())) {
			return ADD_PLAYER;
		} else if (otherPlayer.getRenderInformation().getLastLocation() != null && otherPlayer.getLocation().getZ() != otherPlayer.getRenderInformation().getLastLocation().getZ()) {
			return HEIGHT_UPDATED;
		} else if (otherPlayer.getPlayerFlags().isTeleported() || otherPlayer.getRenderInformation().isOnFirstCycle()) {
			return TELEPORTED;
		}
		return null;
	}
}