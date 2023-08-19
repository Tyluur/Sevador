package com.sevador.game.node.model.skills.mining;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.gameobject.ObjectBuilder;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.mining.MiningData.RockDefinitions;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;
import com.sevador.utility.Priority;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Mining extends SkillAction {

	private GameObject rock;
	private RockDefinitions definitions;

	private int emoteId;
	private int pickaxeTime;

	public Mining(GameObject rock, RockDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	public Mining(GameObject rock) {

	}

	private int getMiningDelay(Player player) {
		int summoningBonus = 0;
		if (player.getFamiliar() != null)
			if (player.getFamiliar().getId() == 7342
					|| player.getFamiliar().getId() == 7342)
				summoningBonus += 10;
			else if (player.getFamiliar().getId() == 6832
					|| player.getFamiliar().getId() == 6831)
				summoningBonus++;
		int mineTimer = definitions.getOreBaseTime()
				- (player.getSkills().getLevel(14) + summoningBonus)
				- Misc.random(pickaxeTime);
		if (mineTimer < 1 + definitions.getOreRandomTime())
			mineTimer = 1 + Misc.random(definitions.getOreRandomTime());
		return mineTimer;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPacketSender().sendMessage("You swing your pickaxe at the rock.");
		setActionDelay(player, getMiningDelay(player));
		rock.setLife(definitions.getRandomLifeProbability());
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.getUpdateMasks().register(
				new Animation(emoteId, 0, false, Priority.HIGHEST));
		return checkAll(player);
	}

	private void addOre(Player player) {
		if (!checkAll(player)) {
			player.getUpdateMasks().register(new Animation(-1, 0, false));
			return;
		}
		double xpBoost = 0;
		int idSome = 0;
		if (definitions == RockDefinitions.Granite_Ore) {
			idSome = Misc.random(2) * 2;
			if (idSome == 2)
				xpBoost += 10;
			else if (idSome == 4)
				xpBoost += 25;
		} else if (definitions == RockDefinitions.Sandstone_Ore) {
			idSome = Misc.random(3) * 2;
			xpBoost += idSome / 2 * 10;
		} else if (player.getFamiliar() != null
				&& (player.getFamiliar().getId() == 7342 || player
						.getFamiliar().getId() == 7342))
			xpBoost += 40;
		((Skills) player.getSkills()).addExperience(Skills.MINING,
				definitions.getXp() + xpBoost);
		if (definitions.getOreId() != -1) {
			player.getInventory().add(new Item(definitions.getOreId() + idSome, 1));
			player.getInventory().add(new Item(995, 5000));
			String oreName = ItemDefinition
					.forId(definitions.getOreId() + idSome).getName()
					.toLowerCase();
			player.getPacketSender().sendMessage("You mine some " + oreName + ".");
		}
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		rock.decrementObjectLife();
		if (rock.getLife() == 0) {
			ObjectBuilder.replace(rock, new GameObject(
					definitions.getEmptyId(), rock.getLocation()), definitions
					.getRespawnDelay() * 600);
			player.getUpdateMasks().register(
					new Animation(-1, 0, false, Priority.HIGHEST));
			return -1;
		}
		/*
		 * if (!player.getInventory().hasFreeSlots() && definitions.getOreId()
		 * != -1) { player.getUpdateMasks().register( new Animation(-1, 0,
		 * false, Priority.HIGHEST));
		 * player.getPacketSender().sendMessage("Not enough space in your inventory."); return -1;
		 * }
		 */
		return getMiningDelay(player);
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	private boolean checkAll(Player player) {
		if (!hasPickaxe(player)) {
			player.getPacketSender().sendMessage("You need a pickaxe to mine this rock.");
			return false;
		}
		if (!setPickaxe(player)) {
			player.getPacketSender().sendMessage("You dont have the required level to use this pickaxe.");
			return false;
		}
		if (player.getInventory().freeSlots() == 0) {
			player.getPacketSender().sendMessage("Not enough space in your inventory.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPacketSender().sendMessage("You need a mining level of "
					+ definitions.getLevel() + " to mine this rock.");
			return false;
		}
		return true;
	}

	private boolean hasPickaxe(Player player) {
		int[] picks = { 15259, 1275, 1271, 1273, 1269, 1267, 1265, 13661 };
		for (int pick : picks)
			if (player.getInventory().contains(pick, 1))
				return true;
		int weaponId = player.getEquipment().get(Constants.SLOT_WEAPON) == null ? -1
				: player.getEquipment().get(Constants.SLOT_WEAPON).getId();
		if (weaponId == -1)
			return false;
		switch (weaponId) {
		case 1265:// Bronze PickAxe
		case 1267:// Iron PickAxe
		case 1269:// Steel PickAxe
		case 1273:// Mithril PickAxe
		case 1271:// Adamant PickAxe
		case 1275:// Rune PickAxe
		case 15259:// Dragon PickAxe
		case 13661: // Inferno adze
			return true;
		default:
			return false;
		}
	}

	private boolean setPickaxe(Player player) {
		int level = player.getSkills().getLevel(Skills.MINING);
		int weaponId = player.getEquipment().get(Constants.SLOT_WEAPON) == null ? -1
				: player.getEquipment().get(Constants.SLOT_WEAPON).getId();
		if (weaponId != -1) {
			switch (weaponId) {
			case 15259: // dragon pickaxe
				if (level >= 61) {
					emoteId = 12190;
					pickaxeTime = 13;
					return true;
				}
				break;
			case 1275: // rune pickaxe
				if (level >= 41) {
					emoteId = 624;
					pickaxeTime = 10;
					return true;
				}
				break;
			case 1271: // adam pickaxe
				if (level >= 31) {
					emoteId = 628;
					pickaxeTime = 7;
					return true;
				}
				break;
			case 1273: // mith pickaxe
				if (level >= 21) {
					emoteId = 629;
					pickaxeTime = 5;
					return true;
				}
				break;
			case 1269: // steel pickaxe
				if (level >= 6) {
					emoteId = 627;
					pickaxeTime = 3;
					return true;
				}
				break;
			case 1267: // iron pickaxe
				emoteId = 626;
				pickaxeTime = 2;
				return true;
			case 1265: // bronze axe
				emoteId = 625;
				pickaxeTime = 1;
				return true;
			case 13661: // Inferno adze
				if (level >= 61) {
					emoteId = 10222;
					pickaxeTime = 13;
					return true;
				}
				break;
			}
		}
		if (player.getInventory().contains(15259, 1)) {
			if (level >= 61) {
				emoteId = 12190;
				pickaxeTime = 13;
				return true;
			}
		}
		if (player.getInventory().contains(1275, 1)) {
			if (level >= 41) {
				emoteId = 624;
				pickaxeTime = 10;
				return true;
			}
		}
		if (player.getInventory().contains(1271, 1)) {
			if (level >= 31) {
				emoteId = 628;
				pickaxeTime = 7;
				return true;
			}
		}
		if (player.getInventory().contains(1273, 1)) {
			if (level >= 21) {
				emoteId = 629;
				pickaxeTime = 5;
				return true;
			}
		}
		if (player.getInventory().contains(1269, 1)) {
			if (level >= 6) {
				emoteId = 627;
				pickaxeTime = 3;
				return true;
			}
		}
		if (player.getInventory().contains(1267, 1)) {
			emoteId = 626;
			pickaxeTime = 2;
			return true;
		}
		if (player.getInventory().contains(1265, 1)) {
			emoteId = 625;
			pickaxeTime = 1;
			return true;
		}
		if (player.getInventory().contains(13661, 1)) {
			if (level >= 61) {
				emoteId = 10222;
				pickaxeTime = 13;
				return true;
			}
		}
		return false;

	}
}
