package com.sevador.game.minigames;

import java.io.Serializable;
import java.util.Random;

import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.utility.Misc;

/**
 * 
 * @author 'Tyluur <tyluur@zandium.org>
 * 
 */
@SuppressWarnings("serial")
public class FightCaves implements Serializable {

	private static final Random RANDOM = new Random();

	private static final int[] BASE_NPCS = new int[] { 2734, 2736, 2739, 2741,
			2743, 2745 };

	private int stage;

	public NPC[] getSpawns() {
		int[] npcs = new int[6];
		int index = 0;
		int id = stage;
		for (int i = 6; i >= 1; i--) {
			int threshold = (1 << i) - 1;
			if (id >= threshold) {
				for (int j = 0; j <= id / threshold; j++) {
					npcs[index++] = BASE_NPCS[i - 1]
							+ (i != 6 ? RANDOM.nextInt(2) : 0);
					id -= threshold;
				}
			}
		}
		NPC[] enemies = new NPC[index];
		for (int i = 0; i < enemies.length; i++) {
			int random = Misc.random(3);
			switch (random) {
			case 0:
				enemies[i] = new NPC(npcs[i], NodeType.AVIANSIE, false);
				enemies[i].setLocation(Location.locate(2399 + Misc.random(3), 5086 - Misc.random(3), 0));
				break;
			case 1:
				enemies[i] = new NPC(npcs[i], NodeType.AVIANSIE, false);
				enemies[i].setLocation(Location.locate(2399 + Misc.random(3), 5086 - Misc.random(3), 0));
				break;
			case 2:
				enemies[i] = new NPC(npcs[i], NodeType.AVIANSIE, false);
				enemies[i].setLocation(Location.locate(2399 + Misc.random(3), 5086 - Misc.random(3), 0));
				break;
			case 3:
				enemies[i] = new NPC(npcs[i], NodeType.AVIANSIE, false);
				enemies[i].setLocation(Location.locate(2399 + Misc.random(3), 5086 - Misc.random(3), 0));
				break;
			}
		}
		return enemies;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getStage() {
		return stage;
	}

}
