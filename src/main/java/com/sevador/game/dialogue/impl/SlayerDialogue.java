package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinitions;

import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.model.skills.slayer.SlayerTask;
import com.sevador.game.node.model.skills.slayer.SlayerTask.Master;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SlayerDialogue extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue((short) 241,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"'Ello, and what are you after then?" }, (byte) 1,
				npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendDialogue(
					(short) 237,
					new String[] {
							"Select an Option",
							"I need "
									+ (player.getTask() != null ? "another"
											: "a") + " assignment",
							"Do you have anything for trade?",
							"I am here to discuss any rewards I might be eligible for.",
							"Er...nothing..." });
			break;
		case 0:
			switch (componentId) {
			case 1:
				Master master = Master.forId(npcId);
				if (player.getTask() == null) {
					SlayerTask.random(player, master);
					sendEntityDialogue((short) 241, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Your slayer task is to kill "
									+ player.getTask().getTaskAmount() + " "
									+ player.getTask().getName().toLowerCase()
									+ "s." }, (byte) 1, npcId, DialAnims.CONFUSED.getAnim());
					stage = -2;
				} else {
					sendEntityDialogue((short) 242, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You're still hunting "
									+ player.getTask().getName()
									+ "; come back when you've finished",
							"your task." }, (byte) 1, npcId,
							DialAnims.CONFUSED.getAnim());
					stage = -2;
				}
				if (!player.getInventory().contains(4155, 1)) {
					player.getInventory().addItem(4155, 1);
				}
				break;
			case 4:
				sendEntityDialogue((short) 241, new String[] {
						player.getCredentials().getUsername(),
						"Er...nothing..." }, (byte) 0, player.getIndex(), 9827);
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
