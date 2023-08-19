package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinitions;

import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.misc.ShopManager;
import com.sevador.utility.Constants;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class FinancialAdvsior extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				(short) 243,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello! I am the man who handles donations on " + Constants.SERVER_NAME + ". The", "currency used is donator tickets (ecto tokens)", "would you like to view the shop?"}, (byte) 1, npcId,
						DialAnims.HAPPY_TALKING.getAnim());
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue((short) SEND_2_OPTIONS, new String[] {
					"Choose an option",
					"Yes, show me the shop, please.", "No thank you." });
			stage = 0;
		} else if (stage == 0) {
			switch(componentId) {
			case 1:
				player.getPacketSender().sendChatBoxInterface(137);
				ShopManager.open(player, 948, 1);
				break;
			case 2:
				end();
				break;
			}
		}
	}

	@Override
	public void finish() {
	}

}
