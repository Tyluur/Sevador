package com.sevador.update;

import java.util.LinkedList;
import java.util.List;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Holds the player's rendering information.
 * 
 * @author Jolt environment v2 development team
 * @author Emperor (converted to Java + NPC information).
 *
 */
public class RenderInformation {

	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * Holds the players' hash locations.
	 */
	public final int[] hashLocations = new int[2048];
	
	/**
	 * The amount of local players.
	 */
	public int localsCount = 0;
	
	/**
	 * The amount of global players.
	 */
	public int globalsCount = 0;
	
	/**
	 * The local player indexes.
	 */
	public final short[] locals = new short[2048];
	
	/**
	 * The global player indexes.
	 */
	public final short[] globals = new short[2048];
	
	/**
	 * The local players.
	 */
	public final boolean[] isLocal = new boolean[2048];
	
	/**
	 * The skipped player indexes.
	 */
	public final byte[] skips = new byte[2048];

	/**
	 * The list of local NPCs.
	 */
	private List<NPC> localNpcs = new LinkedList<NPC>();
    
    /**
     * The player's last location.
     */
    private Location lastLocation;
    
    /**
     * If the player has just logged in.
     */
    private boolean onFirstCycle;

    /**
     * The amount of added players in the current update cycle.
     */
	private int added;
    
	/**
	 * Constructs a new {@code RenderInformation} {@code Object}.
	 * @param player The player.
	 */
	public RenderInformation(Player player) {
		this.player = player;
		this.onFirstCycle = true;
	}
	
	/**
	 * Updates the player's map region packet with player information.
	 * @param packet The packet.
	 */
	public void enterWorld(OutgoingPacket packet) {
        int myindex = player.getIndex();
        locals[localsCount++] = (short) myindex;
        isLocal[myindex] = true;
        hashLocations[myindex] = 0;
        packet.startBitAccess();
        packet.putBits(30, player.getLocation().get30BitsHash());
        for (short index = 1; index < 2048; index++) {
            if (index == myindex) {
                continue;
            }
            globals[globalsCount++] = index;
        	Player p = NodeWorker.getPlayers().get(index);
        	if (p == null || !p.isLoggedIn()) {
        		packet.putBits(18, 0);
        		continue;
        	}
        	packet.putBits(18, p.getLocation().get18BitsHash());
        }
        packet.finishBitAccess();
	}
	
	/**
	 * Updates the player rendering information.
	 */
	public void updateInformation() {
		localsCount = 0;
		globalsCount = 0;
		added = 0;
        onFirstCycle = false;
        lastLocation = player.getLocation();
        for (short i = 1; i < 2048; i++) {
        	skips[i] >>= 1;
        	if (isLocal[i]) {
        		locals[localsCount++] = i;
        	} else {
        		globals[globalsCount++] = i;
        	}
        	Player p = NodeWorker.getPlayers().get(i);
        	if (p != null && p.isLoggedIn()) {
                hashLocations[i] = p.getLocation().get18BitsHash();
        	}
        }
        player.getPlayerFlags().setTeleported(false);
	}

	/**
	 * @return the lastLocation
	 */
	public Location getLastLocation() {
		return lastLocation;
	}

	/**
	 * @return the onFirstCycle
	 */
	public boolean isOnFirstCycle() {
		return onFirstCycle;
	}

	/**
	 * Gets the list of local npcs.
	 * @return The list of local npcs.
	 */
	public List<NPC> getLocalNPCs() {
		return localNpcs;
	}

	/**
	 * Gets the amount of currently added players in this cycle. 
	 * @return The amount, incremented.
	 */
	public int getAddedIncr() {
		return added++;
	}

}