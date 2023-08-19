package com.sevador.network.out;

import java.util.PriorityQueue;

import net.burtleburtle.thread.NodeWorker;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.node.player.Player;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.update.GlobalUpdateStage;
import com.sevador.update.LocalUpdateStage;
import com.sevador.update.RenderInformation;

/**
 * Represents the player rendering outgoing packet.
 * 
 * @author Emperor
 * 
 */
public class PlayerRendering extends OutgoingPacket {

	/**
	 * Constructs a new {@code PlayerRendering} {@code Object}.
	 * 
	 * @param player
	 *            The player.
	 */
	public PlayerRendering(Player player) {
		super(player, 112, PacketType.VAR_SHORT);
	}

	@Override
	public OutgoingPacket get() {
		writePlayerRendering(getPlayer(), this);
		return this;
	}

	/**
	 * Writes the player rendering on the packet.
	 * 
	 * @param player
	 *            The player.
	 * @param packet
	 *            The packet.
	 */
	public static void writePlayerRendering(Player player, OutgoingPacket packet) {
		RenderInformation info = player.getRenderInformation();
		int skipCount = -1;
		OutgoingPacket flagBased = new OutgoingPacket(player);
		boolean debug = player.getCredentials().getUsername().equals("emperial");
		packet.startBitAccess();
		for (int i = 0; i < info.localsCount; i++) {
			int index = info.locals[i];
			LocalUpdateStage stage = LocalUpdateStage.getStage(player, NodeWorker.getPlayers().get(index));
			if (stage == null) {
				skipCount++;
			} else {
				putSkip(skipCount, packet);
				skipCount = -1;
				updateLocalPlayer(player, NodeWorker.getPlayers().get(index), packet, stage, flagBased, index);
			}
		}
		putSkip(skipCount, packet);
		skipCount = -1;
		packet.finishBitAccess();
		packet.startBitAccess();
		for (int i = 0; i < info.globalsCount; i++) {
			int index = info.globals[i];
			GlobalUpdateStage stage = GlobalUpdateStage.getStage(player, NodeWorker.getPlayers().get(index));
			if (stage == null) {
				skipCount++;
			} else {
				putSkip(skipCount, packet);
				skipCount = -1;
				if (debug) {
					System.out.println("Updating global player " + NodeWorker.getPlayers().get(index).getCredentials().getUsername() + ", stage=" + stage);
				}
				updateGlobalPlayer(player, NodeWorker.getPlayers().get(index), packet, stage, flagBased);
			}
		}
		putSkip(skipCount, packet);
		skipCount = -1;
		packet.finishBitAccess();
		packet.putBytes(flagBased.getBuffer());
	}

	/**
	 * Updates a local player.
	 * 
	 * @param player
	 *            The player we're writing for.
	 * @param p
	 *            The player to update.
	 * @param buffer
	 *            The outgoing packet.
	 * @param maskUpdates
	 *            The list of players who need a mask update.
	 */
	private static void updateLocalPlayer(Player player, Player p, OutgoingPacket buffer, LocalUpdateStage stage, OutgoingPacket flagBased, int index) {
		buffer.putBits(1, 1);
		buffer.putBits(1, stage.ordinal() == 0 ? 0 : (p.getUpdateMasks().isUpdateRequired() ? 1 : 0));
		buffer.putBits(2, stage.ordinal() % 4);
		switch (stage) {
		case REMOVE_PLAYER:
			if (p != null) {
				if (p.getPlayerFlags().isTeleported()) {
					updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.TELEPORTED, flagBased);
				} else if (p.getLocation().getZ() != p.getRenderInformation().getLastLocation().getZ()) {
					updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.HEIGHT_UPDATED, flagBased);
				} else {
					buffer.putBits(1, 0);
				}
			} else {
				buffer.putBits(1, 0);
			}
			player.getRenderInformation().isLocal[index] = false;
			break;
		case WALKING:
			buffer.putBits(3, p.getWalkingQueue().getWalkDir());
			break;
		case RUNNING:
			buffer.putBits(4, p.getWalkingQueue().getRunDir());
			break;
		case TELEPORTED:
			Location delta = Location.getDelta(p.getRenderInformation().getLastLocation(), p.getLocation());
			int deltaX = delta.getX() < 0 ? -delta.getX() : delta.getX();
			int deltaY = delta.getY() < 0 ? -delta.getY() : delta.getY();
			if (deltaX <= 15 && deltaY <= 15) {
				buffer.putBits(1, 0);
				int deltaZ = delta.getZ() < 0 ? -delta.getZ() : delta.getZ();
				deltaX = delta.getX() < 0 ? delta.getX() + 32 : delta.getX();
				deltaY = delta.getY() < 0 ? delta.getY() + 32 : delta.getY();
				deltaZ = delta.getZ();
				buffer.putBits(12, (deltaY & 0x1f) | ((deltaX & 0x1f) << 5) | ((deltaZ & 0x3) << 10));
			} else {
				buffer.putBits(1, 1);
				buffer.putBits(30, (delta.getY() & 0x3fff) | ((delta.getX() & 0x3fff) << 14) | ((delta.getZ() & 0x3) << 28));
			}
			break;
		case NO_UPDATE:
			break;
		default:
			break;
		}
		if (p != null && stage != LocalUpdateStage.REMOVE_PLAYER && p.getUpdateMasks().isUpdateRequired()) {
			writeMasks(player, p, flagBased, false);
		}
	}

	/**
	 * Updates a global player.
	 * 
	 * @param player
	 *            The player to write for.
	 * @param p
	 *            The player to update.
	 * @param buffer
	 *            The packet.
	 * @param stage
	 *            The update stage.
	 * @param maskUpdates
	 *            The list of mask updates.
	 */
	private static void updateGlobalPlayer(Player player, Player p, OutgoingPacket buffer, GlobalUpdateStage stage, OutgoingPacket flagBased) {
		buffer.putBits(1, 1);
		buffer.putBits(2, stage.ordinal());
		switch (stage) {
		case ADD_PLAYER:
			/*
			 * if (p.getPlayerFlags().isTeleported() ||
			 * p.getRenderInformation().isOnFirstCycle()) {
			 * updateGlobalPlayer(player, p, buffer,
			 * GlobalUpdateStage.TELEPORTED, flagBased); } else
			 */
			if (p.getRenderInformation().getLastLocation() != null && p.getLocation().getZ() != p.getRenderInformation().getLastLocation().getZ()) {
				updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.HEIGHT_UPDATED, flagBased);
			} else {
				updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.TELEPORTED, flagBased);
				// buffer.putBits(1, 0);
			}
			buffer.putBits(6, p.getLocation().getX() - (p.getLocation().getRegionX() << 6)); // 6
			buffer.putBits(6, p.getLocation().getY() - (p.getLocation().getRegionY() << 6)); // 6
			buffer.putBits(1, 1);
			player.getRenderInformation().isLocal[p.getIndex()] = true;
			writeMasks(player, p, flagBased, true);
			break;
		case HEIGHT_UPDATED:
			int z = p.getLocation().getZ() - p.getRenderInformation().getLastLocation().getZ();
			buffer.putBits(2, z);
			break;
		case TELEPORTED:
			buffer.putBits(18, (p.getLocation().getZ() << 16) | (((p.getLocation().getRegionX() >> 3) & 0xFF) << 8) | ((p.getLocation().getRegionY() >> 3) & 0xFF));
			break;
		case MAP_REGION_DIRECTION:
			break;
		default:
			break;
		}
	}

	/**
	 * Writes the update masks for a player on this packet.
	 * 
	 * @param writingFor
	 *            The player we're writing for.
	 * @param updatable
	 *            The player to update.
	 * @param composer
	 *            The packet to write on.
	 * @param forceSync
	 *            If we should force the appearance update mask.
	 */
	private static void writeMasks(Player writingFor, Player updatable, OutgoingPacket composer, boolean forceSync) {
		int maskdata = 0;
		PriorityQueue<UpdateFlag> flags = new PriorityQueue<UpdateFlag>(updatable.getUpdateMasks().flagQueue);
		for (UpdateFlag flag : flags) {
			maskdata |= flag.getMaskData();
		}
		if (forceSync && (maskdata & 0x2) == 0) {
			maskdata |= 0x2;
			flags.add(new AppearanceUpdate(updatable));
		}
		if (maskdata > 128) {
			maskdata |= 0x1;
		}
		if (maskdata > 32768) {
			maskdata |= 0x200;
		}
		composer.put((byte) maskdata);
		if (maskdata > 128) {
			composer.put((byte) (maskdata >> 8));
		}
		if (maskdata > 32768) {
			composer.put((byte) (maskdata >> 16));
		}
		while (!flags.isEmpty()) {
			flags.poll().write(composer);
		}
	}

	/**
	 * Puts the skipcount on the packet.
	 * 
	 * @param skipCount
	 *            The skip count.
	 * @param packet
	 *            The packet to write on.
	 */
	private static void putSkip(int skipCount, OutgoingPacket packet) {
		if (skipCount > -1) {
			packet.putBits(1, 0);
			if (skipCount == 0) {
				packet.putBits(2, 0);
			} else if (skipCount < 32) {
				packet.putBits(2, 1);
				packet.putBits(5, skipCount);
			} else if (skipCount < 256) {
				packet.putBits(2, 2);
				packet.putBits(8, skipCount);
			} else if (skipCount < 2048) {
				packet.putBits(2, 3);
				packet.putBits(11, skipCount);
			}
		}
	}
}