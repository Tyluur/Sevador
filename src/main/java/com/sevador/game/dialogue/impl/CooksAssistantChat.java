package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.content.quest.impl.CooksAssistant;
import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.misc.ShopManager;
import com.sevador.game.node.Item;
import com.sevador.utility.Misc;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class CooksAssistantChat extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		if (npcId != 13236) {
			if (player.getQuestListener().start(CooksAssistant.ID).getProgress() < 2) {
				sendEntityDialogue(
						SEND_1_TEXT_CHAT,
						new String[] {
								Misc.formatPlayerNameForDisplay(NPCDefinition
										.forId(npcId).name),
						"What am I to do?" }, IS_NPC, npcId, DialAnims.WORRIED.getAnim());
			} else if (player.getQuestListener().start(CooksAssistant.ID).getProgress() == 2) {
				sendEntityDialogue(
						SEND_1_TEXT_CHAT,
						new String[] {
								Misc.formatPlayerNameForDisplay(NPCDefinition
										.forId(npcId).name),
						"How are you getting on with finding the ingredients?" }, IS_NPC, npcId, DialAnims.WORRIED.getAnim());
			} else if (player.getQuestListener().start(CooksAssistant.ID).completed()) {
				sendEntityDialogue(
						SEND_2_TEXT_CHAT,
						new String[] {
								Misc.formatPlayerNameForDisplay(NPCDefinition
										.forId(npcId).name),
										"You again! Would you like to view my collection", "of the best combat gloves? They're inexpensive, too!" }, IS_NPC, npcId, DialAnims.WORRIED.getAnim());
			}
		} else {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							Misc.formatPlayerNameForDisplay(NPCDefinition
									.forId(npcId).name),
									"Hello, is there anything you would like?", "I can offer you a supply of special eggs or flour." }, IS_NPC, npcId, DialAnims.WORRIED.getAnim());
		}
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (npcId != 13236) {
			if (player.getQuestListener().getProgress(CooksAssistant.ID) == -1) {
				if (stage == -1) {
					sendDialogue(SEND_4_OPTIONS, new String[]{ "Select an Option", 
							"What's wrong?",
							"Can you make me a cake?",
							"You dont look very happy.",
							"Nice hat!", });
					stage = 1;
				} else if (stage == 1) {
					sendEntityDialogue((short) 241, new String[] {
							player.getCredentials().getUsername(),
					"What's wrong?" }, (byte) 0, player.getIndex(),
					9827);
					stage = 2;
				} else if (stage == 2) {
					sendEntityDialogue(
							SEND_4_TEXT_CHAT,
							new String[] {
									Misc.formatPlayerNameForDisplay(NPCDefinition
											.forId(npcId).name),
											"Oh dear, oh dear, oh dear. I'm in a terrible, terrible", "mess! It's the Duke's birthday today, and I should be", "making him a lovely, big birthday cake using special", "ingredients..." }, IS_NPC, npcId, DialAnims.WORRIED.getAnim());
					stage = 3;
				} else if (stage == 3) {
					sendEntityDialogue(
							SEND_4_TEXT_CHAT,
							new String[] {
									Misc.formatPlayerNameForDisplay(NPCDefinition
											.forId(npcId).name),
											"...but I've forgotten to get the ingredients. I'll never get", "them in time now. He'll sack me! What ever will I do? I", "have four children and a goat to look after. Would you", "help me? Please?" }, IS_NPC, npcId, DialAnims.WORRIED.getAnim());
					stage = 4;
				} else if (stage == 4) {
					sendDialogue(SEND_2_OPTIONS, new String[]{ "Select an Option", 
							"Sure, I'll help you!", "No, I have more important things to worry about."});
					stage = 5;
				} else if (stage == 5) {
					switch(componentId) {
					case 1:
						sendEntityDialogue((short) 241, new String[] {
								player.getCredentials().getUsername(),
						"I'm always happy to help a cook in distress." }, (byte) 0, player.getIndex(),
						DialAnims.HAPPY_TALKING.getAnim());
						stage = 6;
						break;
					case 2:
						end();
						break;
					}
				} else if (stage == 6) {
					sendEntityDialogue(
							SEND_4_TEXT_CHAT,
							new String[] {
									Misc.formatPlayerNameForDisplay(NPCDefinition
											.forId(npcId).name),
											"Oh thank you, thank you. I must tell you that this is", "no ordinary cake, though - only the best ingredients will", "do! I need a super large egg, top quality milk","and some extra fine flour." }, IS_NPC, npcId, DialAnims.HAPPY_TALKING.getAnim());
					stage = 7;
				} else if (stage == 7) {
					sendEntityDialogue((short) 241, new String[] {
							player.getCredentials().getUsername(),
					"Where would I find those, then?" }, (byte) 0, player.getIndex(),
					DialAnims.CALM_TALK.getAnim());
					stage = 8;
				} else if (stage == 8) {
					sendEntityDialogue(
							SEND_3_TEXT_CHAT,
							new String[] {
									Misc.formatPlayerNameForDisplay(NPCDefinition
											.forId(npcId).name),
											"That's the problem; I don't exactly know. I usually", "send my assistant to get them for me, but he quit.", "I only know they're around Lumbridge..." }, IS_NPC, npcId, DialAnims.HAPPY_TALKING.getAnim());
					stage = 9;
				} else if (stage == 9) {
					if (!player.getInventory().contains(193, 1) && !player.getInventory().contains(3727, 1)) {
						player.getInventory().add(new Item(3727));
						player.getInventory().add(new Item(1931));
					}
					player.getQuestListener().get(CooksAssistant.ID).setProgress(2);
					stage = 10;
				} else if (stage == 10) {
					end();
				}
			} else if (player.getQuestListener().start(CooksAssistant.ID).getProgress() == 2) {
				if (stage == -1) {
					if (player.getInventory().contains(15413, 1) && player.getInventory().contains(15414, 1) && player.getInventory().contains(15412, 1)) {
						player.getInventory().remove(15414, 1);
						player.getInventory().remove(15413, 1);
						player.getInventory().remove(15412, 1);
						sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
								"Thank you very much, " + Misc.formatPlayerNameForDisplay(player.getCredentials().getDisplayName()) + "! I truly appreciate your help!" }, IS_NPC, npcId, DialAnims.HAPPY_TALKING.getAnim());
						player.getQuestListener().get(CooksAssistant.ID).setProgress(3);
						player.getQuestListener().get(CooksAssistant.ID).finish(player);
					} else {
						sendEntityDialogue((short) 241, new String[] {
								player.getCredentials().getUsername(),
						"I don't have them yet." }, (byte) 0, player.getIndex(),
						DialAnims.CALM_TALK.getAnim());
					}
				} else {
					end();
				}
			} else if (player.getQuestListener().start(CooksAssistant.ID).getProgress() == 3) {
				if (stage == -1) {
					sendDialogue(SEND_2_OPTIONS, new String[]{ "Select an Option", 
							"Yeah, I'd appreciate it!", "No thank you."});
					stage = 0;
				} else if (stage == 0) {
					switch(componentId) {
					case 1:
						end();
						ShopManager.open(player, 14337, 1);
						break;
					case 2:
						end();
						break;
					}
				}
			}
		} else {
			if (stage == -1) {
				sendDialogue(SEND_2_OPTIONS, new String[]{ "Select an Option", 
						"Could I have the chef's flour and special egg? (500K)", "No, never mind"});
				stage = 0;
			} else if (stage == 0) {
				switch(componentId) {
				case 1:
					if (!player.getQuestListener().start(CooksAssistant.ID).completed()) {
						if (player.getInventory().contains(995, 500000)) {
							player.getInventory().remove(995, 500000);
							player.getInventory().add(new Item(15414));
							player.getInventory().add(new Item(15412));
							player.getPacketSender().sendMessage("You get the last two ingredients for the Cook.");
						} else {
							player.getDialogueManager().startDialogue("SimpleMessage", "You don't have 500K coins to exchange for the goods.");
						}
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage", "What do I need these for?");
					}
					break;
				case 2:
					stage = -2;
					break;
				}
			}
		}
		if (stage == -2) end();
	}

	@Override
	public void finish() {

	}

}
