package com.sevador.game.node.player;

import java.io.Serializable;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.ItemsContainer;
import com.sevador.network.out.ContainerPacket;

public final class Equipment implements Serializable {

	private static final long serialVersionUID = -4147163237095647617L;

	public static final byte SLOT_HAT = 0, SLOT_CAPE = 1, SLOT_AMULET = 2,
			SLOT_WEAPON = 3, SLOT_CHEST = 4, SLOT_SHIELD = 5, SLOT_LEGS = 7,
			SLOT_HANDS = 9, SLOT_FEET = 10, SLOT_RING = 12, SLOT_ARROWS = 13,
			SLOT_AURA = 14;

	private static String[] CAPES = { "cloak", "cape", "ava's", "TokHaar" };

	private static String[] HATS = { "visor", "ears", "goggles", "bearhead",
			"tiara", "cowl", "druidic wreath", "halo", "Royal", "crown",
			"sallet", "helm", "hood", "coif", "Coif", "partyhat", "hat", "cap",
			" bandana", "full helm (t)", "full helm (g)", "cav", "boater",
			"helmet", "afro", "beard", "gnome goggles", "mask",
			"Helm of neitiznot", "mitre" };

	private static String[] BOOTS = { "boots", "Boots", "shoes", "Shoes",
			"flippers" };

	private static String[] GLOVES = { "gloves", "gauntlets", "Gloves",
			"vambraces", "vamb", "bracers", "brace" };

	private static String[] AMULETS = { "stole", "amulet", "necklace",
			"Amulet of", "scarf", "Super dominion medallion" };

	private static String[] SHIELDS = { "tome of frost", "kiteshield",
			"sq shield", "Toktz-ket", "books", "book", "kiteshield (t)",
			"kiteshield (g)", "kiteshield(h)", "defender", "shield",
			"deflector" };

	private static String[] ARROWS = { "arrow", "arrows", "arrow(p)",
			"arrow(+)", "arrow(s)", "bolt", "Bolt rack", "Opal bolts",
			"Dragon bolts", "bolts (e)", "bolts", "Hand cannon shot" };

	private static String[] RINGS = { "ring" };

	private static String[] BODY = { "poncho", "apron", "robe top", "armour",
			"hauberk", "platebody", "chainbody", "robetop", "leathertop",
			"platemail", "top", "brassard", "body", "platebody (t)",
			"platebody (g)", "body(g)", "body_(g)", "chestplate", "torso",
			"shirt", "Rock-shell plate" };

	private static String[] AURAS = { "poison purge", "runic accuracy",
			"sharpshooter", "lumberjack", "quarrymaster", "call of the sea",
			"reverence", "five finger discount", "resourceful", "equilibrium",
			"inspiration", "vampyrism", "penance", "wisdom", "jack of trades" };

	private static int[] BODY_LIST = { 21463, 21549, 544, 6107 };

	private static int[] LEGS_LIST = { 542, 6108, 10340, 7398 };

	private static String[] LEGS = { "leggings", "void knight robe",
			"druidic robe", "cuisse", "pants", "platelegs", "plateskirt",
			"skirt", "bottoms", "chaps", "platelegs (t)", "platelegs (g)",
			"bottom", "skirt", "skirt (g)", "skirt (t)", "chaps (g)",
			"chaps (t)", "tassets", "legs", "trousers", "robe bottom" };

	private static String[] WEAPONS = { "bolas", "blade", "Butterfly net",
			"scythe", "rapier", "hatchet", "bow", "Hand cannon",
			"Inferno adze", "Silverlight", "Darklight", "wand",
			"Statius's warhammer", "anchor", "spear.", "Vesta's longsword.",
			"scimitar", "longsword", "sword", "longbow", "shortbow", "dagger",
			"mace", "halberd", "spear", "Abyssal whip", "Abyssal vine whip",
			"Ornate katana", "axe", "flail", "crossbow", "Torags hammers",
			"dagger(p)", "dagger (p++)", "dagger(+)", "dagger(s)", "spear(p)",
			"spear(+)", "spear(s)", "spear(kp)", "maul", "dart", "dart(p)",
			"javelin", "javelin(p)", "knife", "knife(p)", "Longbow",
			"Shortbow", "Crossbow", "Toktz-xil", "Toktz-mej", "Tzhaar-ket",
			"staff", "Staff", "godsword", "c'bow", "Crystal bow", "Dark bow",
			"claws", "warhammer", "hammers", "adze", "hand", "Broomstick",
			"Flowers", "flowers", "trident", "excalibur" };

	private static String[] NOT_FULL_BODY = { "zombie shirt" };

	private static String[] FULL_BODY = { "robe", "pernix body",
			"vesta's chainbody", "armour", "hauberk", "top", "shirt",
			"platebody", "Ahrims robetop", "Karils leathertop", "brassard",
			"chestplate", "torso", "Morrigan's", "Zuriel's", "changshan jacket" };

	private static String[] FULL_HAT = { "helm", "cowl", "sallet", "med helm",
			"coif", "Dharoks helm", "Initiate helm", "Coif",
			"Helm of neitiznot" };

	private static String[] FULL_MASK = { "sallet", "full helm", "mask",
			"Veracs helm", "Guthans helm", "Torags helm", "Karils coif",
			"full helm (t)", "full helm (g)" };

	private ItemsContainer<Item> items;

	private transient Player player;
	private transient int equipmentHpIncrease;

	static final int[] DISABLED_SLOTS = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 1, 1, 0 };

	public Equipment() {
		items = new ItemsContainer<Item>(15, false);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void init() {
		player.getIOSession().write(new ContainerPacket(player, 94, items.toArray(), false));
		refresh(null);
	}

	public void refresh(int... slots) {
		if (slots != null) {
			player.getIOSession().write(new ContainerPacket(player, 94, items.toArray(), false));
			player.getCombatDefinitions().checkAttackStyle();
		}
		player.getCombatDefinitions().refreshBonuses();
		player.getPacketSender().sendMessage("WD");
		refreshConfigs(slots == null);
	}

	public void reset() {
		items.reset();
		init();
	}

	public Item getItem(int slot) {
		return items.get(slot);
	}

	public void sendExamine(int slotId) {
		Item item = items.get(slotId);
		if (item == null)
			return;
		//player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
	}

	public void refreshConfigs(boolean init) {
		double hpIncrease = 0;
		for (int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			if (item == null)
				continue;
			int id = item.getId();
			if (index == Equipment.SLOT_HAT) {
				if (id == 20135 || id == 20137 // torva
						|| id == 20147 || id == 20149 // pernix
						|| id == 20159 || id == 20161 // virtus
				)
					hpIncrease += 66;
//				else if (id == Runecrafting.AIR_TIARA)
//					player.getPackets().sendConfig(491, 1);
//				else if (id == Runecrafting.EARTH_TIARA)
//					player.getPackets().sendConfig(491, 8);
//				else if (id == Runecrafting.FIRE_TIARA)
//					player.getPackets().sendConfig(491, 16);
//				else if (id == Runecrafting.WATER_TIARA)
//					player.getPackets().sendConfig(491, 4);
//				else if (id == Runecrafting.BODY_TIARA)
//					player.getPackets().sendConfig(491, 32);
//				else if (id == Runecrafting.MIND_TIARA)
//					player.getPackets().sendConfig(491, 2);
//				else if (id == Runecrafting.OMNI_TIARA)
//					player.getPackets().sendConfig(491, -1);

			} else if (index == Equipment.SLOT_CHEST) {
				if (id == 20139 || id == 20141 // torva
						|| id == 20151 || id == 20153 // pernix
						|| id == 20163 || id == 20165 // virtus
				)
					hpIncrease += 200;
			} else if (index == Equipment.SLOT_LEGS) {
				if (id == 20143 || id == 20145 // torva
						|| id == 20155 || id == 20157 // pernix
						|| id == 20167 || id == 20169 // virtus
				)
					hpIncrease += 134;
			}
		}
		if (hpIncrease != equipmentHpIncrease) {
			equipmentHpIncrease = (int) hpIncrease;
			if (!init)
				player.getSkills().heal(0);
		}
	}

	public static boolean isFullBody(Item item) {
		String itemName = item.getDefinition().getName();
		if (itemName == null)
			return false;
		itemName = itemName.toLowerCase();
		for (int i = 0; i < NOT_FULL_BODY.length; i++)
			if (itemName.contains(NOT_FULL_BODY[i].toLowerCase()))
				return false;
		for (int i = 0; i < FULL_BODY.length; i++)
			if (itemName.contains(FULL_BODY[i].toLowerCase()))
				return true;
		return false;
	}

	public static boolean isFullHat(Item item) {
		String itemName = item.getDefinition().getName();
		if (itemName == null)
			return false;
		itemName = itemName.toLowerCase();
		for (int i = 0; i < FULL_HAT.length; i++) {
			if (itemName.contains(FULL_HAT[i].toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullMask(Item item) {
		String itemName = item.getDefinition().getName();
		if (itemName == null)
			return false;
		itemName = itemName.toLowerCase();
		for (int i = 0; i < FULL_MASK.length; i++)
			if (itemName.contains(FULL_MASK[i].toLowerCase()))
				return true;
		return false;
	}

	public static int getItemSlot(int itemId) {
		for (int i = 0; i < BODY_LIST.length; i++)
			if (itemId == BODY_LIST[i])
				return 4;
		for (int i = 0; i < LEGS_LIST.length; i++)
			if (itemId == LEGS_LIST[i])
				return 7;
		String item = ItemDefinition.forId(itemId).getName()
				.toLowerCase();
		if (item == null)
			return -1;
		for (int i = 0; i < CAPES.length; i++)
			if (item.contains(CAPES[i].toLowerCase()))
				return 1;
		for (int i = 0; i < BOOTS.length; i++)
			if (item.contains(BOOTS[i].toLowerCase()))
				return 10;
		for (int i = 0; i < GLOVES.length; i++)
			if (item.contains(GLOVES[i].toLowerCase()))
				return 9;
		for (int i = 0; i < SHIELDS.length; i++)
			if (item.contains(SHIELDS[i].toLowerCase()))
				return 5;
		for (int i = 0; i < AMULETS.length; i++)
			if (item.contains(AMULETS[i].toLowerCase()))
				return 2;
		for (int i = 0; i < ARROWS.length; i++)
			if (item.contains(ARROWS[i].toLowerCase()))
				return 13;
		for (int i = 0; i < RINGS.length; i++)
			if (item.contains(RINGS[i].toLowerCase()))
				return 12;
		for (int i = 0; i < WEAPONS.length; i++)
			if (item.contains(WEAPONS[i].toLowerCase()))
				return 3;
		for (int i = 0; i < HATS.length; i++)
			if (item.contains(HATS[i].toLowerCase()))
				return 0;
		for (int i = 0; i < BODY.length; i++)
			if (item.contains(BODY[i].toLowerCase()))
				return 4;
		for (int i = 0; i < LEGS.length; i++)
			if (item.contains(LEGS[i].toLowerCase()))
				return 7;
		for (int i = 0; i < AURAS.length; i++)
			if (item.contains(AURAS[i].toLowerCase()))
				return SLOT_AURA;
		return -1;
	}

	public boolean hasTwoHandedWeapon() {
		Item item = items.get(SLOT_WEAPON);
		if (item == null)
			return false;
		return isTwoHandedWeapon(item);
	}

	public static boolean isTwoHandedWeapon(Item item) {
		int itemId = item.getId();
		if (itemId == 4212)
			return true;
		else if (itemId == 4214)
			return true;
		else if (itemId == 20281)
			return true;
		String wepEquiped = item.getDefinition().getName().toLowerCase();
		if (wepEquiped == null)
			return false;
		else if (wepEquiped.endsWith("claws"))
			return true;
		else if (wepEquiped.endsWith("anchor"))
			return true;
		else if (wepEquiped.contains("2h sword"))
			return true;
		else if (wepEquiped.contains("katana"))
			return true;
		else if (wepEquiped.equals("seercull"))
			return true;
		else if (wepEquiped.contains("shortbow"))
			return true;
		else if (wepEquiped.contains("longbow"))
			return true;
		else if (wepEquiped.contains("shortbow"))
			return true;
		else if (wepEquiped.contains("bow full"))
			return true;
		else if (wepEquiped.equals("zaryte bow"))
			return true;
		else if (wepEquiped.equals("dark bow"))
			return true;
		else if (wepEquiped.endsWith("halberd"))
			return true;
		else if (wepEquiped.contains("maul"))
			return true;
		else if (wepEquiped.equals("karil's crossbow"))
			return true;
		else if (wepEquiped.equals("torag's hammers"))
			return true;
		else if (wepEquiped.equals("verac's flail"))
			return true;
		else if (wepEquiped.contains("greataxe"))
			return true;
		else if (wepEquiped.contains("spear"))
			return true;
		else if (wepEquiped.equals("tzhaar-ket-om"))
			return true;
		else if (wepEquiped.contains("godsword"))
			return true;
		else if (wepEquiped.equals("saradomin sword"))
			return true;
		else if (wepEquiped.equals("hand cannon"))
			return true;
		return false;
	}

	public int getWeaponRenderEmote() {
		Item weapon = items.get(3);
		if (weapon == null)
			return 1426;
		return weapon.getDefinition().getRenderAnimId();
	}

	public boolean hasShield() {
		return items.get(5) != null;
	}

	public int getWeaponId() {
		Item item = items.get(SLOT_WEAPON);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getChestId() {
		Item item = items.get(SLOT_CHEST);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getHatId() {
		Item item = items.get(SLOT_HAT);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getShieldId() {
		Item item = items.get(SLOT_SHIELD);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getLegsId() {
		Item item = items.get(SLOT_LEGS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void removeAmmo(int ammoId, int ammount) {
		if (ammount == -1) {
			items.remove(SLOT_WEAPON, new Item(ammoId, 1));
			refresh(SLOT_WEAPON);
		} else {
			items.remove(SLOT_ARROWS, new Item(ammoId, ammount));
			refresh(SLOT_ARROWS);
		}
	}

	public int getAuraId() {
		Item item = items.get(SLOT_AURA);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getCapeId() {
		Item item = items.get(SLOT_CAPE);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getRingId() {
		Item item = items.get(SLOT_RING);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getAmmoId() {
		Item item = items.get(SLOT_ARROWS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public void deleteItem(int itemId, int amount) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
	}

	public void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index])
				changedSlots[count++] = index;
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}

	public int getBootsId() {
		Item item = items.get(SLOT_FEET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public int getGlovesId() {
		Item item = items.get(SLOT_HANDS);
		if (item == null)
			return -1;
		return item.getId();
	}

	public ItemsContainer<Item> getItems() {
		return items;
	}

	public int getEquipmentHpIncrease() {
		return equipmentHpIncrease;
	}

	public void setEquipmentHpIncrease(int hp) {
		this.equipmentHpIncrease = hp;
	}

	public boolean wearingArmour() {
		if (getItem(SLOT_HAT) != null || getItem(SLOT_CAPE) != null
				|| getItem(SLOT_AMULET) != null || getItem(SLOT_WEAPON) != null
				|| getItem(SLOT_CHEST) != null || getItem(SLOT_SHIELD) != null
				|| getItem(SLOT_LEGS) != null || getItem(SLOT_HANDS) != null
				|| getItem(SLOT_FEET) != null || getItem(SLOT_RING) != null)
			return false;
		return true;
	}

	public int getAmuletId() {
		Item item = items.get(SLOT_AMULET);
		if (item == null)
			return -1;
		return item.getId();
	}

	public Item get(byte slotRing) {
		return items.get(slotRing);
	}

}
