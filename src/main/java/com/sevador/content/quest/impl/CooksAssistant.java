package com.sevador.content.quest.impl;

import java.io.Serializable;

import com.sevador.content.quest.Quest;
import com.sevador.content.quest.QuestEvent;
import com.sevador.game.node.player.Player;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class CooksAssistant extends Quest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8060094507493795760L;

	/**
	 * The quest guide stages.
	 */
	private static final String[][] GUIDE = {
		{" Speak to the cook in the kitchen of Lumbridge Castle."}, 
		{ "Find a special pot and bucket for the chef!" }
	};

	public CooksAssistant() {
		super(ID, 1, "Cook's Assistant");
	}

	public static final int ID = 0;

	@Override
	public Quest create() {
		return new CooksAssistant();
	}

	@Override
	public boolean passEvent(QuestEvent event) {
		return false;
	}

	@Override
	public void initialize(Player player) {
		refresh(player);
		if (getProgress() == 3  && !completed()) {
			finish(player);
		}
	}

	@Override
	public void finish(Player player) {
		player.getQuestListener().addQuestPoints(getQuestPoints());
		sendCompletionInterface(player, 18681, "1 Quest Point", "5000 cooking exp.", "Ability to purchase recipe", "for disaster gloves.", "", "", "");
		refresh(player);
	}

	@Override
	public boolean completed() {
		return getProgress() == 3;
	}

	@Override
	public void sendQuestGuide(Player player) {
		int max = getProgress() + 1 == 0 ? 1 : getProgress() + 1;
		String[][] stages = new String[max][];
		for (int i = 0; i < max; i++) {
			stages[i] = GUIDE[i];
		}	
		sendGuide(player, stages);
	}

}
