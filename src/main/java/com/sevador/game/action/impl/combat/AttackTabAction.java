package com.sevador.game.action.impl.combat;

import net.burtleburtle.cache.format.ItemDefinition;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.button.AttackTabButtonEvent;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.ConfigPacket;
import com.sevador.utility.Constants;
import com.sevador.utility.WeaponInterface;

/**
 * Handles the clicking of an attack tab button.
 * 
 * @author Emperor
 * 
 */
public class AttackTabAction extends Action {

	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * The button id.
	 */
	private final int buttonId;

	/**
	 * Constructs a new {@code AttackTabAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity.
	 * @param buttonId
	 *            The button id.
	 */
	public AttackTabAction(Entity entity, int buttonId) {
		super(entity);
		if (buttonId > 10) {
			addFlag(CombatAction.FLAG);
		}
		this.buttonId = buttonId;
	}

	@Override
	public boolean execute() {
		Player player = entity.getPlayer();
		switch (buttonId) {
		case 4: // Special attack bar.
			TypeHandler handler = EventManager.getSpecialAttackEvent(player
					.getEquipment().getNew(3).getId());
			if (!player.getSettings().isSpecialEnabled()
					&& AttackTabButtonEvent.instantSpecial(player, handler)) {
				return true;
			}
			player.getSettings().setSpecialEnabled(
					!player.getSettings().isSpecialEnabled());
			return true;
		case 15:// auto retaliate
			player.getSettings().setRetaliating(
					!player.getSettings().isRetaliating());
			break;
		case 11:
		case 12:
		case 13:
		case 14:
			toggleStyle(player, buttonId);
			break;
		}
		return true;
	}

	/**
	 * Sets the current attack tyle used.
	 * 
	 * @param p
	 *            The player.
	 * @param buttonId
	 *            The button id clicked.
	 */
	public static void toggleStyle(Player p, int buttonId) {
		if (p.getAttribute("autocastId", -1) != -1) {
			p.removeAttribute("autocastId");
			p.getIOSession().write(new ConfigPacket(p, 108, -1));
		}
		int select = buttonId - 11;
		p.getIOSession().write(new ConfigPacket(p, 43, select));
		p.getSettings().setAttackBox(select);
		calculateType(p);
		p.getEquipment().refresh();
	}

	/**
	 * Calculates the current attack type used.
	 */
	public static void calculateType(Player player) {
		int itemId = player.getEquipment().getNew(Constants.SLOT_WEAPON).getId();
		int groupId = itemId == 1 ? 0 : ItemDefinition.forId(itemId).getGroupId();
		int select = player.getSettings().getAttackBox();
		int type = WeaponInterface.getType(groupId, select);
		int style = WeaponInterface.getStyle(groupId, select);
		if (player.getProperties() != null)
			player.getProperties().setAttackType(type);
		if (player.getProperties() != null)
			player.getProperties().setAttackStyle(style);
		if (player.getIOSession() != null)
			player.getIOSession().write(new ConfigPacket(player, 48, select));
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}