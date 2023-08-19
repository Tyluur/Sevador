package com.sevador.utility.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;

/**
 * An application used for dumping current grand exchange item prices.
 * @author Emperor
 */
public final class GEPriceDumper {

	/**
	 * The website to dump from.
	 */
	private static final String WEBSITE = "http://tip.it/runescape/index.php?gec&itemid=";
	
	/**
	 * The main method.
	 * @param args The arguments cast on runtime.
	 */
	public static void main(String[] args) {
		dump(0, 22308);
		load();
	}
	
	/**
	 * Loads the grand exchange prices.
	 */
	private static void load() {
		try {
			DataInputStream dat = new DataInputStream(new FileInputStream("./data/geprices.dat"));
			BufferedWriter bw = new BufferedWriter(new FileWriter("./geprices.txt"));
			while (true) {
				int itemId = dat.readShort();
				if (itemId == -1) {
					break;
				}
				int value = dat.readInt();
				bw.write(itemId + " - " + value);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			dat.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Dumps the grand exchange prices.
	 * @param startId The start item id.
	 * @param endId The end item id.
	 */
	private static void dump(int startId, int endId) {
		Cache.init();
		ItemDefinition.init();
		DataOutputStream dat = null;
        try {
        	dat = new DataOutputStream(new FileOutputStream("./data/grand_exchange_prices.dat"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return;
		}
        System.out.println("Dumping [" + startId + "-" + endId + "] on website - " + WEBSITE + "...");
		Map<Integer, Integer> values = new HashMap<Integer, Integer>();
		BufferedReader br;
		for (int id = startId; id < endId; id++) {
			try {
				if (id % 100 == 0) {
					System.out.println("Dumped id " + id + ".");
				}
				br = new BufferedReader(new InputStreamReader(new URL(new StringBuilder(WEBSITE).append(id).toString()).openStream()));
				String s;
				br.skip(15768);
				while ((s = br.readLine()) != null) {
					if (s.startsWith("<tr><td colspan=\"4\"><b>Current Market Price: </b>")) {
						String value = s.replace("<tr><td colspan=\"4\"><b>Current Market Price: </b>", "").replace("gp</td></tr>", "").replace(",", "");
						try {
							int val = Integer.parseInt(value);
							values.put(id, val);
							write(id, val, dat);
						} catch (Throwable t) {
							t.printStackTrace();
							System.out.println(value);
						}
						break;
					} else if (s.contains("Invalid item number!")) {
						ItemDefinition def = ItemDefinition.forId(id);
						if (def != null && def.isNoted() && id != 0) {
							if (ItemDefinition.forId(id - 1).getName().equals(def.getName())) {
								write(id, values.get(id - 1), dat);
							} else if (id == 10843) {
								write(id, values.get(10828), dat);
							}
						}
						break;
					}
				}
				br.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		try {
			dat.writeShort(-1);
			dat.flush();
			dat.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Dumped " + values.size() + " item prices!");
	}
	
	/**
	 * Writes the price for an id on the output stream.
	 * @param id The item id.
	 * @param value The value.
	 * @param dat The output stream to write on.
	 */
	private static void write(int id, Integer value, DataOutputStream dat) {
		try {
			write(id, value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void write(int id, int value) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("./geprices.txt"));
		bw.write(id + " - " + value);
		try {
			while (true) {
				if (id == -1) break;
				bw.write(id + " - " + value);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}