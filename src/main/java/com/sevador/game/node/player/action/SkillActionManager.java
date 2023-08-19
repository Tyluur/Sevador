package com.sevador.game.node.player.action;

import com.sevador.game.node.player.Player;

public final class SkillActionManager {

	private Player player;
	private SkillAction action;
	private SkillAction lastAction;
	private int actionDelay;

	public SkillActionManager(Player player) {
		this.player = player;
	}

	public void process() {
		if (action != null) {
			if (player.getSkills().getLifepoints() < 1)
				forceStop();
			else if (!action.process(player))
				forceStop();
		}
		if (actionDelay > 0) {
			actionDelay--;
			return;
		}
		if (action == null)
			return;
		int delay = action.processWithDelay(player);
		if (delay == -1) {
			forceStop();
			return;
		}
		actionDelay += delay;
	}

	public boolean setSkill(SkillAction skill) {
		forceStop();
		if (!skill.start(player))
			return false;
		this.action = skill;
		return true;
	}
	
	public boolean startLastAction() {
		if (lastAction == null) return false;
		forceStop();
		if (!lastAction.start(player)) return false;
		this.action = lastAction;
		return true;
	}

	public void forceStop() {
		if (action == null)
			return;
		lastAction = action;
		action.stop(player);
		action = null;
	}

	public int getActionDelay() {
		return actionDelay;
	}

	public void addActionDelay(int skillDelay) {
		this.actionDelay += skillDelay;
	}

	public void setActionDelay(int skillDelay) {
		this.actionDelay = skillDelay;
	}

	public boolean hasSkillWorking() {
		return action != null;
	}
}
