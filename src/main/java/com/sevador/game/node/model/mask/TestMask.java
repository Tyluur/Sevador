package com.sevador.game.node.model.mask;

import com.sevador.network.OutgoingPacket;

public class TestMask extends UpdateFlag {

	@SuppressWarnings("unused")
	private Object[] args;
	
	public TestMask(Object...objects) {
		this.args = objects;
	}
	
	/*
	 *  for (DamageHit hit : other.getDamageManager().getHits()) {
            if (hit.getPartner() != null) {
                bldr.writeSmart(32767);
            }
            int type = hit.getType().toInteger();
            if (type != 9) {
                if (hit.isMax()) {
                    type += 10;
                }
                if (hit.getDamage() < 1) {
                    type = 8;//If there is no damage display a shield!
                }
                if (hit.getAttacker() == player || hit.getVictim() == player) {
                    bldr.writeSmart(type);
                } else {
                    bldr.writeSmart(type + 14);
                }
            } else {
                bldr.writeSmart(9);
            }
            bldr.writeSmart(hit.getDamage());
            if (hit.getPartner() != null) {
                int type2 = hit.getPartner().getType().toInteger();
                if (hit.getAttacker() == player || hit.getVictim() == player) {
                    bldr.writeSmart(type2);
                } else {
                    bldr.writeSmart(type2 + 14);
                }
                bldr.writeSmart(hit.getPartner().getDamage());
            }
            bldr.writeSmart(hit.getDelay());
            bldr.writeByte(hit.getCurrentHealth());
        }
        
        /*ordinal 2	if ((i & 0x8) != 0) {
					int i_23_ = buffer.getByteA((int) -128);
					if ((i_23_ ^ 0xffffffff) < -1) {
						for (int i_24_ = 0; i_24_ < i_23_; i_24_++) {
							int i_25_ = -1;
							int i_26_ = -1;
							int i_27_ = buffer.a(false);
							int i_28_ = -1;
							if ((i_27_ ^ 0xffffffff) == -32768) {
								i_27_ = buffer.a(bool);
								i_26_ = buffer.a(false);
								i_25_ = buffer.a(false);
								i_28_ = buffer.a(false);
							} else if ((i_27_ ^ 0xffffffff) == -32767)
								i_27_ = -1;
							else
								i_26_ = buffer.a(false);
							int i_29_ = buffer.a(false);
							int i_30_ = buffer.getUnsignedByte(-125);
							var_av.a(i_26_, i_29_, i_30_, i_28_, i_27_,
									(byte) 13, bj.o, i_25_);
						}
					}
				}
	 */
	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putByteA(4); //Amount of hits.
		for (int i = 0; i < 4; i++) {
			outgoing.putSmart(16); //Hit type?
			outgoing.putSmart(990); //Damage?
			outgoing.putSmart(0); //Delay?
			outgoing.put(255); //Current hp left?
		}
	}
	
	@Override
	public int getMaskData() {
		return 0x8;
	}

	@Override
	public int getOrdinal() {
		return 2;
	}
/*	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.put(1);
		outgoing.putLEShort(49152);
		outgoing.putLEShortA(3222);
		outgoing.putLEShortA(111);
	}

	@Override
	public int getMaskData() {
		return 0x40000;
	}

	@Override
	public int getOrdinal() {
		return 3;
	}
	
/*	@Override
	public void write(OutgoingPacket outgoing) {
		outgoing.putByteA(0);
		outgoing.putByteC(0);
		outgoing.putByteA(0);
		outgoing.putByteA(0);
		outgoing.putLEShort(2);
		outgoing.putLEShort(2);
	}

	@Override
	public int getMaskData() {
		return 0x20000;
	}

	@Override
	public int getOrdinal() {
		return 1;
	}*/

}