package com.sevador.game.node.model.skills.slayer;

import java.io.Serializable;

import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.utility.Misc;

/**
 * @author Wolfey
 * @author Mystic Flow
 */
public class SlayerTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3885979679549716755L;

	public enum Master {
		VANNAKA(1597, new Object[][] { { "Bat", 1, 25, 50, 8.0 },
				{ "Goblin", 1, 10, 45, 10.0 },
				{ "Crawling Hand", 1, 10, 60, 10.0 },
				{ "Cave crawler", 5, 35, 75, 22.0 },
				{ "Cockatrice", 25, 50, 175, 37.0 },
				{ "Green dragon", 35, 35, 200, 40.0 },
				{ "Cave horror", 58, 50, 135, 55.0 },
				{ "Fire giant", 62, 40, 120, 111.0 },
				{ "Zombie hand", 70, 40, 60, 115.0 },
				{ "Skeletal hand", 60, 50, 100, 90.0 },
				{ "Werewolf", 65, 70, 120, 105.0 },
				{ "Baby red dragon", 50, 60, 120, 50.0 },
				{ "Moss giant", 35, 30, 100, 40.0 },
				{ "Bronze dragon", 50, 30, 70, 125.0 },
				{ "Iron dragon", 65, 30, 70, 173.2 },
				{ "Steel dragon", 79, 30, 70, 220.4 },
				{ "Bloodveld", 50, 60, 90, 120 },
				{ "Gargoyle", 75, 70, 100, 105 },
				{ "Hellhound", 1, 50, 100, 116 } }),
		KURADAL(9084,
				new Object[][] { { "Bloodveld", 50, 60, 90, 120 },
						{ "Gargoyle", 75, 70, 100, 105 },
						{ "Dark beast", 70, 90, 130, 225.4 },
						{ "Greater demon", 1, 50, 90, 87 },
						{ "Hellhound", 1, 50, 100, 116 }
				// {"Aquanites", 78, 50, 90, 125},
				// {"Desert strykewyrm", 77, 50, 90, 120},
				// {"Jungle strykewyrm", 73, 40, 80, 110},
				// {"Ice strykewyrm", 93, 68, 120, 330},
				// {"Spiritual mage", 83, 60, 100, 88},
				// {"Skeletal wyvern", 72, 40, 80, 210},
				}), DURADEL(8466, new Object[][] {
				{ "Bloodveld", 50, 60, 90, 120 },
				{ "Gargoyle", 75, 70, 100, 105 },
				{ "Dark beast", 70, 90, 130, 225.4 },
				{ "Greater demon", 1, 50, 90, 87 },
				{ "Hellhound", 1, 50, 100, 116 },
		// {"Aquanites", 78, 50, 90, 125},
		// {"Desert strykewyrm", 77, 50, 90, 120},
		// {"Jungle strykewyrm", 73, 40, 80, 110},
		// {"Ice strykewyrm", 93, 68, 120, 330},
		// {"Spiritual mage", 83, 60, 100, 88},
		// {"Skeletal wyvern", 72, 40, 80, 210},
				}), 
		SUMONA(7780, new Object[][] { { "Bat", 1, 25, 50, 8.0 },
				{ "Goblin", 1, 10, 45, 10.0 },
				{ "Crawling Hand", 1, 10, 60, 10.0 },
				{ "Cave crawler", 5, 35, 75, 22.0 },
				{ "Cockatrice", 25, 50, 175, 37.0 },
				{ "Green dragon", 35, 35, 200, 40.0 },
				{ "Cave horror", 58, 50, 135, 55.0 },
				{ "Fire giant", 62, 40, 120, 111.0 },
				{ "Zombie hand", 70, 40, 60, 115.0 },
				{ "Skeletal hand", 60, 50, 100, 90.0 },
				{ "Werewolf", 65, 70, 120, 105.0 },
				{ "Baby red dragon", 50, 60, 120, 50.0 },
				{ "Moss giant", 35, 30, 100, 40.0 },
				{ "Bronze dragon", 50, 30, 70, 125.0 },
				{ "Iron dragon", 65, 30, 70, 173.2 },
				{ "Steel dragon", 79, 30, 70, 220.4 },
				{ "Abyssal demons", 85, 60, 130, 150 },
				{ "Nechryael", 80, 60, 120, 105 },
				{ "Aberrant spectre", 60, 50, 100, 90 },
				{ "Infernal mage", 45, 30, 80, 60 },
				{ "Bloodveld", 50, 60, 90, 120 },
				{ "Gargoyle", 75, 70, 100, 105 },
				{ "Dark beast", 70, 90, 130, 225.4 },
				{ "Greater demon", 1, 50, 90, 87 },
				{ "Hellhound", 1, 50, 100, 116 }
		// {"Aquanites", 78, 50, 90, 125},
		// {"Desert strykewyrm", 77, 50, 90, 120},
		// {"Jungle strykewyrm", 73, 40, 80, 110},
		// {"Ice strykewyrm", 93, 68, 120, 330},
		// {"Spiritual mage", 83, 60, 100, 88},
		// {"Skeletal wyvern", 72, 40, 80, 210},
				});

		private int id;
		private Object[][] data;

		private Master(int id, Object[][] data) {
			this.id = id;
			this.data = data;
		}

		public static Master forId(int id) {
			for (Master master : Master.values()) {
				if (master.id == id) {
					return master;
				}
			}
			return null;
		}

		public int getId() {
			return id;
		}

	}

	private Master master;
	private int taskId;
	private int taskAmount;
	private int amountKilled;

	public SlayerTask(Master master, int taskId, int taskAmount) {
		this.master = master;
		this.taskId = taskId;
		this.taskAmount = taskAmount;
	}

	public String getName() {
		return (String) master.data[taskId][0];
	}

	public static SlayerTask random(Player player, Master master) {
		SlayerTask task = null;
		while (true) {
			int random = Misc.random(master.data.length - 1);
			int requiredLevel = (Integer) master.data[random][1];
			if (player.getSkills().getLevel(Skills.SLAYER) < requiredLevel) {
				continue;
			}
			int minimum = (Integer) master.data[random][2];
			int maximum = (Integer) master.data[random][3];
			if (task == null) {
				task = new SlayerTask(master, random, Misc.random(minimum,
						maximum));
				player.setTask(task);
			}
			break;
		}
		return task;
	}

	public int getTaskId() {
		return taskId;
	}

	public int getTaskAmount() {
		return taskAmount;
	}

	public void decreaseAmount() {
		taskAmount--;
	}

	public int getXPAmount() {
		Object obj = master.data[taskId][4];
		if (obj instanceof Double) {
			return (int) Math.round((Double) obj);
		}
		if (obj instanceof Integer) {
			return (Integer) obj;
		}
		return 0;
	}

	public Master getMaster() {
		return master;
	}

	/**
	 * @return the amountKilled
	 */
	public int getAmountKilled() {
		return amountKilled;
	}

	/**
	 * @param amountKilled
	 *            the amountKilled to set
	 */
	public void setAmountKilled(int amountKilled) {
		this.amountKilled = amountKilled;
	}

}
