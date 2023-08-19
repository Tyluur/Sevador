package com.sevador.game.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.burtleburtle.thread.NodeWorker;

import org.jboss.netty.channel.ChannelFutureListener;

import com.sevador.Main;
import com.sevador.database.DatabaseConnection;
import com.sevador.game.node.Item;
import com.sevador.game.node.Node;
import com.sevador.game.node.player.Player;
import com.sevador.network.IOSession;
import com.sevador.network.OutgoingPacket;
import com.sevador.utility.Constants;
import com.sevador.utility.Misc;
import com.sevador.utility.ReturnCodes;
import com.sevador.utility.punish.UserPunishHandler;

/**
 * Handles the player world loading.
 * 
 * @author Emperor
 * 
 * @param <N>
 *            The node type.
 */
public final class PlayerWorldLoader implements WorldLoader<Player> {

	@Override
	public LoginResult checkLogin(Node node) {
		Player player = (Player) node;
		/*if (!player.getIOSession().isInLobby() && NodeWorker.getPlayer(player.getCredentials().getUsername()) != null) {
			System.out.println("Return code [type=" + ReturnCodes.ALREADY_ONLINE + "].");
			return new LoginResult(ReturnCodes.ALREADY_ONLINE, player);
		} else */
		if (player.getIOSession().isInLobby() 
				&& (NodeWorker.getLobbyPlayer(player.getCredentials().getUsername()) != null 
				|| NodeWorker.getPlayer(player.getCredentials().getUsername()) != null)) {
			return new LoginResult(ReturnCodes.ALREADY_ONLINE, player);
		} else if (!player.getIOSession().isInLobby() && NodeWorker.getPlayers().size() >= Constants.MAXIMUM_PLAYERS) {
			return new LoginResult(ReturnCodes.FULL_WORLD, player);
		} else if (player.getIOSession().isInLobby() && NodeWorker.getLobbyPlayers().size() >= Constants.MAXIMUM_PLAYERS) {
			return new LoginResult(ReturnCodes.FULL_WORLD, player);
		} else if (UserPunishHandler.isBanned(player)) {
			return new LoginResult(ReturnCodes.ACCOUNT_DISABLED, player);
		}
		if (!Constants.MULTILOG_ALLOWED) {
			for (Player pl : NodeWorker.getPlayers()) {
				String myIP = Misc.formatIp(player.getIOSession().getChannel()
						.getRemoteAddress().toString());
				String playerIps = Misc.formatIp(pl.getIOSession().getChannel()
						.getRemoteAddress().toString());
				if (player.getIOSession().isInLobby() && myIP.equals(playerIps)) {
					return new LoginResult(ReturnCodes.LOGIN_LIMIT_EXCEEDED,
							player);
				}
			}
			for (Player pl : NodeWorker.getLobbyPlayers()) {
				String myIP = Misc.formatIp(player.getIOSession().getChannel()
						.getRemoteAddress().toString());
				String playerIps = Misc.formatIp(pl.getIOSession().getChannel()
						.getRemoteAddress().toString());
				if (player.getIOSession().isInLobby() && myIP.equals(playerIps)) {
					return new LoginResult(ReturnCodes.LOGIN_LIMIT_EXCEEDED,
							player);
				}
			}
		}
		return new LoginResult(ReturnCodes.SUCCESFUL, player);
	}

	@Override
	public Player load(LoginResult result) {
		Player player = (Player) result.getNode();
		int code = result.getReturnCode().getValue();
		OutgoingPacket response = new OutgoingPacket(player, -1);
		if (code != 2) {
			response.put(code);
			if (player != null && player.getIOSession() != null && response != null	&& player.getIOSession().write(response) != null)
				player.getIOSession().write(response).addListener(ChannelFutureListener.CLOSE);
			return null;
		}
		File saved = new File(Constants.SAVE_PATH + player.getCredentials().getUsername() + "" + suffix + "");
		
		if (player != null && !saved.exists()) {
			save(player);
		}
		String username = player.getCredentials().getUsername();
		IOSession session = player.getIOSession();
		if (NodeWorker.containsPlayer(username)) {
			code = ReturnCodes.ALREADY_ONLINE.getValue();
		}
		String pass = player.getCredentials().getPassword();
		if (saved.exists()) {
			player = (Player) load(saved);
			if (player == null) {
				code = ReturnCodes.ERROR_LOADING_PROFILE.getValue();
				player = (Player) result.getNode();
				boolean deleted = saved.delete();
				System.err.println("" + saved.getName() + " is a nulled file, deleted? " + deleted);
			}
			player.setIOSession(session);
		}
		player.getIOSession().setPlayer(player);
		response.put(code);
		player.getIOSession().write(response);
		if (code != 2) {
			return null;
		}
		return player;
	}

	@Override
	public Player save(Node node) {
		return save((Player) node, false);
	}

	/**
	 * Saves the player.
	 * 
	 * @param player
	 *            The player.
	 * @param force
	 *            If we should force the saving.
	 * @return The player instance.
	 */
	public Player save(Player player, boolean force) {
		if (!player.getIOSession().isInLobby()) {
			if (!force) {
				if (player != null && player.getCombatAction() != null && player.getCombatAction().getLastAttacker() != null) {
					return null;
				}
			}
			player.getActionManager().clear();
		}
		store(player, new File("" + Constants.SAVE_PATH + ""
				+ player.getCredentials().getUsername() + "" + suffix + ""));
		return player;
	}

	/**
	 * Deserialises the file and returns the constructed object.
	 * 
	 * @param f
	 *            The file to load.
	 * @return The constructed object.
	 */
	public static Object load(File f) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			Object object = in.readObject();
			in.close();
			return object;
		} catch (Throwable t) {
			Main.getLogger().info(f.getName().replaceAll("" + suffix + "", "")+ " is nulled, deleting it now.");
			f.delete();
			t.printStackTrace();
		}
		return null;
	}

	/**
	 * Stores the serializable object to a file.
	 * 
	 * @param o
	 *            The serializable object.
	 * @param f
	 *            The file to store to.
	 */
	public synchronized static void store(Serializable o, File f) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(f));
			out.writeObject(o);
			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates all player profiles.
	 */
	public static void fixAll() {
		final File[] directory = new File(Constants.SAVE_PATH).listFiles();
		int fixCount = 0;
		int totalCount = 0;
		for (File child : directory) {
			if (fixPlayer(child)) {
				fixCount++;
			}
			totalCount++;
		}
		System.err.println("Finished " + fixCount + " / " + totalCount
				+ " profiles.");
	}

	public static final Object loadSerializedFile(File f) throws IOException, ClassNotFoundException {
		try {
			if (!f.exists())
				return null;
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			Object object = in.readObject();
			in.close();
			return object;
		} catch (Throwable e) {
			Main.getLogger().info("ERROR!: " + f.getAbsolutePath() + " is nulled! Exitting JVM.");
			f.delete();
			System.exit(0);
		}
		return null;
	}


	/**
	 * Updates or replaces an existing serialized object from a file with the
	 * new one.
	 * 
	 * @param f
	 *            The old file
	 * @param o
	 * 
	 */
	public static boolean fixPlayer(File f) {
		if (!f.exists()) {
			System.err.println("Don't exists!");
			return false;
		}
		Player old = (Player) load(f);
		if (old == null) {
			System.err.println("Profile fucked up!");
			return false;
		}
		Player fixed = new Player(old.getCredentials().getUsername(), old
				.getCredentials().getPassword());
		fixed.setSkills(old.getSkills());
		for (int i = 0; i < old.getInventory().size(); i++) {
			Item item = old.getInventory().get(i);
			if (item != null) {
				System.out.println("Adding item: "
						+ item.getDefinition().getName() + " to inv.");
				fixed.getInventory().add(item);
			} else {
				System.out.println("Don't have items at inventory.");
			}
		}
		for (int j = 0; j < old.getEquipment().size(); j++) {
			Item item2 = old.getEquipment().get((byte) j);
			if (item2 != null) {
				System.out.println("Adding item: "
						+ item2.getDefinition().getName() + " to equipment.");
				fixed.getEquipment().add(item2);
			} else {
				System.out.println("Don't have items equipted.");
			}
		}
		for (int k = 0; k < old.getBank().getContainer().size(); k++) {
			Item item3 = old.getBank().getContainer().get(k);
			if (item3 != null) {
				System.out.println("Adding item: "
						+ item3.getDefinition().getName() + " to bank.");
				fixed.getBank().getContainer().add(item3);
			} else {
				System.out.println("Don't have items at bank.");
			}
		}
		store(fixed, new File(Constants.SAVE_PATH
				+ old.getCredentials().getUsername() + "-fixed" + suffix));
		System.err.println("Fixed: " + old.getCredentials().getUsername()
				+ " profile!");
		return true;
	}

	private boolean verifyPassword(String inputPassword,
			String storedPasswordHash, String storedSalt) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
		byte[] inputPasswordHash = digest.digest(inputPassword.getBytes());
		digest.reset();

		byte[] storedSaltHash = digest.digest(storedSalt.getBytes());
		digest.reset();

		byte[] finalHash = digest.digest(new StringBuilder().append(toHexString(storedSaltHash)).append(toHexString(inputPasswordHash)).toString().getBytes());
		return toHexString(finalHash).equalsIgnoreCase(storedPasswordHash);
	}

	private String toHexString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfByte = (data[i] >>> 4) & 0x0F;
			int twoHalfs = 0;
			do {
				if ((0 <= halfByte) && (halfByte <= 9))
					buf.append((char) ('0' + halfByte));
				else
					buf.append((char) ('a' + (halfByte - 10)));
				halfByte = data[i] & 0x0F;
			} while (twoHalfs++ < 1);
		}
		return buf.toString();
	}

	public static final String suffix = ".ser";

	public static final void storeSerializableClass(Serializable o, File f) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(o);
		out.close();
	}


}
