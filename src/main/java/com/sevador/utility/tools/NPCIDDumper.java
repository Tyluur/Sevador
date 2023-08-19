package com.sevador.utility.tools;

import net.burtleburtle.cache.Cache;
import net.burtleburtle.cache.format.NPCDefinition;

/**
 * Generates the NPCS id of the specified name.
 * 
 * @author Tyluur
 * 
 */
public class NPCIDDumper {
	
	private static final String NAME = "vote";
	
	public static void main(String[] args) {
		Cache.init();
		for (int i = 0; i < Cache.getAmountOfNpcs(); i++) {
			NPCDefinition def = NPCDefinition.forId(i);
			if (def.name.toLowerCase().contains(NAME)) {
				System.err.println(i + " << " + def.name);
				//System.err.println("EventManager.register(" + i + ", this);");
			}
		}
	}
	

}
