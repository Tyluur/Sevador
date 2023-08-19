package com.sevador.game.node.control.impl;

import com.sevador.game.node.control.Controler;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class StartControler extends Controler {

	@Override
	public void start() {
		player.getDialogueManager().startDialogue("StartDialogue");
	}

}
