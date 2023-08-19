package com.sevador.game.node.model.skills.thieving;

import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 * 
 */
public class HomeStalls {

	/**
	 * The animation for thieving
	 */

	public static final int thievingAnimation = 832;

	public enum BasicStalls {

		CRAFTING(5000, 10, 1, 4874),
		FOOD(10000, 15, 15, 4875),
		GENERAL(15000, 20, 35, 4876), 
		MAGIC(20000, 30, 45, 4877),
		SCIMITAR(30000, 45, 60, 4878);

		private int reward;
		private int xp;
		private int req;
		private int id;

		BasicStalls(int reward, int xp, int req, int id) {
			this.reward = reward;
			this.xp = xp;
			this.req = req;
			this.id = id;
		}

		public int getReward() {
			return reward;
		}

		public int getXp() {
			return xp;
		}

		public int getReq() {
			return req;
		}

		public int getId() {
			return id;
		}
	}

	public static boolean canThieve(final Player player, GameObject obj) {
		int id = obj.getId();
		for (BasicStalls stalls : BasicStalls.values()) {
			if (stalls.getId() == id) {
				if (player.getSkills().getLevel(Skills.THIEVING) < stalls
						.getReq()) {
					player.getPacketSender().sendMessage("You need a thieving level of "
							+ stalls.getReq() + " to steal from this stall.");
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public static void startThieving(final Player player, final GameObject obj) {
		//NOT USED
	}

}
