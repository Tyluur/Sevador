package com.sevador.game.event.object;

import net.burtleburtle.tick.Tick;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.utility.OptionType;

/**
 * @author Tyluur<lethium@hotmail.co.uk>
 * 
 */
public class VarrockTunnelEvent implements ObjectEvent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.argonite.game.event.ObjectEvent#init()
	 */
	@Override
	public boolean init() {
		EventManager.register(9311, this);
		return EventManager.register(9312, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ObjectEvent#handle(org.argonite.game.node.model
	 * .player.Player, org.argonite.game.node.model.gameobject.GameObject,
	 * org.argonite.util.OptionType)
	 */
	@Override
	public boolean handle(final Player player, final GameObject obj,
			OptionType type) {
		if (obj.getId() == 9311) {
			player.setAnimation(new Animation(2589));
			World.getWorld().submit(new Tick(2) {

				@Override
				public boolean run() {
					player.getProperties().setTeleportLocation(
							Location.locate(3139, 3516, player.getLocation()
									.getZ()));
					World.getWorld().submit(new Tick(1) {

						@Override
						public boolean run() {
							player.getProperties().setTeleportLocation(
									Location.locate(3144, 3514, player
											.getLocation().getZ()));
							player.setAnimation(new Animation(2591));
							return true;
						}

					});
					return true;
				}

			});
		} else {
			player.setAnimation(new Animation(2589));
			World.getWorld().submit(new Tick(2) {

				@Override
				public boolean run() {
					player.getProperties().setTeleportLocation(obj.getLocation());
					World.getWorld().submit(new Tick(1) {

						@Override
						public boolean run() {
							player.getProperties().setTeleportLocation(
									Location.locate(3138, 3516, 0));
							player.setAnimation(new Animation(2591));
							return true;
						}

					});
					return true;
				}

			});
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.argonite.game.event.ObjectEvent#setDestination(org.argonite.game.
	 * node.model.player.Player,
	 * org.argonite.game.node.model.gameobject.GameObject)
	 */
	@Override
	public void setDestination(Player p, GameObject obj) {
		obj.getLocation();
	}

}
