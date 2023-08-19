package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.model.Location;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class MrEx extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				SEND_2_TEXT_CHAT,
				new String[] {
						Misc.formatPlayerNameForDisplay(NPCDefinition
								.forId(npcId).name),
						"Greetings, "
								+ player.getCredentials().getDisplayName()
								+ ", would you like to ",
						"go to different locations on " + Constants.SERVER_NAME
								+ "?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getCredentials().getDisplayName(), "Well.... where can you take me?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 0;
		} else if (stage == 0) {
			sendDialogue(SEND_5_OPTIONS, new String[]{ "Where would you like to go?", "Barrows", "King Black Dragon", "Forinthry Dungeon", "Corporeal Beast", "" });
			stage = 1;
		} else if (stage == 1) {
			switch(componentId) {
			case 1:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(3564, 3288,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 2:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(2273, 4680,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 3:
				player.getActionManager().register(
						new TeleportAction(player, Location.locate(3037, 10171,
								0), TeleportAction.MODERN_ANIM,
								TeleportAction.MODERN_GRAPHIC,
								TeleportAction.MODERN_END_ANIM,
								TeleportAction.MODERN_END_GRAPHIC, 0, 3, 4));
				break;
			case 4:
				player.teleport(new Location(2960, 4383, 2));
				break;
				default:
					player.getDialogueManager().startDialogue("SimpleMessage", "There is no teleport available yet here.");
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
