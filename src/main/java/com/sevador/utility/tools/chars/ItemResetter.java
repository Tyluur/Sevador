package com.sevador.utility.tools.chars;

import java.io.File;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.Main;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.impl.BankContainerListener;
import com.sevador.game.node.model.container.impl.EquipmentContainerListener;
import com.sevador.game.node.model.container.impl.InventoryContainerListener;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.utility.Constants;
import com.sevador.utility.saving.SerializeSave;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class ItemResetter {

	private static int count = 0;

	public static void main(String[] args) {
		Cache.init();
		ItemDefinition.init();
		File dir = new File(Constants.SAVE_PATH);
		File[] accs = dir.listFiles();
		for (File acc : accs) {
			if (acc == null) continue;
			if (acc.getName().endsWith(".ser")) {
				try {
					Player player = (Player) PlayerWorldLoader.load(acc);
					player.getInventory().setListener(new InventoryContainerListener(player));
					player.getEquipment().setListener(new EquipmentContainerListener(player));
					player.getBank().getContainer().setListener(new BankContainerListener(player));
					for (Item item : player.getInventory().toArray()) {
						if (item == null || player.getInventory() == null) continue;
						player.getInventory().remove(item);
					}
					for (Item item : player.getEquipment().toArray()) {
						if (item == null || player.getEquipment() == null) continue;
						player.getEquipment().remove(item);
					}
					for (Item item : player.getBank().getContainer().toArray()) {
						if (item == null || player.getBank().getContainer() == null) continue;
						player.getBank().getContainer().remove(item);
					}
					SerializeSave.savePlayer(player);
					count++;
					Main.getLogger().info("Finished with player " + acc.getName() + ". " + count + " of " + accs.length + "."); 
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
	}

}
