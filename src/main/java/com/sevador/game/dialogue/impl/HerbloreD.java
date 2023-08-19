package com.sevador.game.dialogue.impl;

import com.sevador.content.SkillsDialogue;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.skills.herblore.Herblore;


/**
 * @author Tyluur
 */

public class HerbloreD extends Dialogue {

	public HerbloreD() {
	}

	public void start() {
		items = ((Integer) parameters[0]).intValue();
		first = (Item) parameters[1];
		second = (Item) parameters[2];
		int amount;
		if (first.getId() == 233)
			amount = player.getInventory().getAmount(second.getId());
		else if (second.getId() == 233) {
			amount = player.getInventory().getAmount(first.getId());
		} else {
			amount = player.getInventory().getAmount(first.getId());
			if (amount > player.getInventory().getAmount(second.getId()))
				amount = player.getInventory().getAmount(second.getId());
		}
		SkillsDialogue.sendSkillsDialogue(player, 0, "Choose how many you wish to make,<br>then click on the item to begin.", amount, new int[] { items }, null);
	}

	public void run(int interfaceId, int componentId) {
		player.getSkillAction().setSkill(new Herblore(first, second, SkillsDialogue.getQuantity(player)));
		end();
	}

	public void finish() {
	}

	private int items;
	private Item first;
	private Item second;
}
