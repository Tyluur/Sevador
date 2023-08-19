package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Misc;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class DuelChallenge implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(640, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		switch (buttonId) {
		case 20:
			Player playerToChallenge = player.getAttribute("Challenger");
			boolean friendly = player.getAttribute("friendlyDuel");
			playerToChallenge
					.getIOSession()
					.write(new MessagePacket(
							playerToChallenge,
							101,
							"wishes to duel with you ("
									+ (friendly ? "friendly" : "staked") + ").",
							Misc.formatPlayerNameForDisplay(player
									.getCredentials().getDisplayName())));
			player.getPacketSender().sendCloseInterface();
			break;
		case 19:
			player.getIOSession().write(new ConfigPacket(player, 283, 134217728));
			player.setAttribute("friendlyDuel", false);
			break;
		case 18:
			player.getIOSession().write(new ConfigPacket(player, 283, 67108864));
			player.setAttribute("friendlyDuel", true);
			break;
		}
		return true;
	}
}
