package com.sevador.game.event.button;

import net.burtleburtle.tick.Tick;

import com.sevador.game.action.impl.combat.AttackTabAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.special.PowerOfLight;
import com.sevador.game.event.special.QuickSmash;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Interaction;
import com.sevador.game.node.model.combat.Movement;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;

/**
 * Handles an attack tab button event.
 * @author Emperor
 *
 */
public class AttackTabButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(884, this);
	}

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		switch (buttonId) {
		case 4:
		case 15:
		case 11:
		case 12:
		case 13:
		case 14:
			player.getActionManager().register(new AttackTabAction(player, buttonId));
			return true;
		}
		return false;
	}

	/**
	 * Checks and executes an instant special.
	 * @param player The player.
	 * @param handler The special attack type handler.
	 * @return {@code True} if a special attack event got fired, {@code false} if not.
	 */
	public static final boolean instantSpecial(Player player, TypeHandler handler) {
		if (handler == null) {
			return false;
		}
		if (handler instanceof QuickSmash) {
			final Interaction i = new Interaction(player, player.getCombatAction().getVictim());
			if (i.victim != null && !CombatType.MELEE.canInteract(player, Movement.getDestination(player, i.victim))) {
				i.victim = null;
			}
			if (handler.handle(i) && i.commence()) {
				World.getWorld().submit(new Tick(1) {
					@Override
					public boolean run() {
						try {
							return i.tick();
						} catch (Throwable t) {
							t.printStackTrace();
						}
						return true;
					}
				});
				return true;
			}
		} else if (handler instanceof PowerOfLight) {
			handler.handle(new Interaction(player, null));
			return true;
		}
		return false;
	}
}
