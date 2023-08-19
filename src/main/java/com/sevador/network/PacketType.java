package com.sevador.network;

/**
 * The different kinds of packet types.
 * 
 * @author Emperor
 *
 */
public enum PacketType {
	
	/**
	 * The standard packet type.
	 */
	STANDARD(0), 
	
	/**
	 * The var byte packet type.
	 */
	VAR_BYTE(1), 
	
	/**
	 * The var short packet type.
	 */
	VAR_SHORT(2);

	/**
	 * The packet size.
	 */
	private final int size;

	/**
	 * Constructs a new {@code PacketType} {@code Object}.
	 * @param size The size.
	 */
	private PacketType(int size) {
		this.size = size;
	}

	/**
	 * Gets the size.
	 * @return The size.
	 */
	public int getSize() {
		return size;
	}

}
