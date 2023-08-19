package com.sevador;

import java.io.File;

import net.burtleburtle.cores.CoresManager;
import net.burtleburtle.thread.NodeWorker;

import com.sevador.content.grandExchange.GrandExchange;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.utility.Constants;
import com.sevador.utility.punish.UserPunishHandler;

/**
 * The Shutdown Hook Is called when
 * the application is terminated.
 * 
 * @author Emperor
 * 
 */
public final class ShutdownHook extends Thread {

	@Override
	public void run() {
		Main.getLogger().info("Server shutdown hook executed...");
		CoresManager.shutdown();
		try {
			UserPunishHandler.onShutDown();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		for (Player p : NodeWorker.getPlayers()) {
			if (p != null) {
				try {
					PlayerWorldLoader.store(p, new File(Constants.SAVE_PATH + "" + p.getCredentials().getUsername() + "" + PlayerWorldLoader.suffix));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		GrandExchange.save();
		Main.getLogger().info("Grand Exchange offers saved.");
	}
	
}