package net.burtleburtle.cache;

import java.nio.ByteBuffer;

public class FileInformationTable {

    private int[] entry_revision;
    private int[] entry_crc32;
    private int revision;
    private int entryCount;
    private int[] entry_real_sub_count;
    private int[] subEntryCount;
    private int[] entry_name_hash;
    private byte[][] sub_entry_name_hash;
    private int[][] entry_sub_ptr;

    public static FileInformationTable createFileInformationTable(int data, byte[] fileData) throws Exception {
        CacheContainer c = new CacheContainer(fileData);
        ByteBuffer buffer = ByteBuffer.wrap(c.decompress());
        FileInformationTable table = new FileInformationTable();
        int version = buffer.get();
        table.revision = version < 6 ? 0 : buffer.getInt();
        int flags = buffer.get();
        boolean named = (0x1 & flags) != 0;
        boolean whirlpool = (0x2 & flags) != 0;
        int count = gsmartInt(buffer);
        int entryCount = 0;
        int[] spacing = new int[count];
        for (int i = 0; i < count; i++) {
            spacing[i] = entryCount += gsmartInt(buffer);
            if (table.entryCount < spacing[i]) {
                table.entryCount = spacing[i];
            }
        }
        table.entryCount++;
        table.entry_revision = new int[table.entryCount];
        table.subEntryCount = new int[table.entryCount];
        table.entry_real_sub_count = new int[table.entryCount];
        table.entry_crc32 = new int[table.entryCount];
        if (whirlpool) {
            table.sub_entry_name_hash = new byte[table.entryCount][];
        }
        table.entry_sub_ptr = new int[table.entryCount][];
        if (named) {
            table.entry_name_hash = new int[table.entryCount];
            for (int i = 0; i < table.entryCount; i++) {
                table.entry_name_hash[i] = -1;
            }
            for (int i : spacing) {
                table.entry_name_hash[i] = buffer.getInt();
            }
        }
        for (int i : spacing) {
            table.entry_crc32[i] = buffer.getInt();
        }
        if (whirlpool) {
            for (int i = 0; i < count; i++) {
                byte[] digest = new byte[64];
                buffer.get(digest, 0, 64);
                table.sub_entry_name_hash[spacing[i]] = digest;
            }
        }
        for (int i : spacing) {
            table.entry_revision[i] = buffer.getInt();
        }
        for (int i : spacing) {
            table.subEntryCount[i] = gsmartInt(buffer);
        }
        for (int i : spacing) {
            int i_19_ = table.subEntryCount[i];
            entryCount = 0;
            table.entry_sub_ptr[i] = new int[i_19_];
            int i_20_ = -1;
            for (int i_21_ = 0; i_19_ > i_21_; i_21_++) {
                int i_22_ = table.entry_sub_ptr[i][i_21_] = entryCount += gsmartInt(buffer);
                if (i_20_ < i_22_) {
                    i_20_ = i_22_;
                }
            }
            table.entry_real_sub_count[i] = i_20_ + 1;
            if (i_20_ + 1 == i_19_) {
                table.entry_sub_ptr[i] = null;
            }
        }
        if (named) {
            table.sub_entry_name_hash = new byte[table.entryCount][];
            for (int i : spacing) {
                int i_25_ = table.subEntryCount[i];
                table.sub_entry_name_hash[i] = new byte[table.entry_real_sub_count[i]];
                for (int k = 0; table.entry_real_sub_count[i] > k; k++) {
                    table.sub_entry_name_hash[i][k] = -1;
                }
                for (int k = 0; k < i_25_; k++) {
                    int i_28_;
                    if (table.entry_sub_ptr[i] == null) {
                        i_28_ = k;
                    } else {
                        i_28_ = (table.entry_sub_ptr[i][k]);
                    }
                    table.sub_entry_name_hash[i][i_28_] = (byte) buffer.getInt();
                }
            }
        }
        return table;
    }
    
    public static int gsmartInt(ByteBuffer buffer) {
    	/*
    	 * if (((Stream) this).buffer[((Stream) this).index] < i)
				return 0x7fffffff & getInt(true);
			return getShort((byte) 126);
    	 */
    	int peek = buffer.getShort(buffer.position());
		if (peek < 0) {
			return 0x7fffffff & buffer.getInt();
		}
		return buffer.getShort() & 0xFFFF;
	}

    public int getEntryCount() {
        return entryCount;
    }

    public int[] getEntry_real_sub_count() {
        return entry_real_sub_count;
    }

    public int[] getArchiveCount() {
        return subEntryCount;
    }

    public int[][] getEntry_sub_ptr() {
        return entry_sub_ptr;
    }

    public int getRevision() {
        return revision;
    }

    public int findName(String name) {
        int hash = calcJaghash(name);
        for (int i = 0; i < entry_name_hash.length; i++) {
            if (entry_name_hash[i] == hash) {
                return i;
            }
        }
        return -1;
    }

    public int calcJaghash(String n) {
        int count = 0;
        byte[] characters = n.getBytes();
        for (int i = 0; i < n.length(); i++) {
            count = (characters[i] & 0xff) + ((count << 5) - count);
        }
        return count;
    }
}