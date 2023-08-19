package com.sevador.game.node.model.skills.agility;

import java.util.ArrayList;
import java.util.List;

import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.skills.agility.obstacles.BarbarianCourse;
import com.sevador.game.node.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Agility {

	private static List<AbstractCourse> courses = new ArrayList<AbstractCourse>();

	static {
		//courses.add(new GnomeStrongholdCourse());
		courses.add(new BarbarianCourse());
		//courses.add(new WorldObstacles());
	}

	public static void init() {

	}

	public static boolean handleObject(Player player, GameObject gameObject) {
		for (AbstractCourse course : courses) {
			if (course.handleObject(player, gameObject)) {		
				return true;
			}
		}
		return false;
	}
}
