package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinitions;

import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;
import com.sevador.network.out.InterfacePacket;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Roddeck extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				(short) 243,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Greetings, adventurer "
								+ Misc.formatPlayerNameForDisplay(player
										.getCredentials().getDisplayName())
								+ ", would you",
						"like a dice bag, or do",
						"you want something else?" }, (byte) 1, npcId,
				DialAnims.HAPPY_TALKING.getAnim());
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendDialogue((short) SEND_4_OPTIONS, new String[] {
					"What would you like to say?",
					"Yes, I'd truly appreciate that.",
					"No, I'd rather have my experience locked.",
					"I could really use another teleport crystal.",
					"Nothing, never mind." });
			break;
		case 0:
			switch (componentId) {
			case 1:
				if (player.getInventory().contains(995, 1000000)) {
					sendEntityDialogue(
							(short) SEND_2_TEXT_CHAT,
							new String[] {
									NPCDefinitions.getNPCDefinitions(npcId).name,
									"Have fun dicing! Make sure to be an honest dicer", "or face the consequences!" }, (byte) 1, npcId,
							DialAnims.HAPPY_TALKING.getAnim());
					player.getInventory().remove(995, 1000000);
					player.getInventory().addItem(15098, 1);
				} else {
					sendEntityDialogue(
							(short) SEND_2_TEXT_CHAT,
							new String[] {
									NPCDefinitions.getNPCDefinitions(npcId).name,
									"I can only sell them to you for 1M coins", "come back when you have it to give me." }, (byte) 1, npcId,
							DialAnims.HAPPY_TALKING.getAnim());
				}
				end();
				break;
			case 2:
				player.getCredentials().setExperienceLocked(
						player.getCredentials().isExperienceLocked() ? false
								: true);
				player.getDialogueManager()
						.startDialogue(
								"SimpleMessage",
								"Your experience is now <col=FF6000>"
										+ (player.getCredentials()
												.isExperienceLocked() ? "locked"
												: "unlocked") + "</col>.");
				break;
			case 3:
				if (player.getInventory().contains(995, 500000)) {
					sendEntityDialogue((short) 241, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"There you go! Have fun on "
									+ Constants.SERVER_NAME + "." }, (byte) 1,
							npcId, DialAnims.HAPPY_TALKING.getAnim());
					player.getInventory().remove(995, 500000);
					player.getInventory().add(new Item(6099));
				} else {
					sendEntityDialogue(
							(short) 241,
							new String[] {
									NPCDefinitions.getNPCDefinitions(npcId).name,
									"What a shame, you don't have 500,000 coins to give me!" },
							(byte) 1, npcId, DialAnims.SAD.getAnim());
					stage = -2;
				}
				break;
			}
			break;
		case 1:
			switch (componentId) {
			case 1:
				player.getSettings().setSpellBook(192);
				if (player.getIOSession().getScreenSizeMode() < 2) {
					player.getIOSession().write(
							new InterfacePacket(player, 548, 210, 192, true));
				} else {
					player.getIOSession().write(
							new InterfacePacket(player, 746, 96, 192, true));
				}
				break;
			case 2:
				player.getSettings().setSpellBook(193);
				if (player.getIOSession().getScreenSizeMode() < 2) {
					player.getIOSession().write(
							new InterfacePacket(player, 548, 210, 193, true));
				} else {
					player.getIOSession().write(
							new InterfacePacket(player, 746, 96, 193, true));
				}
				break;
			case 3:
				player.getSettings().setSpellBook(430);
				if (player.getIOSession().getScreenSizeMode() < 2) {
					player.getIOSession().write(
							new InterfacePacket(player, 548, 210, 430, true));
				} else {
					player.getIOSession().write(
							new InterfacePacket(player, 746, 96, 430, true));
				}
				break;
			case 4:
				end();
				break;
			}
			break;
			default:
				end();
		}

	}

	@Override
	public void finish() {

	}

}
