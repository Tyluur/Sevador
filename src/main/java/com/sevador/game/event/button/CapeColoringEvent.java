package com.sevador.game.event.button;

import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.player.Player;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class CapeColoringEvent implements ButtonEvent {

	@Override
	public boolean init() {
		EventManager.register(19, this);
		return EventManager.register(20, this);
	}
	
	public static final int[] COLOR_INDEXES = {71, 34, 95, 83};

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		switch (interfaceId) {
		case 20:
			switch (buttonId) {
			case 114:
			case 142:
				player.getPacketSender().sendCloseInterface();
				if (player.getAttribute("customizingCapeId") != null) {
					player.removeAttribute("customizingCapeId");
					return true;
				}
				player.getUpdateMasks().register(new AppearanceUpdate(player));
				break;
			case 71:
			case 34:
			case 95:
			case 83:
				int colorIndex = 0;
				for (int i = 0; i < COLOR_INDEXES.length; i++)
					if (COLOR_INDEXES[i] == buttonId)
						colorIndex = i;
				player.setAttribute("color_editing_index", colorIndex);
				player.getPacketSender().sendInterface(19);
				player.getPacketSender().sendConfig(2174, player.getCapeRecolouring().getColours()[colorIndex]);
				break;
			case 58:
				player.getCapeRecolouring().reset();
				if (player.getAttribute("customizingCapeId") == null)
					player.getCapeRecolouring().refresh();
				break;
			}
			break;
		case 19:
			switch (buttonId) {
			case 21:
				player.getCapeRecolouring().displayInterface();
				break;
			}
			break;
		}
		return true;
	}

}
