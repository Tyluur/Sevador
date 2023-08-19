package com.sevador.game.dialogue.impl;

import com.sevador.game.dialogue.Dialogue;

public class BarrowsD extends Dialogue {

	public BarrowsD() {
	}

	public void start() {
		sendDialogue((short) 241, new String[] { "",
				"You've found a hidden tunnel, do you want to enter?" });
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue((short) 229, new String[] { "Select an Option",
					"Yes, I'm fearless.", "No way, that looks scary!" });
		} else if (stage == 0) {
			if (componentId == 2) {
				sendDialogue((short) 241, new String[] { "",
						"Talk to the Strange Old Man up top for rewards." });
				stage = 1;
			}
			if (componentId == 3) {
				end();
			}
		} else {
			end();
		}
	}

	public void finish() {
	}
}
