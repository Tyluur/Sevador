package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.skills.prayer.PrayerType;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.PlayerCurseHandler;
import com.sevador.game.node.player.PlayerPrayerHandler;

/**
 * Handles a prayer tab button event.
 * @author Emperor
 *
 */
public class PrayerTabButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(271, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		if (buttonId == 43) {
			if (player.getPrayer() instanceof PlayerPrayerHandler) {
				((PlayerPrayerHandler) player.getPrayer()).openQuickMenu(player);
			} else {
				((PlayerCurseHandler) player.getPrayer()).openQuickMenu(player);
			}
			return true;
		}
		if (slot == -1) {
			return false;
		}
		if (player.getPrayer().isCurses()) {
			slot += 30;
		}
		player.getPrayer().activate(player, PrayerType.values()[slot]);
		return true;
	}

}
