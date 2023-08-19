package com.sevador.game.action.impl;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.player.Player;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.StringPacket;

/**
 * Opens the Equipment stats interface.
 * @author Emperor
 *
 */
public class EquipStatsAction extends Action {

	/**
	 * The bonus names.
	 */
	private static final String[] BONUS_NAMES = { 
		"Stab: ", "Slash: ", "Crush: ", "Magic: ", "Ranged: ", 
		"Stab: ", "Slash: ", "Crush: ", "Magic: ", "Ranged: ", 
		"Summoning: ", "Absorb Melee: ", "Absorb Magic: ", 
		"Absorb Ranged: ", "Strength: ", "Ranged Strength: ", "Prayer: ", "Magic Damage: "
	};
	
	/**
	 * If the interface has been opened.
	 */
	private boolean opened = false;
	
	/**
	 * If the euqipment stats interface gets opened while banking.
	 */
	private final boolean banking;
	
	/**
	 * Constructs a new {@code EquipStatsAction} {@code Object}.
	 * @param p The player.
	 * @param banking If the player is opening this through bank interface.
	 */
	public EquipStatsAction(Player p, boolean banking) {
		super(p);
		addFlag((DEFAULT_RESET & ~ActionFlag.CLOSE_INTERFACE) & ~MovementAction.FLAG);
		this.banking = banking;
	}

	@Override
	public boolean execute() {
		if (!opened) {
			Player p = entity.getPlayer();
			p.getIOSession().write(new CS2Config(p, 199, -1));
			if (banking) {
				p.getIOSession().write(new ConfigPacket(p, 1249, 268435464));
				p.getIOSession().write(new InterfaceConfig(p, 667, 49, false));
				p.getIOSession().write(new InterfaceConfig(p, 667, 50, false));
			} else {
				p.getIOSession().write(new ConfigPacket(p, 1249, 0));
				p.getIOSession().write(new InterfaceConfig(p, 667, 49, true));
				p.getIOSession().write(new InterfaceConfig(p, 667, 50, true));
			}
			p.getIOSession().write(new AccessMask(p, 0, 15, 667, 7, 0, 1538));
			p.getIOSession().write(new AccessMask(p, 0, 28, 670, 0, 0, 1538));
			p.getIOSession().write(new CS2Config(p, 779, 28));
			p.getPacketSender().sendInterface(667);
			p.getPacketSender().sendInventoryInterface(670);
			entity.setAttribute("equipStatsOpened", true);
			refreshEquipmentStats(p);
			opened = true;
		}
		return false;
	}

	@Override
	public boolean dispose(Action a) {
		entity.removeAttribute("equipStatsOpened");
		Player p = entity.getPlayer();
		p.getPacketSender().setDefaultInventory();
		p.getPacketSender().sendCloseInterface();
		return true;
	}
	
	/**
	 * Refreshes the equipment stats.
	 * @param player The player.
	 */
	public static void refreshEquipmentStats(Player player) {
		if (!player.getAttribute("equipStatsOpened", false)) {
			return;
		}
		for (int i = 0; i < 11; i++) {
			int bonus = player.getProperties().getStats()[i];
			player.getIOSession().write(new StringPacket(player, new StringBuilder(BONUS_NAMES[i])
				.append(bonus < 0 ? "" : "+").append(bonus).toString(), 667, 31 + i));			
		}
		for (int i = 0; i < 3; i++) {
			int bonus = player.getProperties().getStats()[15 + i];
			player.getIOSession().write(new StringPacket(player, new StringBuilder(BONUS_NAMES[11 + i])
				.append(bonus < 0 ? "" : "+").append(bonus).toString(), 667, 42 + i));
		}
		for (int i = 0; i < 4; i++) {
			int bonus = player.getProperties().getStats()[11 + i];
			player.getIOSession().write(new StringPacket(player, new StringBuilder(BONUS_NAMES[14 + i])
				.append(bonus < 0 ? "" : "+").append(bonus).append(i == 3 ? "%" : "").toString(), 667, 45 + i));
		}
		player.getIOSession().write(new CS2Config(player, 779, 1712));
		player.getIOSession().write(new CS2Config(player, 199, -1));
		player.getPacketSender().sendConfig(1248, 134218753);
		player.getIOSession().write(new StringPacket(player, "Weight: " + player.getProperties().getCarriedWeight(player), 667, 76));
	}
	
	@Override
	public int getActionType() {
		return ActionFlag.CLOSE_INTERFACE;
	}

}