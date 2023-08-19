package com.sevador.game.action.impl;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.misc.Shop;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.player.Player;

/**
 * Handles a shopping action.
 * @author Emperor
 *
 */
public final class ShoppingAction extends Action {
	
	/**
	 * The shop viewed by the player.
	 */
	private final Shop shop;
	
	/**
	 * Constructs a new {@code ShoppingAction} {@code Object}.
	 * @param entity The player viewing the shop.
	 * @param shop The shop viewed.
	 */
	public ShoppingAction(Entity entity, Shop shop) {
		super(entity);
		addFlag((DEFAULT_RESET & ~ActionFlag.CLOSE_INTERFACE) & ~ActionFlag.CLOSE_CHATBOX);
		this.shop = shop;
	}
	
	@Override
	public boolean execute() {
		/*
		 * empty.
		 */
		return false;
	}
	
	@Override
	public boolean dispose(Action a) {
		Player p = entity.getPlayer();
		p.removeAttribute("open:shop");
		p.getPacketSender().setDefaultInventory();
		p.getPacketSender().sendCloseInterface();
		shop.getPlayers().remove(p);
		return true;
	}

	@Override
	public int getActionType() {
		return ActionFlag.CLOSE_INTERFACE;
	}

}