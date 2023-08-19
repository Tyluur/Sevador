package com.sevador.game.node.model.skills.smithing;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.smithing.SmithingData.ForgingBar;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.ItemOnInterfacePacket;
import com.sevador.network.out.SendSprite;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Misc;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Smithing extends SkillAction {
	private static int HAMMER = 2347;
	private static int SMITHING_INTERFACE = 300;
	private ForgingBar bar;
	private int index;
	private int ticks;

	public Smithing(int ticks, int index) {
		this.index = index;
		this.ticks = ticks;
	}

	@Override
	public boolean process(Player player) {
		if (!player.getInventory().contains(HAMMER, 1)) {
			Item item = new Item(bar.getBarId(), 1);
			if (item.getDefinition() == null) item.setDefinition(ItemDefinition.forId(item.getId()));
			player.getDialogueManager().startDialogue("SimpleMessage","You need a hammer in order to work with a bar of "+ item.getDefinition().getName().toLowerCase().replace(" bar", "") + ".");
			return false;
		}
		if (!player.getInventory().contains(bar.getBarId(),ForgingInterface.getActualAmount(bar.getLevel()+ ForgingInterface.getFixedAmount(bar,bar.getItems()[index]), bar, bar.getItems()[index].getId()))) {
			player.getDialogueManager().startDialogue("SimpleMessage",
					"You do not have sufficient bars!");
			return false;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < ForgingInterface
				.getLevels(index, player)) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a Smithing level of "
							+ ForgingInterface.getLevels(index, player)
							+ " to create this.");
			return false;
		}
		player.getPacketSender().sendCloseInterface().setDefaultInventory();
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		ticks--;
		player.setAnimation(new Animation(898));
		player.getInventory().remove(
				bar.getBarId(),
				ForgingInterface.getActualAmount(
						bar.getLevel()
						+ ForgingInterface.getFixedAmount(bar,
								bar.getItems()[index]), bar,
								bar.getItems()[index].getId()));
		player.getInventory()
		.addItem(
				bar.getItems()[index].getId(),
				ForgingInterface.getForgedAmount(bar.getItems()[index]
						.getId()));
		((Skills) player.getSkills()).addExperience(Skills.SMITHING, getExperience(player));
		if (ticks > 0) {
			return 3;
		}
		return -1;
	}

	private double getExperience(Player player) {
		int levelRequired = bar.getLevel()
				+ ForgingInterface.getFixedAmount(bar, bar.getItems()[index]);
		int barAmount = ForgingInterface.getActualAmount(levelRequired, bar,
				bar.getItems()[index].getId());
		return bar.getExperience()[barAmount == 5 ? 3 : barAmount - 1];
	}

	@Override
	public boolean start(Player player) {
		if ((bar = ForgingBar.forId(((Integer) player.getTemporaryAttributtes()
				.get("itemUsed")))) == null) {
			return false;
		}
		if (!player.getInventory().contains(HAMMER, 1)) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a hammer in order to work with a bar of "
							+ new Item(bar.getBarId(), 1).getDefinition()
							.getName().replace("Bar ", "") + ".");
			return false;
		}
		if (player.getSkills().getLevel(Skills.SMITHING) < ForgingInterface
				.getLevels(index, player)) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a Smithing level of "
							+ ForgingInterface.getLevels(index, player)
							+ " to create this.");
			return false;
		}
		return true;
	}

	@Override
	public void stop(Player player) {
		this.setActionDelay(player, 3);
	}

	public static class ForgingInterface {

		public static final int componentChilds[] = new int[30];
		public static final int CLICKED_CHILDS[] = { 28, -1, 5, 1 };

		public static void handleIComponents(Player player, int componentId) {
			int slot = -1;
			int ticks = -1;
			for (int i = 3; i <= 6; i++) {
				for (int index = 0; index < componentChilds.length; index++) {
					if (componentChilds[index] + i != componentId)
						continue;
					slot = index;
					ticks = CLICKED_CHILDS[i - 3];
					break;
				}
			}
			player.getSkillAction().setSkill(new Smithing(ticks, slot));
		}

		private static void calculateComponentConfigurations() {
			int base = 18;
			for (int i = 0; i < componentChilds.length; i++) {
				if (base == 250) {
					base = 267;
				}
				componentChilds[i] = base;
				base += 8;
			}
		}

		private static int getBasedAmount(Item item) {
			String def = item.getDefinition().getName();
			if (def.contains("dagger")) {
				return 1;
			} else if (def.contains("hatchet") || def.contains("mace")
					|| def.contains("iron spit")) {
				return 2;
			} else if (def.contains("bolts") || def.contains("med helm")) {
				return 3;
			} else if (def.contains("sword") || def.contains("dart tip")
					|| def.contains("nails") || def.contains("wire")) {
				return 4;
			} else if (def.contains("arrow") || def.contains("pickaxe")
					|| def.contains("scimitar")) {
				return 5;
			} else if (def.contains("longsword") || def.contains("limbs")) {
				return 6;
			} else if (def.contains("knife") || def.contains("full helm")
					|| def.contains("studs")) {
				return 7;
			} else if (def.contains("sq shield") || def.contains("warhammer")
					|| def.contains("grapple tip")) {
				return 9;
			} else if (def.contains("battleaxe")) {
				return 10;
			} else if (def.contains("chainbody") || def.contains("oil lantern")) {
				return 11;
			} else if (def.contains("kiteshield")) {
				return 12;
			} else if (def.contains("claws")) {
				return 13;
			} else if (def.contains("2h sword")) {
				return 14;
			} else if (def.contains("plateskirt") || def.contains("platelegs")) {
				return 16;
			} else if (def.contains("platebody")) {
				return 18;
			} else if (def.contains("bullseye lantern")) {
				return 19;
			}
			return 1;
		}

		private static int getFixedAmount(ForgingBar bar, Item item) {
			String name = item.getDefinition().getName();
			int increment = getBasedAmount(item);
			if (name.contains("dagger") && bar != ForgingBar.BRONZE) {
				increment--;
			} else if (name.contains("hatchet") && bar == ForgingBar.BRONZE) {
				increment--;
			}
			return increment;
		}

		public static int getForgedAmount(int id) {
			String name = ItemDefinition.forId(id).getName();
			if (name.contains("knife")) {
				return 5;
			} else if (name.contains("bolts") || name.contains("dart tip")) {
				return 10;
			} else if (name.contains("arrowtips") || name.contains("nails")) {
				return 15;
			}
			return 1;
		}

		public static String[] getStrings(Player player, ForgingBar bar,
				int index, int itemId) {
			if (itemId == -1 || index < 0 || index >= bar.getItems().length) {
				return null;
			}
			StringBuilder barName = new StringBuilder();
			StringBuilder levelString = new StringBuilder();
			String name = ItemDefinition.forId(itemId).getName()
					.toLowerCase();
			String barVariableName = bar.toString().toLowerCase();
			int levelRequired = bar.getLevel()
					+ getFixedAmount(bar, bar.getItems()[index]);
			int barAmount = getActualAmount(levelRequired, bar, itemId);
			if (player.getInventory().getAmount(new Item(bar.getBarId())) >= barAmount) {
				barName.append("<col=00FF00>");
			}
			barName.append(barAmount).append(" ").append(barAmount > 1 ? "bars" : "bar");
			if (levelRequired >= 99) {
				levelRequired = 99;
			}
			if (player.getSkills().getLevel(Skills.SMITHING) >= levelRequired) {
				levelString.append("<col=FFFFFF>");
			}
			levelString.append(Misc.formatPlayerNameForDisplay(name.replace(barVariableName + " ", "")));
			return new String[] { levelString.toString(), barName.toString() };
		}

		public static int getLevels(int slot, Player player) {
			ForgingBar bar = ForgingBar.forId((Integer) player
					.getTemporaryAttributtes().get("itemUsed"));
			int base = bar.getLevel();
			int barAmount = getFixedAmount(bar, bar.getItems()[slot]);
			int level = base + barAmount;
			if (level > 99) {
				level = 99;
			}
			return level;
		}

		private static void sendComponentConfigs(Player player, ForgingBar bar) {
			for (int i : bar.getComponentChilds())
				player.getIOSession().write(new InterfaceConfig(player, SMITHING_INTERFACE, i -1, false));
		}

		public static int getActualAmount(int levelRequired, ForgingBar bar,
				int id) {
			if (levelRequired >= 99) {
				levelRequired = 99;
			}
			int level = levelRequired - bar.getLevel();
			String name = ItemDefinition.forId(id).getName().toLowerCase();
			if (level >= 0 && level <= 4) {
				return 1;
			} else if (level >= 4 && level <= 8) {
				if (name.contains("knife") || name.contains("arrowtips")
						|| name.contains("limb") || name.contains("studs")) {
					return 1;
				}
				return 2;
			} else if (level >= 9 && level <= 16) {
				if (name.contains("grapple")) {
					return 1;
				} else if (name.contains("claws")) {
					return 2;
				}
				return 3;
			} else if (level >= 17) {
				if (name.contains("bullseye")) {
					return 1;
				}
				return 5;
			}
			return 1;
		}

		public static void sendSmithingInterface(Player player, ForgingBar bar) {
			calculateComponentConfigurations();
			sendComponentConfigs(player, bar);
			for (int i = 0; i < bar.getItems().length; i++) {
				player.getIOSession().write(new ItemOnInterfacePacket(player, SMITHING_INTERFACE, componentChilds[i], 1, 	bar.getItems()[i].getId() ));
				player.getIOSession().write(new SendSprite(player, SMITHING_INTERFACE, componentChilds[i], bar.getItems()[i].getId()));
				String[] name = getStrings(player, bar, i,
						bar.getItems()[i].getId());
				if (name != null) {
					player.getIOSession().write(new StringPacket(player, name[0], 300, componentChilds[i] + 1));
					player.getIOSession().write(new StringPacket(player, name[1], 300, componentChilds[i] + 2));
				}
			}
			player.getIOSession().write(new StringPacket(player, Misc.formatPlayerNameForDisplay(bar.toString().toLowerCase()), 300, 14));
			player.getPacketSender().sendInterface(SMITHING_INTERFACE);
		}
	}
}
