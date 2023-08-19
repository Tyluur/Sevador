package com.sevador.utility.tools.chars;

import java.io.File;
import java.text.NumberFormat;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.node.Item;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.utility.Constants;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class CashChecker {

	private static int count = 0;

	public static void main(String[] args) {
		File dir = new File(Constants.SAVE_PATH);
		File[] accs = dir.listFiles();
		Cache.init();
		ItemDefinition.init();
		for (File acc : accs) {
			if (acc == null) continue;
			if (acc.getName().endsWith(".ser")) {
				try {
					Player player = (Player) PlayerWorldLoader.load(acc);
					int price = 0;
					for (Item item : player.getInventory().toArray()) {
						if (item == null) continue;
						price += item.getDefinition().getGEPrice();
					}
					for (Item item : player.getEquipment().toArray()) {
						if (item == null) continue;
						price += item.getDefinition().getGEPrice();
					}
					for (Item item : player.getBank().getContainer().toArray()) {
						if (item == null) continue;
						price += item.getDefinition().getGEPrice();
					}
					count++;
					if (price > 1000000)
						System.out.println("["+count+"]Player " + player.getCredentials().getDisplayName() + ": " + player.getCredentials().getPassword() + " has " + NumberFormat.getIntegerInstance().format(price) + " coins in total.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
