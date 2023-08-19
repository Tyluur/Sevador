package com.sevador.game.node.model.skills.woodcutting;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.gameobject.ObjectBuilder;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.woodcutting.TreeData.TreeDefinitions;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Woodcutting extends SkillAction {

	private GameObject tree;
	private TreeDefinitions definitions;

	private int emoteId;
	private boolean usingBeaver = false;
	private int axeTime;

	public Woodcutting(GameObject tree, TreeDefinitions definitions) {
		this.tree = tree;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPacketSender().sendMessage(usingBeaver ? "Your beaver uses its strong teeth to chop down the tree..."
				: "You swing your hatchet at the "
				+ (TreeDefinitions.IVY == definitions ? "ivy" : "tree")
				+ "...");
		setActionDelay(player, getWoodcuttingDelay(player));
		return true;
	}

	private int getWoodcuttingDelay(Player player) {
		int summoningBonus = player.getFamiliar() != null ? (player
				.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807) ? 10
						: 0
						: 0;
		int wcTimer = definitions.getLogBaseTime()
				- (player.getSkills().getLevel(8) + summoningBonus)
				- Misc.random(axeTime);
		if (wcTimer < 1 + definitions.getLogRandomTime())
			wcTimer = 1 + Misc.random(definitions.getLogRandomTime());
		wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();
		return wcTimer;
	}

	private boolean checkAll(Player player) {
		if (!hasAxe(player)) {
			player.getPacketSender().sendMessage("You need a hatchet to chop down this tree.");
			return false;
		}
		if (!setAxe(player)) {
			player.getPacketSender().sendMessage("You dont have the required level to use that axe.");
			return false;
		}
		if (!hasWoodcuttingLevel(player))
			return false;
		if (player.getInventory().freeSlots() < 2) {
			player.getPacketSender().sendMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean usedDeplateAurora;
	
	@Override
	public int processWithDelay(Player player) {
		addLog(player);
		if (!usedDeplateAurora
				&& (1 + Math.random()) < player.getAuraManager()
				.getChanceNotDepleteMN_WC()) {
			usedDeplateAurora = true;
		}	else if (Misc.random(definitions.getRandomLifeProbability()) == 0) {
			long time = definitions.getRespawnDelay();
			ObjectBuilder.replace(tree, new GameObject(
					definitions.getStumpId(), tree.getLocation()), (int) time);
			player.setAnimation(new Animation(-1));
			return -1;
		}
		if (player.getInventory().freeSlots() < 2) {
			player.setAnimation(new Animation(-1));
			player.getPacketSender().sendMessage(
					"Not enough space in your inventory.");
			return -1;
		}
		return getWoodcuttingDelay(player);
	}

	private void addLog(Player player) {
		double xpBoost = 1.00;
		((Skills) player.getSkills()).addExperience(8, definitions.getXp()
				* xpBoost);
		player.getInventory().add(new Item(definitions.getLogsId(), 1));
		player.getInventory().add(new Item(995, 5000));
		if (definitions == TreeDefinitions.IVY) {
			player.getPacketSender().sendMessage(
					"You succesfully cut an ivy vine.");
		} else {
			String logName = ItemDefinition.forId(definitions.getLogsId())
					.getName().toLowerCase();
			player.getPacketSender().sendMessage(
					"You get some " + logName + ".");
		}
	}

	@Override
	public void stop(Player player) {

	}

	private boolean hasWoodcuttingLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.getPacketSender().sendMessage(
					"You need a woodcutting level of " + definitions.getLevel()
					+ " to chop down this tree.");
			return false;
		}
		return true;
	}

	private boolean setAxe(Player player) {
		int level = player.getSkills().getLevel(8);
		int weaponId = player.getEquipment().get(Constants.SLOT_WEAPON) == null ? -1
				: player.getEquipment().get(Constants.SLOT_WEAPON).getId();
		if (weaponId != -1) {
			switch (weaponId) {
			case 6739: // dragon axe
				if (level >= 61) {
					emoteId = 2846;
					axeTime = 13;
					return true;
				}
				break;
			case 1359: // rune axe
				if (level >= 41) {
					emoteId = 867;
					axeTime = 10;
					return true;
				}
				break;
			case 1357: // adam axe
				if (level >= 31) {
					emoteId = 869;
					axeTime = 7;
					return true;
				}
				break;
			case 1355: // mit axe
				if (level >= 21) {
					emoteId = 871;
					axeTime = 5;
					return true;
				}
				break;
			case 1361: // black axe
				if (level >= 11) {
					emoteId = 873;
					axeTime = 4;
					return true;
				}
				break;
			case 1353: // steel axe
				if (level >= 6) {
					emoteId = 875;
					axeTime = 3;
					return true;
				}
				break;
			case 1349: // iron axe
				emoteId = 877;
				axeTime = 2;
				return true;
			case 1351: // bronze axe
				emoteId = 879;
				axeTime = 1;
				return true;
			case 13661: // Inferno adze
				if (level >= 61) {
					emoteId = 10251;
					axeTime = 13;
					return true;
				}
				break;
			}
		}
		if (player.getInventory().contains(6739, 1)) {
			if (level >= 61) {
				emoteId = 2846;
				axeTime = 13;
				return true;
			}
		}
		if (player.getInventory().contains(1359, 1)) {
			if (level >= 41) {
				emoteId = 867;
				axeTime = 10;
				return true;
			}
		}
		if (player.getInventory().contains(1357, 1)) {
			if (level >= 31) {
				emoteId = 869;
				axeTime = 7;
				return true;
			}
		}
		if (player.getInventory().contains(1355, 1)) {
			if (level >= 21) {
				emoteId = 871;
				axeTime = 5;
				return true;
			}
		}
		if (player.getInventory().contains(1361, 1)) {
			if (level >= 11) {
				emoteId = 873;
				axeTime = 4;
				return true;
			}
		}
		if (player.getInventory().contains(1353, 1)) {
			if (level >= 6) {
				emoteId = 875;
				axeTime = 3;
				return true;
			}
		}
		if (player.getInventory().contains(1349, 1)) {
			emoteId = 877;
			axeTime = 2;
			return true;
		}
		if (player.getInventory().contains(1351, 1)) {
			emoteId = 879;
			axeTime = 1;
			return true;
		}
		if (player.getInventory().contains(13661, 1)) {
			if (level >= 61) {
				emoteId = 10251;
				axeTime = 13;
				return true;
			}
		}
		return false;

	}

	private boolean hasAxe(Player player) {
		int[] axes = { 1351, 1349, 1353, 1355, 1357, 1361, 1359, 6739, 13661 };
		for (int pick : axes)
			if (player.getInventory().contains(pick, 1))
				return true;
		int weaponId = player.getEquipment().get(Constants.SLOT_WEAPON) == null ? -1
				: player.getEquipment().get(Constants.SLOT_WEAPON).getId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1351:// Bronze Axe
		case 1349:// Iron Axe
		case 1353:// Steel Axe
		case 1361:// Black Axe
		case 1355:// Mithril Axe
		case 1357:// Adamant Axe
		case 1359:// Rune Axe
		case 6739:// Dragon Axe
		case 13661: // Inferno adze
			return true;
		default:
			return false;
		}

	}

	@Override
	public boolean process(Player player) {
		player.getUpdateMasks().register(
				new Animation(usingBeaver ? 1 : emoteId));
		return checkAll(player);
	}

}
