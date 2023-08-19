package com.sevador.game.node.model.skills.fishing;

import com.sevador.game.node.model.mask.Animation;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class FishingData {
	public enum Fish {

		ANCHOVIES(321, 15, 40), BASS(363, 46, 100), COD(341, 23, 45), CAVE_FISH(
				15264, 85, 300), HERRING(345, 10, 30), LOBSTER(377, 40, 90), MACKEREL(
				353, 16, 20), MANTA(389, 81, 46), MONKFISH(7944, 62, 120), PIKE(
				349, 25, 60), SALMON(331, 30, 70), SARDINES(327, 5, 20), SEA_TURTLE(
				395, 79, 38), SEAWEED(401, 30, 0), OYSTER(407, 30, 0), SHARK(
				383, 76, 110), SHRIMP(317, 1, 10), SWORDFISH(371, 50, 100), TROUT(
				335, 20, 50), TUNA(359, 35, 80), CRAYFISH(13435, 1, 10);

		private final int id, level, xp;

		private Fish(int id, int level, int xp) {
			this.id = id;
			this.level = level;
			this.xp = xp;
		}

		public int getId() {
			return id;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}
	}

	public enum FishingSpots {
		CRAYFISH(6267, 1, 13431, -1, new Animation(619), Fish.CRAYFISH),
		BAIT(327, 2, 307, 313, new Animation(622), Fish.SARDINES, Fish.HERRING),
		NET(327, 1, 303, -1, new Animation(621), Fish.SHRIMP,
				Fish.ANCHOVIES), LURE(328, 1, 309, 314, new Animation(622),
				Fish.TROUT, Fish.SALMON), LURE2(329, 1, 309, 314,
				new Animation(622), Fish.TROUT, Fish.SALMON), BAIT2(328, 2,
				307, 313, new Animation(622), Fish.PIKE), BAIT3(329, 2, 307,
				313, new Animation(622), Fish.PIKE, Fish.CAVE_FISH), CAGE(6267,
				1, 301, -1, new Animation(619), Fish.LOBSTER), CAGE2(312, 1,
				301, -1, new Animation(619), Fish.LOBSTER), HARPOON(312, 2,
				311, -1, new Animation(618), Fish.TUNA, Fish.SWORDFISH,
				Fish.SHARK), BIG_NET(313, 1, 305, -1, new Animation(620),
				Fish.MACKEREL, Fish.COD, Fish.BASS, Fish.SEAWEED, Fish.OYSTER), HARPOON2(
				313, 2, 311, -1, new Animation(618), Fish.TUNA, Fish.SWORDFISH,
				Fish.SHARK), HARPOON3(952, 1, 311, -1, new Animation(621),
				Fish.TUNA, Fish.SWORDFISH, Fish.SHARK), NET2(952, 2, 303, -1,
				new Animation(618), Fish.MONKFISH);

		private final Fish[] fish;
		private final int id, option, tool, bait;
		private final Animation animation;

		private FishingSpots(int id, int option, int tool, int bait,
				Animation animation, Fish... fish) {
			this.id = id;
			this.tool = tool;
			this.bait = bait;
			this.animation = animation;
			this.fish = fish;
			this.option = option;
		}

		public Fish[] getFish() {
			return fish;
		}

		public int getId() {
			return id;
		}

		public int getOption() {
			return option;
		}

		public int getTool() {
			return tool;
		}

		public int getBait() {
			return bait;
		}

		public Animation getAnimation() {
			return animation;
		}
	}
}
