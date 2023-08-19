package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 *
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class MusicPacket extends OutgoingPacket {

	private int musicId;
	
	public MusicPacket(Player player, int musicId) {
		super(player, 51);
		this.musicId = musicId;
	}

	@Override
	public OutgoingPacket get() {
		putShortA(musicId);
		putByteS(1);
		putByteC(255);
		return this;
	}

}
