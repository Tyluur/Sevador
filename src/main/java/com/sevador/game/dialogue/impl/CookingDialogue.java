package com.sevador.game.dialogue.impl;

import com.sevador.content.SkillsDialogue;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.skills.cooking.Cooking;
import com.sevador.game.node.model.skills.cooking.CookingData.Cookables;

public class CookingDialogue extends Dialogue {

	public CookingDialogue() {
	}

	public void start() {
		cooking = (Cookables) parameters[0];
		object = (GameObject) parameters[1];
		SkillsDialogue.sendSkillsDialogue(player, 2, "Choose how many you wish to cook,<br>then click on the item to begin.", player.getInventory().getAmount(cooking.getRawItem()), new int[][] {{cooking.getProduct().getId() }, {cooking.getRawItem().getId()}}, null);
	}

	public void run(int interfaceId, int componentId) {
		player.getSkillAction().setSkill(
				new Cooking(object, cooking.getRawItem(), SkillsDialogue
						.getQuantity(player)));
		end();
	}

	public void finish() {
	}

	private Cookables cooking;
	private GameObject object;
}
