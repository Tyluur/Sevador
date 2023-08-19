package com.sevador.game.node.model.skills.agility;

import net.burtleburtle.tick.Tick;

import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.world.World;
import com.sevador.utility.OptionType;
/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class AbstractCourse {

	public static final Animation CLIMB_ANIMATION = ClimbingHandler.CLIMB_ANIMATION, CROSS_ANIMATION = new Animation(9908);

	public abstract boolean handleObject(Player player, GameObject object);

	public void forceWalk(final Player player, Animation animation, int toX, int toY, int ticks, int[] forceMovement, final String finishMessage) {
		forceWalk(player, animation, toX, toY, ticks, forceMovement, finishMessage, 0.0);
	}

	public void forceWalk(final Player player, Animation animation, int toX, int toY, int ticks, int[] forceMovement, final String finishMessage, final double experience) {
		final boolean running;
		if (forceMovement != null) {
			player.forceMovement(animation, toX, toY, forceMovement[0], forceMovement[1], forceMovement[2], ticks, forceMovement.length < 4);
			reverseActions(player, false);
			running = player.getWalkingQueue().isRunning();
			player.setAnimation(animation);
		} else {
			running = player.getWalkingQueue().isRunning();
			player.setAnimation(animation);
			player.getWalkingQueue().setRunning(false);
			player.requestWalk(toX, toY);
			reverseActions(player, false);
		}
		if (experience > 0) {
			player.submitTick("restoreAgility", new Tick(ticks) {

				@Override
				public boolean run() {
					stop();
					((Skills) player.getSkills()).addExperience(Skills.AGILITY, experience);
					reverseActions(player, true);
					if (finishMessage != null)
						player.getPacketSender().sendMessage(finishMessage);
					player.setAnimation(new Animation(-1));
					player.getWalkingQueue().setRunning(running);
					return true;
				}
			}, true);
		}
	}

	public void delayForceWalk(final Player player, Animation animation, int toX, int toY, int ticks, int[] forceMovement, final String finishMessage, final int delay) {
		delayForceWalk(player, animation, toX, toY, ticks, forceMovement, finishMessage, delay, 0.0);
	}

	public void delayForceWalk(final Player player, final Animation animation, final int toX, final int toY, final int ticks, final int[] forceMovement, final String finishMessage, final int delay, final double experience) {
		World.getWorld().submit(new Tick(delay) {
			@Override
			public boolean run() {
				stop();
				forceWalk(player, animation, toX, toY, ticks, forceMovement, finishMessage, experience);
				return true;
			}
		});
	}

	public void reverseActions(Player player, boolean allowed) {
		if (!allowed) {
			player.setAttribute("cantMove", Boolean.TRUE);
			player.setAttribute("busy", Boolean.TRUE);
		} else {
			player.removeAttribute("cantMove");
			player.removeAttribute("busy");
		}
	}

	public void climb(final Player player, final Location location, final String climbMessage, final String arrivalMessage, final double experience) {
		player.setAnimation(CLIMB_ANIMATION);
		player.getPacketSender().sendMessage(climbMessage);
		reverseActions(player, false);
		player.submitTick("agility_tick", new Tick(1) {
			private boolean done = false;

			@Override
			public boolean run() {
				if (done) {
					((Skills) player.getSkills()).addExperience(Skills.AGILITY, experience);
					player.getPacketSender().sendMessage(arrivalMessage);
					stop();
					return true;
				}
				done = true;
				player.teleport(location);
				reverseActions(player, true);
				return true;
			}
		});
	}

	public void advanceCourseStage(Player player, String attributeIdentifier, int expectedStage, int nextStage) {
		int currentStage = player.getAttribute(attributeIdentifier, 0);
		if (currentStage == expectedStage) {
			player.setAttribute(attributeIdentifier, nextStage);
		}
	}

	/**
	 * @author Steve <golden_32@live.com>
	 * @author 'Mystic Flow <Steven@rune-server.org>
	 */
	public static class ClimbingHandler {

		public static final Animation CLIMB_ANIMATION =  Animation.create(828);

		public static boolean handleClimb(final Player player, GameObject object, OptionType clickOption) {
			int option = 0;
			switch (clickOption) {
			case FIRST:
				option = 0;
				break;
			case SECOND:
				option = 1;
				break;
			case THIRD:
				option = 2;
				break;
			case FOURTH:
				break;
			default:
				break;
			}
			String name = object.getDefinition().name.toLowerCase();
			if (!name.contains("ladder") && !name.contains("stair") && !name.contains("staircase")) {
				return false;
			}
			Location teleport = null;
			if (name.contains("staircase")) {
				int x = 0;
				int y = 0;
				switch (object.getRotation()) {
				case 3:
					y += 2;
					x++;
					break;
				case 1:
					y--;
					break;
				}
				String optionSelected = object.getDefinition().options[option].toLowerCase();
				boolean up = true;
				if (optionSelected.equals("climb-down")) {
					x = -x;
					y = -y;
					up = false;
					switch (object.getRotation()) {
					case 1:
						y--;
						x--;
						break;
					}
				}
				if(object.getId() == 4495)
					player.teleport(Location.locate(3418, 3541, 2));
				else if(object.getId() == 4496)
					player.teleport(Location.locate(3412, 3541, 1));
				else
					player.teleport(object.getLocation().transform(x, y, up ? 1 : -1));
			} else if (name.contains("trapdoor")) {
				teleport = player.getLocation().transform(0, 6400, 0);
			} else if (name.contains("ladder")) {
				if (player.getLocation().getY() > 8500) {
					teleport = player.getLocation().transform(0, -6400, 0);
				} else {
					String optionSelected = object.getDefinition().options[option].toLowerCase();
					if (optionSelected.contains("-up")) {
						teleport = player.getLocation().transform(0, 0, 1);
					} else if (optionSelected.contains("-down")) {
						boolean isZero = player.getLocation().getZ() == 0;
						teleport = player.getLocation().transform(0, isZero ? 6400 : 0, isZero ? 0 : -1);
					} else if (optionSelected.equals("climb")) {
						showInterfaceForClimb(player);
					}
				}
			}
			if (teleport != null) {
				final Location finalTeleport = teleport;
				player.setAnimation(CLIMB_ANIMATION);
				World.getWorld().submit(new Tick(2) {
					@Override
					public boolean run() {
						stop();
						player.teleport(finalTeleport);
						return true;
					}
				});
			}
			return true;
		}

		private static void showInterfaceForClimb(Player player) {
			//DialogueManager.sendOptionDialogue(player, new int[]{233, 234, -1}, "Go up", "Go down", "Cancel");
		}
	}


}
