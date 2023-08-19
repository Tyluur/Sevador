package com.sevador;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import net.burtleburtle.cores.CoresManager;
import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.thread.NodeWorker;
import net.burtleburtle.thread.ThreadWorkingSet;

import com.sevador.game.world.World;
import com.sevador.network.ChannelHandler;
import com.sevador.network.KeyMap;
import com.sevador.utility.Constants;
import com.sevador.utility.ServerLogger;

/**
 * Helper class destinated to Start the Server.
 * 
 * @author Tyluur
 * 
 */
public class Main {

	/**
	 * The cached thread pool executor instance.
	 * 
	 * @see Executors#newFixedThreadPool()
	 */
	private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/**
	 * The {@link NodeWorker} instance.
	 */
	private static final NodeWorker NODE_WORKER = new NodeWorker();

	/**
	 * The {@link ServerLogger } instance.
	 */

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	/**
	 * The {@code ThreadWorkingSet} instance.
	 */
	private static final ThreadWorkingSet WORKING_SET = new ThreadWorkingSet();

	/**
	 * If We Should Debug.
	 */
	public static boolean DEBUG = Constants.isWindows();

	/**
	 * Program Running time.
	 */
	public static long UPTIME;

	/**
	 * The Default Virtual Machine Running method,
	 * Also initiate all the Managers.
	 * 
	 * @param args
	 *            Program arguments.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//System.setOut(new ServerLogger(System.out));
		CoresManager.init();
		UPTIME = System.currentTimeMillis();
		EXECUTOR_SERVICE.submit(NODE_WORKER);
		EXECUTOR_SERVICE.submit(new MajorUpdateWorker());
		KeyMap.initialize();
		World.getWorld().initialize();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		if (!ChannelHandler.init()) {
			System.exit(1);
			return;
		}
		System.gc();
		System.runFinalization();
		Main.getLogger().info("Server took " + "(" + (System.currentTimeMillis() - UPTIME) + " ms) to Run.");
	}

	/**
	 * Gets the {@code NodeWorker} instance used.
	 * 
	 * @return The nodeWorker.
	 */
	public synchronized static NodeWorker getNodeWorker() {
		return NODE_WORKER;
	}

	/**
	 * Gets the working set for threads.
	 * 
	 * @return The working set for threads.
	 */
	public synchronized static ThreadWorkingSet getWorkingSet() {
		return WORKING_SET;
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

}
