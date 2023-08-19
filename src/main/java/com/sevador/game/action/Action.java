package com.sevador.game.action;

import com.sevador.game.node.model.Entity;

/**
 * Represents an action.
 * @author Emperor
 *
 */
public abstract class Action {
	
	/**
	 * The default reset flag (chatbox & normal interfaces closed, combat, movement and skills reset).
	 */
	public static int DEFAULT_RESET = ActionFlag.CLOSE_INTERFACE | ActionFlag.CLOSE_CHATBOX;/*
			| CombatAction.FLAG | MovementAction.FLAG 
			| SkillAction.FLAG | DeathAction.FLAG
			| RestAction.FLAG;*/
	
	/**
	 * The entity which is using this action.
	 */
	public final Entity entity;
	
	/**
	 * The action type.
	 */
	private final ActionType type;
	
	/**
	 * The action flag, which is used to determin which actions to reset
	 */
	private int flag = 0;

	/**
	 * Constructs a new {@code SkillAction} {@code Object}.
	 * @param entity The entity.
	 */
	public Action(Entity entity) {
		this.entity = entity;
		this.type = ActionType.DEFAULT;
	}
	
	/**
	 * Constructs a new {@code SkillAction} {@code Object}.
	 * @param entity The entity.
	 * @param type The action type of this action.
	 */
	public Action(Entity entity, ActionType type) {
		this.entity = entity;
		this.type = type;
	}
	
	/**
	 * The method called when this action is supposed to unregister.
	 * @return {@code True} if the action should be unregistered, {@code false} if not.
	 */
	public abstract boolean execute();
	
	/**
	 * Gets the action's type flag.
	 * @return The type flag.
	 */
	public abstract int getActionType();
	
	/**
	 * Disposes this action (eg. if the action got overriden).
	 * @return {@code True} if succesful, {@code false} if the action could not be disposed.
	 */
	public boolean dispose(Action action) {
		return true;
	}	
	
	/**
	 * @return the type
	 */
	public ActionType getType() {
		return type;
	}

	/**
	 * @return the flag
	 */
	public int getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void addFlag(int flag) {
		this.flag |= flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void removeFlag(int flag) {
		this.flag &= ~flag;
	}
	
}