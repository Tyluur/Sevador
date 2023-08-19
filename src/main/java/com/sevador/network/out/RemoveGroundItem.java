package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.game.region.GroundItem;
import com.sevador.network.OutgoingPacket;

/**
 * Notifies the client to remove a ground item.
 * @author Lumby
 * @author Emperor
 *
 */
public final class RemoveGroundItem extends OutgoingPacket {
	
	/**
	 * The ground item we're removing.
	 */
	private final GroundItem item;
	
	/**
	 * Constructs a new {@code RemoveGroundItem} {@code Object}.
	 * @param player The player.
	 * @param item The ground item to remove.
	 */
	public RemoveGroundItem(Player player, GroundItem item){
		super(player, 46);
		this.item = item;
	}
	
	@Override
	public OutgoingPacket get() {
		int deltaX = item.getLocation().getX() - (getPlayer().getLocation().getRegionX() << 3);
		int deltaY = item.getLocation().getY() - (getPlayer().getLocation().getRegionY() << 3);
		getPlayer().getIOSession().write(new UpdateTilePosition(getPlayer(), item.getLocation()));
		putShortA(item.getItem().getId());
		putByteS((deltaX & 0x7)  << 4 | deltaY & 0x7);
		return this;
	}

}
