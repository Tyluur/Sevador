package com.sevador.utility.loaders;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;

import com.sevador.game.node.npc.NPCCombatDefinitions;

public final class NPCCombatDefinitionsL {

	private final static HashMap<Integer, NPCCombatDefinitions> npcCombatDefinitions = new HashMap<Integer, NPCCombatDefinitions>();
	private final static NPCCombatDefinitions DEFAULT_DEFINITION = new NPCCombatDefinitions(
			1, -1, -1, -1, 5, 1, 1, 0, NPCCombatDefinitions.MELEE, -1, -1,
			NPCCombatDefinitions.PASSIVE);
	private static final String PACKED_PATH = "data/nodes/npcs/packedCombatDefinitions.ncd";

	public static void init() {
		if (new File(PACKED_PATH).exists())
			loadPackedNPCCombatDefinitions();
		else
			loadUnpackedNPCCombatDefinitions();

		/*
		 * FORMAT - HITPOINTS - ATTACKANIM - DEFANIM - DEATHANIM - ATKDELAY-
		 * DEATHDELAY - RESPAWNDELAY - MAXHIT -ATTACKSTYLE - ATTACKGFX -
		 * ATTACKPROJEXCTILE - AGRESSIVENESSTYPE
		 */
		
		npcCombatDefinitions.remove(1267);

		npcCombatDefinitions.put(1267, new NPCCombatDefinitions(500, 1312,
				1313, 1314, 2, 2, 0, 44, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));

		npcCombatDefinitions.put(2734, new NPCCombatDefinitions(100, 9232,
				9231, 9230, 6, 2, 0, 44, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));
		npcCombatDefinitions.put(2735, new NPCCombatDefinitions(100, 9232,
				9231, 9230, 6, 2, 0, 44, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));

		npcCombatDefinitions.put(2736, new NPCCombatDefinitions(200, 9233,
				9235, 9234, 6, 2, 0, 74, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));
		npcCombatDefinitions.put(2737, new NPCCombatDefinitions(200, 9233,
				9235, 9234, 6, 2, 0, 74, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));
		npcCombatDefinitions.put(2738, new NPCCombatDefinitions(100, 9233,
				9235, 9234, 6, 2, 0, 44, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));

		npcCombatDefinitions.put(2739, new NPCCombatDefinitions(400, 9243,
				9242, 9239, 6, 2, 0, 134, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));
		npcCombatDefinitions.put(2740, new NPCCombatDefinitions(400, 9243,
				9242, 9239, 6, 2, 0, 134, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));

		npcCombatDefinitions.put(2741, new NPCCombatDefinitions(800, 9252,
				9253, 9257, 6, 3, 0, 280, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));
		npcCombatDefinitions.put(2742, new NPCCombatDefinitions(800, 9252,
				9253, 9257, 6, 3, 0, 280, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));

		npcCombatDefinitions.put(2745, new NPCCombatDefinitions(2500, 9277,
				9278, 9279, 6, 2, 0, 44, NPCCombatDefinitions.MELEE, -1, -1,
				NPCCombatDefinitions.AGRESSIVE));

		npcCombatDefinitions.put(2881, new NPCCombatDefinitions(2550, 2855,
				2854, 2856, 7, 1, 60, 300, 1, 65535, 65535, 1));
		npcCombatDefinitions.put(2882, new NPCCombatDefinitions(2550, 2854,
				2852, 2856, 4, 1, 60, 610, 2, 65535, 65535, 1));
		npcCombatDefinitions.put(2883, new NPCCombatDefinitions(2550, 2853,
				2854, 2856, 4, 1, 60, 280, 0, 65535, 65535, 1));

		npcCombatDefinitions.put(6203, new NPCCombatDefinitions(2500, 14963,
				14962, 14969, 7, 1, 60, 463, 0, 65535, 65535, 11));

		npcCombatDefinitions.put(6222, new NPCCombatDefinitions(2500, 6977,
				6974, 6975, 7, 1, 60, 463, 0, 65535, 65535, 11));

		npcCombatDefinitions.put(9177, new NPCCombatDefinitions(3500, 12064,
				12070, 12069, 7, 1, 60, 334, 0, 65535, 65535, 1));

		/*
		 * 
		 * npcCombatDefinitions.put(npcId, new NPCCombatDefinitions(hitpoints,
		 * attackAnim, defenceAnim, deathAnim, attackDelay, deathDelay,
		 * respawnDelay, maxHit, attackStyle, attackGfx, attackProjectile,
		 * agressivenessType));
		 */
	}

	public static NPCCombatDefinitions getNPCCombatDefinitions(int npcId) {
		NPCCombatDefinitions def = npcCombatDefinitions.get(npcId);
		if (def == null)
			return DEFAULT_DEFINITION;
		return def;
	}

	private static void loadUnpackedNPCCombatDefinitions() {
		int count = 0;
		System.out.println("Packing npc combat definitions...");
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(
					PACKED_PATH));
			BufferedReader in = new BufferedReader(new FileReader(
					"data/nodes/npcs/unpackedCombatDefinitionsList.txt"));
			while (true) {
				String line = in.readLine();
				count++;
				if (line == null)
					break;
				if (line.startsWith("//"))
					continue;
				String[] splitedLine = line.split(" - ", 2);
				if (splitedLine.length != 2)
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + count
									+ ", " + line);
				int npcId = Integer.parseInt(splitedLine[0]);
				String[] splitedLine2 = splitedLine[1].split(" ", 12);
				if (splitedLine2.length != 12)
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + count
									+ ", " + line);
				int hitpoints = Integer.parseInt(splitedLine2[0]);
				int attackAnim = Integer.parseInt(splitedLine2[1]);
				int defenceAnim = Integer.parseInt(splitedLine2[2]);
				int deathAnim = Integer.parseInt(splitedLine2[3]);
				int attackDelay = Integer.parseInt(splitedLine2[4]);
				int deathDelay = Integer.parseInt(splitedLine2[5]);
				int respawnDelay = Integer.parseInt(splitedLine2[6]);
				int maxHit = Integer.parseInt(splitedLine2[7]);
				int attackStyle;
				if (splitedLine2[8].equalsIgnoreCase("MELEE"))
					attackStyle = NPCCombatDefinitions.MELEE;
				else if (splitedLine2[8].equalsIgnoreCase("RANGE"))
					attackStyle = NPCCombatDefinitions.RANGE;
				else if (splitedLine2[8].equalsIgnoreCase("MAGE"))
					attackStyle = NPCCombatDefinitions.MAGE;
				else if (splitedLine2[8].equalsIgnoreCase("SPECIAL"))
					attackStyle = NPCCombatDefinitions.SPECIAL;
				else if (splitedLine2[8].equalsIgnoreCase("SPECIAL2"))
					attackStyle = NPCCombatDefinitions.SPECIAL2;
				else
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + line);
				int attackGfx = Integer.parseInt(splitedLine2[9]);
				int attackProjectile = Integer.parseInt(splitedLine2[10]);
				int agressivenessType;
				if (splitedLine2[11].equalsIgnoreCase("PASSIVE"))
					agressivenessType = NPCCombatDefinitions.PASSIVE;
				else if (splitedLine2[11].equalsIgnoreCase("AGRESSIVE"))
					agressivenessType = NPCCombatDefinitions.AGRESSIVE;
				else
					throw new RuntimeException(
							"Invalid NPC Combat Definitions line: " + line);
				out.writeShort(npcId);
				out.writeShort(hitpoints);
				out.writeShort(attackAnim);
				out.writeShort(defenceAnim);
				out.writeShort(deathAnim);
				out.writeByte(attackDelay);
				out.writeByte(deathDelay);
				out.writeInt(respawnDelay);
				out.writeShort(maxHit);
				out.writeByte(attackStyle);
				out.writeShort(attackGfx);
				out.writeShort(attackProjectile);
				out.writeByte(agressivenessType);
				npcCombatDefinitions.put(npcId, new NPCCombatDefinitions(
						hitpoints, attackAnim, defenceAnim, deathAnim,
						attackDelay, deathDelay, respawnDelay, maxHit,
						attackStyle, attackGfx, attackProjectile,
						agressivenessType));
			}
			in.close();
			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static void loadPackedNPCCombatDefinitions() {
		try {
			RandomAccessFile in = new RandomAccessFile(PACKED_PATH, "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
					channel.size());
			while (buffer.hasRemaining()) {
				int npcId = buffer.getShort() & 0xffff;
				int hitpoints = buffer.getShort() & 0xffff;
				int attackAnim = buffer.getShort() & 0xffff;
				int defenceAnim = buffer.getShort() & 0xffff;
				int deathAnim = buffer.getShort() & 0xffff;
				int attackDelay = buffer.get() & 0xff;
				int deathDelay = buffer.get() & 0xff;
				int respawnDelay = buffer.getInt();
				int maxHit = buffer.getShort() & 0xffff;
				int attackStyle = buffer.get() & 0xff;
				int attackGfx = buffer.getShort() & 0xffff;
				int attackProjectile = buffer.getShort() & 0xffff;
				int agressivenessType = buffer.get() & 0xff;
				npcCombatDefinitions.put(npcId, new NPCCombatDefinitions(
						hitpoints, attackAnim, defenceAnim, deathAnim,
						attackDelay, deathDelay, respawnDelay, maxHit,
						attackStyle, attackGfx, attackProjectile,
						agressivenessType));
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private NPCCombatDefinitionsL() {

	}

}
