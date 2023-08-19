package com.sevador.game.node.model.skills.cooking;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.cooking.CookingData.Cookables;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.utility.Misc;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Cooking extends SkillAction {

	private int amount;
	private GameObject object;
	private Item item;
	private Cookables cook;
	private Animation COOKING = new Animation(883);

	public Cooking(GameObject object, Item item, int amount) {
		this.amount = amount;
		this.item = item;
		this.object = object;
	}

	@Override
	public boolean start(Player player) {
		if ((this.cook = Cookables.forId((short) item.getId())) == null) {
			return false;
		}
		if (cook.isFireOnly() && object.getId() != 2732) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You may only cook this on a fire.");
		} else if (cook.isSpitRoast() && object.getId() != 11363) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You may only cook this on an iron spit.");
			return false;
		} else if (player.getSkills().getLevel(Skills.COOKING) < cook.getLvl()) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a cooking level of " + cook.getLvl()
							+ " to cook this food.");
			return false;
		}
		player.getPacketSender().sendMessage(
				"You attempt to cook the "
						+ cook.getProduct().getDefinition().getName()
								.toLowerCase() + ".");
		return true;
	}

	private boolean isBurned(Player player) {
		int levelsToStopBurn = cook.getBurningLvl()
				- player.getSkills().getLevel(Skills.COOKING);
		if (levelsToStopBurn > 20) {
			levelsToStopBurn = 20;
		}
		return Misc.random(34) <= levelsToStopBurn;
	}

	@Override
	public boolean process(Player player) {
		if (!player.getInventory().contains(item.getId(), 1)) {
			return false;
		}
		if (!player.getInventory().contains(cook.getRawItem().getId(), 1)) {
			return false;
		}
		if (player.getSkills().getLevel(Skills.COOKING) < cook.getLvl()) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a cooking level of " + cook.getLvl()
							+ " to cook this.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		amount--;
		player.getUpdateMasks().register(COOKING);
		if ((player.getSkills().getLevel(Skills.COOKING) >= cook
				.getBurningLvl()) ? false : isBurned(player)) {
			player.getInventory().remove(new Item(item.getId(), 1));
			player.getInventory().add(
					new Item(cook.getBurntId().getId(), cook.getBurntId()
							.getAmount()));
			player.getPacketSender().sendMessage("Oops! You accidentally burnt the "
					+ cook.getProduct().getDefinition().getName().toLowerCase()
					+ ".");
		} else {
			player.getInventory().remove(new Item(item.getId(), 1));
			player.getInventory().add(
					new Item(cook.getProduct().getId(), cook.getProduct().getAmount()));
			player.getInventory().add(new Item(995, 5000));
			((Skills) player.getSkills()).addExperience(Skills.COOKING, cook.getXp());
			player.getPacketSender().sendMessage("You successfully cook the "
					+ cook.getProduct().getDefinition().getName().toLowerCase()
					+ ".");
		}
		if (amount > 0) {
			player.getPacketSender().sendMessage("You attempt to cook the "
					+ cook.getProduct().getDefinition().getName().toLowerCase()
					+ ".");
			return 1;
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	public static Cookables isCookingSkill(Item item) {
		return Cookables.forId((short) item.getId());
	}

}
