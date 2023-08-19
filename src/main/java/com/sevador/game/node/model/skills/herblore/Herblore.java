package com.sevador.game.node.model.skills.herblore;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.herblore.HerbloreData.Ingredients;
import com.sevador.game.node.model.skills.herblore.HerbloreData.RawIngredient;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Herblore extends SkillAction {

	public static final short VIAL = 227;
	public static final short CUP_OF_HOT_WATER = 4460;
	public static final short COCONUT_MILK = 5935;
	public static final short PESTLE_AND_MORTAR = 233;
	public static final short SWAMP_TAR = 1939;
	private Item node;
	private Item otherItem;
	private Ingredients ingredients;
	private RawIngredient rawIngredient;
	private int ticks;
	private byte slot;

	public Herblore(Item node, Item otherNode, int amount) {
		this.otherItem = otherNode;
		this.ticks = amount;
		if (node.getId() == PESTLE_AND_MORTAR
				|| otherNode.getId() == PESTLE_AND_MORTAR) {
			this.rawIngredient = RawIngredient.forId(node.getId());
			if (rawIngredient == null) {
				rawIngredient = RawIngredient.forId(otherItem.getId());
				this.node = otherNode;
				this.otherItem = node;
			}
		} else {
			this.ingredients = Ingredients.forId(node.getId());
			if (ingredients == null) {
				ingredients = Ingredients.forId(otherItem.getId());
				this.node = otherNode;
				this.otherItem = node;
			}
		}
	}

	@Override
	public boolean start(Player player) {
		if (player == null || node == null) {
			return false;
		}
		if ((ingredients == null && rawIngredient == null) || otherItem == null) {
			return false;
		}
		if (ingredients != null) {
			this.slot = ingredients.getSlot(otherItem.getId());
			if (player.getSkills().getLevel(Skills.HERBLORE) < ingredients
					.getLevels()[slot]) {
				player.getDialogueManager().startDialogue(
						"SimpleMessage",
						"You need a herblore level of "
								+ ingredients.getLevels()[slot]
								+ " to combine these ingredients.");
				return false;
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (ingredients == Ingredients.TORSTOL && otherItem.getId() != VIAL) {
			if (!player.getInventory().containsOneItem(15309)
					|| !player.getInventory().containsOneItem(15313)
					|| !player.getInventory().containsOneItem(15317)
					|| !player.getInventory().containsOneItem(15321)
					|| !player.getInventory().containsOneItem(15325)) {
				stop(player);
				return false;
			}
		}
		if (!player.getInventory().contains(node.getId(), node.getAmount())
				|| !player.getInventory().contains(otherItem.getId(),
						otherItem.getAmount())) {
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (node.getId() == PESTLE_AND_MORTAR
				|| otherItem.getId() == PESTLE_AND_MORTAR) {
			player.setAnimation(new Animation(364));
		} else {
			player.setAnimation(new Animation(363));
		}
		ticks--;
		if (otherItem.getId() == VIAL || otherItem.getId() == COCONUT_MILK
				|| node.getId() == VIAL || node.getId() == COCONUT_MILK) {
			player.getPacketSender().sendMessage(
					"You add the "
							+ node.getDefinition().getName().toLowerCase()
									.replace("clean", "")
							+ " into the vial of "
							+ (otherItem.getId() == VIAL ? "water." : "milk."));
		} else if (otherItem.getId() == SWAMP_TAR || node.getId() == SWAMP_TAR) {
			player.getPacketSender().sendMessage(
					"You add the "
							+ node.getDefinition().getName().toLowerCase()
									.replace("clean ", "")
							+ " on the swamp tar.", true);
		} else if (otherItem.getId() == PESTLE_AND_MORTAR
				|| node.getId() == PESTLE_AND_MORTAR) {
			player.getPacketSender().sendMessage(
					"You crush the "
							+ node.getDefinition().getName().toLowerCase()
							+ " with your pestle and mortar.", true);
		} else if (ingredients == Ingredients.TORSTOL
				&& otherItem.getId() != VIAL) {
			player.getPacketSender()
					.sendMessage(
							"You combine the torstol with the potions and get an overload.");
			 player.getInventory().remove(new Item(node.getId()),
					new Item(15309), new Item(15313), new Item(15317),
					new Item(15321), new Item(15325));
				player.getInventory().add(new Item(995, 5000));
				player.getInventory().add(new Item(ingredients.getRewards()[slot], 1));
				((Skills) player.getSkills()).addExperience(Skills.HERBLORE, ingredients.getExperience()[slot]);
		} else {
			player.getPacketSender().sendMessage(
					"You mix the "
							+ node.getDefinition().getName().toLowerCase()
							+ " into your potion.", true);
		}
		player.getInventory().remove(new Item(node.getId(), 1),
				rawIngredient == null ? new Item(otherItem.getId(), 1) : null);
		player.getInventory().add(rawIngredient != null ? rawIngredient.getCrushedItem(): new Item(ingredients.getRewards()[slot], 1));
		player.getInventory().add(new Item(995, 5000));
		((Skills) player.getSkills()).addExperience(Skills.HERBLORE,rawIngredient != null ? 0 : ingredients.getExperience()[slot]);
		if (ticks > 0) {
			return 1;
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
		this.setActionDelay(player, 3);
	}

	public static int isHerbloreSkill(Item first, Item other) {
		Item swap = first;
		Ingredients ingredient = Ingredients.forId(first.getId());
		if (ingredient == null) {
			ingredient = Ingredients.forId(other.getId());
			first = other;
			other = swap;
		}
		if (ingredient != null) {
			int slot = ingredient.getSlot(other.getId());
			return slot > -1 ? ingredient.getRewards()[slot] : -1;
		}
		swap = first;
		RawIngredient raw = RawIngredient.forId(first.getId());
		if (raw == null) {
			raw = RawIngredient.forId(other.getId());
			first = other;
			other = swap;
		}
		if (raw != null) {
			return other.getId() == PESTLE_AND_MORTAR ? raw.getCrushedItem()
					.getId() : -1;
		}
		return -1;
	}

}
