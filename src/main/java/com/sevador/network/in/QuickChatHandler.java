package com.sevador.network.in;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.content.misc.QuickChatUtils;
import com.sevador.game.node.ChatMessage;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
@SuppressWarnings("unused")
public class QuickChatHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		/*packet.readByte();
		int fileId = packet.readShort();
		byte data[] = null;
		if (packet.getSize() > 3) {
			data = new byte[packet.getSize() - 3];
			packet.readBytes(data, data.length);
		}
		data = QuickChatUtils.getData(player, fileId, data);
		for (Player pl : NodeWorker.getPlayers()) {
			if (pl.getRegion().getRegionID() == player.getRegion().getRegionID())
				pl.getPacketSender().sendPublicChatMessage(pl, player.getIndex(), player.getCredentials().getRights(), new ChatMessage(fileId, data));
		}*/
		player.getPacketSender().sendMessage("Quick chat has been temporarily removed.");
		return true;
	}

}
