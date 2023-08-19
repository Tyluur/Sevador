package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;

/**
 * Handles the dialogue for the npc id given.
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */

public class Mandrith extends Dialogue {

	public Mandrith() {
	}

	private static int moneyCount = 0;
	public static final int[][] artefacts = { { 14876, 5000000 },
			{ 14877, 1000000 }, { 14878, 750000 }, { 14879, 500000 },
			{ 14880, 400000 }, { 14881, 300000 }, { 14882, 250000 },
			{ 14883, 200000 }, { 14884, 150000 }, { 14885, 100000 },
			{ 14886, 75000 }, { 14888, 40000 }, { 14889, 30000 },
			{ 14890, 20000 }, { 14891, 10000 }, { 14892, 5000 } };

	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				(short) 242,
				new String[] {
						NPCDefinition.forId(npcId).name,
						"Greetings, brave warrior. I see you have found some of",
						"the ancient artefacts my brother and I are seeking." },
				(byte) 1, npcId, DialAnims.LAUGH_EXCITED.getAnim());
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue((short) 241,
					new String[] { NPCDefinition.forId(npcId).name,
							"You wouldn't be willing to sell them to me?" },
					(byte) 1, npcId, DialAnims.MEAN_FACE.getAnim());
			stage = 1;
		} else if (stage == 1) {
			sendDialogue((short) 236, new String[] { "Choose an option.",
					"Sure, I can do that.", "No, sorry." });
			stage = 2;
		} else if (stage == 2) {
			switch (componentId) {
			case 1:
				stage = 3;
				player.getDialogueManager().continueDialogue(236, 1);
				break;
			case 2:
				end();
				break;
			}
		} else if (stage == 3) {
			for (int i = 0; i < Mandrith.artefacts.length; i++) {
				for (Item items : player.getInventory().toArray()) {
					if (items == null)
						continue;
					if (items.getId() == Mandrith.artefacts[i][0]) {
						moneyCount += Mandrith.artefacts[i][1];
						player.getInventory().remove(items);
						sendEntityDialogue((short) 241, new String[] {
								NPCDefinition.forId(npcId).name,
								"Thank you, in exchange, I give you "
										+ moneyCount + " coins." }, (byte) 1,
								npcId, DialAnims.CALM_TALK.getAnim());
						player.getInventory().add(new Item(995, moneyCount));
						moneyCount = 0;
						stage = -2;
						return;
					}
				}
			}
			sendEntityDialogue(
					(short) 241,
					new String[] { NPCDefinition.forId(npcId).name,
							"Fool! You are wasting my time; you have no artefacts!" },
					(byte) 1, npcId, DialAnims.DEPRESSED.getAnim());
			stage = -2;
			return;
		} else {
			end();
		}
	}

	public void finish() {
	}

	private int npcId;
}
