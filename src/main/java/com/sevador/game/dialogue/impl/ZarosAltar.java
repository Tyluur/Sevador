package com.sevador.game.dialogue.impl;

import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.player.PlayerCurseHandler;
import com.sevador.game.node.player.PlayerPrayerHandler;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.ConfigPacket;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class ZarosAltar extends Dialogue {

	@Override
	public void start() {
		if (!player.getPrayer().isCurses())
			sendDialogue((short) 236, new String[] {
					"Change from prayers to curses?",
					"Yes, replace my prayers with curses.", "Never mind." });
		else
			sendDialogue((short) 236, new String[] {
					"Change from curses to prayers?",
					"Yes, replace my curses with prayers.", "Never mind." });
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == 236 && componentId == 1) {
			if (!player.getPrayer().isCurses()) {
				sendDialogue(
						(short) 243,
						new String[] {
								"",
								"The altar fills your head with dark thoughts, purging the",
								"prayers from your memory and leaving only curses in",
								" their place." });
				PlayerCurseHandler p = new PlayerCurseHandler();
				player.setPrayer(p);
				p.sendConfig(player);
				player.getIOSession().write(new ConfigPacket(player, 1584, 1));
				player.getIOSession().write(
						new AccessMask(player, 0, 27, 271, 6, 0, 2));
			} else {
				sendDialogue(
						(short) 242,
						new String[] {
								"",
								"The altar eases its grip on your mid. The curses slip from",
								"your memory and you recall the prayers you used to know." });
				player.getPrayer().reset(player);
				PlayerPrayerHandler p = new PlayerPrayerHandler();
				player.setPrayer(p);
				p.sendConfig(player);
				player.getIOSession().write(new ConfigPacket(player, 1584, 0));
				player.getIOSession().write(
						new AccessMask(player, 0, 27, 271, 6, 0, 2));
			}
		} else {
			end();
		}
	}

	@Override
	public void finish() {

	}

}
