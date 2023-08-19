package com.sevador.game.action.impl;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.container.Container;
import com.sevador.game.node.model.container.Container.Type;
import com.sevador.game.node.model.container.impl.TradeContainerListener;
import com.sevador.game.node.player.Player;
import com.sevador.game.world.PlayerWorldLoader;
import com.sevador.game.world.WorldLoader;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.MessagePacket;
import com.sevador.network.out.StringPacket;
import com.sevador.utility.Misc;
import com.sevador.utility.TradeState;

/**
 * Handles a trade action.
 * @author Emperor
 *
 */
public final class TradeAction extends Action {

	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * The parameters used in the cs2script.
	 */
	private static final Object[] PARAMS = new Object[] { "", "", "", "Value<col=FF9040>", "Remove-X", "Remove-All", 
		"Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, 335 << 16 | 31 };
	
	/**
	 * The parameters used in the second cs2script.
	 */
	private static final Object[] PARAMS_1 = new Object[] { "", "", "Lend", "Value<col=FF9040>", "Offer-X", "Offer-All", 
		"Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, 336 << 16 };
	
	/**
	 * The parameters used in the third cs2script.
	 */
	private static final Object[] PARAMS_2 = new Object[] { "", "", "", "", "", "", "", "", "Value<col=FF9040>", 
		-1, 0, 7, 4, 90, 335 << 16 | 34 };
	
	/**
	 * Add the flag to the default reset flag.
	 */
	static {
		DEFAULT_RESET |= FLAG;
	}
	
	/**
	 * The player to trade with.
	 */
	public final Player other;
	
	/**
	 * The current trade state.
	 */
	private TradeState state = TradeState.REQUESTING;
	
	/**
	 * Constructs a new {@code TradeAction} {@code Object}.
	 * @param entity The entity requesting a trade.
	 * @param other The player to trade with.
	 */
	public TradeAction(Entity entity, Player other) {
		super(entity);
		addFlag(DEFAULT_RESET & ~ActionFlag.CLOSE_INTERFACE);
		this.other = other;
	}
	

	/**
	 * The loader instance.
	 */
	private static final WorldLoader<Player> PLAYER_LOADER = new PlayerWorldLoader();

	@Override
	public boolean execute() {
		switch (state) {
		case FIRST_SCREEN:
		case SECOND_SCREEN:
			return false;
		case REQUESTING:
			if (entity.getAttribute("request:trade") != other) {
				if (entity.getLocation().getDistance(other.getLocation()) > 1) {
					entity.getCombatAction().getMovement().go(other, CombatType.MELEE);
					return false;
				}
				entity.setAttribute("request:trade", other);
				if (other.getAttribute("request:trade") != entity) {
					other.getIOSession().write(new MessagePacket(other, 100, "wishes to trade with you.", 
							Misc.formatPlayerNameForDisplay(entity.getPlayer().getCredentials().getUsername())));
					entity.getPlayer().getPacketSender().sendMessage("Sending trade request...");
				}
			}
			if (other.getAttribute("request:trade") == entity) {
				open(entity.getPlayer(), other);
				entity.setAttribute("trade:state", state = TradeState.FIRST_SCREEN);
			}
			return false;
		case FINISHED:
			return true;
		}
		return true;
	}
	
	@Override
	public boolean dispose(Action a) {
		if (state == TradeState.FINISHED) {
			return true;
		}
		entity.removeAttribute("request:trade");
		Container c = entity.getAttribute("trade-cont");
		if (c != null) {
			entity.removeAttribute("trade:accepted");
			entity.getPlayer().getPacketSender().sendCloseInterface().setDefaultInventory();
			entity.getPlayer().getInventory().addAll(c);
			c.clear();
			entity.removeAttribute("trade-cont");
			other.getActionManager().unregister(FLAG);
		} else if (state != TradeState.REQUESTING) {
			entity.getPlayer().getPacketSender().sendMessage("You've declined the trade.");
			other.getPacketSender().sendMessage("The other player has declined the trade.");
		}
		return true;
	}

	/**
	 * Accepts the trade.
	 */
	public void accept() {
		if (other.getAttribute("trade:accepted", false)) {
			switch (state) {
			case FIRST_SCREEN:
				TradeAction a = other.getActionManager().get(FLAG);
				if (a != null) {
					other.setAttribute("trade:state", a.state = TradeState.SECOND_SCREEN);
					entity.setAttribute("trade:state", state = TradeState.SECOND_SCREEN);
					openSecond(entity.getPlayer(), other);
					openSecond(other, entity.getPlayer());
				} else {
					decline();
				}
				break;
			case SECOND_SCREEN:
				a = other.getActionManager().get(FLAG);
				if (a != null) {
					a.state = TradeState.FINISHED;
					state = TradeState.FINISHED;
					other.getInventory().addAll((Container) entity.getAttribute("trade-cont"));
					entity.getPlayer().getInventory().addAll((Container) other.getAttribute("trade-cont"));
					other.removeAttribute("trade-cont");
					entity.removeAttribute("trade-cont");
					other.removeAttribute("trade:state");
					entity.removeAttribute("trade:state");
					other.removeAttribute("trade:accepted");
					entity.removeAttribute("trade:accepted");
					other.removeAttribute("request:trade");
					entity.removeAttribute("request:trade");
					other.getPacketSender().sendCloseInterface().setDefaultInventory().sendMessage("Accepted trade.");
					entity.getPlayer().getPacketSender().sendCloseInterface().setDefaultInventory().sendMessage("Accepted trade.");
					PLAYER_LOADER.save(entity);
					PLAYER_LOADER.save(other);
				} else {
					decline();
				}
			case FINISHED:
				break;
			case REQUESTING:
				break;
			default:
				break;
			}
			return;
		}
		if (!other.getInventory().hasSpaceFor((Container) entity.getAttribute("trade-cont"))) {
			entity.getPlayer().getPacketSender().sendMessage("The other player doesn't have enough space in his inventory.");
			return;
		}
		if (!entity.getPlayer().getInventory().hasSpaceFor((Container) other.getAttribute("trade-cont"))) {
			entity.getPlayer().getPacketSender().sendMessage("You do not have enough inventory space to accept this trade.");
			return;
		}
		int interfaceId = state == TradeState.FIRST_SCREEN ? 335 : 334;
		int child = state == TradeState.FIRST_SCREEN ? 37 : 34;
		entity.getPlayer().getIOSession().write(new StringPacket(entity.getPlayer(), "Waiting for other player...", interfaceId, child));
		other.getIOSession().write(new StringPacket(other, "The other player has accepted.", interfaceId, child));
		entity.setAttribute("trade:accepted", true);
	}
	
	/**
	 * Declines the trade.
	 */
	public void decline() {
		entity.getActionManager().unregister(FLAG);
	}

	/**
	 * Opens the trade screen for a player.
	 * @param player The player opening the trade screen.
	 * @param other The player the player is trading with.
	 */
	private static void open(Player player, Player other) {
		player.getIOSession().write(new CS2Script(player, 150, "IviiiIsssssssss", PARAMS));
		player.getIOSession().write(new AccessMask(player, 0, 27, 335, 31, 0, 1150));
		player.getIOSession().write(new CS2Script(player, 695, "IviiiIsssssssss", PARAMS_2));
		player.getIOSession().write(new AccessMask(player, 0, 27, 335, 34, 0, 1026));
		player.getPacketSender().sendInterface(335);
		player.getIOSession().write(new CS2Script(player, 150, "IviiiIsssssssss", PARAMS_1));
		player.getIOSession().write(new AccessMask(player, 0, 27, 336, 0, 0, 1278));
		player.getPacketSender().sendInventoryInterface(336);
		String name = Misc.formatPlayerNameForDisplay(other.getCredentials().getDisplayName());
		player.getIOSession().write(new StringPacket(player, "", 335, 37));
		player.getIOSession().write(new StringPacket(player, "Trading with: " + name, 335, 15));
		player.getIOSession().write(new StringPacket(player, name, 335, 22));
		Container c = new Container(Type.STANDARD, 28, new TradeContainerListener(player, other));
		player.setAttribute("trade-cont", c);
		c.refresh();
	}
	
	/**
	 * Opens the second trade screen for a player.
	 * @param player The player opening the trade screen.
	 * @param other The player the player is trading with.
	 */
	private static void openSecond(Player player, Player other) {
		player.removeAttribute("trade:accepted");
		player.getPacketSender().sendInterface(334);
		player.getIOSession().write(new StringPacket(player, "<col=00FFFF>Trading with:<br><col=00FFFF>" + Misc.formatPlayerNameForDisplay(other.getCredentials().getUsername()), 334, 54));
		player.getIOSession().write(new StringPacket(player, "Are you sure you want to make this trade?", 334, 34));
	}
	
	@Override
	public int getActionType() {
		return FLAG;
	}

}