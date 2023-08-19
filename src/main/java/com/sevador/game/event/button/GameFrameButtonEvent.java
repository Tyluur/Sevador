package com.sevador.game.event.button;

import com.sevador.game.action.impl.packetactions.RestAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.PlayerCurseHandler;
import com.sevador.game.node.player.PlayerPrayerHandler;

/**
 * Represents the button event for a game frame button.
 * @author Emperor
 *
 */
public class GameFrameButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(749, this);
		return EventManager.register(750, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		switch (interfaceId) {
		case 749:
			switch (opcode) {
			case 7:
				if (player.getPrayer() instanceof PlayerPrayerHandler) {
					((PlayerPrayerHandler) player.getPrayer()).openQuickMenu(player);
				} else {
					((PlayerCurseHandler) player.getPrayer()).openQuickMenu(player);
				}
				return true;
			case 85:
				if (player.getPrayer() instanceof PlayerPrayerHandler) {
					((PlayerPrayerHandler) player.getPrayer()).switchQuick(player);
				} else {
					((PlayerCurseHandler) player.getPrayer()).switchQuick(player);
				}
				return true;
			default:
				return false;
			}
		case 750:
			switch (opcode) {
			case 85:
				switch (buttonId) {
				case 1:
		            player.getSettings().setRunToggled(!player.getSettings().isRunToggled());
					return true;
				default:
					return false;
				}
			case 7:
				switch (buttonId) {
				case 1:
		            player.getActionManager().register(new RestAction(player));
					return true;
				default:
					return false;
				}
			default:
				return false;
			}
		}
		return false;
	}

}
