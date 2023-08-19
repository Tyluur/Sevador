package com.sevador.game.dialogue.impl;

import com.sevador.content.SkillsDialogue;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.skills.fletch.Fletching;
import com.sevador.game.node.model.skills.fletch.FletchingData.Fletch;

public class FletchingD extends Dialogue {

	public FletchingD() {
	}

	public void start() {
		items = (Fletch) parameters[0];
		boolean maxQuantityTen = Fletching.maxMakeQuantityTen(items);
		SkillsDialogue
				.sendSkillsDialogue(
						player,
						maxQuantityTen ? 9 : 0,
						"Choose how many you wish to make,<br>then click on the item to begin.",
						maxQuantityTen ? 10 : 28, items.getProduct(),
						maxQuantityTen ? null
								: new SkillsDialogue.ItemNameFilter() {

									public String rename(String name) {
										return name.replace(" (u)", "");
									}
								});
	}

	public void run(int interfaceId, int componentId) {
		int option = SkillsDialogue.getItemSlot(componentId);
		if (option > items.getProduct().length) {
			end();
			return;
		}
		int quantity = SkillsDialogue.getQuantity(player);
		int invQuantity = player.getInventory().getAmount(
				new Item(items.getId()));
		if (quantity > invQuantity)
			quantity = invQuantity;
		player.getSkillAction().setSkill(new Fletching(items, option, quantity));
		end();
	}

	public void finish() {
	}

	private Fletch items;
}
