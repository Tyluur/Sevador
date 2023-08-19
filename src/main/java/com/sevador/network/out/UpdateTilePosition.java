package com.sevador.network.out;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Updates the tile position.
 * @author Lumby
 *
 */
public class UpdateTilePosition extends OutgoingPacket {

	/**
	 * The location.
	 */
	private final Location location;
	
	/**
	 * Constructs a new {@code UpdateTilePosition} {@code Object}.
	 * @param player The player.
	 * @param location The location.
	 */
	public UpdateTilePosition(Player player, Location location){
		super(player, 77);
		this.location = location;
	}
	
	@Override
	public OutgoingPacket get(){
		int localX = location.getRegionX() - (getPlayer().getRegion().getRegionX() - 6);
		int localY = location.getRegionY() - (getPlayer().getRegion().getRegionY() - 6);
		return putByteS(location.getZ()).//z
			putByteC(localX).//x
			putByteA(localY);//y
	}
}
