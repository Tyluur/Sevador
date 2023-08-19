package com.sevador.game.dialogue.impl;

import net.burtleburtle.cache.format.NPCDefinitions;

import com.sevador.game.dialogue.DialAnims;
import com.sevador.game.dialogue.Dialogue;
import com.sevador.game.node.Item;
import com.sevador.utility.Misc;

public class BarrowsRewards extends Dialogue {

	public BarrowsRewards() {
	}

	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				(short) 243,
				new String[] {
						NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello, wild adventurer. I am the barrows reward master.",
						"If you have defeated all of the barrows monsters,",
				"I can give you a reward. Would you like that?" },
				(byte) 1, npcId, DialAnims.HAPPY_TALKING.getAnim());
	}

	public static final int[][] COMMON_REWARDS = {
		{558, 0, 1795}, {562, 0, 773}, {560, 0, 391}, {565, 0, 164},
		{995, 800, 5000}, {4740, 50, 150}
	};

	public static final int[] RARE_REWARDS = {
		1149, 985, 987
	};

	protected static final int[] BARROW_REWARDS = {
		4757, 4759, 4753, 4755, // Verac's
		4736, 4738, 4734, 4732, // Karil's
		4745, 4747, 4749, 4751, // Torag's
		4708, 4710, 4712, 4714, // Ahrim's
		4716, 4718, 4720, 4722, // Dharok's
		4724, 4726, 4728, 4730, // Guthan's
	};

	public void reset() {
		for (int i = 0; i < player.getKilledBarrowBrothers().length; i++) {
			player.getKilledBarrowBrothers()[i] = false;
		}
		player.setHiddenBrother(0);
		player.setBarrowsKillCount(0);
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_2_OPTIONS, new String[] { "Select an option.",
					"Yes", "No" });
		} else if (stage == 0) {
			switch (componentId) {
			case 1:
				boolean killedAll = false;
				int i = 4;
				int i2 = 0;
				for (boolean b : player.getSettings().getKilledBrothers())
					if (b) {
						i2++;
						System.err.println("HEII" +  i2 + " : " + i);
					}
				if (i2==i)
					killedAll = true;
				if (killedAll) {
					player.removeAttribute("canLoot");
					for (int[] data : COMMON_REWARDS) {
						if (player.getRandom().nextDouble() > 0.40) {
							int id = data[0];
							int amount = Misc.random(data[1], data[2]);
							player.getInventory().add(new Item(id, amount));
						}
					}
					int chance = 1;
					for (int j = 0; j < player.getSettings().getKilledBrothers().length; j++) {
						if (player.getSettings().getKilledBrothers()[j]) {
							player.getSettings().getKilledBrothers()[j] = false;
							chance += 3;
						}
					}
					if (player.getRandom().nextInt(100) <= (chance > 15 ? 15 : chance)) {
						int item = BARROW_REWARDS[player.getRandom().nextInt(BARROW_REWARDS.length)];
						player.getInventory().add(new Item(item, 1));
					}
					player.getPlayerAreaTick().updateBarrowsInterface();
				} else {
					player.getPlayerAreaTick().updateBarrowsInterface();
					sendEntityDialogue((short) 242, new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Don't pull a quick one on me! Kill all of the",
					"Barrows brothers then come talk to me." },
					(byte) 1, npcId, DialAnims.MEAN_FACE.getAnim());
					stage = -2;
				}
				break;
			case 2:
				end();
				break;
			}
		} else {
			end();
		}
	}

	public void finish() {
	}

	int npcId;
}
