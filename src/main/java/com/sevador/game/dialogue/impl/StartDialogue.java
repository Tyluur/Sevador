package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.content.misc.CharacterDesign;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class StartDialogue extends Dialogue {

	int npcId = 8863;

	@Override
	public void start() {
		CharacterDesign.randomizeLook(player, player.getCredentials().getAppearance());
		player.setAttribute("inTutorial", true);
		sendEntityDialogue(
				SEND_3_TEXT_CHAT,
				new String[] { Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
						"Welcome to the most advanced 666 server, " + Misc.formatPlayerNameForDisplay(player.getCredentials().getUsername())+"!",
						"Please choose an experience mode type","to enhance your gameplay!" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_4_OPTIONS, new String[]{ "Choose an experience mode type.",
					"Play in <col=810541>Legend</col> Mode(<col=3399FF>50x</col> RuneScape XP)", 
					"Play in <col=810541>Medium</col> Mode (<col=3399FF>500X</col> RuneScape XP)", 
					"Play in <col=810541>Easy</col> Mode (<col=3399FF>::pure & 1000x</col> RuneScape XP)", 
					"Play in <col=810541>Extreme</col> Mode(<col=3399FF>10x</col> RuneScape XP)" });
			stage = 0;	
		}
		if (stage == 0) {
			switch(componentId) {
			case 1:
				player.getSettings().setExpMode(50);
				stage = 1;
				player.getDialogueManager().continueDialogue(interfaceId, componentId);
				break;
			case 2:
				player.getSettings().setExpMode(500);
				stage = 1;
				player.getDialogueManager().continueDialogue(interfaceId, componentId);
				break;
			case 3:
				player.getSettings().setExpMode(1000);
				stage = 1;
				player.getDialogueManager().continueDialogue(interfaceId, componentId);
				break;
			case 4:
				player.getSettings().setExpMode(10);
				stage = 1;
				player.getDialogueManager().continueDialogue(interfaceId, componentId);
				break;
			}
		} else if (stage == 1) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,new String[] {Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
							"I see you are a newcomer on " + Constants.SERVER_NAME + ".",
							"I am Roddeck, I will be teaching you the basics!" }, IS_NPC, npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			player.getProperties().setTeleportLocation(new Location(3151, 3478, 0));
			sendEntityDialogue(SEND_2_TEXT_CHAT,new String[] {Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
					"This is the Grand Exchange. You can buy and sell basic",
					"items here. However, you can't barter rares." }, IS_NPC, npcId, 9827);
			stage = 3;
		} else if (stage == 3) {
			player.getProperties().setTeleportLocation(new Location(2680, 3717, 0));
			sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] {Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
					"These are the rock crabs, you can get here by using your ",
					"teleportation crystal. They are in other teleports - > ", "kill monsters -> rock crabs." }, IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			player.getProperties().setTeleportLocation(new Location(3164, 3495, 0));
			sendEntityDialogue(SEND_2_TEXT_CHAT,new String[] {Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
					"These are the thieving stalls. Steal from them and sell",
					"your reward to the grand exchange. You get coins, too!" }, IS_NPC, npcId, 9827);
			stage = 5;
		} else if (stage == 5) {
			player.getProperties().setTeleportLocation(Entity.DEFAULT_LOCATION);
			sendEntityDialogue(SEND_2_TEXT_CHAT,new String[] {Misc.formatPlayerNameForDisplay(NPCDefinition.forId(npcId).name),
					"That's the most important information you'll need to know!",
					"Have fun on " + Constants.SERVER_NAME+"! Make sure to abide by the rules." }, IS_NPC, npcId, 9827);
			stage = -2;
			end();
			player.removeAttribute("inTutorial");
			CharacterDesign.initiate(player);
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
