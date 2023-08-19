package com.sevador.game.node.player.action;

import com.sevador.game.node.player.Player;

public abstract class SkillAction {

	public abstract boolean start(Player player);

	public abstract boolean process(Player player);

	public abstract int processWithDelay(Player player);
	
	public abstract void stop(Player player);

	protected final void setActionDelay(Player player, int delay) {
		player.getSkillAction().setActionDelay(delay);
	}
}
