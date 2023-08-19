package com.sevador.game.node.model.skills.firemaking;


/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class FiremakingData {

	public static enum Fire {
		NORMAL(1511, 1, 30, 2732, 40, 20), ACHEY(2862, 1, 30, 2732, 40, 1), OAK(
				1521, 15, 45, 2732, 60, 1), WILLOW(1519, 30, 45, 2732, 90, 1), TEAK(
				6333, 35, 45, 2732, 105, 1), ARCTIC_PINE(10810, 42, 50, 2732,
				125, 1), MAPLE(1517, 45, 50, 2732, 135, 1), MAHOGANY(6332, 50,
				70, 2732, 157.5, 1), EUCALYPTUS(12581, 58, 70, 2732, 193.5, 1), YEW(
				1515, 60, 80, 2732, 202.5, 1), MAGIC(1513, 75, 90, 2732, 303.8,
				1), CURSED_MAGIC(13567, 82, 100, 2732, 303.8, 1);

		private int logId;
		private int level;
		private int life;
		private int fireId;
		private int time;
		private double xp;

		Fire(int logId, int level, int life, int fireId, double xp, int time) {
			this.logId = logId;
			this.level = level;
			this.life = life;
			this.fireId = fireId;
			this.xp = xp;
			this.time = time;
		}

		public int getLogId() {
			return logId;
		}

		public int getLevel() {
			return level;
		}

		public int getLife() {
			return (life * 600);
		}

		public int getFireId() {
			return fireId;
		}

		public double getExperience() {
			return xp;
		}

		public int getTime() {
			return time;
		}
	}
}
