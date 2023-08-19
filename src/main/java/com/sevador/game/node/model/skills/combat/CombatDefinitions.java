package com.sevador.game.node.model.skills.combat;

import java.io.Serializable;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.InterfacePacket;
import com.sevador.utility.Constants;
import com.sevador.utility.ItemBonuses;

public final class CombatDefinitions implements Serializable {

	private static final long serialVersionUID = 2102201264836121104L;
	public static final int STAB_ATTACK = 0, SLASH_ATTACK = 1,
			CRUSH_ATTACK = 2, RANGE_ATTACK = 4, MAGIC_ATTACK = 3;
	public static final int STAB_DEF = 5, SLASH_DEF = 6, CRUSH_DEF = 7,
			RANGE_DEF = 9, MAGIC_DEF = 8, SUMMONING_DEF = 10;
	public static final int STRENGTH_BONUS = 14, RANGED_STR_BONUS = 15,
			MAGIC_DAMAGE = 17, PRAYER_BONUS = 16;
	public static final int ABSORVE_MELEE_BONUS = 11, ABSORVE_RANGE_BONUS = 13,
			ABSORVE_MAGE_BONUS = 12;

	public static final int SHARED = -1;
	private transient Player player;
	private transient boolean usingSpecialAttack;
	private transient int[] bonuses;

	// saving stuff

	private byte attackStyle;
	private byte specialAttackPercentage;
	private boolean autoRelatie;
	private byte sortSpellBook;
	private boolean showCombatSpells;
	private boolean showSkillSpells;
	private boolean showMiscallaneousSpells;
	private boolean showTeleportSpells;
	private boolean defensiveCasting;
	private transient boolean dungeonneringSpellBook;
	private byte spellBook;
	private byte autoCastSpell;

	public int getSpellId() {
		Integer tempCastSpell = (Integer) player.getTemporaryAttributtes().get(
				"tempCastSpell");
		if (tempCastSpell != null)
			return tempCastSpell + 256;
		return autoCastSpell;
	}

	public int getAutoCastSpell() {
		return autoCastSpell;
	}

	public void resetSpells(boolean removeAutoSpell) {
		player.getTemporaryAttributtes().remove("tempCastSpell");
		if (removeAutoSpell) {
			setAutoCastSpell(0);
			refreshAutoCastSpell();
		}
	}

	public void setAutoCastSpell(int id) {
		autoCastSpell = (byte) id;
		refreshAutoCastSpell();
	}

	public void refreshAutoCastSpell() {
		refreshAttackStyle();
		player.getIOSession().write(
				new ConfigPacket(player, 108, getSpellAutoCastConfigValue()));
	}

	public int getSpellAutoCastConfigValue() {
		if (dungeonneringSpellBook)
			return 0;
		if (spellBook == 0) {
			switch (autoCastSpell) {
			case 25:
				return 3;
			case 28:
				return 5;
			case 30:
				return 7;
			case 32:
				return 9;
			case 34:
				return 11; // air bolt
			case 39:
				return 13;// water bolt
			case 42:
				return 15;// earth bolt
			case 45:
				return 17; // fire bolt
			case 49:
				return 19;// air blast
			case 52:
				return 21;// water blast
			case 58:
				return 23;// earth blast
			case 63:
				return 25;// fire blast
			case 66: // Saradomin Strike
				return 41;
			case 67:// Claws of Guthix
				return 39;
			case 68:// Flames of Zammorak
				return 43;
			case 70:
				return 27;// air wave
			case 73:
				return 29;// water wave
			case 77:
				return 31;// earth wave
			case 80:
				return 33;// fire wave
			case 84:
				return 47;
			case 87:
				return 49;
			case 89:
				return 51;
			case 91:
				return 53;
			case 99:
				return 145;
			default:
				return 0;
			}
		} else if (spellBook == 1) {
			switch (autoCastSpell) {
			case 28:
				return 63;
			case 32:
				return 65;
			case 24:
				return 67;
			case 20:
				return 69;
			case 30:
				return 71;
			case 34:
				return 73;
			case 26:
				return 75;
			case 22:
				return 77;
			case 29:
				return 79;
			case 33:
				return 81;
			case 25:
				return 83;
			case 21:
				return 85;
			case 31:
				return 87;
			case 35:
				return 89;
			case 27:
				return 91;
			case 23:
				return 93;
			default:
				return 0;
			}
		} else {
			return 0;
		}
	}

	public CombatDefinitions() {
		specialAttackPercentage = 100;
		autoRelatie = true;
		showCombatSpells = true;
		showSkillSpells = true;
		showMiscallaneousSpells = true;
		showTeleportSpells = true;
	}

	public void refreshSpellBookScrollBar_DefCast() {
		player.getIOSession().write(
				new ConfigPacket(player, 439,
						(dungeonneringSpellBook ? 3 : spellBook)
								+ (defensiveCasting ? 0 : 1 << 8)));
	}

	public int getSpellBook() {
		if (dungeonneringSpellBook)
			return 950; // dung book
		else {
			if (spellBook == 0)
				return 192; // normal
			else if (spellBook == 1)
				return 193; // ancients
			else
				return 430; // lunar
		}

	}

	public void switchShowCombatSpells() {
		showCombatSpells = !showCombatSpells;
		refreshSpellBook();
	}

	public void switchShowSkillSpells() {
		showSkillSpells = !showSkillSpells;
		refreshSpellBook();
	}

	public void switchShowMiscallaneousSpells() {
		showMiscallaneousSpells = !showMiscallaneousSpells;
		refreshSpellBook();
	}

	public void switchShowTeleportSkillSpells() {
		showTeleportSpells = !showTeleportSpells;
		refreshSpellBook();
	}

	public void switchDefensiveCasting() {
		defensiveCasting = !defensiveCasting;
		refreshSpellBookScrollBar_DefCast();
	}

	public void setSortSpellBook(int sortId) {
		this.sortSpellBook = (byte) sortId;
		refreshSpellBook();
	}

	public boolean isDefensiveCasting() {
		return defensiveCasting;
	}

	public void refreshSpellBook() {
		if (spellBook == 0) {
			player.getIOSession().write(
					new ConfigPacket(player, 1376, sortSpellBook
							| (showCombatSpells ? 0 : 1 << 9)
							| (showSkillSpells ? 0 : 1 << 10)
							| (showMiscallaneousSpells ? 0 : 1 << 11)
							| (showTeleportSpells ? 0 : 1 << 12)));
		} else if (spellBook == 1) {
			player.getIOSession().write(
					new ConfigPacket(player, 1376, sortSpellBook << 3
							| (showCombatSpells ? 0 : 1 << 16)
							| (showTeleportSpells ? 0 : 1 << 17)));
		} else if (spellBook == 2) {
			player.getIOSession().write(
					new ConfigPacket(player, 1376, sortSpellBook << 6
							| (showCombatSpells ? 0 : 1 << 13)
							| (showMiscallaneousSpells ? 0 : 1 << 14)
							| (showTeleportSpells ? 0 : 1 << 15)));
		}
	}

	public static final int getMeleeDefenceBonus(int bonusId) {
		if (bonusId == STAB_ATTACK)
			return STAB_DEF;
		if (bonusId == SLASH_DEF)
			return SLASH_DEF;
		return CRUSH_DEF;
	}

	public static final int getMeleeBonusStyle(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			String weaponName = ItemDefinition.forId(weaponId)
					.getName().toLowerCase();
			if (weaponName.contains("whip"))
				return SLASH_ATTACK;
			if (weaponName.contains("staff of light")) {
				switch (attackStyle) {
				case 0:
					return STAB_ATTACK;
				case 1:
					return SLASH_ATTACK;
				default:
					return CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("staff")
					|| weaponName.contains("granite mace")
					|| weaponName.contains("warhammer")
					|| weaponName.contains("tzhaar-ket-em")
					|| weaponName.contains("tzhaar-ket-om")
					|| weaponName.contains("maul"))
				return CRUSH_ATTACK;
			if (weaponName.contains("scimitar")
					|| weaponName.contains("korasi's sword")
					|| weaponName.contains("hatchet")
					|| weaponName.contains("claws")
					|| weaponName.contains("longsword")) {
				switch (attackStyle) {
				case 2:
					return STAB_ATTACK;
				default:
					return SLASH_ATTACK;
				}
			}
			if (weaponName.contains("mace") || weaponName.contains("anchor")) {
				switch (attackStyle) {
				case 2:
					return STAB_ATTACK;
				default:
					return CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
				case 1:
					return SLASH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}
			if (weaponName.contains("spear")) {
				switch (attackStyle) {
				case 1:
					return SLASH_ATTACK;
				case 2:
					return CRUSH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}
			if (weaponName.contains("pickaxe")) {
				switch (attackStyle) {
				case 2:
					return CRUSH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}

			if (weaponName.contains("dagger") || weaponName.contains("rapier")) {
				switch (attackStyle) {
				case 2:
					return SLASH_ATTACK;
				default:
					return STAB_ATTACK;
				}
			}

			if (weaponName.contains("godsword")
					|| weaponName.contains("greataxe")
					|| weaponName.contains("2h sword")
					|| weaponName.equals("saradomin sword")) {
				switch (attackStyle) {
				case 2:
					return CRUSH_ATTACK;
				default:
					return SLASH_ATTACK;
				}
			}

		}
		switch (weaponId) {
		default:
			return CRUSH_ATTACK;
		}
	}

	public static final int getXpStyle(int weaponId, int attackStyle) {
		// TODO SHARED
		if (weaponId != -1) {
			String weaponName = ItemDefinition.forId(weaponId)
					.getName().toLowerCase();
			if (weaponName.contains("whip")) {
				switch (attackStyle) {
				case 0:
					return Skills.ATTACK;
				case 1:
					return SHARED;
				case 2:
				default:
					return Skills.DEFENCE;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
				case 0:
					return SHARED;
				case 1:
					return Skills.STRENGTH;
				case 2:
				default:
					return Skills.DEFENCE;
				}
			}
			if (weaponName.contains("staff")) {
				switch (attackStyle) {
				case 0:
					return Skills.ATTACK;
				case 1:
					return Skills.STRENGTH;
				case 2:
				default:
					return Skills.DEFENCE;
				}
			}
			if (weaponName.contains("godsword") || weaponName.contains("sword")
					|| weaponName.contains("2h")) {
				switch (attackStyle) {
				case 0:
					return Skills.ATTACK;
				case 1:
					return Skills.STRENGTH;
				case 2:
					return Skills.STRENGTH;
				case 3:
				default:
					return Skills.DEFENCE;
				}
			}
		}
		switch (weaponId) {
		case -1:
			switch (attackStyle) {
			case 0:
				return Skills.ATTACK;
			case 1:
				return Skills.STRENGTH;
			case 2:
			default:
				return Skills.DEFENCE;
			}
		default:
			switch (attackStyle) {
			case 0:
				return Skills.ATTACK;
			case 1:
				return Skills.STRENGTH;
			case 2:
				return SHARED;
			case 3:
			default:
				return Skills.DEFENCE;
			}
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
		bonuses = new int[18];
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public void refreshBonuses() {
		bonuses = new int[18];
		for (Item item : player.getEquipment().toArray()) {
			if (item == null)
				continue;
			int[] bonuses = ItemBonuses.getItemBonuses(item.getId());
			if (bonuses == null)
				continue;
			for (int id = 0; id < bonuses.length; id++) {
				if (id == 15 && this.bonuses[id] != 0)
					continue;
				this.bonuses[id] += bonuses[id];
			}
		}
	}

	public void resetSpecialAttack() {
		desecreaseSpecialAttack(0);
		specialAttackPercentage = 100;
		refreshSpecialAttackPercentage();
	}

	public void setSpecialAttack(int special) {
		desecreaseSpecialAttack(0);
		specialAttackPercentage = (byte) special;
		refreshSpecialAttackPercentage();
	}

	public void init() {
		refreshUsingSpecialAttack();
		refreshSpecialAttackPercentage();
		refreshAutoRelatie();
		refreshAttackStyle();
		refreshSpellBook();
		refreshAutoCastSpell();
		refreshSpellBookScrollBar_DefCast();
	}

	public void checkAttackStyle() {
		if (autoCastSpell == 0)
			setAttackStyle(attackStyle);
	}

	public void setAttackStyle(int style) {
		int maxSize = 3;
		int weaponId = player.getEquipment().get(Constants.SLOT_WEAPON) == null ? -1
				: player.getEquipment().get(Constants.SLOT_WEAPON).getId();
		String name = weaponId == -1 ? "" : ItemDefinition.forId(weaponId).getName().toLowerCase();
		if (weaponId == -1 || /*PlayerCombat.isRanging(player) != 0
				|| */name.contains("whip") || name.contains("halberd"))
			maxSize = 2;
		if (style > maxSize)
			style = maxSize;
		if (style != attackStyle) {
			attackStyle = (byte) style;
			if (autoCastSpell > 1)
				resetSpells(true);
			else
				refreshAttackStyle();
		} else if (autoCastSpell > 1)
			resetSpells(true);
	}

	public void refreshAttackStyle() {
		player.getIOSession().write(new ConfigPacket(player, 43, autoCastSpell > 0 ? 4 : attackStyle));
	}

	public void switchUsingSpecialAttack() {
		usingSpecialAttack = !usingSpecialAttack;
		refreshUsingSpecialAttack();
	}

	public void desecreaseSpecialAttack(int ammount) {
		usingSpecialAttack = false;
		refreshUsingSpecialAttack();
		if (ammount > 0) {
			specialAttackPercentage -= ammount;
			refreshSpecialAttackPercentage();
		}
	}

	public boolean hasRingOfVigour() {
		if (player.getEquipment().get(Constants.SLOT_RING) == null) return false;
		if (player.getEquipment().get(Constants.SLOT_RING).getId() == 19669); return true;
	}

	public int getSpecialAttackPercentage() {
		return specialAttackPercentage;
	}

	public void refreshUsingSpecialAttack() {
		player.getIOSession().write(new ConfigPacket(player, 301, usingSpecialAttack ? 1 : 0));
	}

	public void refreshSpecialAttackPercentage() {
		player.getIOSession().write(new ConfigPacket(player, 300, specialAttackPercentage * 10));
	}

	public void switchAutoRelatie() {
		autoRelatie = !autoRelatie;
		refreshAutoRelatie();
	}

	public void refreshAutoRelatie() {
		player.getIOSession().write(new ConfigPacket(player, 172, autoRelatie ? 0 : 1));
	}

	public boolean isUsingSpecialAttack() {
		return usingSpecialAttack;
	}

	public int getAttackStyle() {
		return attackStyle;
	}

	public boolean isAutoRelatie() {
		return autoRelatie;
	}

	public void setAutoRelatie(boolean autoRelatie) {
		this.autoRelatie = autoRelatie;
	}

	public boolean isDungeonneringSpellBook() {
		return dungeonneringSpellBook;
	}

	public void setSpellBook(int book) {
		switch(book) {
		case 1:
			player.getSettings().setSpellBook(192);
			if (player.getIOSession().getScreenSizeMode() < 2) {
				player.getIOSession().write(
						new InterfacePacket(player, 548, 210, 192, true));
			} else {
				player.getIOSession().write(
						new InterfacePacket(player, 746, 96, 192, true));
			}
			break;
		case 2:
			player.getSettings().setSpellBook(193);
			if (player.getIOSession().getScreenSizeMode() < 2) {
				player.getIOSession().write(
						new InterfacePacket(player, 548, 210, 193, true));
			} else {
				player.getIOSession().write(
						new InterfacePacket(player, 746, 96, 193, true));
			}
			break;
		case 3:
			player.getSettings().setSpellBook(430);
			if (player.getIOSession().getScreenSizeMode() < 2) {
				player.getIOSession().write(
						new InterfacePacket(player, 548, 210, 430, true));
			} else {
				player.getIOSession().write(
						new InterfacePacket(player, 746, 96, 430, true));
			}
			break;
		}
	}

}
