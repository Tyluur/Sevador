package com.sevador.utility;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * A utility class for buffer-related methods.
 * 
 * @author Steven Galarza
 *
 */
public class BufferUtils {
	

    private static byte[] HUFFMAN_BIT_SIZES = {22, 22, 22, 22, 22, 22, 21, 22, 22, 20, 22, 22, 22, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 3, 8, 22, 16, 22, 16, 17, 7, 13, 13, 13, 16, 7, 10, 6, 16, 10, 11, 12, 12, 12, 12, 13, 13, 14, 14, 11, 14, 19, 15, 17, 8, 11, 9, 10, 10, 10, 10, 11, 10, 9, 7, 12, 11, 10, 10, 9, 10, 10, 12, 10, 9, 8, 12, 12, 9, 14, 8, 12, 17, 16, 17, 22, 13, 21, 4, 7, 6, 5, 3, 6, 6, 5, 4, 10, 7, 5, 6, 4, 4, 6, 10, 5, 4, 4, 5, 7, 6, 10, 6, 10, 22, 19, 22, 14, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 21, 22, 21, 22, 22, 22, 21, 22, 22};
    private static int[] HUFFMAN_MASKS = {0, 1024, 2048, 3072, 4096, 5120, 6144, 8192, 9216, 12288, 10240, 11264, 16384, 18432, 17408, 20480, 21504, 22528, 23552, 24576, 25600, 26624, 27648, 28672, 29696, 30720, 31744, 32768, 33792, 34816, 35840, 36864, 536870912, 16777216, 37888, 65536, 38912, 131072, 196608, 33554432, 524288, 1048576, 1572864, 262144, 67108864, 4194304, 134217728, 327680, 8388608, 2097152, 12582912, 13631488, 14680064, 15728640, 100663296, 101187584, 101711872, 101974016, 102760448, 102236160, 40960, 393216, 229376, 117440512, 104857600, 109051904, 201326592, 205520896, 209715200, 213909504, 106954752, 218103808, 226492416, 234881024, 222298112, 224395264, 268435456, 272629760, 276824064, 285212672, 289406976, 223346688, 293601280, 301989888, 318767104, 297795584, 298844160, 310378496, 102498304, 335544320, 299892736, 300941312, 301006848, 300974080, 39936, 301465600, 49152, 1073741824, 369098752, 402653184, 1342177280, 1610612736, 469762048, 1476395008, -2147483648, -1879048192, 352321536, 1543503872, -2013265920, -1610612736, -1342177280, -1073741824, -1543503872, 356515840, -1476395008, -805306368, -536870912, -268435456, 1577058304, -134217728, 360710144, -67108864, 364904448, 51200, 57344, 52224, 301203456, 53248, 54272, 55296, 56320, 301072384, 301073408, 301074432, 301075456, 301076480, 301077504, 301078528, 301079552, 301080576, 301081600, 301082624, 301083648, 301084672, 301085696, 301086720, 301087744, 301088768, 301089792, 301090816, 301091840, 301092864, 301093888, 301094912, 301095936, 301096960, 301097984, 301099008, 301100032, 301101056, 301102080, 301103104, 301104128, 301105152, 301106176, 301107200, 301108224, 301109248, 301110272, 301111296, 301112320, 301113344, 301114368, 301115392, 301116416, 301117440, 301118464, 301119488, 301120512, 301121536, 301122560, 301123584, 301124608, 301125632, 301126656, 301127680, 301128704, 301129728, 301130752, 301131776, 301132800, 301133824, 301134848, 301135872, 301136896, 301137920, 301138944, 301139968, 301140992, 301142016, 301143040, 301144064, 301145088, 301146112, 301147136, 301148160, 301149184, 301150208, 301151232, 301152256, 301153280, 301154304, 301155328, 301156352, 301157376, 301158400, 301159424, 301160448, 301161472, 301162496, 301163520, 301164544, 301165568, 301166592, 301167616, 301168640, 301169664, 301170688, 301171712, 301172736, 301173760, 301174784, 301175808, 301176832, 301177856, 301178880, 301179904, 301180928, 301181952, 301182976, 301184000, 301185024, 301186048, 301187072, 301188096, 301189120, 301190144, 301191168, 301193216, 301195264, 301194240, 301197312, 301198336, 301199360, 301201408, 301202432};

	public static void writeRS2String(ChannelBuffer buffer, String string) {
		buffer.writeBytes(string.getBytes());
		buffer.writeByte((byte) 0);
	}

	public static void writeRS2String(ByteBuffer buffer, String string) {
		buffer.put(string.getBytes());
		buffer.put((byte) 0);
	}

	public static String readRS2String(ChannelBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while (buffer.readable() && (b = buffer.readByte()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}

	public static String readRS2String(ByteBuffer buffer) {
		StringBuilder sb = new StringBuilder();
		byte b;
		while (buffer.remaining() > 0 && (b = buffer.get()) != 0) {
			sb.append((char) b);
		}
		return sb.toString();
	}
	/**
	 * Compresses text using the huffman algorithm.
	 *
	 * @param text        The text to pack.
	 * @param dest        The array to write to.
	 * @param startOffset The start offset from where to write to the destination array.
	 * @return The number of bytes written.
	 */
	public static int huffmanCompress(String text, byte[] dest, int startOffset) {
		try {
			int key = 0;
			int position = startOffset << 3;
			for (int i = 0; i < text.length(); i++) {
				int character = text.getBytes()[i] & 0xff;
				int mask = HUFFMAN_MASKS[character];
				int size = HUFFMAN_BIT_SIZES[character];
				int offset = position >> 3;
			int bitOffset = position & 0x7;
			key &= (-bitOffset >> 31);
			position += size;
			int byteSize = (((bitOffset + size) - 1) >> 3) + offset;
			bitOffset += 24;
			dest[offset] = (byte) (key = (key | (mask >>> bitOffset)));
			if (byteSize > offset) {
				offset++;
				bitOffset -= 8;
				dest[offset] = (byte) (key = mask >>> bitOffset);
				if (byteSize > offset) {
					offset++;
					bitOffset -= 8;
					dest[offset] = (byte) (key = mask >>> bitOffset);
					if (byteSize > offset) {
						bitOffset -= 8;
						offset++;
						dest[offset] = (byte) (key = mask >>> bitOffset);
						if (offset < byteSize) {
							bitOffset -= 8;
							offset++;
							dest[offset] = (byte) (key = mask << -bitOffset);
						}
					}
				}
			}
			}
			return (7 + position >> 3) - startOffset;
		} catch (Exception e) {
		}
		return 0;
	}


	public static int readSmart(ByteBuffer buf) {
		int peek = buf.get(buf.position()) & 0xFF;
		if (peek < 128) {
			return buf.get();
		} else {
			return (buf.getShort() & 0xFFFF) - 32768;
		}
	}

	public static int getMediumInt(ByteBuffer buffer) {
		return ((buffer.get() & 0xFF) << 16) | ((buffer.get() & 0xFF) << 8) | (buffer.get() & 0xFF);
	}

	public static int readSmart2(ByteBuffer buffer) {
		int i_26_ = 0;
		int i_27_;
		for (i_27_ = readSmart(buffer); i_27_ == 32767; i_27_ = readSmart(buffer)) {
			i_26_ += 32767;
		}
		i_26_ += i_27_;
		return i_26_;
	}

	public static void writeInt(int val, int index, byte[] buffer) {
		buffer[index++] = (byte) (val >> 24);
		buffer[index++] = (byte) (val >> 16);
		buffer[index++] = (byte) (val >> 8);
		buffer[index++] = (byte) val;
	}

	public static int readInt(int index, byte[] buffer) {
		return ((buffer[index++] & 0xff) << 24) | ((buffer[index++] & 0xff) << 16) | ((buffer[index++] & 0xff) << 8) | (buffer[index++] & 0xff);
	}

	private static final char[] CHARACTERS = {
		'\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', 
		'\u2021', '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', 
		'\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c', '\u201d', 
		'\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', 
		'\u203a', '\u0153', '\0', '\u017e', '\u0178'
	};

	public static char getCPCharacter(ByteBuffer buffer) {
		int read = buffer.get() & 0xff;
		if (read == 0) {
			throw new IllegalArgumentException("Non cp1252 character 0x" + Integer.toString(read, 16) + " provided");
		}
		if (read >= 128 && read < 160) {
			char cpChar = CHARACTERS[read - 128];
			if (cpChar == '\0') {
				cpChar = '?';
			}
			read = cpChar;
		}
		return (char) read;
	}

}
