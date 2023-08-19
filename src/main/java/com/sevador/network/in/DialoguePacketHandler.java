package com.sevador.network.in;

import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * Handles incoming dialogue packets.
 * 
 * @author Emperor
 * 
 */
public final class DialoguePacketHandler implements PacketSkeleton {

	@SuppressWarnings("unused")
	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int interfaceHash = packet.readInt2();
		int arg = packet.readLEShortA();
		int interfaceId = interfaceHash >> 16;
		int childId = interfaceHash & 0xFF;
		if (interfaceId == 740) {
			player.getDialogueManager().finishDialogue();
			player.getPacketSender().sendChatBoxInterface(137);
			//player.getSkillAction().startLastAction();
		}
		if (interfaceId != 94)
			player.getDialogueManager().continueDialogue(interfaceId, childId);
		else if (childId == 3 && player.getAttribute("destroyItem") != null
				&& player.getAttribute("destroyItemSlot") != null) {
			Item delItem = player.getAttribute("destroyItem");
			int slot = player.getAttribute("destroyItemSlot");
			if (delItem != null && slot != -1) {
				player.getInventory().remove(delItem);
				player.getPacketSender().sendChatBoxInterface(137);
			}
		}
		return true;
	}

}