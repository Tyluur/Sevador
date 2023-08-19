package com.sevador.network.out;

import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * The packet handling the removing of an object.
 * @author Emperor
 *
 */
public class RemoveObject extends OutgoingPacket {

	/**
	 * The game object to remove.
	 */
	private final GameObject object;
	
	/**
	 * Constructs a new {@code RemoveObject} {@code Object}.
	 * @param player The player.
	 * @param object The game object to remove.
	 */
	public RemoveObject(Player player, GameObject object) {
		super(player, 110);
		this.object = object;
	}
	
	@Override
	public OutgoingPacket get() {
		int localX = object.getLocation().getX() - (getPlayer().getRegion().getRegionX() - 6) * 8;
		int localY = object.getLocation().getY() - (getPlayer().getRegion().getRegionY() - 6) * 8;
		getPlayer().getIOSession().write(new UpdateTilePosition(getPlayer(), object.getLocation()));
		put(((localX - ((localX >> 3) << 3)) << 4) | ((localY - ((localY >> 3) << 3)) & 0x7));
		putByteC((object.getType() << 2) + (object.getRotation() & 3));
		return this;
	}

}