package com.sevador.game.node.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sevador.game.minigames.pestcontrol.PestControlActivity;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ActivityManager {

	private static final ActivityManager INSTANCE = new ActivityManager();

	private List<Activity> globalActivities = new ArrayList<Activity>();
	private List<Activity> activeActivities = new ArrayList<Activity>();
	
	public List<Activity> getGlobalActivities() {
		return globalActivities;
	}

	public ActivityManager() {
		register(PestControlActivity.EASY_PC);
		register(PestControlActivity.MEDIUM_PC);
		register(PestControlActivity.HARD_PC);
	}

	public void register(Activity activity) {
		globalActivities.add(activity);
	}

	public void start(Activity activity) {
		if (activity.start()) {
			activeActivities.add(activity);
		}
	}

	public void tick() {
		for (Activity activity : globalActivities) {
			if (activity.getConstraint().constrains(activity)) {
				activity.idleTick();
			}
		}
		Activity activity = null;
		for (Iterator<Activity> itr = activeActivities.iterator(); itr.hasNext(); activity = itr.next()) {
			if (activity.getConstraint().constrains(activity)) {
				activity.end();
				itr.remove();
				continue;
			}
			activity.activeTick();
		}
	}

	public static ActivityManager getActivityManger() {
		return INSTANCE;
	}

}
