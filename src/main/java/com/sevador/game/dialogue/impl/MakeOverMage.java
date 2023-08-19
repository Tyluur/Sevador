package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.content.misc.CharacterDesign;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;
import com.sevador.utility.Misc;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class MakeOverMage extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				SEND_3_TEXT_CHAT,
				new String[] {
						Misc.formatPlayerNameForDisplay(NPCDefinition
								.forId(npcId).name),
								"Hello adventurer! I am the make over mage, would", "you like to change your appearance? I ask only", "of a small fee of 500,000 GP." }, IS_NPC, npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_OPTIONS, new String[]{ "Where would you like to go?",
					"Yes please", "No thank you",  });
			stage = 0;
		} else if (stage == 0) {
			switch(componentId) {
			case 1:
				sendEntityDialogue((short) 241, new String[] { player.getCredentials().getUsername(), "Yes, please" }, (byte) 0, player.getIndex(), 9827);
				stage = 1;
				break;
			case 2:
				sendEntityDialogue((short) 241, new String[] { player.getCredentials().getUsername(), "No thank you." }, (byte) 0, player.getIndex(), 9827);
				stage = -2;
				break;
			}
		} else if (stage == 1) {
			if (player.getInventory().contains(995, 500000)) {
				player.getInventory().remove(new Item(995, 500000));
				CharacterDesign.initiate(player);
				stage = -2;
			} else {
				sendEntityDialogue(
						SEND_2_TEXT_CHAT,
						new String[] {Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
										"Sorry, it seems like you don't have 500,000x coins", "to give me. Come back later!" }, IS_NPC, npcId, 9827);
				stage = -2;
			}
		}
		else {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
