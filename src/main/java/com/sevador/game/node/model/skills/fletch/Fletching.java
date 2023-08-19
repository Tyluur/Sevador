package com.sevador.game.node.model.skills.fletch;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.skills.fletch.FletchingData.Fletch;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Fletching extends SkillAction {

	public static final int KNIFE = 946;
	public static final int CHISLE = 1755;
	public static final int BOW_STRING = 1777;
	public static final int CROSSBOW_STRING = 9438;
	private Fletch fletch;
	private int option;
	private int ticks;

	public Fletching(Fletch fletch, int option, int ticks) {
		this.fletch = fletch;
		this.option = option;
		this.ticks = ticks;
	}

	@Override
	public boolean start(Player player) {
		if (option >= fletch.getProduct().length)
			return false;
		if (!process(player))
			return false;
		player.getPacketSender().sendMessage(
				"You attempt to create a "
						+ new Item(fletch.getProduct()[option]).getDefinition()
								.getName().toLowerCase().replace("(u)", "")
						+ "...", true);
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (ticks <= 0)
			return false;
		if (player.getSkills().getLevel(Skills.FLETCHING) < fletch.getLevel()[option]) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a fletching level of "
							+ fletch.getLevel()[option] + " to fletch this.");
			return false;
		}
		if (!player.getInventory().contains(fletch.getId(), 1)
				|| !player.getInventory().contains(fletch.getSelected(), 1))
			return false;
		return true;
	}

	public static boolean maxMakeQuantityTen(Fletch fletch) {
		return fletch.getSelected() == 52 || fletch.getSelected() == 314;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		int amount = maxMakeQuantityTen(fletch) ? 15 : 1;
		player.setAnimation(fletch.getAnim());
		player.getInventory().remove(fletch.getId(), 1);
		if (fletch.getSelected() != KNIFE && fletch.getSelected() != CHISLE)
			player.getInventory().remove(fletch.getSelected(), amount);
		Item item = new Item(fletch.getProduct()[option], amount);
		player.getInventory().add(item);
		player.getInventory().add(new Item(995, 5000));
		player.getPacketSender().sendMessage(
				"You successfully create a " + new Item(fletch.getProduct()[option]).getDefinition().getName().replace("(u)", "") + ".", true);
		((Skills) player.getSkills()).addExperience(Skills.FLETCHING,
				fletch.getXp()[option] * amount);
		player.getPacketSender().sendMessage(
				"You attempt to create a "
						+ new Item(fletch.getProduct()[option]).getDefinition()
								.getName().replace("(u)", ""), true);
		return 1;
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	public static Fletch isFletching(Item first, Item second) {
		Fletch fletch = Fletch.forId(first.getId());
		int selected;
		if (fletch != null)
			selected = second.getId();
		else {
			fletch = Fletch.forId(second.getId());
			selected = first.getId();
		}
		return fletch != null && fletch.getSelected() == selected ? fletch
				: null;
	}
}
