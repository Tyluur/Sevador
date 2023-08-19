package com.sevador.game.event.item;

import java.util.HashMap;
import java.util.Map;

import com.sevador.game.action.impl.TeleportAction;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles the teletab item action events.
 * @author Emperor
 *
 */
public final class TeletabActionEvent implements ItemActionEvent {

	/**
	 * The teletabs mapping.
	 */
	public static final Map<Integer, Teletab> TELETABS = new HashMap<Integer, Teletab>();
	
	/**
	 * Represents a teletab.
	 * @author Emperor
	 *
	 */
	public static enum Teletab {
		VARROCK_TELEPORT(8007, Location.locate(3212, 3421, 0)),
		LUMBRIDGE_TELEPORT(8008, Location.locate(3222, 3217, 0)),
		FALADOR_TELEPORT(8009, Location.locate(2964, 3378, 0)),
		CAMELOT_TELEPORT(8010, Location.locate(2757, 3477, 0)),
		ARDOUGNE_TELEPORT(8011, Location.locate(2662, 3303, 0)),
		WATCHTOWER_TELEPORT(8012, Location.locate(2547, 3113, 2)),
		TELEPORT_TO_HOUSE(8013, Entity.DEFAULT_LOCATION);
		
		/**
		 * The item id.
		 */
		final int itemId;
		
		/**
		 * The location to teleport to.
		 */
		final Location location;
		
		/**
		 * Constructs a new {@code Teletab} {@code Object}.
		 * @param itemId The item id.
		 * @param location The location.
		 */
		private Teletab(int itemId, Location location) {
			this.itemId = itemId;
			this.location = location;
		}
		
		public Location getLocation() {
			return location;
		}
	}
	
	@Override
	public boolean init() {
		for (Teletab t : Teletab.values()) {
			if (!EventManager.register(t.itemId,  this)) {
				return false;
			}
			TELETABS.put(t.itemId, t);
		}
		return true;
	}

	@Override
	public boolean handle(Player player, Item item, int interfaceId, int slot, OptionType type) {
		Teletab tab = TELETABS.get((int) item.getId());
		if (tab != null) {
			player.getActionManager().register(new TeleportAction(player, tab.location, 
					TeleportAction.TELETAB_START, TeleportAction.TELETAB_GFX,
					TeleportAction.TELETAB_END, new Graphic(-1, 0, 0, false),
					0, 4, 5, new Item(item.getId(), 1)));
			return true;
		}
		return false;
	}

}