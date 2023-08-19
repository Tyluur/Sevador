/*package com.sevador.game.node.model.skills.agility.obstacles;

import net.burtleburtle.tick.Tick;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.agility.AbstractCourse;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;

*//**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 *//*
public class WorldObstacles extends AbstractCourse {

	public static final Location[] VARROCK_TUNNEL_LOCATIONS = {
		Location.locate(3138, 3516, 0), Location.locate(3144, 3514, 0)
	};

	public static final Animation ROCK_CLIMB_ANIMATION = Animation.create(1148);

	@Override
	public boolean handleObject(final Player player, final GameObject object) {
		Tick tick;
		//2589, 2590, 2591
		switch (object.getId()) {
		case 9316: // Rocks
			forceWalk(player, null, player.getLocation().getX() + 1, player.getLocation().getY(), 1, null, null);
			delayForceWalk(player, ROCK_CLIMB_ANIMATION, 2488, 3516, 2, new int[] {20, 60, 2}, null, 3);
			delayForceWalk(player, null, 2489, 3517, 1, null, null, 6);
			delayForceWalk(player, ROCK_CLIMB_ANIMATION, 2489, 3521, 1, new int[] {0, 120, 2}, null, 8);
			return true;
		case 9317: // Rocks
			forceWalk(player, null, player.getLocation().getX(), player.getLocation().getY() - 5, 2, null, null);
			delayForceWalk(player, null, 2488, 3516, 1, null, null, 4);
			delayForceWalk(player, null, 2486, 3515, 1, null, null, 6);
			World.getWorld().submit(new Tick(1) {

				private int stage;

				@Override
				public void execute() {
					switch (stage) {
					case 0:
						player.setRenderAnimation(0);
						break;
					case 3:
						player.resetRenderAnimation();
						break;
					case 5:
						player.setRenderAnimation(0);
						break;
					case 6:
						reverseActions(player, true);
						player.resetRenderAnimation();
						stop();
						break;
					}
					stage++;
				}
			});
			return true;
		case 9311: // Underwall tunnel north
		case 9312: // Underwall tunnel south
			final int index = object.getLocation().getY() == 3516 ? 0 : 1;
			final Location to = VARROCK_TUNNEL_LOCATIONS[index];
			final Location next = VARROCK_TUNNEL_LOCATIONS[index == 0 ? 1 : 0];
			tick = new Tick(player.getLocation() == to ? 0 : 1) {
				@Override
				public void execute() {
					if (player.getLocation() == to) {
						stop();
						player.faceObject(object);
						forceWalk(player, Animation.create(2589), player.getLocation().getX() + (index == 1 ? -1 : 1), player.getLocation().getY(), 1, new int[] {0, 60, object.getId() == 9311 ? 1 : 3}, null);
						World.getWorld().submit(new Tick(2) {
							private int cycles = 0;
							@Override
							public void execute() {
								switch (cycles) {
								case 0:
									player.setAttribute("cantMove", Boolean.TRUE);
									player.animate(Animation.create(2590));
									break;
								case 1:
									player.teleport(next.getX() + (index == 1 ? 1 : -1), next.getY(), 0);
									break;
								case 2:
									forceWalk(player, Animation.create(2591), player.getLocation().getX() + (index == 1 ? -1 : 1), player.getLocation().getY(), 1, new int[] {20, 30, object.getId() == 9311 ? 3 : 1}, null);
									break;
								case 3:
									stop();
									player.removeAttribute("cantMove");
									break;
								}
								cycles++;
							}
						});
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				player.requestWalk(to.getX(), to.getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		}
		return false;
	}

}
*/