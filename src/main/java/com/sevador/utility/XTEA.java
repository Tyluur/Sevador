package com.sevador.utility;

/**
 * @author 'Mystic Flow
 */
public class XTEA {

	private static final int DELTA = -1640531527;
	private static final int SUM = -957401312;

	public static byte[] decrypt(int[] cryption, byte[] data, int offset, int length) {
		int k = 0;
		for(int i = 0; i < cryption.length; i++) {
			if(cryption[i] == 0) {
				k++;
			}
		}
		if(k == cryption.length) {
			return data;
		}
		int numBlocks = (length - offset) / 8;
		int[] dataBlock = new int[2];

		for (int i = 0; i < numBlocks; i++) {
			dataBlock[0] = BufferUtils.readInt((i * 8) + offset, data);
			dataBlock[1] = BufferUtils.readInt((i * 8) + offset + 4, data);
			
			long sum = SUM;
			int round = 0;
			
			do {
				dataBlock[1] -= (cryption[(int) ((sum & 0x1933) >>> 11)] + sum ^ dataBlock[0] + (dataBlock[0] << 4 ^ dataBlock[0] >>> 5));
				sum -= DELTA;
				dataBlock[0] -= ((dataBlock[1] << 4 ^ dataBlock[1] >>> 5) + dataBlock[1] ^ cryption[(int) (sum & 0x3)] + sum);	
				round++;
			} while(round < 32);
			
			BufferUtils.writeInt(dataBlock[0], (i * 8) + offset, data);
			BufferUtils.writeInt(dataBlock[1], (i * 8) + offset + 4, data);
		}
		return data;
	}
}
