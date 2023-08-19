package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Creates a new send run energy packet.
 * @author Emperor
 *
 */
public class RunEnergy extends OutgoingPacket {

	/**
	 * The amount of run energy to send.
	 */
	private final int amount;
	
	/**
	 * Constructs a new {@code RunEnergy} {@code Object}.
	 * @param player The player.
	 * @param amount The amount.
	 */
	public RunEnergy(Player player, double amount) {
		super(player, 18);
		this.amount = (int) amount;
	}
	
	@Override
	public OutgoingPacket get() {
		put(amount);
		return this;
	}

}
