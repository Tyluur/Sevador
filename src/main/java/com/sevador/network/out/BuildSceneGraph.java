package com.sevador.network.out;

import net.burtleburtle.cache.format.LandscapeParser;
import net.burtleburtle.tick.Tick;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.ObjectBuilder;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.GroundItemManager;
import com.sevador.game.world.World;
import com.sevador.network.OutgoingPacket;
import com.sevador.network.PacketType;
import com.sevador.utility.MapData;

/**
 * Creates a new BuildSceneGraph packet.
 * @author Emperor
 *
 */
public class BuildSceneGraph extends OutgoingPacket {

	/**
	 * If the packet gets send on login.
	 */
	private final boolean onLogin;
	
	/**
	 * Constructs a new {@code BuildSceneGraph} packet {@code Object}.
	 * @param player The player.
	 * @param onLogin If this packet gets send on login.
	 */
	public BuildSceneGraph(Player player, boolean onLogin) {
		super(player, 19, PacketType.VAR_SHORT);
		this.onLogin = onLogin;
	}
	
	@Override
	public OutgoingPacket get() {
		Location pos = getPlayer().getLocation();
		if (onLogin) {
			getPlayer().getRenderInformation().enterWorld(this);
		}
		int regionX = pos.getRegionX();
		int regionY = pos.getRegionY();
		putByteC(1); //Force refresh? 1 : 0
		putLEShort(regionY);
		putLEShortA(regionX);
		putByteS(0); //Scene graph size index.
		for (int sectorX = (regionX - 6) >> 3; sectorX <= (regionX + 6) >> 3; sectorX++) {
			for (int sectorY = (regionY - 6) >> 3; sectorY <= (regionY + 6) >> 3; sectorY++) {
				int region = sectorY | (sectorX << 8);
				int[] mapData = MapData.getMapData().get(region);
				if (mapData == null) {
					mapData = new int[4];
				}
				for (int i = 0; i < 4; i++) {
					putInt(mapData[i]);
				}
				LandscapeParser.parseLandscape(region, mapData);
			}
		}
		getPlayer().setRegion(getPlayer().getLocation());
		getPlayer().getPlayerFlags().setMapRegionChanged(false);
		World.getWorld().submit(new Tick(1) {
			@Override
			public boolean run() {
				if (!getPlayer().isLoggedIn()) {
					return false;
				}
				ObjectBuilder.enterRegion(getPlayer());
				GroundItemManager.refresh(getPlayer());
				return true;
			}			
		});
		return this;
	}
}