package com.sevador.game.world;

import java.util.logging.Level;

import net.burtleburtle.cache.format.BodyData;
import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.Main;
import com.sevador.game.action.impl.emote.Emote;
import com.sevador.game.event.EventManager;
import com.sevador.game.misc.ShopManager;
import com.sevador.game.node.NodeTypeRepositary;
import com.sevador.game.node.model.combat.form.Ammunition;
import com.sevador.game.node.model.combat.form.RangeWeapon;
import com.sevador.utility.punish.UserPunishHandler;

/**
 * Initializes the nodes.
 * @author Emperor
 *
 */
public class NodeInitializer {

	/**
	 * Initializes the nodes (eg. spawning, setting definitions, ...).
	 * @return {@code True} if succesful.
	 */
	public static boolean init() {
		World.getWorld().getBackgroundLoader().submit(new Runnable() {
			@Override
			public void run() {
				ItemDefinition.setBodyData(BodyData.read().partsData);
				ItemDefinition.init();
			}
		});
		World.getWorld().getBackgroundLoader().submit(new Runnable() {
			@Override
			public void run() {
				NodeTypeRepositary.init();
				Emote.init();
				RangeWeapon.initialize();
				Ammunition.initialize();
			}
		});
		World.getWorld().getBackgroundLoader().submit(new Runnable() {
			@Override
			public void run() {
				ShopManager.load();
				UserPunishHandler.init();
				NPCWorldLoader.init();
			}
		});
		World.getWorld().getBackgroundLoader().submit(new Runnable() {
			@Override
			public void run() {
				try {
					if (!EventManager.init()) {
						System.exit(1);
						Main.getLogger().log(Level.SEVERE, "ERROR! EVENT MANAGER COULD NOT REGISTER.");
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		});
		return true;
	}
}