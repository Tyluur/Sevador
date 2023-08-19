package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;


/**
 * The lobby login packet.
 * @author Emperor
 *
 */
public class LobbyResponse extends OutgoingPacket {
	
	/**
	 * Constructs a new {@code LobbyResponse} {@code Object}.
	 * @param player The player.
	 */
	public LobbyResponse(Player player) {
		super(player);
	}
	
	@Override
	public OutgoingPacket get() {
		put(0);
		put(0);
		put(0);
		put(0);
		put(0);
		putLong(0); //This is not a long, this is some new byte
		put(0); // byte + int = long type
		putInt(0);
		put(getPlayer().getCredentials().isDonator() ? 0x2 : 0x2); //Members flag = 0; if member flag |= 0x1 else if subscribed flag |= 0x2
		putInt(0);
		put(0);
		putInt(0);
		putShort(0); //Recovery questions (date set).
		putShort(0); //Unread messages.
		putShort(1); //Unread messages.
		String addr = Misc.formatIp(getPlayer().getIOSession().getChannel().getRemoteAddress().toString());
		int hashCode = Misc.IPAddressToNumber(addr);
		putInt(hashCode); //last login ip
		put(3); //Email registered.
		putShort(0);
		putShort(0);
		put(0);
		putGJString2(getPlayer().getCredentials().getUsername());
		put(0);
		putInt(1);
		put(1); //1
		putShort(1); // current world id
		putGJString2(Constants.isWindows() ? "127.0.0.1" : "lethium.no-ip.org");
		OutgoingPacket lobbyResponse = new OutgoingPacket(null);
		lobbyResponse.put((byte) getBuffer().writerIndex());
		lobbyResponse.putBytes(getBuffer());
		return lobbyResponse;
	}

}