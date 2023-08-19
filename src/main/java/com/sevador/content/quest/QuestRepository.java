package com.sevador.content.quest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The quests repository.
 * @author Emperor
 */
public final class QuestRepository implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6218716251878541275L;
	
	/**
	 * The quests mapping.
	 */
	private final Map<Integer, Quest> quests = new HashMap<Integer, Quest>();
	
	/**
	 * Adds a quest on the repository.
	 * @param id The quest id.
	 * @param quest The quest.
	 * @return {@code True} when a quest was already registered, {@code false} if not.
	 */
	public boolean put(int id, Quest quest) {
		return quests.put(id, quest) != null;
	}
	
	/**
	 * Retrieves a Quest from the repository.
	 * @param id The quest id.
	 * @return The Quest object.
	 */
	public Quest get(int id) {
		return quests.get(id);
	}
	
	/**
	 * Gets a Quest from the repository.
	 * @param name The quest name.
	 * @return The Quest object.
	 */
	public Quest get(String name) {
		for (int key : quests.keySet()) {
			Quest q = quests.get(key);
			if (q != null && q.getName().equals(name)) {
				return q;
			}
		}
		return null;
	}

	/**
	 * Gets the quest mapping.
	 * @return The quests mapping.
	 */
	public Map<Integer, Quest> mapping() {
		return quests;
	}

}