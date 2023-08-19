package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinitions;

import com.sevador.game.dialogue.Dialogue;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SlayerGem extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue((short) 241,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello there, "
								+ player.getCredentials().getDisplayName()
								+ ", what can I help you with?" }, (byte) 1,
				npcId, 9827);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendDialogue((short) 237, new String[] { "Select an Option",
					"How am I doing so far?", "Who are you?", "Where are you?",
					"Er...nothing..." });
			break;
		case 0:
			switch (componentId) {
			case 1:
				if (player.getTask() != null)
					sendEntityDialogue((short) 241, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Your current assignment is "
									+ player.getTask().getName().toLowerCase()
									+ "; only "
									+ player.getTask().getTaskAmount()
									+ " more to go." }, (byte) 1, npcId, 9827);
				else
					sendEntityDialogue((short) 242, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You have no slayer task; use the trapdoor in Edgeville", "and talk to me for a new slayer task." }, (byte) 1, npcId, 9827);
				stage = -2;
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
