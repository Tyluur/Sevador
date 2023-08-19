package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SummoningButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(662, this);
		EventManager.register(808, this);
		return EventManager.register(747, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		if (interfaceId == 747) {
			if (buttonId == 7) {
				player.getFamiliar().selectLeftOption();
			} else if (buttonId == 18)
				player.getFamiliar().sendFollowerDetails();
			else if (buttonId == 10)
				player.getFamiliar().call();
			else if (buttonId == 13)
				player.getFamiliar().fullRestore();
		} else if (interfaceId == 808) {
			int summoningLeftClickOption = (buttonId - 7) / 2;
			if (buttonId >= 7 && buttonId <= 19) {
				player.getFamiliar().setLeftclickOption(
						summoningLeftClickOption);
			} else if (buttonId == 21)
				player.getFamiliar().confirmLeftOption();
			else if (buttonId == 25)
				player.getFamiliar().setLeftclickOption(7);
		} else if (interfaceId == 662) {
			if (player.getFamiliar() == null)
				return true;
			if (buttonId == 49)
				player.getFamiliar().call();
			else if (buttonId == 51)
				player.getFamiliar().dismiss();
			else if (buttonId == 67)
				player.getFamiliar().getBurdenBeast().withdraw(1, 1, 1);
			else if (buttonId == 69)
				player.getFamiliar().fullRestore();
			else if (buttonId == 74) {
			}
		}
		return true;
	}

}
