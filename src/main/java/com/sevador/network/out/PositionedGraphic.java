package com.sevador.network.out;

import com.sevador.game.node.model.LocationGraphic;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Represents the positioned graphic outgoing packet.
 * @author Lumby
 *
 */
public class PositionedGraphic extends OutgoingPacket {

	/**
	 * The graphic.
	 */
	private final LocationGraphic graphic;
	
	/**
	 * Constructs a new {@code PositionedGraphic} {@code Object}.
	 * @param player The player.
	 * @param graphic The graphic to send.
	 */
	public PositionedGraphic(Player player, LocationGraphic graphic){
		super(player, 12);
		this.graphic = graphic;
	}
	
	@Override
	public OutgoingPacket get(){
		int regionX = graphic.getLocation().getX() - (getPlayer().getLocation().getRegionX() << 3);
		int regionY = graphic.getLocation().getY() - (getPlayer().getLocation().getRegionY() << 3);
		getPlayer().getIOSession().write(new UpdateTilePosition(getPlayer(), graphic.getLocation()));
		put((regionX << 4) & 0x79 | regionY & 0x7);
		putShort(graphic.getId());
		put(0);//Idk
		putShort(0);//Idk
		put(0);//Idk
		return this;
	}
}
