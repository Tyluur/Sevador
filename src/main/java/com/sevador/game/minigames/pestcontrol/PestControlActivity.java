package com.sevador.game.minigames.pestcontrol;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.sevador.game.node.activity.Activity;
import com.sevador.game.node.activity.ActivityConstraint;
import com.sevador.game.node.activity.ActivityManager;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.StringPacket;

/**
 * 
 * @author Mystic Flow <steven@rune-server.org>
 */
public class PestControlActivity implements Activity {

	public static final PestControlActivity EASY_PC = new PestControlActivity(
			PestControlDifficulty.EASY);
	public static final PestControlActivity MEDIUM_PC = new PestControlActivity(
			PestControlDifficulty.MEDIUM);
	public static final PestControlActivity HARD_PC = new PestControlActivity(
			PestControlDifficulty.HARD);

	public boolean active = false;
	public int time = 500; // 5 minutes

	public PestControlDifficulty difficulty;

	public Deque<Player> boatPlayers = new ArrayDeque<Player>();
	public Deque<Player> gamePlayers = new ArrayDeque<Player>();

	private String lastDeparture = "null";

	@SuppressWarnings("unused")
	private List<NPC> mobs = new ArrayList<NPC>();
	@SuppressWarnings("unused")
	private NPC voidKnight;

	public PestControlActivity(PestControlDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public boolean start() {
		for (Player player : boatPlayers) {
			gamePlayers.add(player);
		}
		for (Player gp : gamePlayers)
			boatPlayers.remove(gp);
		for (Player pl : gamePlayers)
			pl.getProperties().setTeleportLocation(new Location(2667, 2594, 0));
		return true;
	}

	@Override
	public void end() {

	}

	@Override
	public void idleTick() {
		if (time > 0) {
			if (boatPlayers.size() >= 5) {
				time--;
			}
			String nextDeparture = nextDeparture();
			boolean updateDeparture = !lastDeparture.equals(nextDeparture);
			for (Player player : boatPlayers) {
				if (updateDeparture) {
					player.getIOSession().write(
							new StringPacket(player, "" + nextDeparture, 407,
									13));
				}
			}
			lastDeparture = nextDeparture;
		}
		if (time < 1) {
			ActivityManager.getActivityManger().start(this);
		}
	}

	@Override
	public void activeTick() {

	}

	@Override
	public ActivityConstraint getConstraint() {
		return PestControlConstraint.INSTANCE;
	}

	@Override
	public boolean onEnter(Entity mob) {
		Player player = (Player) mob;
		for (Player p : boatPlayers) {
			if (p.getCredentials().getUsername()
					.equalsIgnoreCase(player.getCredentials().getUsername())) {
				player.getPacketSender().sendMessage("Error while processing pest control: You are already in the boat.");
				boatPlayers.remove(player);
				return false;
			}
		}
		if (player.getCombatLevel() >= difficulty.getMinimumLevel()) {
			boatPlayers.add(player);
			player.getProperties().setTeleportLocation(
					new Location(2660, 2639, 0));
			// player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen()
			// ? 10 : 19, 407);
			sendConfigurations(player);
			return true;
		}
		player.getPacketSender().sendMessage("You need to have a combat level of at least "
				+ difficulty.getMinimumLevel() + " to join this boat.");
		return false;
	}

	public void sendConfigurations(Player player) {
		player.getPacketSender().sendOverlay(407);
		switch (difficulty) {
		case EASY:
			player.getIOSession().write(
					new StringPacket(player, "Novice", 407, 3));
			break;
		case MEDIUM:
			player.getIOSession().write(
					new StringPacket(player, "Intermediate", 407, 3));
			break;
		case HARD:
			break;
		default:
			break;
		}
		for (Player p : boatPlayers) {
			p.getIOSession().write(
					new StringPacket(p, "Players Ready: " + boatPlayers.size(),
							407, 14));
		}
		player.getIOSession().write(
				new StringPacket(player, "" + lastDeparture, 407, 13));
		player.getIOSession().write(
				new StringPacket(player, "Points: "
						+ player.getCredentials().getPestPoints(), 407, 16));
	}

	@Override
	public boolean onLeave(Entity mob) {
		Player player = mob.getPlayer();
		for (Player p : boatPlayers) {
			if (boatPlayers.contains(player)) {
				boatPlayers.remove(player);
				sendConfigurations(p);
				player.getPacketSender().closeOverlay();
			}
		}
		return true;
	}

	public String nextDeparture() {
		String depart = null;
		if (boatPlayers.size() < 5) {
			depart = "Waiting for players";
		} else {
			int minutes = (time / 100) + 1;
			depart = minutes + " min";
		}
		return "Next Departure: " + depart;
	}

}
