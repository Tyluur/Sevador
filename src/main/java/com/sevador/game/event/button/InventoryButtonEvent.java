package com.sevador.game.event.button;

import java.util.List;

import net.burtleburtle.tick.Tick;

import com.sevador.game.action.impl.EquipAction;
import com.sevador.game.action.impl.ItemAction;
import com.sevador.game.action.impl.packetactions.DropItemAction;
import com.sevador.game.event.ButtonEvent;
import com.sevador.game.event.EventManager;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.World;
import com.sevador.network.out.MessagePacket;
import com.sevador.utility.Misc;
import com.sevador.utility.OptionType;

/**
 * Handles an inventory button event.
 * @author Emperor
 *
 */
public class InventoryButtonEvent implements ButtonEvent {

	@Override
	public boolean init() {
		return EventManager.register(679, this);
	}

	private static final int PRIVATE_ROLL = 85, CLAN_ROLL = 7, CHOOSE_DICE = 66, PUT_AWAY = 84;

	@Override
	public boolean handle(Player player, int opcode, int interfaceId, int buttonId, int itemId, int slot) {
		if (itemId == 15098) {
			if ((Integer) (player.getForumTable("posts")) < 100) {
				player.getPacketSender().sendMessage("You must have a post count of 100 to use a dice; you only have " + (Integer) (player.getForumTable("posts")) + " right now.");
				return true;
			}
			switch(opcode) {
			case PRIVATE_ROLL:
				rollDice(player, null);
				return true;
			case CLAN_ROLL:
				if (player.getSettings().getCurrentFriendChat() != null) {
					rollDice(player, player.getSettings().getCurrentFriendChat().getMembers());
				} else {
					player.getPacketSender().sendMessage("You aren't in a Friends Chat Channel.");
				}
				return true;
			case CHOOSE_DICE:
				return true;
			case PUT_AWAY:
				return true;
			}
		}
		switch (opcode) {
		case 85:
			player.getActionManager().register(new ItemAction(player, OptionType.FIRST, itemId, 679, slot));
			return true;
		case 66:
			player.getActionManager().register(new ItemAction(player, OptionType.SECOND, itemId, 679, slot));
			return true;
		case 84:
			player.getActionManager().register(new ItemAction(player, OptionType.THIRD, itemId, 679, slot));
			return true;
		case 7:
			if (itemId == 4155 && player.getTask() != null)
				player.getPacketSender().sendMessage("Your current assignment is "
						+ player.getTask().getName().toLowerCase() + "; only "
						+ player.getTask().getTaskAmount() + " more to go.");
			else if (player.getTask() == null && itemId == 4155)
				player.getPacketSender().sendMessage("You do not have a slayer task.");
			else {
				player.getActionManager().register(new EquipAction(player, itemId, slot, true));
				return true;
			}
			return true;
		case 40:
			player.getActionManager().register(new DropItemAction(player, slot, itemId));
			return true;
		case 54:
			player.getIOSession().write(new MessagePacket(player, player.getInventory().get(slot).getDefinition().getExamine()));
			return true;
		}
		return false;
	}

	public void rollDice(final Player roller, final List<Player> friends) {
		roller.setAnimation(new Animation(11900));
		roller.getPacketSender().sendMessage("Rolling...");
		World.getWorld().submit(new Tick(3) {

			@Override
			public boolean run() {
				final boolean diceChance = true;
				int chance = Misc.random(diceChance ? 60 : 1, 100);	
				if (friends != null) {
					for (Player f : friends) {
						f.getPacketSender().sendMessage("Friends Chat channel-mate <col=FF0000>" + Misc.formatPlayerNameForDisplay(roller.getCredentials().getUsername()) + "</col> rolled <col=FF0000>" + chance + "</col> on the percentile dice.");
					}
				} else {
					roller.getPacketSender().sendMessage("Friends Chat channel-mate <col=FF0000>" + Misc.formatPlayerNameForDisplay(roller.getCredentials().getUsername()) + "</col> rolled <col=FF0000>" + chance + "</col> on the percentile dice.");
				}
				return true;
			}

		});
		//		World.getSingleton().("dice_roll", new Tick(3) {
		//			public void execute() {
		//				stop();
		//				int chance = Misc.random(CommandAction.diceChance ? 60 : 1, 100);
		//				roller.getPacketSender().sendMessage("You rolled <col=FF0000>" + chance + "</col> on the perecentile dice.");
		//				if (players != null) {
		//					for (Player player : players) {
		//						if (player != roller) {
		//							player.getPacketSender().sendMessage("Friends Chat channel-mate <col=FF0000>" + Misc.formatPlayerNameForDisplay(roller.getUsername()) + "</col> rolled <col=FF0000>" + chance + "</col> on the percentile dice.");
		//						}
		//					}
		//				}
		//			}
		//		});
	}

}
