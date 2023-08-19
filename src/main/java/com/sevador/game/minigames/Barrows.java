package com.sevador.game.minigames;

import java.awt.Point;
import java.util.Random;

import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.tick.Tick;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.utility.Misc;
import com.sevador.utility.world.Area;

/**
 *
 * @author `Discardedx2
 */
public class Barrows {

	public static enum BarrowsBrother {
		AHRIM(2025, 6821),
		DHAROK(2026, 6771),
		GUTHAN(2027, 6773),
		KARIL(2028, 6823),
		TORAG(2029, 6772),
		VERAC(2030, 6822);

		private int npcId;
		private String name;
		private int coffinId;

		private BarrowsBrother(int npcId, int coffinId) {
			this.npcId = npcId;
			this.coffinId = coffinId;
			this.name = super.toString().toLowerCase();
		}

		public int getNpcId() {
			return npcId;
		}

		public int getCoffinId() {
			return coffinId;
		}

		public Area getHillArea() {
			return World.getWorld().getAreaManager().getAreaByName(name + "_hill");
		}
	}

	public static final Item SPADE = new Item(952, 1);

	public static final Random RAND = new Random();

	public static final int[] TUNNEL_NPCS = {
		2036, 2032, 4920, 2031
	};
	
	public static boolean enterCrypt(final Player player) {
		final BarrowsBrother[] brothers = BarrowsBrother.values();
		for (BarrowsBrother brother : brothers) {
			final Area hill = brother.getHillArea();
			if (hill.contains(player.getLocation())) {
				World.getWorld().submit(new Tick(2) {

					@Override
					public boolean run() {
						if (player.getAttribute("barrows_tunnel") == null) {
							player.setAttribute("barrows_tunnel", brothers[RAND.nextInt(brothers.length)]);
						}
						player.teleport(Location.locate(hill.centerX, hill.centerY + 6400, 3));
						player.getPacketSender().sendMessage("You've broken into a crypt!");
						// TODO: ActionSender.updateMinimap(player, ActionSender.BLACKOUT_MAP);
						stop();
						return true;
					}
				});
				return true;
			}
		}
		return false;
	}

	public static boolean stairInteraction(Player player, int stairs) {
		if (stairs >= 6702 && stairs <= 6707) {
			int randomX = Misc.random(2, -4);
			int randomY = Misc.random(2, -4);
			for (Area area : World.getWorld().getAreaManager().getAreas()) {
				int depth = 5;
				for (int myX = player.getLocation().getX() - depth; myX < player.getLocation().getX() + depth; myX++) {
					for (int myY = player.getLocation().getY() - depth; myY < player.getLocation().getY() + depth; myY++) {
						if (area.centerX == myX && area.centerY + 6400 == myY) {
							Point center = area.getCenterPoint();
							player.getProperties().setTeleportLocation(new Location(center.x + randomX, center.y + randomY, 0));
							NPC npc = player.getAttribute("currentlyFightingBrother");
							if (npc != null) {
								NodeWorker.getNPCs().remove(npc);
								player.removeAttribute("currentlyFightingBrother");
							}
							return true;
						}
					}
				}
			}
			player.getPacketSender().sendMessage("An error has occured - Barrows->stairInteraction");
		}
		return false;
	}

	public static int getIndexForBrother(NPC npc) {
		for (BarrowsBrother brother : BarrowsBrother.values()) {
			if (brother.npcId == npc.getId()) {
				return brother.ordinal();
			}
		}
		return -1;
	}

}
