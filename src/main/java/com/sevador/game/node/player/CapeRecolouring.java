package com.sevador.game.node.player;

import java.io.Serializable;
import java.util.Arrays;

import net.burtleburtle.cache.format.CacheItemDefinition;

import com.sevador.network.out.SendSprite;

/**
 * @author Tyluur<tyluur@zandium.org>
 */
public class CapeRecolouring implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7719757723677233654L;

	public int[] maxColours; // max cape
	public int[] completionistColours; // completionist cape
	public int[] completionistTrimmedColours; // completionist cape (t)
	private Player player;


	public CapeRecolouring(Player player) {
		this.player = player;
		maxColours = CacheItemDefinition.getItemDefinition(20767).originalModelColors.clone();
		completionistColours = CacheItemDefinition.getItemDefinition(20767).originalModelColors.clone();
		completionistTrimmedColours = CacheItemDefinition.getItemDefinition(20771).originalModelColors.clone();
	}


	public void displayInterface() {
		player.getPacketSender().sendInterface(20);
		refresh();
		int capeId = player.getEquipment().get(Equipment.SLOT_CAPE) == null ? -1 : player.getEquipment().get(Equipment.SLOT_CAPE).getId();
		if (player.getAttribute("customizingCapeId") != null)
			capeId = player.getAttribute("customizingCapeId", -1);
		// removes default cape ?
		player.getIOSession().write(new SendSprite(player, 20, 55, CacheItemDefinition.getItemDefinition(capeId).maleWornModelId1));
	}

	public void reset() {
		int capeId = player.getEquipment().get(Equipment.SLOT_CAPE) == null ? -1 : player.getEquipment().get(Equipment.SLOT_CAPE).getId();
		if (player.getAttribute("customizingCapeId") != null)
			capeId = player.getAttribute("customizingCapeId", -1);
		switch (capeId) {
		case 20767:
			maxColours = CacheItemDefinition.getItemDefinition(capeId).originalModelColors.clone();
			break;
		case 20769:
			completionistColours = CacheItemDefinition.getItemDefinition(capeId).originalModelColors.clone();
			break;
		case 20771:
			completionistTrimmedColours = CacheItemDefinition.getItemDefinition(capeId).originalModelColors.clone();
			break;
		}

	}

	public void refresh() {
		int[] colors = getColours();
		player.getPacketSender().sendConfig(2172, (colors[0] << 16) + colors[1]);
		player.getPacketSender().sendConfig(2173, (colors[2] << 16) + colors[3]);
	}


	public int[] getColours() {
		int capeId = player.getEquipment().get(Equipment.SLOT_CAPE) == null ? -1 : player.getEquipment().get(Equipment.SLOT_CAPE).getId();
		if (player.getAttribute("customizingCapeId") != null)
			capeId = player.getAttribute("customizingCapeId", -1);
		switch (capeId) {
		case 20767:
			return maxColours;
		case 20769:
			return completionistColours;
		case 20771:
			return completionistColours;
		}
		return null;
	}


	public boolean isRecolourable(CacheItemDefinition itemDef) {
		return !Arrays.equals(itemDef.originalModelColors, getColours());
	}

}
