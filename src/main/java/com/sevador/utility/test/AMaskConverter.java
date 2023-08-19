package com.sevador.utility.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Emperor
 *
 */
public class AMaskConverter {

	public static void main(String...strings) throws IOException {
		File f = new File("Roar.txt");
		if (!f.exists()) {
			throw new FileNotFoundException("File was not found!");
		}
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String s;
		while ((s = reader.readLine()) != null) {
			if (s.equals("AMASKSHORT 0, 8978490, 99, 2046") || s.equals("")) {
				System.out.println("\n=============================================================================\n");
			}
			if (s.startsWith("AMASKSHORT") || s.startsWith("NIGGER")) {
				int start = 11;
				if (s.startsWith("NIGGER")) {
					start = 7;
				}
				String[] arg = s.substring(start).split(", ");
				int min = Integer.parseInt(arg[0]);
				int idata = Integer.parseInt(arg[1]);
				int max = Integer.parseInt(arg[2]);
				int value = Integer.parseInt(arg[3]);
				System.out.println("PacketHandler.offer(new AccessMask(player, " + min + ", " + max + ", " + (idata >> 16) + ", " + (idata & 0xFFF) + ", " + (value >> 16) + ", " + (value & 0xFFF) + "));");
//				AMASKSHORT 28, 44498944, 55, 2097152
			} else if (s.startsWith("Bconfig? ID:")) {
				String form = s.substring(12).replaceAll(" ", "");
				String[] bcon = form.split("Value:");
				System.out.println("PacketHandler.offer(new CS2Config(player, " + Integer.parseInt(bcon[0]) + ", " + Integer.parseInt(bcon[1]) + "));");
			} else if (s.startsWith("Sendconfig ID:")) {
				String form = s.substring(14).replaceAll(" ", "");
				String[] bcon = form.split("Value:");
				System.out.println("PacketHandler.offer(new ConfigPacket(player, " + Integer.parseInt(bcon[0]) + ", " + Integer.parseInt(bcon[1]) + "));");
				
			}
		}
	}
}