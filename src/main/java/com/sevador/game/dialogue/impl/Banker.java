package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinitions;

import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.misc.InputHandler;
import com.sevador.utility.Constants;

public class Banker extends Dialogue {

	public Banker() {
	}

	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue((short) 241,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Good day, How may I help you?" }, (byte) 1, npcId,
				9827);
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue((short) 237,
					new String[] { "What would you like to say?",
							"I'd like to access my bank account, please.",
							"I'd like to check my PIN settings.",
							"I'd like to see my collection box.",
							"What is this place?" });
		} else if (stage == 0) {
			if (componentId == 1) {
				player.getBank().open();
				end();
			} else if (componentId == 2) {
				sendEntityDialogue((short) 241,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								"Would you like a bank pin?" }, (byte) 1,
						npcId, 9827);
				stage = 6;
			} else if (componentId == 3)
				end();
			else if (componentId == 4) {
				stage = 1;
				sendEntityDialogue((short) 241, new String[] {
						player.getCredentials().getUsername(),
						"What is this place?" }, (byte) 0, player.getIndex(),
						9827);
			} else {
				end();
			}
		} else if (stage == 1) {
			stage = 2;
			sendEntityDialogue((short) 242,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"This is one of the many Banks of "
									+ Constants.SERVER_NAME + ". We have",
							"branches in many other towns." }, (byte) 1, npcId,
					9827);
		} else if (stage == 2) {
			stage = 3;
			sendDialogue((short) 236, new String[] {
					"What would you like to say?", "And what do you do?",
					"Didnt you used to be called the Bank of Varrock?" });
		} else if (stage == 3) {
			if (componentId == 1) {
				stage = 4;
				sendEntityDialogue((short) 241, new String[] {
						player.getCredentials().getUsername(),
						"And what do you do?" }, (byte) 0, player.getIndex(),
						9827);
			} else if (componentId == 2) {
				stage = 5;
				sendEntityDialogue((short) 241, new String[] {
						player.getCredentials().getUsername(),
						"Didnt you used to be called the Bank of Varrock?" },
						(byte) 0, player.getIndex(), 9827);
			} else {
				end();
			}
		} else if (stage == 4) {
			stage = -2;
			sendEntityDialogue(
					(short) 243,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"We will look after your items and money for you.",
							"Leave your valuables with us if you want to keep them",
							"safe." }, (byte) 1, npcId, 9827);
		} else if (stage == 5) {
			stage = -2;
			sendEntityDialogue(
					(short) 243,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Yes we did, but people kept on coming into our",
							"signs were wrong. They acted as if we didn't know",
							"what town we were in or something." }, (byte) 1,
					npcId, 9827);
		} else if (stage == 6) {
			stage = 7;
			sendDialogue((short) 236, new String[] {
					"What would you like to say?", "Yes, ofcourse!",
					"No thank you." });
		} else if (stage == 7) {
			if (player.getCredentials().getBankPin() == null) {
				if (componentId == 1) {
					InputHandler.requestStringInput(player, 1,
							"What should your bank pin be?");
					end();
				} else if (componentId == 2) {
					end();
				}
			} else {
				stage = 8;
				sendEntityDialogue(
						(short) 242,
						new String[] {
								NPCDefinitions.getNPCDefinitions(npcId).name,
								"You already have a bank pin set! Do you want to",
								"replace your current bank pin?" }, (byte) 1,
						npcId, DialAnims.SAD.getAnim());
			}
		} else if (stage == 8) {
			stage = 9;
			sendDialogue((short) 236, new String[] {
					"What would you like to say?", "I would appreciate that.",
					"No, thank you." });
		} else if (stage == 9) {
			if (componentId == 1) {
				InputHandler.requestStringInput(player, 1,
						"What should your bank pin be?");
				end();
			} else {
				end();
			}
		} else {
			end();
		}
	}

	public void finish() {
	}

	int npcId;
}
