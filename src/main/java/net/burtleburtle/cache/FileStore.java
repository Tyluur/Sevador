package net.burtleburtle.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.burtleburtle.cache.stream.RSRandomAccessFile;


public class FileStore {

    private static final int IDX_BLOCK_LEN = 6;
    private static final int BLOCK_HEADER_LEN = 8;
    private static final int BLOCK_LEN = 512;
    private static final int TOTAL_BLOCK_LEN = BLOCK_HEADER_LEN + BLOCK_LEN;

    private ByteBuffer tempBuffer = ByteBuffer.allocateDirect(TOTAL_BLOCK_LEN);

    private RSRandomAccessFile data;

    private int id;
    private RSRandomAccessFile index;

    private FileChannel indexChannel, dataChannel;

    private int maxSize;

    public FileStore(int id, String path) {
        this.id = id;
        try {
            this.data = new RSRandomAccessFile(path + "main_file_cache.dat2", "r");
            this.index = new RSRandomAccessFile(path + "main_file_cache.idx" + id, "r");
            this.indexChannel = index.getChannel();
            this.dataChannel = data.getChannel();
            this.maxSize = id == 255 ? 500000 : 1000000;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized byte[] get(int fileId) {
        try {
            if (fileId * IDX_BLOCK_LEN + IDX_BLOCK_LEN > indexChannel.size()) {
                return null;
            }
            tempBuffer.position(0).limit(IDX_BLOCK_LEN);
            indexChannel.read(tempBuffer, fileId * IDX_BLOCK_LEN);
            tempBuffer.flip();
            int size = getMediumInt(tempBuffer);
            int block = getMediumInt(tempBuffer);
            if (size < 0 || size > maxSize) {
                return null;
            }
            if (block <= 0 || block > dataChannel.size() / TOTAL_BLOCK_LEN) {
                return null;
            }
            ByteBuffer fileBuffer = ByteBuffer.allocate(size);
            int remaining = size;
            int chunk = 0;
            while (remaining > 0) {
                if (block < 1) {
                    return null;
                }
                int blockSize = remaining > BLOCK_LEN ? BLOCK_LEN : remaining;
                tempBuffer.position(0).limit(blockSize + BLOCK_HEADER_LEN);
                dataChannel.read(tempBuffer, block * TOTAL_BLOCK_LEN);
                tempBuffer.flip();

                int currentFile = tempBuffer.getShort() & 0xffff;
                int currentChunk = tempBuffer.getShort() & 0xffff;
                int nextBlock = getMediumInt(tempBuffer);
                int currentIndex = tempBuffer.get() & 0xff;

                if (fileId != currentFile || chunk != currentChunk || id != currentIndex) {
                    return null;
                }
                if (nextBlock < 0 || nextBlock > dataChannel.size() / TOTAL_BLOCK_LEN) {
                    return null;
                }

                fileBuffer.put(tempBuffer);
                remaining -= blockSize;
                block = nextBlock;
                chunk++;
            }

            fileBuffer.flip();
            return fileBuffer.array();
        } catch (Exception e) {
            return null;
        }
    }

    private static int getMediumInt(ByteBuffer buffer) {
        return ((buffer.get() & 0xff) << 16) | ((buffer.get() & 0xff) << 8) | (buffer.get() & 0xff);
    }

    public int getId() {
        return id;
    }

    public int length() {
        try {
            return (int) (index.length() / IDX_BLOCK_LEN);
        } catch (IOException e) {
            return 0;
        }
    }
}