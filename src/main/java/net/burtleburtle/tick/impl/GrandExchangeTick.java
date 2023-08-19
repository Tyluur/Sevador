package net.burtleburtle.tick.impl;

import com.sevador.content.grandExchange.GrandExchange;
import com.sevador.game.node.player.Player;

import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.tick.Tick;

/**
 * @author Tyluur
 */

public class GrandExchangeTick extends Tick {

	/**
	 * The singleton of this class.
	 */
	private static GrandExchangeTick SINGLETON = new GrandExchangeTick();

	public GrandExchangeTick() {
		super(10);
	}

	@Override
	public boolean run() {
		GrandExchange.save();
		for (Player pl : NodeWorker.getPlayers()) {
			if (pl.getAttribute("area:wilderness") != null && pl.getAttribute("area:wilderness").equals(true)) continue;
			pl.getPacketSender().sendReportPlayersOnline(pl);
		}
		return false;
	}

	public static GrandExchangeTick getSingleton() {
		return SINGLETON;
	}

	public static void setSingleton(GrandExchangeTick sINGLETON) {
		SINGLETON = sINGLETON;
	}

}