package net.burtleburtle.cache.stream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RSRandomAccessFile extends RandomAccessFile {

    public RSRandomAccessFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public RSRandomAccessFile(File file, String mode) throws FileNotFoundException {
        super(file, mode);
    }

    public int read24BitInt() throws IOException {
        int i = readByte() << 16;
        i += readByte() << 8;
        return i + readByte();
    }

    public void write24BitInt(int i) throws IOException {
        writeByte(i >> 16);
        writeByte(i >> 8);
        writeByte(i);
    }
}