package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.WindowsPane;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class WorldMapEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(755, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		if (buttonId == 44) {
			player.getIOSession().write(new WindowsPane(player, player.getIOSession().getDisplayMode() < 2 ? 548 : 746, 2));
		}
		return true;
	}

}
