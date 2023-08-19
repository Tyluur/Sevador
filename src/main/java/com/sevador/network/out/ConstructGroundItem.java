package com.sevador.network.out;

import com.sevador.game.node.player.Player;
import com.sevador.game.region.GroundItem;
import com.sevador.network.OutgoingPacket;

/**
 * Represents the construct ground item outgoing packet.
 * @author Lumby
 *
 */
public final class ConstructGroundItem extends OutgoingPacket {

	/**
	 * The location of the ground item we are creating
	 */
	private final GroundItem item;
	
	/**
	 * Constructs a new {@code ConstructGroundItem} {@code Object}.
	 * @param player The player.
	 * @param item The ground item to construct.
	 */
	public ConstructGroundItem(Player player, GroundItem item){
		super(player, 35);
		this.item = item;
	}
	
	@Override
	public OutgoingPacket get(){
		int deltaX = item.getLocation().getX() - (getPlayer().getLocation().getRegionX() << 3);
		int deltaY = item.getLocation().getY() - (getPlayer().getLocation().getRegionY() << 3);
		getPlayer().getIOSession().write(new UpdateTilePosition(getPlayer(), item.getLocation()));
		putLEShort(item.getItem().getAmount());//amount
		putShort(0);
		put((deltaX & 0x7)  << 4 | deltaY & 0x7);
		putShortA(item.getItem().getId());//itemId
		return this;
	}
}
