package com.sevador.game.dialogue.impl;

import com.sevador.game.dialogue.Dialogue;

public class SimpleMessage extends Dialogue {

	public SimpleMessage() {
	}

	public void start() {
		if (parameters.length == 1)
			sendDialogue((short) 210, new String[] { (String) parameters[0] });
		else if (parameters.length == 2)
			sendDialogue((short) 211, new String[] { (String) parameters[0],
					(String) parameters[1] });
		else if (parameters.length == 3)
			sendDialogue((short) 212, new String[] { (String) parameters[0],
					(String) parameters[1], (String) parameters[2] });
		else
			sendDialogue((short) 213, new String[] { (String) parameters[0],
					(String) parameters[1], (String) parameters[2],
					(String) parameters[3] });
	}

	public void run(int interfaceId, int componentId) {
		end();
	}

	public void finish() {
	}
}
