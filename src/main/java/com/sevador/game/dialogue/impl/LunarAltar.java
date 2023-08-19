package com.sevador.game.dialogue.impl;

import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.network.out.InterfacePacket;

public class LunarAltar extends Dialogue {

	public LunarAltar() {
	}

	public void start() {
		sendDialogue((short) 236, new String[] { "Change spellbooks?",
				"Yes, replace my spellbook.", "Never mind." });
	}

	public void run(int interfaceId, int componentId) {
		if (interfaceId == 236 && componentId == 1) {
			if (player.getSettings().getSpellBook() != 430) {
				player.setAnimation(new Animation(645));
				sendDialogue((short) 242, new String[] { "",
						"Your mind clears and you switch",
						"back to the lunar spellbook." });
				player.getSettings().setSpellBook(430);
				if (player.getIOSession().getScreenSizeMode() < 2) {
					player.getIOSession().write(
							new InterfacePacket(player, 548, 210, 430, true));
				} else {
					player.getIOSession().write(
							new InterfacePacket(player, 746, 96, 430, true));
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
