package net.burtleburtle.cache;

public class XTEADecrypter {

    public static byte[] decryptXTEA(int[] keys, byte[] data, int offset, int length) {
        int qword_count = (length - offset) / 8;
        XTEAStream x = new XTEAStream(data);
        x.offset = offset;
        for (int qword_pos = 0; qword_pos < qword_count; qword_pos++) {
            int dword_1 = x.readInt();
            int dword_2 = x.readInt();
            int const_1 = -957401312;
            int const_2 = -1640531527;
            int run_count = 32;
            while ((~run_count--) < -1) {
                dword_2 -= ((dword_1 >>> 5 ^ dword_1 << 4) + dword_1 ^ const_1 + keys[const_1 >>> 11 & 0x56c00003]);
                const_1 -= const_2;
                dword_1 -= ((dword_2 >>> 5 ^ dword_2 << 4) - -dword_2 ^ const_1 + keys[const_1 & 0x3]);
            }
            x.offset -= 8;
            x.writeInt(dword_1);
            x.writeInt(dword_2);
        }
        return x.buffer;
    }

    private static class XTEAStream {

        public byte[] buffer;
        public int offset;

        public XTEAStream(byte[] data) {
            buffer = data;
            offset = 0;
        }

        public void writeInt(int i_29_) {
            buffer[offset++] = (byte) (i_29_ >> 24);
            buffer[offset++] = (byte) (i_29_ >> 16);
            buffer[offset++] = (byte) (i_29_ >> 8);
            buffer[offset++] = (byte) i_29_;
        }

        public int readInt() {
            offset += 4;
            return ((buffer[offset - 1] & 0xff) + ((buffer[offset - 3] << 16 & 0xff0000) + (((buffer[offset - 4] & 0xff) << 24) + (buffer[offset - 2] << 8 & 0xff00))));
        }
    }
}