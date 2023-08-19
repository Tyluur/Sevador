package com.sevador.network.out;

import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionManager;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;

/**
 * Handles the NPC Rendering.
 * 
 * @author Sean
 * @author Emperor
 * 
 */
public class NPCRendering extends OutgoingPacket {

	/**
	 * Constructs a new {@code NPCRendering} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 */
	public NPCRendering(Player player) {
		super(player, 6, PacketType.VAR_SHORT);
	}

	@Override
	public OutgoingPacket get() {
		Player p = getPlayer();
		Location playerLocation = p.getLocation();
		OutgoingPacket updateBlock = new OutgoingPacket(p);
		List<NPC> localNpcs = p.getRenderInformation().getLocalNPCs();
		startBitAccess();
		putBits(8, localNpcs.size());
		for (Iterator<NPC> it$ = localNpcs.iterator(); it$.hasNext();) {
			NPC npc = it$.next();
			if (npc.isVisible()
					&& npc.getLocation().isWithinDistance(playerLocation)) {
				updateNPCMovement(npc, this);
				if (npc.getUpdateMasks().isUpdateRequired()) {
					updateNPC(updateBlock, npc);
				}
			} else {
				it$.remove();
				putBits(1, 1);
				putBits(2, 3);
			}
		}
		for (NPC npc : RegionManager.getLocalNPCs(p.getLocation())) {
			if (localNpcs.size() >= 255) {
				break;
			}
			if (localNpcs.contains(npc) || !npc.isVisible()) {
				continue;
			}
			addNpc(p, npc, this);
			if (npc != null && npc.getUpdateMasks() != null && npc.getUpdateMasks().isUpdateRequired()) {
				updateNPC(updateBlock, npc);
			}
		}
		putBits(15, 32767);
		finishBitAccess();
		putBytes(updateBlock.getBuffer());
		return this;
	}

	/**
	 * Adds an NPC.
	 * 
	 * @param player
	 *            The player.
	 * @param npc
	 *            The npc.
	 * @param buf
	 *            The outgoing packet.
	 */
	private static void addNpc(Player player, NPC npc, OutgoingPacket buf) {
		try {
		int x = npc.getLocation().getX() - player.getLocation().getX();
		int y = npc.getLocation().getY() - player.getLocation().getY();
		if (x < 0) {
			x += 32;
		}
		if (y < 0) {
			y += 32;
		}
		buf.putBits(15, npc.getIndex());
		buf.putBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
		buf.putBits(2, npc.getLocation().getZ());
		buf.putBits(5, x);
		buf.putBits(1, 0); // 1
		buf.putBits(3, npc.getRotation());
		buf.putBits(5, y);
		buf.putBits(15, npc.getId());
		player.getRenderInformation().getLocalNPCs().add(npc);
		} catch (Exception e) {}
	}

	/**
	 * Updates an npcs movement.
	 * 
	 * @param npc
	 *            The npc.
	 * @param buf
	 *            The buffer.
	 */
	private static void updateNPCMovement(NPC npc, OutgoingPacket buf) {
		if (npc.getWalkingQueue().getRunDir() == -1) {
			if (npc.getWalkingQueue().getWalkDir() == -1) {
				if (npc.getUpdateMasks().isUpdateRequired()) {
					buf.putBits(1, 1);
					buf.putBits(2, 0);
				} else {
					buf.putBits(1, 0);
				}
			} else {
				buf.putBits(1, 1);
				buf.putBits(2, 1);
				buf.putBits(3, npc.getWalkingQueue().getWalkDir());
				buf.putBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
			}
		} else {
			buf.putBits(1, 1);
			buf.putBits(2, 2);
			buf.putBits(1, 1);
			buf.putBits(3, npc.getWalkingQueue().getWalkDir());
			buf.putBits(3, npc.getWalkingQueue().getRunDir());
			buf.putBits(1, npc.getUpdateMasks().isUpdateRequired() ? 1 : 0);
		}
	}

	/**
	 * Writes the NPC flag-based updating.
	 * 
	 * @param packet
	 *            The packet to write on.
	 * @param npc
	 *            The npc.
	 */
	private static void updateNPC(OutgoingPacket packet, NPC npc) {
		int maskdata = 0;
		PriorityQueue<UpdateFlag> flags = new PriorityQueue<UpdateFlag>(
				npc.getUpdateMasks().flagQueue);
		for (UpdateFlag flag : flags) {
			maskdata |= flag.getMaskData();
		}
		if (maskdata > 128) {
			maskdata |= 0x2;
		}
		if (maskdata > 32768) {
			maskdata |= 0x2000;
		}
		packet.put((byte) maskdata);
		if (maskdata > 128) {
			packet.put((byte) (maskdata >> 8));
		}
		if (maskdata > 32768) {
			packet.put((byte) (maskdata >> 16));
		}
		while (!flags.isEmpty()) {
			flags.poll().write(packet);
		}
	}
}