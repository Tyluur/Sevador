package com.sevador.game.node.model.mask;

import net.burtleburtle.cache.format.CacheItemDefinition;
import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.game.node.player.Appearance;
import com.sevador.game.node.player.Equipment;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.utility.Misc;

/**
 * Represents a player's appearance update flag.
 * @author Emperor
 *
 */
public class AppearanceUpdate extends UpdateFlag {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The player's appearance.
	 */
	private final Appearance appearance;

	/**
	 * Constructs a new {@code AppearanceUpdate} {@code Object}.
	 * @param player The player.
	 */
	public AppearanceUpdate(Player player) {
		this.player = player;
		this.appearance = player.getCredentials().getAppearance();
	}

	@Override
	public void write(OutgoingPacket outgoing) {
		OutgoingPacket playerUpdate = new OutgoingPacket(player);
		int bitSet = 0;
		bitSet |= 0x4; //Enable combat colouring.
		if (!appearance.isMale()) {
			bitSet |= 0x1;
		}
		if (appearance.getNpcId() != -1) {
			bitSet |= (NPCDefinition.forId(appearance.getNpcId()).size - 1) << 3;
		}
		playerUpdate.put(bitSet);
		playerUpdate.put(player.getCredentials().getDisplayTitle());
		playerUpdate.put(-1); //skull icon
		playerUpdate.put(player.getPrayer().getHeadIcon()); //Headicon.
		playerUpdate.put(0);// TODO:refactor
		if (appearance.getNpcId() == -1) {
			for (int i = 0; i < ItemDefinition.getBodyData().length; i++) {
				if (ItemDefinition.getBodyData()[i] != 1) {
					int d = appearance.getBodyPart(i);
					if (d == 0) {
						playerUpdate.put(0);
					} else {
						playerUpdate.putShort((short) d);
					}
				}
			}
			bitSet = 0;
			int part = 0;
			int slotHash = 0;
			for (int i = 0; i < ItemDefinition.getBodyData().length; i++) {
				if (ItemDefinition.getBodyData()[i] != 1) {
					int itemId = player.getEquipment().get(i) == null ? -1 : player.getEquipment().get(i).getId();
					if (i == 1) {
						if ((itemId == 20767 || itemId == 20769 || itemId == 20771) && player.getCapeRecolouring().isRecolourable(CacheItemDefinition.getItemDefinition(itemId))) {
							bitSet |= 1 << part;
							slotHash |= 0x1;
						}
					} 
				}
				part++;
			}
			playerUpdate.putShort(bitSet);
			if ((slotHash & 0x1) != 0) { //Only with recolored cape.
				playerUpdate.put(0x4);
				int[] colors = player.getCapeRecolouring().getColours();
				if (colors == null) {
					colors = CacheItemDefinition.getItemDefinition(player.getEquipment().getSlotById(Equipment.SLOT_CAPE)).originalModelColors;
				}
				int[] data = { 12816, colors[1], colors[0], colors[3], colors[2]};
				for (int i = 0; i < data.length; i++) {
					playerUpdate.putShort(data[i]);
				}
			}
			if (appearance.getBodyPart(14) > 0) { //Only with aura.
				playerUpdate.put(0x1);
				playerUpdate.putIntSmart(8719);
				playerUpdate.putIntSmart(8719);
			}
		} else {
			playerUpdate.putShort(-1);
			playerUpdate.putShort(appearance.getNpcId());
			playerUpdate.put(0);
		}
		for (byte i = 0; i < 10; i++) {
			playerUpdate.put(appearance.getColor(i));
		}
		playerUpdate.putShort(appearance.getRenderEmote());
		playerUpdate.putRS2String(Misc.formatPlayerNameForDisplay(player.getCredentials().getDisplayName()));
		playerUpdate.put(player.getCombatLevel());
		playerUpdate.putShort(0);
		playerUpdate.put(0);
		outgoing.put(playerUpdate.getBuffer().writerIndex());
		outgoing.putBytesA(playerUpdate.getBuffer().array(), 0, playerUpdate.getBuffer().writerIndex());
	}

	@Override
	public int getMaskData() {
		return 0x2;
	}

	@Override
	public int getOrdinal() {
		return 6;
	}

}