package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.WindowsPane;

/**
 * Handles a tab button event.
 * 
 * @author Emperor
 * 
 */
public class TabButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(548, this);
		return EventManager.register(746, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		if (interfaceId == 548) {
			switch (opcode) {
			case 40:
				((Skills) player.getSkills()).setExperieneGained(0);
				((Skills) player.getSkills()).addExperience(1, 0);
				break;
			}
			switch(buttonId) {
			case 179:
				if (player.getCombatAction().getVictim() != null) {
					player.getPacketSender().sendMessage("You cannot open the world map when you are in combat.");
					return true;
				}
				int posHash = player.getX() << 14 | player.getY();
				player.getIOSession().write(new WindowsPane(player, 755, 0));
				player.getIOSession().write(new CS2Config(player, 622, posHash));
				player.getIOSession().write(new CS2Config(player, 674, posHash));
				break;
			}
		}
		return false;
	}

}
