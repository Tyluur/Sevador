package com.sevador.game.node.model.skills.fishing;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.fishing.FishingData.Fish;
import com.sevador.game.node.model.skills.fishing.FishingData.FishingSpots;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.utility.Misc;
import com.sevador.utility.Priority;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Fishing extends SkillAction {

	private FishingSpots spot;

	@SuppressWarnings("unused")
	private NPC npc;

	private int fishId;

	private boolean multipleCatch;

	public Fishing(FishingSpots spot, NPC npc) {
		this.spot = spot;
		this.npc = npc;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		fishId = getRandomFish(player);
		if (spot.getFish()[fishId] == Fish.TUNA
				|| spot.getFish()[fishId] == Fish.SHARK
				|| spot.getFish()[fishId] == Fish.SWORDFISH) {
			if (Misc.random(50) <= 5) {
				if (player.getSkills().getLevel(Skills.AGILITY) >= spot
						.getFish()[fishId].getLevel())
					multipleCatch = true;
			}
		}
		player.getPacketSender().sendMessage("You attempt to capture a fish...");
		setActionDelay(player, getFishingDelay(player));
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.getUpdateMasks().register(
				new Animation(spot.getAnimation().getId(), 0, false,
						Priority.HIGHEST));
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		addFish(player);
		return getFishingDelay(player);
	}

	@Override
	public void stop(Player player) {
	}

	/**
	 * Get's the message.
	 * 
	 * @param fish
	 *            The fish catch player is getting.
	 * @return MessagePacket
	 */
	private String getMessage(Item fish) {
		if (spot.getFish()[fishId] == Fish.ANCHOVIES
				|| spot.getFish()[fishId] == Fish.SHRIMP)
			return "You manage to catch some "
					+ fish.getDefinition().getName().toLowerCase() + ".";
		else if (multipleCatch)
			return "Your quick reactions allow you to catch two "
					+ fish.getDefinition().getName().toLowerCase() + ".";
		else
			return "You manage to catch a "
					+ fish.getDefinition().getName().toLowerCase() + ".";
	}

	/**
	 * Adds the fish in player's inventory and give exp.
	 * 
	 * @param player
	 *            The player.
	 */
	private void addFish(Player player) {
		Item fish = new Item(spot.getFish()[fishId].getId(), multipleCatch ? 2
				: 1);
		player.getPacketSender().sendMessage(getMessage(fish));
		if (spot.getBait() != -1)
			player.getInventory().remove(new Item(spot.getBait(), 1));
		((Skills) player.getSkills()).addExperience(Skills.FISHING,
				spot.getFish()[fishId].getXp());
		player.getInventory().add(fish);
		player.getInventory().add(new Item(995, 5000));
		fishId = getRandomFish(player);
	}

	private boolean checkAll(Entity entity) {
		Player player = entity.getPlayer();
		if (player.getSkills().getLevel(Skills.FISHING) < spot.getFish()[fishId]
				.getLevel()) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a fishing level of "
							+ spot.getFish()[fishId].getLevel()
							+ " to fish here.");
			return false;
		}
		if (!player.getInventory().contains(spot.getTool(), 1)) {
			player.getPacketSender().sendMessage("You need a "
					+ new Item(spot.getTool()).getDefinition().name
							.toLowerCase() + " to fish here.");
			return false;
		}
		if (spot.getBait() != -1
				&& !player.getInventory().contains(spot.getBait(), 1)) {
			player.getPacketSender().sendMessage("You don't have enough "
					+ new Item(spot.getBait()).getDefinition().name
							.toLowerCase() + " to fish here.");
			return false;
		}
		if (player.getInventory().freeSlots() < 1) {
			player.getPacketSender().sendMessage("You do not have enough empty slots in your inventory to do this.");
			return false;
		}
		return true;
	}

	/**
	 * Gets the fishing delay.
	 * 
	 * @param player
	 *            The player.
	 * @return Delay
	 */
	private int getFishingDelay(Player player) {
		int playerLevel = player.getSkills().getLevel(Skills.FISHING);
		int fishLevel = spot.getFish()[fishId].getLevel();
		int modifier = spot.getFish()[fishId].getLevel();
		int randomAmt = Misc.random(4);
		double cycleCount = 1, otherBonus = 0;
		cycleCount = Math
				.ceil(((fishLevel + otherBonus) * 50 - playerLevel * 10)
						/ modifier * 0.25 - randomAmt * 4);
		if (cycleCount < 1) {
			cycleCount = 1;
		}
		int delay = (int) cycleCount + 1;
		delay /= player.getAuraManager().getFishingAccurayMultiplier();
		return delay;
	}

	/**
	 * Gets the random fish.
	 * 
	 * @param player
	 *            The player
	 * @return Random fish index.
	 */
	private int getRandomFish(Player player) {
		int random = Misc.random(spot.getFish().length - 1);
		int difference = player.getSkills().getLevel(Skills.FISHING)
				- spot.getFish()[random].getLevel();
		if (difference < -1)
			return random = 0;
		if (random < -1)
			return random = 0;
		return random;
	}

}
