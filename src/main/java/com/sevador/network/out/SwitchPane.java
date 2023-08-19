package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Creates a new {@code switchPane} {@code packet}.
 * @author Emperor
 *
 */
public class SwitchPane extends OutgoingPacket {

	//TODO CONVERT TO 666!!!
	/**
	 * The pane id to switch from.
	 */
	private final int paneFrom;
	
	/**
	 * The window position to switch from.
	 */
	private final int windowPosFrom;
	
	/**
	 * The pane id to switch to.
	 */
	private final int paneTo;
	
	/**
	 * The window position to switch to.
	 */
	private final int windowPosTo;
	
	/**
	 * Constructs a new {@code SwitchPane} {@code Object}.
	 * @param player The player.
	 * @param paneFrom The pane id to switch from.
	 * @param windowPosFrom The window position to switch from.
	 * @param paneTo The pane id to switch to.
	 * @param windowPosTo The window position to switch to.
	 */
	public SwitchPane(Player player, int paneFrom, int windowPosFrom, int paneTo, int windowPosTo) {
		super(player, 72);
		this.paneFrom = paneFrom;
		this.windowPosFrom = windowPosFrom;
		this.paneTo = paneTo;
		this.windowPosTo = windowPosTo;
	}
	
	@Override
	public OutgoingPacket get() {
		//putPacket(72);
		putInt(paneTo << 16 | windowPosTo);
		putInt(paneFrom << 16 | windowPosFrom);
		putLEShort(getPlayer().getPacketSender().getFrameIndex());
		return this;
	}

}
