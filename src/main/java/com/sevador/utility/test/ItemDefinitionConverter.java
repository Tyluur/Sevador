package com.sevador.utility.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;

/**
 * Converts the dumped item definitions to binary format.
 * @author Emperor
 *
 */
public class ItemDefinitionConverter {
	
	/**
	 * The progress bar.
	 */
	//private static ProgressBar progress = new ProgressBar("Item definitions converting.", Cache.getAmountOfItems() << 1);

	/**
	 * The current amount of item definitions done.
	 */
	private static int currentAmount = 0;
	
	/**
	 * The main method.
	 * @param args The arguments.
	 */
	public static void main(String...args) {
        Cache.init();
        loadDefinitions();
        /*BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File("./data/nodes/RenderId.txt")));
			String s;
			while ((s = reader.readLine()) != null) {	
				String[] arg = s.split("	");
				ItemDefinition.forId(Integer.parseInt(arg[0])).setRenderId(Integer.parseInt(arg[1]));
			}
			reader.close();
			reader = new BufferedReader(new FileReader(new File("./data/nodes/EquipId.txt")));
			while ((s = reader.readLine()) != null) {	
				String[] arg = s.split("	");
				ItemDefinition.forId(Integer.parseInt(arg[0])).setEquipId(Integer.parseInt(arg[1]));
			}
			reader.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}*/
        setNotes();
        saveDefinitions();
	}
	
	/**
	 * Sets the noted items.
	 */
	private static void setNotes() {
		@SuppressWarnings("unused")
		ItemDefinition last = ItemDefinition.forId(0);
		for (int i = 1; i < Cache.getAmountOfItems(); i++) {
			ItemDefinition def = ItemDefinition.forId(i);
			if (i == 10843) {
				def.setNoted(true);
			} else if (def.noteTemplateId == -1) {// || last.isTradable() && last.name.equals(def.name)) {
				def.setNoted(false);
			} else {
				def.setNoted(true);
			}
			last = def;
		}
	}
	
	public static final String[] FULL_BODY = { "armour", " body", "hauberk",
		"top", "shirt", "platebody", "Ahrims robetop", "Karils leathertop",
		"brassard", "Robe top", "robetop", "platebody (t)",
		"platebody (g)", "platebody (or)", "chestplate", "torso",
		"Morrigan's", "leather body", "Zuriel's",
		"robe top", "Vesta's chainbody", "Investigator's coat", "armour",
        "hauberk", "top", "shirt", "platebody", "Ahrims robetop",
        "Karils leathertop", "brassard", "Robe top", "robetop",
        "platebody (t)", "platebody (g)", "chestplate", "torso",
        "Morrigan's", "leather body", "robe top", "Pernix body", "Torva platebody" };
	public static final String[] FULL_HAT = { "sallet", "med helm", "coif",
		"Dharoks helm", "hood", "Initiate helm", "Coif", " cowl", 
		"Helm of neitiznot", "Statius's full helm", "full helmet", "mage hat", "full helm", "Virtus mask", "sallet", "med helm", "coif",
        "Dharok's helm", "hood", "Initiate helm", "Coif",
        "Helm of neitiznot" };
	public static final String[] FULL_MASK = { "sallet", "full helm", "mask",
		"Veracs helm", "Guthans helm", "Torags helm", "Karils coif", "Christmas ghost hood",
        "Dragon full helm (or)", "sallet", "full helm", "mask",
        "Veracs helm", "Guthans helm", "Torags helm", "Karils coif",
        "full helm (t)", "full helm (g)", "mask"};
	/**
	 * Loads all the definitions.
	 */
	private static void loadDefinitions() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./ItemDump.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
		String s;
		try {
			ItemDefinition def;
			StringBuilder sb;
			int lastId = -1;
			List<Integer> handledIds = new ArrayList<Integer>();
			while ((s = reader.readLine()) != null) {
				int id = -1;
				try {
					id = getInteger(s);
				} catch (Throwable t) {
					System.out.println("Last id: " + lastId);
				}
				if ((def = ItemDefinition.forId(id)) == null) {
					return;
				}
				if (!handledIds.contains(id) && !def.getName().equals("null")) {
					handledIds.add(id);
					def.setFullBody(isFullBody(def));
					def.setFullHat(isFullHat(def));
					def.setFullMask(isFullMask(def));
				}
				if (s.startsWith("Tradeable")) {
					sb = new StringBuilder("Tradeable").append(id).append(": ");
					def.setTradable(Boolean.parseBoolean(s.replace(sb.toString(), "")));
				} else if (s.startsWith("High-alch")) {
					sb = new StringBuilder("High-alch").append(id).append(": ");
					def.setHighAlch(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("Low-alch")) {
					sb = new StringBuilder("Low-alch").append(id).append(": ");
					def.setLowAlch(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("Dropable")) {
					sb = new StringBuilder("Dropable").append(id).append(": ");
					def.setDropable(Boolean.parseBoolean(s.replace(sb.toString(), "")));
				} else if (s.startsWith("StorePrice")) {
					sb = new StringBuilder("StorePrice").append(id).append(": ");
					def.setStorePrice(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("Examine")) {
					sb = new StringBuilder("Examine").append(id).append(": ");
					def.setExamine(s.replace(sb.toString(), ""));
				} else if (s.startsWith("Bonus")) {
					sb = new StringBuilder("Bonus").append(id).append(": ");
					String[] bonus = s.replace(sb.toString(), "").split(" - ");
					def.getBonus()[Integer.parseInt(bonus[0]) % 15] = Integer.parseInt(bonus[1]);
				} else if (s.startsWith("EquipmentSlot")) {
					sb = new StringBuilder("EquipmentSlot").append(id).append(": ");
					def.setEquipmentSlot(Integer.parseInt(s.replace(sb.toString(), "")));
				} else if (s.startsWith("TwoHanded")) {
					sb = new StringBuilder("TwoHanded").append(id).append(": ");
					def.setTwoHanded(Boolean.parseBoolean(s.replace(sb.toString(), "")));
				} else if (s.startsWith("Absorb")) {
					sb = new StringBuilder("Absorb").append(id).append(": ");
					String[] bonus = s.replace(sb.toString(), "").split(" - ");
					int absorbId = Integer.parseInt(bonus[0]);
					if (absorbId < 3) {
						//def.getAbsorb()[absorbId % 15] = Integer.parseInt(bonus[1]);
					} else {
						def.getBonus()[absorbId % 15] = Integer.parseInt(bonus[1]);
					}
				} else if (s.startsWith("AttackSpeed")) {
					sb = new StringBuilder("AttackSpeed").append(id).append(": ");
					def.setAttackSpeed(Integer.parseInt(s.replace(sb.toString(), "")));
				}
				lastId = id;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFullBody(ItemDefinition def) {
		String weapon = def.name.toLowerCase();
		for (int i = 0; i < FULL_BODY.length; i++) {
			if (weapon.contains(FULL_BODY[i].toLowerCase()) && !weapon.contains("dragonhide") && !weapon.contains("d'hide")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullHat(ItemDefinition def) {
		String weapon = def.name.toLowerCase();
		for (int i = 0; i < FULL_HAT.length; i++) {
			if (weapon.contains(FULL_HAT[i].toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isFullMask(ItemDefinition def) {
		String weapon = def.name.toLowerCase();
		for (int i = 0; i < FULL_MASK.length; i++) {
			if (weapon.contains(FULL_MASK[i].toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets an integer in the string.
	 * @param s The string.
	 * @return The integer.
	 */
	private static int getInteger(String s) {
		StringBuilder sb = new StringBuilder();
		char c;
		boolean foundStart = false;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				sb.append(c);
				foundStart = true;
			} else if (foundStart) {
				break;
			}
		}
		int amount = Integer.parseInt(sb.toString());
		return amount;
	}

	/**
	 * Saves all the definitions.
	 */
	private static void saveDefinitions() {
		File f = new File("./data/nodes/itemDefinitions.bin");
		f.delete();
		try {
			RandomAccessFile raf = new RandomAccessFile("./data/nodes/itemDefinitions.bin", "rw");
			raf.writeShort(Cache.getAmountOfItems());
			for (int x = 0; x < Cache.getAmountOfItems(); x++) {
				currentAmount++;
				//progress.updateStatus(currentAmount);
				if (currentAmount % 500 == 0) {
					System.out.println(currentAmount);
				}
				ItemDefinition itemDef = ItemDefinition.forId(x);
				if (itemDef == null) {
					raf.writeShort(-1);
					continue;
				}
				raf.writeShort(x);
				raf.writeShort(itemDef.getEquipId());
				raf.writeByte(itemDef.isNoted() ? 1 : 0);
				raf.writeBytes(itemDef.isNoted() ? "Swap this note at any bank for the equivalent item." : itemDef.getExamine() != null ? itemDef.getExamine() : "It's a " + itemDef.name);
				raf.writeByte(0);
				if (!itemDef.isNoted()) {
					raf.writeDouble(new Item(x).getWeight());
					raf.writeByte(itemDef.getEquipmentSlot());
					raf.writeByte(itemDef.isFullHat() ? 1 : 0);
					raf.writeByte(itemDef.isFullMask() ? 1 : 0);
					raf.writeByte(itemDef.isFullBody() ? 1 : 0);
					raf.writeByte(itemDef.isTradable() ? 1 : 0);
					raf.writeByte(itemDef.getAttackSpeed());
					raf.writeByte(itemDef.getBonus() == null ? 0 : 1);
					if (itemDef.getBonus() != null) {
						for (int i = 0; i < 15; i++) {
							raf.writeShort(itemDef.getBonus()[i]);
						}
					}
					raf.writeByte(itemDef.getAbsorb() == null ? 0 : 1);
					if (itemDef.getAbsorb() != null) {
						for (int i = 0; i < 3; i++) {
							raf.writeShort(itemDef.getAbsorb()[i]);
						}
					}
				}
				raf.writeInt(itemDef.getHighAlch());
				raf.writeInt(itemDef.getLowAlch());
				raf.writeInt(itemDef.getStorePrice() < 1 ? (int) (itemDef.getHighAlch() * 1.2) : itemDef.getStorePrice());
				raf.writeByte(itemDef.isDropable() ? 1 : 0);
				raf.writeByte(itemDef.isTwoHanded() ? 1 : 0);
			}
			raf.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}