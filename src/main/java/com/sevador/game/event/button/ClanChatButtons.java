package com.sevador.game.event.button;

import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.StringPacket;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 * 
 */
public class ClanChatButtons implements ButtonEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.event.ButtonEvent#init()
	 */
	@Override
	public boolean init() {
		EventManager.register(1108, this);
		return EventManager.register(1109, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ButtonEvent#handle(org.argonite.game.node.model
	 * .player.Player, int, int, int, int, int)
	 */
	@Override
	public boolean handle(Player player, int opcode, int interfaceId,
			int buttonId, int itemId, int slot) {
		switch(interfaceId) {
		case 1109:
			switch(buttonId) {
			case 33:
				player.getPacketSender().sendInterface(1108);
				player.getIOSession().write(new StringPacket(player, FriendChatManager.getFriendChatManager().getChannelName(player.getCredentials().getUsername()), 1108, 22));
				break;
			case 0:
				FriendChatManager.getFriendChatManager().toggleLootshare(player);
				break;
			case 15:
				FriendChatManager.getFriendChatManager().leaveChannel(player);
				break;
			}
			break;
		case 1108: // Clant chat interface
			if (buttonId == 22) {
				if (opcode == 85) {
					InputHandler.requestStringInput(player, 3, "Enter clan prefix:");
				}
			}
		}
		return true;
	}

}
