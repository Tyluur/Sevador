/*package com.sevador.game.node.model.skills.agility.obstacles;

import net.burtleburtle.tick.Tick;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.agility.AbstractCourse;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.world.World;

*//**
 * @author 'Mystic Flow <Steven@rune-server.org>
 *//*
public class GnomeStrongholdCourse extends AbstractCourse {

	private static final Animation RUN_ON_WALL_ANIMATION = Animation.create(2922);

	private final Location defaultRunLocation = Location.locate(2476, 3418, 3), defaultSwingLocation = Location.locate(2486, 3418, 3), defaultJumpLocation = Location.locate(2485, 3433, 3);

	private final Location[] PIPE_LOCATIONS = {
			Location.locate(2483, 3430, 0), Location.locate(2487, 3430, 0)
	};

	private NPC logBalance, obstacleNet, treeBranch, balanceRope, climbingNet;

	public GnomeStrongholdCourse() {
		logBalance = World.getWorld().register(162, Location.locate(2470, 3431, 0));
		obstacleNet = World.getWorld().register(162, Location.locate(2471, 3427, 0));
		treeBranch = World.getWorld().register(162, Location.locate(2475, 3423, 1));
		balanceRope = World.getWorld().register(162, Location.locate(2475, 3419, 2));
		climbingNet = World.getWorld().register(162, Location.locate(2483, 3424, 0));
	}

	//11793
	@Override
	public boolean handleObject(final Player player, final GameObject object) {
		//System.out.println("Hi" + ", " + object.getLocation());
		Tick tick;
		switch (object.getId()) {
		case 2295: // Cross log
			logBalance.forceText("Okay get over that log, quick quick!");
			player.sendMessage("You walk carefully across the slippery log...");
			forceWalk(player, CROSS_ANIMATION, 2474, 3429, 8, null, ".. and make it safely to the other side.", 7.5);
			advanceCourseStage(player, "gnome_stronghold", 0, 1);
			return true;
		case 2285: // Obstacle net
			obstacleNet.forceText("Move it, move it, move it!");
			climb(player, player.getLocation().transform(0, -3, 1), "You climb the netting...", null, 7.5);
			advanceCourseStage(player, "gnome_stronghold", 1, 2);
			return true;
		case 35970: // Tree branch
			treeBranch.forceText("That's it - straight up.");
			climb(player, Location.locate(2473, 3420, 2), "You climb the tree...", "... to the platform above.", 5.0);
			advanceCourseStage(player, "gnome_stronghold", 2, 3);
			return true;
		case 2312: //Balancing Rope
			balanceRope.forceText("Come on scaredy cat, get across that rope!");
			player.sendMessage("You carefully cross the tightrope.");
			forceWalk(player, CROSS_ANIMATION, 2483, 3420, 7, null, null, 7.5);
			advanceCourseStage(player, "gnome_stronghold", 3, 4);
			return true;
		case 2314: //Tree branch
			climb(player, Location.locate(2486, 3420, 0), "You climb down the tree...", "You land on the ground.", 5.0);
			advanceCourseStage(player, "gnome_stronghold", 4, 5);
			return true;
		case 2286: // Obstacle net
			if (player.getLocation().getY() > 3425) {
				player.sendMessage("You can't do that from here.");
				return true;
			}
			climbingNet.forceText("My Granny can move faster than you.");
			player.sendMessage("You climb the netting...");
			climb(player, player.getLocation().transform(0, 2, 0), "You climb the netting...", null, 7.5);
			advanceCourseStage(player, "gnome_stronghold", 5, 6);
			return true;
		case 43544: // Left obstacle pipe
		case 43543: // Right obstacle pipe
			if (player.getLocation().getY() > 3431) {
				player.sendMessage("You can't enter the pipe from this side.");
				return true;
			}
			final int index = object.getId() == 43544 ? 0 : 1;
			final int currentStage = player.getAttribute("gnome_stronghold", 0);
			reverseActions(player, false);
			tick = new Tick(player.getLocation() == PIPE_LOCATIONS[index] ? 0 : 2) {
				@Override
				public void execute() {
					if (player.getLocation() == PIPE_LOCATIONS[index]) {
						stop();
						player.sendMessage("You squeeze into the pipe...");
						forceWalk(player, Animation.create(10580), player.getLocation().getX(), player.getLocation().getY() + 3, 2, new int[]{10, 60, 0}, null);
						delayForceWalk(player, null, player.getLocation().getX(), player.getLocation().getY() + 4, 1, null, null, 3);
						delayForceWalk(player, Animation.create(10580), player.getLocation().getX(), player.getLocation().getY() + 7, 2, new int[]{10, 60, 0}, null, 4, currentStage == 6 ? 46.0 : 7.5);
						if (currentStage == 6) {
							player.removeAttribute("gnome_stronghold");
						}
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				player.requestWalk(PIPE_LOCATIONS[index].getX(), PIPE_LOCATIONS[index].getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		case 43528: // Tree
			if (player.getSkills().getLevel(Skills.AGILITY) < 85) {
				player.sendMessage("You need a level of 85 Agility to access this area.");
				return true;
			}
			climb(player, Location.locate(2472, 3419, 3), "You keep climbing the tree...", "...to an even high platform.", 25.0);
			advanceCourseStage(player, "gnome_stronghold", 3, 7);
			return true;
		case 43581: // Signpost
			if (player.getSkills().getLevel(Skills.AGILITY) < 85) {
				player.sendMessage("You can't do this obstacle!");
				return true;
			}
			reverseActions(player, false);
			tick = new Tick(player.getLocation() == defaultRunLocation ? 0 : 1) {
				@Override
				public void execute() {
					if (player.getLocation() == defaultRunLocation) {
						stop();
						player.getMask().setFacePosition(player.getLocation().transform(1, 0, 0));
						forceWalk(player, RUN_ON_WALL_ANIMATION, 2484, 3418, 4, new int[]{40, 90, 1}, null, 25.0);
						advanceCourseStage(player, "gnome_stronghold", 7, 8);
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				player.requestWalk(defaultRunLocation.getX(), defaultRunLocation.getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		case 43529: // Pole to swing on
			if (player.getSkills().getLevel(Skills.AGILITY) < 85) {
				player.sendMessage("You can't do this obstacle!");
				return true;
			}
			reverseActions(player, false);
			tick = new Tick(player.getLocation() == defaultSwingLocation ? 0 : 2) {
				@Override
				public void execute() {
					if (player.getLocation() == defaultSwingLocation) {
						stop();
						player.animate(Animation.create(11783));
						delayForceWalk(player, Animation.create(11784), player.getLocation().getX(), player.getLocation().getY() + 3, 1, new int[]{0, 30, -1, 0}, null, 1);
						delayForceWalk(player, Animation.create(11785), player.getLocation().getX(), player.getLocation().getY() + 7, 1, new int[]{0, 30, -1, 0}, null, 3);
						delayForceWalk(player, null, player.getLocation().getX(), player.getLocation().getY() + 11, 4, new int[]{0, 30, -1, 0}, null, 8);
						delayForceWalk(player, null, player.getLocation().getX(), 3432, 2, new int[]{0, 30, -1}, null, 13, 25.0);
						advanceCourseStage(player, "gnome_stronghold", 8, 9);
						World.getWorld().submit(new Tick(4) {
							@Override
							public void execute() {
								stop();
								player.animate(Animation.create(11789), true);
							}
						});
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				player.requestWalk(defaultSwingLocation.getX(), defaultSwingLocation.getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		case 43539: //Barrier
			if (player.getSkills().getLevel(Skills.AGILITY) < 85) {
				player.sendMessage("You can't do this obstacle!");
				return true;
			}
			final int currentAdvancedStage = player.getAttribute("gnome_stronghold", 0);
			reverseActions(player, false);
			tick = new Tick(player.getLocation() == defaultJumpLocation ? 0 : 1) {
				@Override
				public void execute() {
					if (player.getLocation() == defaultJumpLocation) {
						stop();
						player.getMask().setFacePosition(player.getLocation().transform(0, 1, 0));
						forceWalk(player, Animation.create(2923), player.getLocation().getX(), player.getLocation().getY() + 1, 3, new int[]{30, 60, -1}, null);
						World.getWorld().submit(new Tick(3) {
							private boolean done = false;

							@Override
							public void execute() {
								if (done) {
									player.setCanAnimate(true);
									stop();
								} else {
									done = true;
									player.teleport(player.getLocation().transform(0, 2, -3));
									player.animate(Animation.create(2924));
									player.setCanAnimate(false);
									if (currentAdvancedStage == 9) {
										((Skills) player.getSkills()).addExperience(Skills.AGILITY, 630);
										player.removeAttribute("gnome_stronghold");
									}
								}
							}
						});
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				player.requestWalk(defaultJumpLocation.getX(), defaultJumpLocation.getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		}
		return false;
	}

}
*/