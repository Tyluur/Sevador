package net.burtleburtle.cache.stream;

public class ByteInputStream {

	public byte[] buffer;
	public int pos;

	public ByteInputStream(byte[] data) {
		buffer = data;
		pos = 0;
	}

	public int readSmart2() {
		int i = 0;
		int i_33_ = readSmart();
		while (i_33_ == 32767) {
			i_33_ = readSmart();
			i += 32767;
		}
		i += i_33_;
		return i;
	}

	public int readSmart() {
		int i = buffer[pos] & 0xff;
		if (i >= 128)
			return readUShort() - 32768;
		return readUByte();
	}

	public int read24BitInt() {
		return (readUnsignedByte() << 16) + (readUnsignedByte() << 8)
				+ (readUnsignedByte());
	}

	public int readUnsignedByte() {
		return readUByte();
	}

	public int readUnsignedShort() {
		return (readUnsignedByte() << 8) + readUnsignedByte();
	}

	public int readInt() {
		return (readUnsignedByte() << 24) + (readUnsignedByte() << 16)
				+ (readUnsignedByte() << 8) + readUnsignedByte();
	}

	public int readBigSmart() {
		if ((buffer[pos] ^ 0xffffffff) <= -1) {
			int value = readUnsignedShort();
			if (value == 32767) {
				return -1;
			}
			return value;
		}
		return readInt() & 0x7fffffff;
	}

	public String readString() {
		StringBuilder s = new StringBuilder();
		int b;
		while ((b = readByte()) != 0)
			s.append((char) b);
		return s.toString();
	}

	public int readUShort() {
		pos += 2;
		return ((buffer[pos - 2] & 0xff) << 8) | (buffer[pos - 1] & 0xff);
	}

	public int readByte() {
		return buffer[pos++];
	}

	public int readUByte() {
		return buffer[pos++] & 0xff;
	}
}