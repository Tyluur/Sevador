package com.sevador.network.out;


import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * The packet used for creating an object.
 * @author Emperor
 *
 */
public class ConstructObject extends OutgoingPacket {

	/**
	 * The game object to construct.
	 */
	private final GameObject object;
	
	/**
	 * Constructs a new {@code ConstructObject} {@code Object}.
	 * @param player The player.
	 * @param object The game object to construct.
	 */
	public ConstructObject(Player player, GameObject object) {
		super(player, 20);
		this.object = object;
	}

	@Override
	public OutgoingPacket get() {
		int localX = object.getLocation().getX() - (getPlayer().getRegion().getRegionX() - 6) * 8;
		int localY = object.getLocation().getY() - (getPlayer().getRegion().getRegionY() - 6) * 8;
		getPlayer().getIOSession().write(new UpdateTilePosition(getPlayer(), object.getLocation()));
		putByteA(((localX - (localX << 3)) << 4) | ((localY - ((localY >> 3) << 3)) & 0x7));
		putShortA(object.getId());
		putByteS((object.getType() << 2) + (3 & object.getRotation()));
		return this;
	}
}