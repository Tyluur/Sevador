package net.burtleburtle.cache.bzip;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GZIPDecompressor {

    public static void decompress(int slen, int off, byte[] in, byte[] out) throws IOException {
        byte in2[] = new byte[slen];
        System.arraycopy(in, off, in2, 0, slen);
        DataInputStream ins = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(in2)));
        try {
            ins.readFully(out);
        } finally {
            ins.close();
        }
    }
}