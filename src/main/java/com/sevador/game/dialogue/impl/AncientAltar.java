package com.sevador.game.dialogue.impl;

import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.network.out.InterfacePacket;

public class AncientAltar extends Dialogue {

	public AncientAltar() {
	}

	public void start() {
		sendDialogue((short) 236, new String[] { "Change spellbooks?",
				"Yes, replace my spellbook.", "Never mind." });
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == 236 && componentId == 1) {
			if (player.getSettings().getSpellBook() != 193) {
				sendDialogue((short) 242, new String[] { "",
						"Your mind clears and you switch",
						"back to the ancient spellbook." });
				player.getSettings().setSpellBook(193);
				if (player.getIOSession().getScreenSizeMode() < 2) {
					player.getIOSession().write(
							new InterfacePacket(player, 548, 210, 193, true));
				} else {
					player.getIOSession().write(
							new InterfacePacket(player, 746, 96, 193, true));
					player.setAnimation(new Animation(645));
				}
			} else {
				player.setAnimation(new Animation(645));
				sendDialogue((short) 242, new String[] { "",
						"Your mind clears and you switch",
						"back to the normal spellbook." });
				player.getSettings().setSpellBook(192);
				if (player.getIOSession().getScreenSizeMode() < 2) {
					player.getIOSession().write(
							new InterfacePacket(player, 548, 210, 192, true));
				} else {
					player.getIOSession().write(
							new InterfacePacket(player, 746, 96, 192, true));
				}
			}
		} else {
			end();
		}
	}

	public void finish() {
	}
}
