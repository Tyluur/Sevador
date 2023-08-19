package net.burtleburtle.tick.impl;

import java.util.LinkedList;
import java.util.List;

import net.burtleburtle.tick.Tick;

import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;

/**
 * The gorund item updating tick task.
 *
 * @author Emperor
 */
public class GroundItemUpdateTick extends Tick {

	/**
	 * The singleton of this class.
	 */
	private static final GroundItemUpdateTick SINGLETON = new GroundItemUpdateTick();

	/**
	 * Constructs a new {@code GroundItemUpdateTick} {@code Object}.
	 */
	private GroundItemUpdateTick() {
		super(1);
	}

	@Override
	public boolean run() {
		List<Runnable> toExecute = new LinkedList<Runnable>();
		for (int i = 0; i < GroundItemManager.getGroundItems().size(); i++) {
			final GroundItem item = GroundItemManager.getGroundItems().get(i);
			if (item == null) {
				continue;
			}
			item.setUpdateTicks(item.getUpdateTicks() - 1);
			if (item.getUpdateTicks() < 1) {
				if (item.isPublic()) {
					toExecute.add(new Runnable() {
						@Override
						public void run() {
							GroundItemManager.removePublicGroundItem(item);
						}
					});
				} else {
					toExecute.add(new Runnable() {
						@Override
						public void run() {
							if (item.getItem() != null && item.getItem().getDefinition().isTradable()) {
								GroundItemManager.setPublic(item);
							} else {
								GroundItemManager.removeGroundItem(item);
							}
						}
					});
				}
			}
		}
		for (Runnable execute : toExecute) {
			execute.run();
		}
		return false;
	}

	/**
	 * @return the singleton
	 */
	public static GroundItemUpdateTick getSingleton() {
		return SINGLETON;
	}

}
