package net.burtleburtle.tick.impl;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.tick.Tick;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.PlayerOptionPacket;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;

/**
 * @author 'Mystic Flow
 */
public class PlayerAreaTick extends Tick {

	private Player player;

	private boolean inWilderness, inDuelArena, inBarrows, updateBarrows;
	

	private int barrowsDrainTime = 10;

	public PlayerAreaTick(Player player) {
		super(1);
		this.player = player;
	}

	@Override
	public boolean run() {
		if (World.getWorld().getAreaManager().getAreaByName("Godwars").contains(player.getLocation())) {
			if (player.getAttribute("inGod", Boolean.FALSE) == Boolean.FALSE) {
				player.getPacketSender().sendOverlay(601);
				player.setAttribute("inGod", Boolean.TRUE);
			}
			player.getIOSession().write(new StringPacket(player, "" + player.getKillCount()[Constants.ARMADYL_KILLS], 601, 8));
			player.getIOSession().write(new StringPacket(player, "" + player.getKillCount()[Constants.BANDOS_KILLS], 601, 9));
			player.getIOSession().write(new StringPacket(player, "" + player.getKillCount()[Constants.SARADOMIN_KILLS], 601, 10));
			player.getIOSession().write(new StringPacket(player, "" + player.getKillCount()[Constants.ZAMORAK_KILLS], 601, 11));
		} else if (World.getWorld().getAreaManager().getAreaByName("WGuild").contains(player.getLocation()) || World.getWorld().getAreaManager().getAreaByName("WGuildCatapult").contains(player.getLocation())) {
			if (player.getAttribute("inWGuild", Boolean.FALSE) == Boolean.FALSE) {
				player.getPacketSender().sendOverlay(1057);
				player.setAttribute("inWGuild", Boolean.TRUE);
				player.setAttribute("update_warriors_guild", true);
			}
			if (player.hasAttribute("update_warriors_guild")) {
				player.getIOSession().write(new StringPacket(player, "" + player.getSettings().getTokens()[0], 1057, 13));
				player.getIOSession().write(new StringPacket(player, "" + player.getSettings().getTokens()[0], 1057, 16));
				player.getIOSession().write(new StringPacket(player, "" + player.getSettings().getTokens()[0], 1057, 19));
				player.getIOSession().write(new StringPacket(player, "" + player.getSettings().getTokens()[0], 1057, 22));
				player.getIOSession().write(new StringPacket(player, "" + player.getSettings().getTokens()[0], 1057, 25));
				player.removeAttribute("update_warriors_guild");
			}
		}  else if (World.getWorld().getAreaManager().getAreaByName("BarrowsUnderground").contains(player.getLocation())) {
			if (!inBarrows || updateBarrows) {
				inBarrows = true;
				player.getPacketSender().sendOverlay(24);
				player.getPacketSender().sendConfig(453, slayedBrothers());
				player.getIOSession().write(new StringPacket(player, Integer.toString(player.getSettings().getKillCount()[0]), 24, 6));
				updateBarrows = false;
			} else {
				if (player.getAttribute("looted_barrows") == Boolean.TRUE) {
					return true;
				}
				if (player.getLocation().getZ() == 0 && player.getAttribute("barrows_tunnel") == null && player.getAttribute("canLoot") != Boolean.TRUE) {
					player.teleport(new Location(3565, 3307, 0));
					inBarrows = false;
				}
				if (barrowsDrainTime > 0) {
					barrowsDrainTime--;
				}
				if (barrowsDrainTime == 0) {
					List<Integer> killedBrothers = null;
					for (int i = 0; i < player.getSettings().getKilledBrothers().length; i++) {
						if (!player.getSettings().getKilledBrothers()[i]) {
							continue;
						}
						if (killedBrothers == null) {
							killedBrothers = new ArrayList<Integer>();
						}
						killedBrothers.add(i);
					}
					if (killedBrothers != null) {
						int head = 4761 + (killedBrothers.get(player.getRandom().nextInt(killedBrothers.size())) * 2);
						if (player.getLocation().getZ() == 0) {
							head++;
						}
						player.getPacketSender().closeOverlay().sendOverlay(24).sendBConfig(player, 1043, head);
					}
					barrowsDrainTime = 13 + Misc.random(5);
				}
			}
		} else if (World.getWorld().getAreaManager().getAreaByName("Duel").contains(player.getLocation())) {
			if (!inDuelArena) {
				inDuelArena = true;
				updateWildernessState(false);
				player.getIOSession().write(new PlayerOptionPacket(player, "Challenge", false, 1));
				player.getPacketSender().sendOverlay(638);
				player.getIOSession().write(new InterfaceConfig(player, 638, 1, true));
			} else {
				if (player.getAttribute("isInDuelArena", Boolean.FALSE) == Boolean.TRUE) {
					player.getIOSession().write(new PlayerOptionPacket(player, "Attack", true, 1));
				}
			}
		}
		return true;
	}

	public void updateWildernessState(boolean inWildy) {
		if (inWildy && !inWilderness) {
			inWilderness = true;
		} else if (!inWildy && inWilderness) {
			player.getPacketSender().closeOverlay();
			inWilderness = false;
		}
	}

	public void enableInterface(boolean multi) {
		/*player.getIOSession().write(new InterfaceConfig(player, 745, 1, !multi));
		player.getIOSession().write(new InterfaceConfig(player, 745, 3, false));
		player.getIOSession().write(new InterfaceConfig(player, 745, 6, false));*/
	}
	
	public int slayedBrothers() {
		int config = 0;
		for (int i = 0; i < player.getSettings().getKilledBrothers().length; i++) {
			if (!player.getSettings().getKilledBrothers()[i]) {
				continue;
			}
			config |= 1 << i;
		}
		int killCount = player.getSettings().getKillCount()[0];
		return (killCount << 1) << 16 | config;
	}

	public boolean inWilderness() {
		return inWilderness;
	}
	
	public void updateBarrowsInterface() {
		updateBarrows = true;
	}

	public boolean isInArdougne() {
		return World.getWorld().getAreaManager().getAreaByName("Ardougne").contains(player.getLocation());
	}
}
