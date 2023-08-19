package com.sevador.update;

import java.util.List;

import net.burtleburtle.seq.impl.EntityUpdateSequence;

import com.sevador.game.node.player.Player;
import com.sevador.game.region.Region;
import com.sevador.game.region.RegionManager;
import com.sevador.network.out.BuildSceneGraph;
import com.sevador.network.out.NPCRendering;
import com.sevador.network.out.PlayerRendering;

/**
 * The region updating task handles a series of region updates.
 * 
 * @author Emperor
 *
 */
public class RegionUpdate implements Runnable {

	/**
	 * The first region slot to update.
	 */
	private int start;
	
	/**
	 * The ending region slot to update.
	 */
	private int end;
	
	/**
	 * The updating sequence.
	 */
	private final EntityUpdateSequence updateSequence;
	
	/**
	 * Constructs a new {@code RegionUpdate} {@code Object}.
	 * @param updateSequence The entity updating sequence.
	 */
	public RegionUpdate(EntityUpdateSequence updateSequence) {
		this.updateSequence = updateSequence;
	}
	
	@Override
	public void run() {
		try {
			for (int i = start; i < end; i++) {
				Region r = (Region) RegionManager.getRegions().values().toArray()[i];
				for (int z = 0; z < 4; z++) {
					List<Player> players = r.getPlayers()[z];
					for (Player p : players) {
						if (p != null && p.isLoggedIn()) {
							if (p.getPlayerFlags().isMapRegionChanged()) {
								p.getIOSession().write(new BuildSceneGraph(p, false));
							}
							p.getIOSession().write(new PlayerRendering(p));
							p.getIOSession().write(new NPCRendering(p));
						}
					}
				}
			}
			updateSequence.finalization(null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

}