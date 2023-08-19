package com.sevador.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import com.sevador.game.node.player.Player;
import com.sevador.utility.Misc;

/**
 * Represents an outgoing packet.
 * @author Emperor
 *
 */
public class OutgoingPacket implements Packet {

	/**
	 * The bit masks.
	 */
	private static final int[] BIT_MASK_OUT = new int[] { 0, 0x1, 0x3, 0x7,
		0xf, 0x1f, 0x3f, 0x7f,
		0xff, 0x1ff, 0x3ff, 0x7ff,
		0xfff, 0x1fff, 0x3fff, 0x7fff,
		0xffff, 0x1ffff, 0x3ffff, 0x7ffff,
		0xfffff, 0x1fffff, 0x3fffff, 0x7fffff,
		0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff,
		0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff,
		-1
	};

	/**
	 * The opcode.
	 */
	private int opcode;

	/**
	 * The player.
	 */
	protected final Player player;

	/**
	 * The offset.
	 */
	private final int length = 0;

	/**
	 * The bit position.
	 */
	private int bitPosition = 0;

	/**
	 * The packet type.
	 */
	private PacketType type;

	/**
	 * The channel buffer.
	 */
	private final ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

	/**
	 * Constructs a new {@code OutgoingPacket} {@code Object}, <br>
	 * with {@link PacketType#STANDARD} as packet type and -1 as opcode.
	 * @param player The player.
	 */
	public OutgoingPacket(Player player) {
		this(player, -1, PacketType.STANDARD);
	}

	/**
	 * Constructs a new {@code OutgoingPacket} {@code Object}, with {@link PacketType#STANDARD} as packet type.
	 * @param player The player.
	 * @param opcode The opcode.
	 */
	public OutgoingPacket(Player player, int opcode) {
		this(player, opcode, PacketType.STANDARD);
	}

	/**
	 * Constructs a new {@code OutgoingPacket} {@code Object}.
	 * @param player The player.
	 * @param opcode The opcode.
	 * @param type The packet type.
	 */
	public OutgoingPacket(Player player, int opcode, PacketType type) {
		this.player = player;
		this.opcode = opcode;
		this.type = type;
	}

	/**
	 * Resets the outgoing packet.
	 */
	public void clear() {
		this.bitPosition = 0;
		buffer.clear();
	}	

	/**
	 * Puts a byte on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket put(int val) {
		buffer.writeByte(val);
		return this;
	}

	/**
	 * Puts a byte-array on the buffer.
	 * @param b The byte-array.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putBytes(byte[] b) {
		buffer.writeBytes(b);
		return this;
	}

	/**
	 * Puts a different channel buffer on the current buffer.
	 * @param other The other channel buffer.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public void putBytes(ChannelBuffer other) {
		buffer.writeBytes(other);
	}

	/**
	 * Puts a short on the buffer.
	 * @param s The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putShort(int s) {
		buffer.writeShort((short) s);
		return this;
	}

	public OutgoingPacket writeByte(int i) {
		writeByte((byte) i);
		return this;
	}

	public OutgoingPacket writeByte(byte b) {
		buffer.writeByte(b);
		return this;
	}


	/**
	 * Puts an int on the buffer.
	 * @param i The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putInt(int i) {
		buffer.writeInt((int) i);
		return this;
	}

	/**
	 * Puts a long on the buffer.
	 * @param l The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putLong(long l) {
		buffer.writeLong(l);
		return this;
	}

	/**
	 * Puts an RS2-String on the buffer.
	 * @param string The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putRS2String(String string) {
		buffer.writeBytes(string.getBytes());
		buffer.writeByte(0);
		return this;
	}

	/**
	 * Puts an GJ-String on the buffer.
	 * @param string The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putGJString(String string) {
		buffer.writeByte(0);
		buffer.writeBytes(string.getBytes());
		buffer.writeByte(0);
		return this;
	}

	/**
	 * Puts a GJ-String on the buffer.
	 * @param string The value.
	 * @return The OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putGJString2(String string) {
		byte[] packed = new byte[256];
		int length = Misc.packGJString2(0, packed, string);
		put(0).putBytes(packed, 0, length).put(0);
		return this;
	}

	/**
	 * Puts an A-type short on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putShortA(int val) {
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) (val + 128));
		return this;
	}

	/**
	 * Puts an A-type byte on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putByteA(int val) {
		buffer.writeByte((byte) (val + 128));
		return this;
	}

	/**
	 * Puts a Little-Endian A-type short on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putLEShortA(int val) {
		buffer.writeByte((byte) (val + 128));
		buffer.writeByte((byte) (val >> 8));
		return this;
	}

	/**
	 * Starts the bit access.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket startBitAccess() {
		bitPosition = buffer.writerIndex() * 8;
		return this;
	}

	/**
	 * Ends the bit access.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket finishBitAccess() {
		buffer.writerIndex((bitPosition + 7) / 8);
		return this;
	}

	/**
	 * Puts bits on the buffer.
	 * @param numBits The numBits.
	 * @param value The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putBits(int numBits, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		int pos = (bitPosition + 7) / 8;
		buffer.ensureWritableBytes(pos + 1); //pos + 1
		buffer.writerIndex(pos);
		byte b;
		for (; numBits > bitOffset; bitOffset = 8) {
			b = buffer.getByte(bytePos);
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos++, (byte) (b | (value >> (numBits - bitOffset)) & BIT_MASK_OUT[bitOffset]));
			numBits -= bitOffset;
		}
		b = buffer.getByte(bytePos);
		if (numBits == bitOffset) {
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos, (byte) (b | value & BIT_MASK_OUT[bitOffset]));
		} else {
			buffer.setByte(bytePos, (byte) (b & ~(BIT_MASK_OUT[numBits] << (bitOffset - numBits))));
			buffer.setByte(bytePos, (byte) (b | (value & BIT_MASK_OUT[numBits]) << (bitOffset - numBits)));
		}
		return this;
	}

	/**
	 * Puts a C-type byte on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putByteC(int val) {
		buffer.writeByte((byte) -val);
		return this;
	}

	/**
	 * Puts a Little-Endian short on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putLEShort(int val) {
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 8));
		return this;
	}

	/**
	 * Puts a v1-int on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putInt1(int val) {
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 24));
		buffer.writeByte((byte) (val >> 16));
		return this;
	}

	/**
	 * Puts a v2-int on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putInt2(int val) {
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 24));
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 8));
		return this;
	}

	/**
	 * Puts a Little-Endian int on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putLEInt(int val) {
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 24));
		return this;
	}

	/**
	 * Puts a byte-array on the buffer.
	 * @param data The byte-array.
	 * @param offset The writer index to start from.
	 * @param length The length.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putBytes(byte[] data, int offset, int length) {
		buffer.writeBytes(data, offset, length);
		return this;
	}

	/**
	 * Puts an A-type byte on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putByteA(byte val) {
		buffer.writeByte((byte) (val + 128));
		return this;
	}

	/**
	 * Puts an S-type byte on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putByteS(int val) {
		buffer.writeByte((byte) (128 - val));
		return this;
	}

	/**
	 * Puts A-type bytes on the array.
	 * @param data The byte-array.
	 * @param offset The offset.
	 * @param len The length.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putBytesA(byte[] data, int offset, int len) {
		for (int k = offset; k < len; k++) {
			buffer.writeByte((byte) (data[k] + 128));
		}
		return this;
	}

	/**
	 * Puts a byte-array on the buffer using the 'LIFO' manner.
	 * @param is The byte-array.
	 * @param offset The index to start from.
	 * @param length The length.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putReverse(byte[] is, int offset, int length) {
		for(int i = (offset + length - 1); i >= offset; i--) {
			buffer.writeByte((byte) is[i]);
		}
		return this;
	}

	/**
	 * Puts a A-type byte array on the buffer using the 'LIFO' manner.
	 * @param is The byte-array.
	 * @param offset The index to start from.
	 * @param length The length.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putReverseA(byte[] is, int offset, int length) {
		for(int i = (offset + length - 1); i >= offset; i--) {
			putByteA(is[i]);
		}
		return this;
	}

	/**
	 * Puts a tri-byte on the buffer.
	 * @param val The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket putMedium(int val) {
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) val);
		return this;
	}

	/**
	 * Sets the opcode.
	 * @param id The opcode.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket setOpcode(int id) {
		this.opcode = id;
		return this;
	}

	/**
	 * Sets the type.
	 * @param type The packet type.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public OutgoingPacket setType(PacketType type) {
		this.type = type;
		return this;
	}

	/**
	 * Puts a smart.
	 * @param val The value.
	 * @return This instance for chaining.
	 */
	public OutgoingPacket putSmart(int val) {
		if(val >= 128) {
			putShort(val + 32768);
		} else {
			put(val);
		}
		return this;
	}

	/**
	 * Puts a smart.
	 * @param val The value.
	 * @return This instance for chaining.
	 */
	public OutgoingPacket putIntSmart(int val) {
		if(val >= 32768) {
			putInt(val + 32768);
		} else {
			putShort(val);
		}
		return this;
	}

	/**
	 * This method should be inherited by sub-classes of this class, and will be used to prepare the packet to send.
	 * @return The outgoing packet instance.
	 */
	public OutgoingPacket get() {
		return this;
	}

	/**
	 * Gets the channel buffer.
	 * @return The buffer.
	 */
	@Override
	public ChannelBuffer getBuffer() {
		return buffer;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public int getOpcode() {
		return opcode;
	}

	@Override
	public int getSize() {
		return length;
	}

	@Override
	public boolean isRaw() {
		return opcode == -1;
	}

	@Override
	public PacketType getType() {
		return type;
	}

	/**
	 * Serializes the channel buffer.
	 * @return The serialized channel buffer.
	 */
	public static ChannelBuffer serialize(OutgoingPacket message) {
		if (!message.isRaw()) {
			int packetLength = message.getBuffer().readableBytes() + 3;
			ChannelBuffer response = ChannelBuffers.buffer(packetLength);
			if (message.getOpcode() > 127) {
				response.writeByte((byte) 128);
			}
			response.writeByte((byte) message.getOpcode());
			if (message.getType() == PacketType.VAR_BYTE) {
				response.writeByte((byte) message.getBuffer().readableBytes());
			} else if (message.getType() == PacketType.VAR_SHORT) {
				if (packetLength > 65535) // Stack overflow.
					throw new IllegalStateException("Could not send a packet with " + packetLength + " bytes within 16 bits.");
				response.writeByte((byte)(message.getBuffer().readableBytes() >> 8));
				response.writeByte((byte) message.getBuffer().readableBytes());
			}
			response.writeBytes(message.getBuffer());
			return response;
		}
		return message.getBuffer();
	}

	public void skip(int skip) {
		for (int i = 0; i < skip; i++) {
			buffer.writeByte((byte) 0);
		}
	}

}