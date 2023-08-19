package com.sevador.game.node.model.skills.mining;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class MiningData {
	public static enum RockDefinitions {

		Copper_Ore(1, 17.5, 436, 10, 1, 11552, 5, 0), Tin_Ore(1, 17.5, 438, 15,
				1, 11552, 5, 0), Iron_Ore(15, 35, 440, 15, 1, 11552, 10, 0), Sandstone_Ore(
				35, 30, 6971, 30, 1, 11552, 10, 0), Silver_Ore(20, 40, 442, 25,
				1, 11552, 20, 0), Coal_Ore(30, 50, 453, 50, 10, 11552, 30, 0), Granite_Ore(
				45, 50, 6979, 50, 10, 11552, 20, 0), Gold_Ore(40, 60, 444, 80,
				20, 11554, 40, 0), Mithril_Ore(55, 80, 447, 100, 20, 11552, 60,
				0), Adamant_Ore(70, 95, 449, 130, 25, 11552, 180, 0), Runite_Ore(
				85, 125, 451, 150, 30, 11552, 360, 0), ESSENCE(-1, -1, -1, -1,
				-1, -1, -1, -1);

		private int level;
		private double xp;
		private int oreId;
		private int oreBaseTime;
		private int oreRandomTime;
		private int emptySpot;
		private int respawnDelay;
		private int randomLifeProbability;

		private RockDefinitions(int level, double xp, int oreId,
				int oreBaseTime, int oreRandomTime, int emptySpot,
				int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.oreId = oreId;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
			this.emptySpot = emptySpot;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getOreId() {
			return oreId;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

		public int getEmptyId() {
			return emptySpot;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

	public enum Essence {

		RUNE_ESSENCE(new int[] { 2491, 1291 }, 5, 1, 1), PURE_ESSENCE(
				new int[] { 2491, 1291 }, 5, 2, 30);

		private int[] objectId;
		@SuppressWarnings("unused")
		private int xp, delay, level;

		Essence(int[] objectId, int xp, int delay, int level) {
			this.objectId = objectId;
			this.setXp(xp);
			this.setDelay(delay);
			this.level = level;
		}

		public int[] getObjectId() {
			return objectId;
		}

		/**
		 * @return the delay
		 */
		public int getDelay() {
			return delay;
		}

		/**
		 * @param delay
		 *            the delay to set
		 */
		public void setDelay(int delay) {
			this.delay = delay;
		}

		/**
		 * @return the xp
		 */
		public int getXp() {
			return xp;
		}

		/**
		 * @param xp
		 *            the xp to set
		 */
		public void setXp(int xp) {
			this.xp = xp;
		}

	}
}
