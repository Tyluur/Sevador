package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.misc.ShopManager;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.utility.Misc;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class MaxSkillcape extends Dialogue {

	public static int[][] SKILLCAPES = { 
		{ 9747, 9748 }, { 9753, 9754 },
		{ 9750, 9751 }, { 9768, 9769 }, { 9756, 9757 }, { 9759, 9760 },
		{ 9762, 9763 }, { 9801, 9802 }, { 9807, 9808 }, { 9783, 9784 },
		{ 9798, 9799 }, { 9804, 9805 }, { 9780, 9781 }, { 9795, 9796 },
		{ 9792, 9793 }, { 9774, 9775 }, { 9771, 9772 }, { 9777, 9778 },
		{ 9786, 9787 }, { 9810, 9811 }, { 9765, 9766 }, { 9948, 9949 },
		{ 9789, 9790 }, { 12169, 12170 }, { 18508, 18509 } 
	};

	public static final Item[] HOODS = { 
		new Item(9749), new Item(9755), new Item(9752), new Item(9770), 
		new Item(9758), new Item(9761), new Item(9764), new Item(9803), 
		new Item(9809), new Item(9785), new Item(9800), new Item(9806), 
		new Item(9782), new Item(9797), new Item(9794), new Item(9776), 
		new Item(9773), new Item(9779), new Item(9788), new Item(9812),
		new Item(9767), new Item(9950), new Item(9791), new Item(12171),
		new Item(18510),
	};	

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				SEND_1_TEXT_CHAT,
				new String[] {
						Misc.formatPlayerNameForDisplay(NPCDefinition
								.forId(npcId).name),
								"Greetings, which kind of cape would you like?" }, IS_NPC, npcId, DialAnims.HAPPY_TALKING.getAnim());
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_OPTIONS, new String[]{ "Choose an Option?", 
					"Skillcape, please.", 
					"Completionist Cape, please.",  });
			stage = 0;
		} else if (stage == 0) {
			if (componentId == 1) {
				end();
				ShopManager.open(player, 3374, 1);
			} else {
				for (int i = 0; i < 24; i++) {
					if (i == Skills.AGILITY || i == Skills.CONSTRUCTION || i == Skills.DUNGEONEERING || i == Skills.HUNTER || i == Skills.FARMING || i == Skills.SUMMONING) continue;
					if (player.getSkills().getLevel(i) == 99) {
						sendEntityDialogue(
								SEND_2_TEXT_CHAT,
								new String[] {
										Misc.formatPlayerNameForDisplay(NPCDefinition
												.forId(npcId).name),
												"You have the appropriate levels, I require a fee of 1M coins", "for this cape, do you want to buy it still?" }, IS_NPC, npcId, DialAnims.HAPPY_TALKING.getAnim());
						stage = 1;
					} else {
						player.getDialogueManager().startDialogue("SimpleMessage", "You need to have a 99 in all available skills first...");
					}
				}
			}
		} else if (stage == 1) {
			sendDialogue(SEND_2_OPTIONS, new String[]{ "Choose an Option?", 
					"Yes", 
					"No",  });
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1) {
				if (player.getInventory().contains(995, 1000000)) {
					player.getInventory().remove(995, 1000000);
					player.getInventory().addItem(20771, 1);
					sendEntityDialogue(
							SEND_1_TEXT_CHAT,
							new String[] {
									Misc.formatPlayerNameForDisplay(NPCDefinition
											.forId(npcId).name),
											"Fare well! Show off your cape with pride, adventurer!" }, IS_NPC, npcId, DialAnims.HAPPY_TALKING.getAnim());
					stage = -2;
				}
			} else 
				end();
		} else {
			end();	
		}
	}

	@Override
	public void finish() {
	}

	public int skillcapeTotal(Player player) {
		int count = 0;
		for (int i = 0; i < 25; i++) {
			if (player.getSkills().getStaticLevel(i) >= 99) {
				count++;
			}
		}
		return count;
	}

}
