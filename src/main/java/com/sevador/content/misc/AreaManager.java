package com.sevador.content.misc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import net.burtleburtle.script.XMLHandler;

import com.sevador.utility.world.Area;

/**
 * Holds info for all the areas.
 *
 * @author Stephen
 */

public class AreaManager {

    private static HashMap<String, Area> loadedAreas = new HashMap<String, Area>();

    public AreaManager() {
        System.out.println("Loading areas...");
        try {
            List<Area> tempAreas = XMLHandler.fromXML("data/xml/areas.xml");
            for (Area area : tempAreas) {
            	loadedAreas.put(area.getName().toLowerCase(), area);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Loaded " + loadedAreas.size() + " areas.");
    }

    public Area getAreaByName(String name) {
        if (loadedAreas.containsKey(name.toLowerCase())) {
        	return loadedAreas.get(name.toLowerCase());
        }
        return null;
        //throw new IllegalArgumentException("Area [ "+name+" ] does not exist");
    }

    public Collection<Area> getAreas() {
        return loadedAreas.values();
    }
}
