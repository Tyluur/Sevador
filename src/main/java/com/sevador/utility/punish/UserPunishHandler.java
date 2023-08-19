package com.sevador.utility.punish;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sevador.game.node.player.Player;

/**
 * Handles the punishments of users, eg. banning, muting, ...
 * @author Emperor
 *
 */
public final class UserPunishHandler {

	/**
	 * The mapping of currently active punishments.
	 */
	private static final Map<String, List<Punishment>> PUNISHMENTS = new HashMap<String, List<Punishment>>();

	/**
	 * The mapping of currently active ip-address dependant punishments.
	 */
	private static final Map<String, List<Punishment>> IP_PUNISHMENTS = new HashMap<String, List<Punishment>>();
	
	/**
	 * Initializes the punishment handler.
	 */
	public static void init() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("./data/nodes/punishments.txt"));
			String s;
			boolean userBased = true;
			while ((s = br.readLine()) != null) {
				if (s.startsWith("punish>")) {
					s = s.replace("punish>", "");
					String key = s.split("<=")[0];
					s = s.replace(new StringBuilder(key).append("<=[type=").toString(), "");
					String[] data = s.replace("duration=", "").replace("]/>", "").split(", ");
					PunishType type = Enum.valueOf(PunishType.class, data[0]);
					flag(key, new Punishment(type, Long.parseLong(data[1])), !userBased);
				} else {
					userBased = s.equals("punishments:user-based>");
				}
			}
			br.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Gets called but is not limited to when the server is shutting down.
	 * @throws IOException When an I/O exception occurs.
	 */
	public static void onShutDown() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter("./data/nodes/punishments.txt"));
		bw.write("punishments:user-based>\n");		
		for (String key : PUNISHMENTS.keySet()) {
			List<Punishment> punishments = PUNISHMENTS.get(key);
			for (Punishment p : punishments) {
				bw.write(new StringBuilder("punish>").append(key).append("<=[type=").append(p.getType()).append(", duration=").append(p.getDuration()).append("]/>\n").toString());
			}
		}
		bw.write("</>\n");
		bw.write("punishments:address-based>\n");	
		for (String key : IP_PUNISHMENTS.keySet()) {
			List<Punishment> punishments = IP_PUNISHMENTS.get(key);
			for (Punishment p : punishments) {
				bw.write(new StringBuilder("punish>").append(key).append("<=[type=").append(p.getType()).append(", duration=").append(p.getDuration()).append("]\n").toString());
			}
		}	
		bw.write("</>\n");
		bw.flush();
		bw.close();
	}
	
	/**
	 * Adds a new punishment to the mapping.
	 * @param key The username or ip-address if the user has to be ip-punished.
	 * @param punishment The punishment to add.
	 * @param ip If the punishment should be handled as an ip-punishment.
	 */
	public static void flag(String key, Punishment punishment, boolean ip) {
		List<Punishment> punishments = ip ? IP_PUNISHMENTS.get(key) : PUNISHMENTS.get(key);
		if (punishments == null) {
			punishments = new ArrayList<Punishment>();
			if (ip) {
				IP_PUNISHMENTS.put(key, punishments);
			} else {
				PUNISHMENTS.put(key, punishments);
			}
		}
		punishments.add(punishment);
	}

	/**
	 * Removes a punishment from the mapping.
	 * @param key The username or ip-address.
	 * @param punishType The punish type.
	 * @param ipBased If the punishment was ip-based.
	 */
	public static void unflag(String key, PunishType punishType, boolean ipBased) {
		List<Punishment> punishments = ipBased ? IP_PUNISHMENTS.get(key) : PUNISHMENTS.get(key);
		if (punishments == null) {
			return;
		}
		for (Iterator<Punishment> it = punishments.iterator(); it.hasNext();) {
			Punishment p = it.next();
			if (p.getType() == punishType) {
				it.remove();
			}
		}
		if (punishments.isEmpty()) {
			if (ipBased) {
				IP_PUNISHMENTS.remove(key);
			} else {
				PUNISHMENTS.remove(key);
			}
		}
	}

	/**
	 * Checks if a player is muted.
	 * @param p The player.
	 * @return {@code True} if so.
	 */
	public static boolean isMuted(Player p) {
		return isPunished(p.getCredentials().getUsername(), PunishType.MUTE) 
				|| isIpPunished(p.getCredentials().getLastAddress(), PunishType.MUTE);
	}
	
	/**
	 * Checks if a player is banned.
	 * @param p The player.
	 * @return {@code True} if the player is banned.
	 */
	public static boolean isBanned(Player p) {
		return isPunished(p.getCredentials().getUsername(), PunishType.BAN) 
				|| isIpPunished(p.getCredentials().getLastAddress(), PunishType.BAN);
	}
	
	/**
	 * Checks if a user is punished.
	 * @param username The user name.
	 * @param type The punish type we're checking.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public static boolean isPunished(String username, PunishType type) {
		if (!PUNISHMENTS.containsKey(username)) {
			return false;
		}
		List<Punishment> punishments = PUNISHMENTS.get(username);
		for (Iterator<Punishment> it = punishments.iterator(); it.hasNext();) {
			Punishment p = it.next();
			if (!p.isPermanent() && p.getDuration() < System.currentTimeMillis()) {
				it.remove();
				if (punishments.isEmpty()) {
					PUNISHMENTS.remove(username);
					return false;
				}
				continue;
			}
			if (p.getType() == type) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if a user is ip-punished.
	 * @param address The user's ip-address.
	 * @param type The punish type we're checking.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public static boolean isIpPunished(String address, PunishType type) {
		if (!IP_PUNISHMENTS.containsKey(address)) {
			return false;
		}
		List<Punishment> punishments = IP_PUNISHMENTS.get(address);
		for (Iterator<Punishment> it = punishments.iterator(); it.hasNext();) {
			Punishment p = it.next();
			if (!p.isPermanent() && p.getDuration() < System.currentTimeMillis()) {
				it.remove();
				if (punishments.isEmpty()) {
					IP_PUNISHMENTS.remove(address);
					return false;
				}
				continue;
			}
			if (p.getType() == type) {
				return true;
			}
		}
		return false;
	}
}