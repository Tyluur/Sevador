package com.sevador.game.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sevador.Main;
import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;

/**
 * Manages the shops.
 * @author Emperor
 *
 */
public final class ShopManager {

	/**
	 * The shops.
	 */
	private static final List<Shop> SHOPS = new ArrayList<Shop>();
	
	/**
	 * The shop owners mapping.
	 */
	private static final Map<Integer, Integer> SHOP_OWNERS = new HashMap<Integer, Integer>();
	
	/**
	 * Loads the shops.
	 * @return {@code True} if succesful.
	 */
	public static boolean load() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("./data/nodes/shops list.txt")));
			String s;
			Shop shop = null;
			SHOPS.clear();
			SHOP_OWNERS.clear();
			while ((s = reader.readLine()) != null) {
				if (s.startsWith("shop ")) {
					if (shop != null) {
						SHOPS.add(shop);
					}
					s = s.replace("shop [", "").replace("]", "");
					String[] data = s.split(", ");
					shop = new Shop(Integer.parseInt(data[0]), data[1]);
				} else if (s.startsWith("item ")) {
					String[] data = s.replace("item [", "").split(", ");
					Item item = new Item(Integer.parseInt(data[0]), 50000);
					shop.getOriginal().add(item);
					shop.getContainer().add(item);
				}
			}
			SHOPS.add(shop);
			reader = new BufferedReader(new FileReader(new File("./data/nodes/shop npc list.txt")));
			while ((s = reader.readLine()) != null) {
				if (s.startsWith("npc [")) {
					s = s.replace("shop [", "").replace("]", "");
					String[] data = s.replace("npc [", "").replace("]", "").split(", ");
					SHOP_OWNERS.put(Integer.parseInt(data[0]) | (Integer.parseInt(data[2]) << 16), Integer.parseInt(data[1]));
				}
			}
			Main.getLogger().info("Loaded " + SHOPS.size() + " game shops.");
			reader.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Updates the shops.
	 */
	public static void tick() {
		
	}
	
	/**
	 * Opens a shop.
	 * @param player The player.
	 * @param npcId The npc's id.
	 * @param option The option type clicked.
	 */
	public static boolean open(Player player, int npcId, int option) {
		Integer id = SHOP_OWNERS.get(npcId | (option << 16));
		for (int i = 0; i < SHOP_OWNERS.size(); i++) {
			if (id == null)
				id = i;
		}
		if (id == null || id >= SHOPS.size()) {
			System.out.println(id + " - " + SHOPS.size());
			return false;
		}
		Shop shop = SHOPS.get(id);
		if (shop == null) {
			return false;
		}
		shop.open(player);
		return true;
	}
	
	/**
	 * Gets the shop owners mapping.
	 * @return The shop owners.
	 */
	public static Map<Integer, Integer> getShopOwners() {
		return SHOP_OWNERS;
	}
}