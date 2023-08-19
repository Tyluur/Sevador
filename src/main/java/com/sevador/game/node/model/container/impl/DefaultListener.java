package com.sevador.game.node.model.container.impl;

import com.sevador.game.node.Item;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.ContainerListener;

/**
 * The default listener used.
 * @author Emperor
 *
 */
public class DefaultListener implements ContainerListener {

	/**
	 * The singleton object.
	 */
	private static final DefaultListener SINGLETON = new DefaultListener();

	/**
	 * Constructs a new {@code DefaultListener} {@code Object}.
	 */	
	private DefaultListener() {
		/**
		 * empty.
		 */
	}

	@Override
	public boolean itemChanged(Container container, int slot) {
		return true;
	}

	@Override
	public boolean itemsAdded(Container container, Item... items) {
		return true;
	}

	@Override
	public boolean itemsRemoved(Container container, Item... items) {
		return true;
	}

	@Override
	public boolean refresh(Container container) {
		return true;
	}

	/**
	 * Gets the singleton object of this class.
	 * @return The singleton object.
	 */
	public static DefaultListener getSingleton() {
		return SINGLETON;
	}

}
