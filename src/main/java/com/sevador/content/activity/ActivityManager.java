package com.sevador.content.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author `Discardedx2
 */
public class ActivityManager {

	private static final ActivityManager INSTANCE = new ActivityManager();

	private List<Activity> globalActivities = new ArrayList<Activity>();
	private List<Activity> activeActivities = new ArrayList<Activity>();

	public ActivityManager() {
		//register(new PestControl(PestControlDifficulty.EASY));
		//register(new WarriorsGuildActivity());
	}
	
	public void register(Activity activity) {
		globalActivities.add(activity);
		//activity.getEventListener().register(EventManager.getEventManager());
	}
	
	public void start(Activity activity) {
		if (activity.start()) {
			activeActivities.add(activity);
		}
	}

	public void tick() {;
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
