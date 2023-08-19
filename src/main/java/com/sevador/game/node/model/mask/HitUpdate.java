package com.sevador.game.node.model.mask;

import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.network.OutgoingPacket;

/**
 * Represents the hit update mask.
 * @author Emperor
 *
 */
public class HitUpdate extends UpdateFlag {

	/**
	 * The entity.
	 */
	public final Entity entity;
	
	/**
	 * Constructs a new {@code HitUpdate} {@code Object}.
	 * @param entity The entity.
	 */
	public HitUpdate(Entity entity){
		this.entity = entity;
	}
	
	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putByteA(entity.getDamageMap().getDamageList().size()); //Amount of hits.
		for (Damage damage : entity.getDamageMap().getDamageList()) {
			if (damage.getSoaked() > 0) {
				outgoing.putSmart(32767);
			}
			int type = damage.getType().ordinal();
			if (type != 9) {
				if (damage.getHit() < 1) {
					type = 8;
				} else if (damage.getHit() + 1 >= damage.getMaximum() * .97) {
					type += 10;
				}
				if (damage.getSource() == outgoing.getPlayer() || entity == outgoing.getPlayer()) {
					outgoing.putSmart(type);
				} else {
					outgoing.putSmart(type + 14);
				}
			} else {
				outgoing.putSmart(type);
			}
			outgoing.putSmart(damage.getHit());
			if (damage.getSoaked() > 0) {
				if (damage.getSource() == entity || entity == outgoing.getPlayer()) {
					outgoing.putSmart(5);
				} else {
					outgoing.putSmart(19);
				}
				outgoing.putSmart(damage.getSoaked());			
			}
			outgoing.putSmart(damage.getDelay());
			if (entity.isNPC()) {
				outgoing.putByteA(damage.getLifepoints());
			} else {
				outgoing.put(damage.getLifepoints());
			}
		}
	}

	@Override
	public int getMaskData() {
		return entity.isNPC() ? 0x10 : 0x8;
	}

	@Override
	public int getOrdinal() {
		return entity.isNPC() ? 7 : 2;
	}

}