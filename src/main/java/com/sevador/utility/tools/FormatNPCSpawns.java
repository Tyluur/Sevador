package com.sevador.utility.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.sevador.Main;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.utility.Constants;
import com.sevador.utility.configuration.FileUtilities;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class FormatNPCSpawns {

	public static void main(String[] args) {
		try {
			boolean ignore = false;
			for (String lines : FileUtilities.readFile("./data/nodes/npcs/npcspawns.txt")) {
				if (lines.startsWith("//") || lines.equals("")) {
					continue;
				}
				if (lines.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (lines.contains("*/")) {
						ignore = false;
					}
					continue;
				}			
				String[] data = lines.split(" ");
				Integer spotId = Integer.parseInt(data[0]);
				for (int spots : Constants.ZAMORAK_NPCS) {
					if (spotId != null) {
						if (spotId == spots) {
							int rotation = 0;
							NPC npc = new NPC(spotId);
							npc.setLocation(new Location(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3])));
							Main.getNodeWorker().offer(npc);
							try {
								File file = new File("data/xml/npc_spawns.xml");
								File temp = File.createTempFile("npc_spawns", ".xml", file.getParentFile());
								String delete = "</map>";
								Location player = new Location(Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
								BufferedReader reader = new BufferedReader(new FileReader(file));
								PrintWriter writer = new PrintWriter(new FileWriter(temp));
								for (String line; (line = reader.readLine()) != null;) {
									line = line.replaceAll(delete, "");
									writer.println(line);
								}
								reader.close();
								writer.close();
								file.delete();
								boolean s = temp.renameTo(file);
								System.err.println(s ? "successfully renamed " + data[0] + " " : "error renaming..");
								BufferedWriter bw = new BufferedWriter(new FileWriter(
										"./data/xml/npc_spawns.xml", true));
								bw.newLine();
								bw.write("  <npc>");
								bw.newLine();
								bw.write("    <!-- " + npc.getDefinition().name + " -->");
								bw.newLine();
								bw.write("    <id>"+spotId+"</id>");
								bw.newLine();
								bw.write("      <x>"+player.getX()+"</x>");
								bw.newLine();
								bw.write("      <y>"+player.getY()+"</y>");
								bw.newLine();
								bw.write("      <z>"+player.getZ()+"</z>");
								bw.newLine();
								bw.write("    <rotation>"+rotation+"</rotation>");
								bw.newLine();
								bw.write("   </npc>");
								bw.newLine();
								bw.write("</map>");
								bw.newLine();
								bw.flush();
								bw.close();
							} catch (Throwable t) {
								t.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
