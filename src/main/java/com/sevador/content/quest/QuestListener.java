package com.sevador.content.quest;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.sevador.Main;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Misc;

/**
 * Handles the event listening and progressing for quests.
 *
 * @author Emperors
 */
@SuppressWarnings("unchecked")
public class QuestListener implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5550377121169469800L;

	/**
	 * The quests mapping.
	 */
	private static final Map<Integer, Quest> QUESTS = new HashMap<Integer, Quest>();

	/**
	 * The maximum quest points.
	 */
	private static int maximumQuestPoints = 0;

	static {
		try {
			for (Object o : Misc.getClasses(QuestListener.class.getPackage().getName() + ".impl")) {
				if (!(o instanceof Quest)) {
					continue;
				}
				Quest quest = (Quest) o;
				maximumQuestPoints += quest.getQuestPoints();
				QUESTS.put(quest.getId(), quest);

			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IllegalStateException("Unable to load quests - " + t);
		}
		Main.getLogger().info("Loaded " + QUESTS.size() + ", there are now " + maximumQuestPoints + " quest points you can achieve.");
	}

	/**
	 * The quest respository.
	 */
	private final QuestRepository repository = new QuestRepository();

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The amount of quest points.
	 */
	protected int questPoints;

	/**
	 * Constructs a new {@code QuestListener} {@code Object}.
	 *
	 * @param player The player.
	 */
	public QuestListener(Player player) {
		this.player = player;
	}

	/**
	 * Initializes the quests.
	 */
	public void init() {
		player.getPacketSender().sendConfig(101, questPoints);
		player.getPacketSender().sendConfig(904, maximumQuestPoints);
		for (Quest q : repository.mapping().values()) {
			q.initialize(player);
		}
	}

	/**
	 * Starts a new quest.
	 *
	 * @param id The quest id to start.
	 * @return The quest we started, or the quest object of the already started quest.
	 */
	public Quest start(int id) {
		Quest quest = repository.get(id);
		if (quest != null) {
			return quest;
		}
		quest = QUESTS.get(id);
		repository.put(id, quest);
		return quest;
	}

	/**
	 * Gets the progress for a certain quest.
	 *
	 * @param id The quest id.
	 * @return The progress value.
	 */
	public int getProgress(int id) {
		Quest quest = repository.get(id);
		if (quest != null) {
			return quest.getProgress();
		}
		return -1;
	}

	/**
	 * Gets a quest.
	 *
	 * @param id The quest id.
	 * @return The quest object.
	 */
	public Quest get(int id) {
		return repository.get(id);
	}

	/**
	 * Gets a quest.
	 *
	 * @param name The quest name.
	 * @return The quest object.
	 */
	public Quest get(String name) {
		return repository.get(name);
	}

	/**
	 * @return the questPoints
	 */
	public int getQuestPoints() {
		return questPoints;
	}

	/**
	 * @param questPoints the questPoints to set
	 */
	public void addQuestPoints(int questPoints) {
		this.questPoints += questPoints;
		player.getPacketSender().sendConfig(101, this.questPoints);
	}
}