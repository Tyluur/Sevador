package com.sevador.network.out;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.Projectile;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;

/**
 * Represents the construct projectile outgoing packet.
 * @author Vincent
 *
 */
public class ConstructProjectile extends OutgoingPacket {
	
	/**
	 * The projectile we're creating
	 */
	private final Projectile projectile;
	
	/**
	 * Constructs a new {@code ConstructProjectile} {@code Object}.
	 * @param player The player.
	 * @param projectile The projectile.
	 */
	public ConstructProjectile(Player player, Projectile projectile) {
		super(player, 27);
		this.projectile = projectile;
	}
	
	@Override
	public OutgoingPacket get() {
		Location end = projectile.isLocationBased() ? projectile
				.getEndLocation() : projectile.getVictim().getLocation();
		Location start = projectile.getSourceLocation();
		getPlayer().getIOSession().write(new UpdateTilePosition(getPlayer(), start));
		int x = start.getX() - (start.getRegionX() << 3);
		int y = start.getY() - (start.getRegionY() << 3);
		put((x & 0x7) << 3 | y & 0x7)
			.put(end.getX() - start.getX())
			.put(end.getY() - start.getY());
		putShort(projectile.getVictim() != null ? (projectile
				.getVictim().isPlayer() ? -(projectile
				.getVictim().getIndex() + 1) : (projectile
				.getVictim().getIndex() + 1)) : -1);
		putShort(projectile.getProjectileId());//gfx
		put(projectile.getStartHeight()); //start height
		put(projectile.getEndHeight()); //end height
		putShort(projectile.getType()); //not sure
		putShort(projectile.getSpeed());//speed
		put(projectile.getAngle()); //angle
		putShort(projectile.getDistance()); //distance
		/*int chunkOffset = packet.getUnsignedByte(-126);
		boolean unknown_flag = (0x80 & chunkOffset) != 0; //not sure, not in 508
		int startX = aia.updatingChunkBaseX + ((0x3c & chunkOffset) >> 3);
		int startY = oaa.updatingChunkBaseY + (0x7 & chunkOffset);
		int targetX = packet.getByte(-109) + startX;
		int targetY = startY - -packet.getByte(i ^ ~0x58);
		int entity = packet.getShorttest(-16098);
		int gfx = packet.getShort((byte) 126);
		int height1 = packet.getUnsignedByte(-107) * 4;
		int height2 = packet.getUnsignedByte(-128) * 4;
		int i_138_ = packet.getShort((byte) 126);
		int speed = packet.getShort((byte) 126);
		int angle = packet.getUnsignedByte(-50);
		int distance = packet.getShort((byte) 126);
					int i_129_ = var_fb.getUnsignedByte(-126);
					boolean bool = (0x80 & i_129_) != 0;
					int i_130_ = aia.a + ((0x3c & i_129_) >> 3);
					int i_131_ = oaa.c + (0x7 & i_129_);
					int i_132_ = var_fb.getByte(-109) + i_130_;
					int i_133_ = i_131_ - -var_fb.getByte(i ^ ~0x58);
					int i_134_ = var_fb.r(-16098);
					int i_135_ = var_fb.getShort((byte) 126);
					int i_136_ = var_fb.getUnsignedByte(-107) * 4;
					int i_137_ = var_fb.getUnsignedByte(-128) * 4;
					int i_138_ = var_fb.getShort((byte) 126);
					int i_139_ = var_fb.getShort((byte) 126);
					int i_140_ = var_fb.getUnsignedByte(-50);
					int i_141_ = var_fb.getShort((byte) 126);*/
		return this;
	}
}
