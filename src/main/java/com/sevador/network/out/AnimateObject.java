package com.sevador.network.out;

import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Handles the animate object outgoing packet.
 * @author Emperor
 *
 */
public class AnimateObject extends OutgoingPacket {

	/**
	 * The animation id.
	 */
	private final int animationId;
	
	/**
	 * The game object.
	 */
	private final GameObject object;
	
	/**
	 * Constructs a new {@code AnimateObject} {@code Object}.
	 * @param player The player.
	 * @param animationId The animation id.
	 * @param object The game object to animate.
	 */
	public AnimateObject(Player player, int animationId, GameObject object) {
		super(player, 9);
		this.animationId = animationId;
		this.object = object;
	}
	
	@Override
	public OutgoingPacket get() {
		/*int i_121_ = var_fb.getLEShort(i ^ 0xf);
		if ((i_121_ ^ 0xffffffff) == -65536)
			i_121_ = -1;
		int i_122_ = var_fb.getByteS(false);
		int i_123_ = (i_122_ >> 4 & 0x7) + aia.a;
		int i_124_ = oaa.c + (0x7 & i_122_);
		int i_125_ = var_fb.getByteA((int) -128);
		int i_126_ = i_125_ >> 2;
		int i_127_ = 0x3 & i_125_;
		int i_128_ = rda.f_l[i_126_];
		*
		 * 
		bldr.writeByteS(((localX - ((localX >> 3) << 3)) << 4)
				| ((localY - ((localY >> 3) << 3)) & 0x7));
		bldr.writeLEShortA(anim);
		bldr.writeByteA((obj.getType() << 2) + (obj.getRotation() & 3));
		player.write(bldr.toMessage());
		 */
		int localX = object.getLocation().getX() - (getPlayer().getRegion().getRegionX() - 6) * 8;
		int localY = object.getLocation().getY() - (getPlayer().getRegion().getRegionY() - 6) * 8;
		getPlayer().getIOSession().write(new UpdateTilePosition(getPlayer(), object.getLocation()));
		putLEShort(animationId);
		putByteS(((localX - ((localX >> 3) << 3)) << 4) | ((localY - ((localY >> 3) << 3)) & 0x7));
		putByteA((object.getType() << 2) + (object.getRotation() & 3));
		return this;
	}

}