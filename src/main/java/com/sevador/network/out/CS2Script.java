package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Represents the CS2Script outgoing packet.
 * @author Emperor
 *
 */
public class CS2Script extends OutgoingPacket {

	/**
	 * The script id.
	 */
	private final int scriptId;
	
	/**
	 * The types.
	 */
	private final String types;
	
	/**
	 * The parameters.
	 */
	private final Object[] parameters;
	
	/**
	 * Constructs a new {@code CS2Script} {@code Object}.
	 * @param player The player.
	 * @param scriptId The script id.
	 * @param types The types.
	 * @param parameters The parameters.
	 */
	public CS2Script(Player player, int scriptId, String types, Object... parameters) {
		super(player, 38, PacketType.VAR_SHORT);
		this.scriptId = scriptId;
		this.types = types;
		this.parameters = parameters;
	}
	
	@Override
	public OutgoingPacket get() {
		if (parameters.length != types.length()) {
			throw new IllegalArgumentException("Parameters size should be the same size as types!");
		}
		putRS2String(types);
		int index = 0;
		for (int i = types.length() - 1; i >= 0; i--) {
			if (types.charAt(i) == 's') {
				putRS2String((String) parameters[index++]);
            } else {
                putInt((Integer) parameters[index++]);
            }
		}
        return putInt(scriptId);
	}

}