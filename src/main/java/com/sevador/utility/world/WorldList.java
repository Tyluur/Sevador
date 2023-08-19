package com.sevador.utility.world;

import java.util.ArrayList;
import java.util.List;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.utility.Constants;


/**
 * Holds all the current worlds.
 * @author Dementhium development team
 *
 */
public class WorldList {
	
	/**
	 * The value for Australia.
	 */
    public static final int COUNTRY_AUSTRALIA = 16;

	/**
	 * The value for Belgium.
	 */
    public static final int COUNTRY_BELGIUM = 22;

	/**
	 * The value for Brazil.
	 */
    public static final int COUNTRY_BRAZIL = 31;

	/**
	 * The value for Canada.
	 */
    public static final int COUNTRY_CANADA = 38;

	/**
	 * The value for Denmark.
	 */
    public static final int COUNTRY_DENMARK = 58;

	/**
	 * The value for Finland.
	 */
    public static final int COUNTRY_FINLAND = 69;

	/**
	 * The value for Ireland.
	 */
    public static final int COUNTRY_IRELAND = 101;

	/**
	 * The value for Mexico.
	 */
    public static final int COUNTRY_MEXICO = 152;

	/**
	 * The value for the Netherlands.
	 */
    public static final int COUNTRY_NETHERLANDS = 161;

	/**
	 * The value for Norway.
	 */
    public static final int COUNTRY_NORWAY = 162;

	/**
	 * The value for Sweden.
	 */
    public static final int COUNTRY_SWEDEN = 191;

	/**
	 * The value for the UK.
	 */
    public static final int COUNTRY_UK = 77;

	/**
	 * The value for USA.
	 */
    public static final int COUNTRY_USA = 225;

	/**
	 * If the world should be highlighted.
	 */
    public static final int FLAG_HIGHLIGHT = 16;

	/**
	 * If the world is a lootshare world.
	 */
    public static final int FLAG_LOOTSHARE = 8;

	/**
	 * If the world is a members world.
	 */
    public static final int FLAG_MEMBERS = 1;

	/**
	 * If the world is free to play.
	 */
    public static final int FLAG_NON_MEMBERS = 0;

	/**
	 * If the world is a PvP-world.
	 */
    public static final int FLAG_PVP = 4;

    /**
     * A list holding all the currently loaded worlds.
     */
    private static final List<WorldDefinition> WORLD_LIST = new ArrayList<WorldDefinition>();

    /**
     * Populates the world list.
     */
    static {
        WORLD_LIST.add(new WorldDefinition(1, 0, FLAG_MEMBERS, "World 1", Constants.isWindows() ? "127.0.0.1" : "lethium.no-ip.org", "USA", COUNTRY_USA));
    }

    /**
     * Gets the packet to update the world list in the lobby.
     * @param player The player.
     * @param worldConfiguration If the configuration should be added.
     * @param worldStatus If the status should be added.
     * @return The {@code OutgoingPacket} to write.
     */
    public static OutgoingPacket getData(Player player, boolean worldConfiguration, boolean worldStatus) {
        OutgoingPacket bldr = new OutgoingPacket(player, 23, PacketType.VAR_SHORT);
        bldr.put(1);
        bldr.put(2);
        bldr.put(1);
        if (worldConfiguration) {
            populateConfiguration(bldr);
        }
        if (worldStatus) {
            populateStatus(bldr);
        }
        return bldr;
    }

    /**
     * Adds the world configuration on the packet.
     * @param buffer The current packet.
     */
    private static void populateConfiguration(OutgoingPacket buffer) {
        buffer.putSmart(WORLD_LIST.size());
        setCountry(buffer);
        buffer.putSmart(0);
        buffer.putSmart(WORLD_LIST.size() + 1);
        buffer.putSmart(WORLD_LIST.size());
        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getWorldId());
            buffer.put(w.getLocation());
            buffer.putInt(w.getFlag());
            buffer.putGJString(w.getActivity());
            buffer.putGJString(w.getIp());
        }
        buffer.putInt(0x94DA4A87);
    }

    /**
     * Adds the world status on the packet.
     * @param buffer The current packet.
     */
    private static void populateStatus(OutgoingPacket buffer) {
        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getWorldId());
			buffer.putShort(NodeWorker.getPlayers().size());
        }
    }

    /**
     * Sets the countries for each world.
     * @param buffer The current packet.
     */
    private static void setCountry(OutgoingPacket buffer) {
        for (WorldDefinition w : WORLD_LIST) {
            buffer.putSmart(w.getCountry());
            buffer.putGJString(w.getRegion());
        }
    }
}
