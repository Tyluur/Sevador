package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Creates the windows pane packet.
 * @author Emperor
 *
 */
public class WindowsPane extends OutgoingPacket {

	/**
	 * The pane id.
	 */
	private final int paneId;
	
	/**
	 * The sub-window id.
	 */
	private final int subWindowId;
	
	/**
	 * Constructs a new {@code WindowsPane} {@code Object}.
	 * @param player The player.
	 * @param paneId The pane id.
	 * @param subWindowsId The sub-window's id.
	 */
	public WindowsPane(Player player, int paneId, int subWindowsId) {
		super(player, 100);
		this.paneId = paneId;
		this.subWindowId = subWindowsId;
	}
	
	@Override
	public OutgoingPacket get() {
		putByteA(subWindowId);
		putLEShortA(paneId);
		return this;
	}

}