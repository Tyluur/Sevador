package com.sevador.utility.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.NPCDefinition;

/**
 * Converts the dumped npc spawns into one xml file.
 * @author Emperor
 *
 */
public class NPCSpawnsConverter {

	/**
	 * The list of ignored NPC ids.
	 */
	private static final List<Integer> IGNORED_NPC_IDS = new ArrayList<Integer>();
	
	/**
	 * Initialize the ignored npc ids list.
	 */
	static {
		for (int i = 6794; i < 6891; i++) {
			IGNORED_NPC_IDS.add(i);
		}
		for (int i = 6900; i < 6970; i++) {
			IGNORED_NPC_IDS.add(i);
		}
		for (int i = 7210; i < 7379; i++) {
			IGNORED_NPC_IDS.add(i);
		}
		for (int i = 6565; i < 6604; i++) {
			IGNORED_NPC_IDS.add(i);
		}
		for (int i = 13296; i < 13299; i++) {
			IGNORED_NPC_IDS.add(i);
		}
		IGNORED_NPC_IDS.add(6894);
		IGNORED_NPC_IDS.add(6895);
		IGNORED_NPC_IDS.add(6896);
		IGNORED_NPC_IDS.add(6897);
		IGNORED_NPC_IDS.add(13634);
		IGNORED_NPC_IDS.add(1091);
	}
	
	/**
	 * The main method.
	 * @param args The argument cast on runtime.
	 */
	public static void main(String...args) {
		Cache.init();
		File directory = new File("./data/nodes/newspawns");
		Map<Integer, String> spawns = new HashMap<Integer, String>();
		BufferedReader br;
		String s;
		for (File f: directory.listFiles()) {
			try {
				br = new BufferedReader(new FileReader(f));
				while ((s = br.readLine()) != null) {
					if (s.equals("")) {
						continue;
					}
					String[] arg = s.split(": ");
					int index = Integer.parseInt(arg[0]);
					if (spawns.containsKey(index)) {
						continue;
					}
					spawns.put(index, arg[1]);
				}
				br.close();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		try {
			int count = 0;
			BufferedWriter bw = new BufferedWriter(new FileWriter("./data/xml/npc_spawns.xml"));
			bw.write("<map>");
			bw.newLine();
			for (int index : spawns.keySet()) {
				//32532: 6794, 1654, 4210, 0, 0
				String[] arg = spawns.get(index).split(", ");
				int id = Integer.parseInt(arg[0]);
				if (IGNORED_NPC_IDS.contains(id) || id >= Cache.getAmountOfNpcs()) {
					continue;
				}
				bw.write("  <npc>");
				bw.newLine();
				if (!NPCDefinition.forId(id).name.equals("null")) {
					bw.write("    <!-- " + NPCDefinition.forId(id).name + " -->");
					bw.newLine();
				}
				bw.write("    <id>" + id + "</id>");
				bw.newLine();
				bw.write("      <x>" + arg[1] + "</x>");
				bw.newLine();
				bw.write("      <y>" + arg[2] + "</y>");
				bw.newLine();
				bw.write("      <z>" + arg[3] + "</z>");
				bw.newLine();
				bw.write("    <rotation>-" + arg[4] + "</rotation>");
				bw.newLine();
				bw.write("   </npc>");
				bw.newLine();
				count++;
			}
			bw.write("</map>");
			bw.newLine();
			bw.flush();
			bw.close();
			System.out.println("Converted " + count + " NPC spawns.");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}