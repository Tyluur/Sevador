package com.sevador.game.action;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.sevador.Main;
import com.sevador.game.action.impl.NullAction;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.action.SkillAction;

/**
 * The {@link SkillAction} managing class.
 * 
 * @author Emperor
 * 
 */
public class ActionManager {

	/**
	 * A list of actions to be executed.
	 */
	private final List<Action> actionList = new ArrayList<Action>();

	/**
	 * A list of queued actions.
	 */
	private final Queue<Action> queuedActions = new ArrayDeque<Action>();

	/**
	 * IF we're queueing.
	 */
	private boolean queuing = false;

	/**
	 * The entity.
	 */
	@SuppressWarnings("unused")
	private final Entity entity;

	/**
	 * The currently being executed actions' flags.
	 */
	private int flag = 0;

	/**
	 * Constructs a new {@code ActionManager} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 */
	public ActionManager(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Registers a new {@code SkillAction} for the entity to execute.
	 * 
	 * @param action
	 *            The action.
	 */
	public void register(Action action) {
		try {
			if (queuing) {
				queuedActions.add(action);
				return;
			}
			if (unregister(action, action.getFlag())) {
				actionList.add(action);
				flag |= action.getActionType();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Unregisters actions depending on the bit flag.
	 * 
	 * @param action
	 *            The action we want to add.
	 * @param flag
	 *            The flag.
	 * @return {@code True} if we can add the action, {@code false} if not.
	 */
	public boolean unregister(Action action, int flag) {
		for (Iterator<Action> it = actionList.iterator(); it.hasNext();) {
			Action a = it.next();
			if ((flag & a.getActionType()) != 0) {
				if (!a.dispose(action)) {
					return false;
				}
				if ((this.flag & a.getActionType()) != 0) {
					this.flag &= ~a.getActionType();
				}
			}
		}
		return true;
	}

	/**
	 * Unregisters all actions using this flag.
	 * 
	 * @param flag
	 *            The action type flag.
	 * @return {@code True} if 1 or more actions got removed, {@code false} if
	 *         not.
	 */
	public boolean unregister(final int flag) {
		int removed = 0;
		for (Iterator<Action> it = actionList.iterator(); it.hasNext();) {
			Action a = it.next();
			if (a.getActionType() == flag
					&& a.dispose(NullAction.getSingleton())) {
				if ((this.flag & a.getActionType()) != 0) {
					this.flag &= ~a.getActionType();
				}
				removed++;
			}
		}
		return removed > 0;
	}

	/**
	 * Resets non-actions depending on this action's reset flag.
	 * 
	 * @param action
	 *            The action.
	 */
	public void reset(Action action) {
		/*
		 * if (entity.isPlayer() && (action.getFlag() &
		 * ActionFlag.CLOSE_INTERFACE) != 0) {
		 * entity.getPlayer().getPacketSender().sendCloseInterface();
		 * entity.getPlayer().getBank().close(); } if (entity.isPlayer() &&
		 * (action.getFlag() & ActionFlag.CLOSE_CHATBOX) != 0) { if
		 * (!entity.getPlayer().getDialogueManager().inDialogue())
		 * entity.getPlayer().getPacketSender().sendChatBoxInterface(137); } if
		 * ((action.getFlag() & MovementAction.FLAG) != 0 &&
		 * action.getActionType() != MovementAction.FLAG) {
		 * entity.getWalkingQueue().reset(); }
		 */
	}

	/**
	 * Checks if we're executing a certain action.
	 * 
	 * @param actionFlag
	 *            The action's flag.
	 * @return {@code true} if so.
	 */
	public boolean contains(int actionFlag) {
		return (flag & actionFlag) != 0;
	}

	/**
	 * Gets an action.
	 * 
	 * @param actionFlag
	 *            The action flag.
	 * @return The action.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(int actionFlag) {
		for (Action a : actionList) {
			if (a.getActionType() == actionFlag) {
				return (T) a;
			}
		}
		return null;
	}

	/**
	 * Updates the actions.
	 */
	public void update() {
		try {
			int newFlag = flag;
			queuing = true;
			for (Iterator<Action> it = actionList.iterator(); it.hasNext();) {
				Action a = it.next();
				try {
					reset(a);
					if ((flag & a.getActionType()) != 0) {
						if (a.execute()) {
							it.remove();
							if ((newFlag & a.getActionType()) != 0) {
								newFlag &= ~a.getActionType();
							}
						}
					} else {
						it.remove();
					}
				} catch (Throwable t) {
					t.printStackTrace();
					Main.getLogger().info("Could not remove action [" + a.getClass().getSimpleName() + "].");
				}
			}
			flag = newFlag;
			queuing = false;
			while (!queuedActions.isEmpty()) {
				register(queuedActions.poll());
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Disposes all actions and clears the action list.
	 */
	public void clear() {
		for (Iterator<Action> it = actionList.iterator(); it.hasNext();) {
			try {
				Action a = it.next();
				a.dispose(NullAction.getSingleton());
				it.remove();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

}