package com.sevador.utility;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

public final class ItemBonuses {

	private static HashMap<Integer, int[]> itemBonuses;
	private final static String PACKED_PATH = "data/nodes/bonuses.ib";

	public static final void init() {
		if (new File(PACKED_PATH).exists())
			loadItemBonuses();
		else
			System.err.println("Missing item bonuses.");
	}

	public static final int[] getItemBonuses(int itemId) {
		return itemBonuses.get(itemId);
	}

	private static final void loadItemBonuses() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			itemBonuses = new HashMap<Integer, int[]>(buffer.remaining() / 38);
			while (buffer.hasRemaining()) {
				itemBonuses.put(23659, new int[] { 3, 3, 3, 3, 3, 12, 12, 12,
						12, 12, 12, 0, 0, 0, 8, 0, 2, 0 });
				itemBonuses.put(7459, new int[] { 7, 7, 7, 4, 7, 7, 7, 7, 4, 7,
						4, 0, 0, 0, 7, 0, 0, 0 });
				itemBonuses.put(7460, new int[] { 8, 8, 8, 4, 8, 8, 8, 8, 4, 8,
						4, 0, 0, 0, 8, 0, 0, 0 });
				itemBonuses.put(7461, new int[] { 9, 9, 9, 5, 9, 9, 9, 9, 5, 9,
						5, 0, 0, 0, 9, 0, 0, 0 });
				itemBonuses.remove(6570);
				itemBonuses.put(6570, new int[] { 1, 1,	1, 1, 1, 11, 11, 11, 11, 11, 11, 0, 0, 0, 4, 0, 2, 0} );
				int itemId = buffer.getShort() & 0xffff;
				int[] bonuses = new int[18];
				for (int index = 0; index < bonuses.length; index++)
					bonuses[index] = buffer.getShort();
				itemBonuses.put(itemId, bonuses);
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private ItemBonuses() {

	}

}
