package com.sevador.content.grandExchange;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sevador.Main;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.utility.Constants;

/**
 * Represents the Grand Exchange of the World.
 * 
 * @author Jefferson
 * 
 */
public class GrandExchange {

	private static final String FILE_LOCATION = (Constants.isWindows() ? "data/grandExchange.ser"
			: "/root/server/grandExchange.ser");

	/**
	 * Offers Database Location.
	 */
	public static final File DATABASE_LOCATION = new File(FILE_LOCATION
			+ PlayerWorldLoader.suffix);

	/**
	 * World offers.
	 */
	private static CopyOnWriteArrayList<ItemOffer> offers;

	/**
	 * @return the offers
	 */
	public static CopyOnWriteArrayList<ItemOffer> getOffers() {
		return offers;
	}

	/**
	 * Creates a new Instance in the JVM.
	 */
	/*	@SuppressWarnings("unchecked")
	public GrandExchange() {
		try {
			if (!DATABASE_LOCATION.exists()) {
				offers = new ArrayList<ItemOffer>();
			} else {
				offers = (ArrayList<ItemOffer>) PlayerWorldLoader.load(DATABASE_LOCATION);
				Main.getLogger().info("Loaded " + offers.size() + " grand exchange offers successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	@SuppressWarnings("unchecked")
	public static void init() {
		File file = new File(FILE_LOCATION);
		if (file.exists())
			try {
				offers = (CopyOnWriteArrayList<ItemOffer>) PlayerWorldLoader.loadSerializedFile(file);
				Main.getLogger().info("Loaded " + offers.size() + " grand exchange item offers!");
				return;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		offers = new CopyOnWriteArrayList<ItemOffer>();
	}

	public static final void save() {
		try {
			PlayerWorldLoader.storeSerializableClass(offers, new File(FILE_LOCATION));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
