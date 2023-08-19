package com.sevador.game.action.impl.packetactions;

import java.util.List;

import net.burtleburtle.tick.TypeTick;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.ActionType;
import com.sevador.game.action.impl.EquipAction;
import com.sevador.game.action.impl.emote.Emote;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.AppearanceUpdate;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.MessagePacket;

/**
 * Handles an emote action.
 * @author Emperor
 *
 */
public class EmoteAction extends Action {

	/**
	 * The action's type-flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * The amount of ticks.
	 */
	private int ticks;
	
	/**
	 * The emote to execute.
	 */
	private Emote emote;
	
	/**
	 * Constructs a new {@code EmoteAction} {@code Object}.
	 * @param player The player.
	 * @param buttonId The button id.
	 */
	public EmoteAction(Player player, int buttonId) {
		super(player);
		addFlag(DEFAULT_RESET | FLAG);
		this.emote = Emote.getEmotes().get(buttonId);
		if (emote != null) {
			if (emote.getRequiredItemSlot() > -1) {
				emote = Emote.getEmotes().get(buttonId | (player.getEquipment().getItem(emote.getRequiredItemSlot()).getId() << 10));
				if (emote == null) {
					player.getIOSession().write(new MessagePacket(player, "You need a skillcape to use this emote."));
					return;
				}
			}
			this.ticks = emote.getTicks();
		} else {
			player.getIOSession().write(new MessagePacket(player, "Unhandled emote - button id: " + buttonId));
		}
	}

	@Override
	public boolean execute() {
		if (emote == null) {
			return true;
		}
		List<TypeTick<Player>> updates = emote.getEmoteUpdates().get(ticks);
		if (updates != null) {
			for (TypeTick<Player> tick : updates) {
				tick.run(entity.getPlayer());
			}
		}
		return --ticks < 1;
	}
	
	@Override
	public boolean dispose(Action action) {
		if (action.getType() == ActionType.MOVEMENT || action.getActionType() == EquipAction.FLAG) {
			return false;
		} else if (action.getActionType() == FLAG) {
			entity.getPlayer().getIOSession().write(new MessagePacket(entity.getPlayer(), "You are already performing an emote!"));
			return false;
		}
		entity.getPlayer().getCredentials().getAppearance().setNpcId(-1);
		entity.getUpdateMasks().register(new AppearanceUpdate(entity.getPlayer()));
		entity.visual(new Animation(-1, 0, false), new Graphic(-1, 0, 0, false));
		return true;
	}
	
	@Override
	public int getActionType() {
		return FLAG;
	}

}