package com.sevador.game.world;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.tick.Tick;
import net.burtleburtle.tick.impl.CleanupTick;
import net.burtleburtle.tick.impl.GrandExchangeTick;
import net.burtleburtle.tick.impl.GroundItemUpdateTick;
import net.burtleburtle.tick.impl.RestorationTick;

import com.sevador.Main;
import com.sevador.content.friendChat.FriendChatManager;
import com.sevador.content.grandExchange.GrandExchange;
import com.sevador.content.misc.AreaManager;
import com.sevador.database.ConnectionPool;
import com.sevador.database.DatabaseConnection;
import com.sevador.database.mysql.MySQLDatabaseConfiguration;
import com.sevador.database.mysql.MySQLDatabaseConnection;
import com.sevador.game.dialogue.DialogueHandler;
import com.sevador.game.node.control.ControlerHandler;
import com.sevador.game.node.npc.DropLoader;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.path.PathEvent;
import com.sevador.utility.EntityList;
import com.sevador.utility.ItemBonuses;
import com.sevador.utility.MapData;
import com.sevador.utility.configuration.ConfigurationNode;
import com.sevador.utility.configuration.ConfigurationParser;
import com.sevador.utility.loaders.NPCCombatDefinitionsL;

/**
 * The game-world, holds and initializes several aspects of the game.
 * 
 * @author Emperor
 * @author Tyluur
 */
public class World {

	/**
	 * An executor service which handles background loading tasks.
	 */
	private static final ExecutorService BACKGROUND_LOADER = Executors.newSingleThreadExecutor();
	/**
	 * The MySQL Connection pool
	 */
	private ConnectionPool<? extends DatabaseConnection> connectionPool;

	/**
	 * A list of ticks to execute.
	 */
	private static final List<Tick> TICKS = new LinkedList<Tick>();

	/**
	 * The instance of the world's area manager.
	 */
	private AreaManager areaManager;

	/**
	 * Initializes the game world.
	 */
	public void initialize() {
		try {
			Cache.init();
			loadConfiguration();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
		FriendChatManager.getFriendChatManager();
		MapData.init();
		NodeInitializer.init();
		NPCCombatDefinitionsL.init();
		ControlerHandler.init();
		DialogueHandler.init();
		ItemBonuses.init();
		GrandExchange.init();
		areaManager = new AreaManager();
		TICKS.add(CleanupTick.getSingleton());
		TICKS.add(RestorationTick.getSingleton());
		TICKS.add(GroundItemUpdateTick.getSingleton());
		TICKS.add(GrandExchangeTick.getSingleton());
		World.getWorld().getDroploader().load();
		Main.getLogger().info("World loaded.");
	}

	/**
	 * Loads server configuration.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws ClassNotFoundException
	 *             if a class loaded through reflection was not found.
	 * @throws IllegalAccessException
	 *             if a class could not be accessed.
	 * @throws InstantiationException
	 *             if a class could not be created.
	 */
	private void loadConfiguration() throws IOException,
	ClassNotFoundException, InstantiationException,
	IllegalAccessException {
		FileInputStream fis = new FileInputStream("data/server.conf");
		try {
			ConfigurationParser parser = new ConfigurationParser(fis);
			ConfigurationNode mainNode = parser.parse();
			if (mainNode.has("database")) {
				ConfigurationNode databaseNode = mainNode.nodeFor("database");
				MySQLDatabaseConfiguration config = new MySQLDatabaseConfiguration();
				long ms = System.currentTimeMillis();
				config.setHost(databaseNode.getString("host"));
				config.setPort(databaseNode.getInteger("port"));
				config.setDatabase(databaseNode.getString("database"));
				config.setUsername(databaseNode.getString("username"));
				config.setPassword(databaseNode.getString("password"));
				connectionPool = new ConnectionPool<MySQLDatabaseConnection>(config);
				Main.getLogger().info("Connected to " + config.getHost()+":"+config.getPort()+" in " + (System.currentTimeMillis() - ms) + " ms.");
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			fis.close();
		}
	}


	/**
	 * Gets the background loader.
	 * 
	 * @return The backgroundLoader.
	 */
	public synchronized ExecutorService getBackgroundLoader() {
		return BACKGROUND_LOADER;
	}

	/**
	 * Submits a new tick to execute.
	 * 
	 * @param tick
	 *            The tick.
	 */
	public void submit(Tick tick) {
		TICKS.add(tick);
	}

	/**
	 * Submits a new {@code PathEvent} on the paths list.
	 * 
	 * @param path
	 *            The path event.
	 */
	public static void submit(PathEvent path) {
		path.run();
	}

	/**
	 * @return the ticks
	 */
	public static List<Tick> getTicks() {
		return TICKS;
	}

	private static final World SINGLETON = new World();

	public static World getWorld() {
		return SINGLETON;
	}

	public Player getPlayerInServer(String name) {
		Player player = getPlayer(name, false);
		if (player != null)
			return player;
		return getPlayer(name, true);
	}

	public Player getPlayer(String name, boolean lobby) {
		EntityList<Player> playerCheckList = lobby ? NodeWorker.getLobbyPlayers() : NodeWorker.getPlayers();
		for (Player pl : playerCheckList) {
			if (pl.getCredentials().getUsername().equals(name)) {
				return pl;
			}
		}
		return null;
	}

	/**
	 * @return the droploader
	 */
	public DropLoader getDroploader() {
		return dropLoader;
	}

	/**
	 * @return the connectionPool
	 */
	public ConnectionPool<? extends DatabaseConnection> getConnectionPool() {
		return connectionPool;
	}

	/**
	 * @param connectionPool the connectionPool to set
	 */
	public void setConnectionPool(ConnectionPool<? extends DatabaseConnection> connectionPool) {
		this.connectionPool = connectionPool;
	}

	/**
	 * @return the areaManager
	 */
	public AreaManager getAreaManager() {
		return areaManager;
	}

	/**
	 * @param areaManager the areaManager to set
	 */
	public void setAreaManager(AreaManager areaManager) {
		this.areaManager = areaManager;
	}

	private transient final DropLoader dropLoader = new DropLoader();


}