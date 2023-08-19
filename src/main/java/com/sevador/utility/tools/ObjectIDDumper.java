package com.sevador.utility.tools;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.ObjectDefinition;

/**
 * Generates the Object id of the specified name.
 * 
 * @author Tyluur
 * 
 */
public class ObjectIDDumper {
	/**
	 * The object name.
	 */
	private static final String KEY = "gilded";

	/**
	 * The output format.
	 */
	private static final String FORMAT = "EventManager.register(@id@, this);//@name@";
	

	/**
	 * The main method.
	 * 
	 * @param args
	 *            The arguments cast on runtime.
	 */
	public static void main(String[] args) {
		Cache.init();
		for (int i = 0; i < Cache.getAmountOfObjects(); i++) {
			ObjectDefinition def = ObjectDefinition.forId(i);
			if (def.getName().toLowerCase().contains(KEY)) {
				System.out.println(FORMAT.replace("@name@", def.getName())
						.replace("@id@", "" + i));
			}
		}
	}

}
