package com.sevador.network.in;

import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.thread.MajorUpdateWorker;
import net.burtleburtle.thread.NodeWorker;

import com.sevador.Main;
import com.sevador.content.grandExchange.ExchangeHandler;
import com.sevador.game.event.EventManager;
import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.model.skills.smithing.Smithing.ForgingInterface;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.ContainerPacket;
import com.sevador.network.out.InterfacePacket;
import com.sevador.network.out.LogoutPacket;

/**
 * Handles an action button.
 * 
 * @author Emperor
 * 
 */
public class ActionButtonPacketHandler implements PacketSkeleton {

	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		int clickData = packet.readLEInt();
		int interfaceId = clickData & 0xFFF;
		int componentId = clickData >> 16;
		int itemId = packet.readShortA();
		int slot = packet.readShortA();
		int buttonId = componentId;
		if (itemId == 65535) {
			itemId = -1;
		}
		if (slot == 65535) {
			slot = -1;
		}
		player.getSkillAction().forceStop();
		if (interfaceId != 182 && player.getAttribute("inTutorial") != null && player.getAttribute("inTutorial").equals(true)) {
			player.getDialogueManager().startDialogue("StartDialogue");
			return true;
		}
		if (interfaceId == 206 || interfaceId == 207) {
			if (interfaceId == 206 && buttonId == 13) 
				if (player.getPriceCheck().isOpen())
					player.getPriceCheck().close();
			switch (packet.getOpcode()) {
			case 48:
				InputHandler.requestInput(player, 4, "Please enter an amount:");
				player.setAttribute("slotId", interfaceId == 206 ? slot / 2 : slot);
				player.setAttribute("itemPriceCheckId", itemId);
				player.setAttribute("inventoryAction", interfaceId == 207);
				return true;
			case 58:
				player.getPacketSender().sendMessage(ItemDefinition.forId(itemId).getExamine());
				return true;
			case 85:
				if (interfaceId == 207) {
					return player.getPriceCheck().checkPrice(itemId, slot, 1);
				}
				if (interfaceId == 206) {
					return player.getPriceCheck().remove(itemId, slot / 2, 1);
				}
				return true;
			case 7:
				if (interfaceId == 206)
					player.getPriceCheck().remove(itemId, slot / 2, 5);
				if (interfaceId == 207) 
					player.getPriceCheck().checkPrice(itemId, slot, 5);
				return true;
			case 66:
				if (interfaceId == 206)
					return player.getPriceCheck().remove(itemId, slot / 2, 10);
				if (interfaceId == 207)
					player.getPriceCheck().checkPrice(itemId, slot, 10);
				return true;
			case 11:
				if (interfaceId == 206)
					player.getPriceCheck().remove(itemId, slot / 2, player.getPriceCheck().getContainer().getAmount(itemId));
				if (interfaceId == 207)
					player.getPriceCheck().checkPrice(itemId, slot, player.getInventory().getAmount(itemId));
				return true;
			default:
				System.err.println("Unhandled opcode: " + packet.getOpcode());
			}
		}
		switch (interfaceId) {
		case 640:
			switch (buttonId) {
			case 18:
			case 21:
				player.getPacketSender().sendConfig(283, 67108864);
				player.setAttribute("isStaking", Boolean.FALSE);
				break;
			case 19:
			case 22:
				player.getPacketSender().sendConfig(283, 134217728);
				player.setAttribute("isStaking", Boolean.TRUE);
				break;
			case 20:
				player.getPacketSender().sendMessage("Sending duel request...");
				NodeWorker.getPlayers().get((Integer) player.getAttribute("duelWithIndex")).getPacketSender().sendDuelReq(NodeWorker.getPlayers().get((Integer) player.getAttribute("duelWithIndex")), player.getCredentials().getUsername(), "wishes to duel with you " + (player.getAttribute("isStaking") == Boolean.TRUE ? "(stake)" : "(friendly)") + ".");
				player.setAttribute("didRequestDuel", Boolean.TRUE);
				player.getPacketSender().sendCloseInterface();
				break;
			}
			break;
		case 300:
			ForgingInterface.handleIComponents(player, buttonId);
			return true;
		case 206:
			return true;
		case 1028:
			if (buttonId == 117) {
				player.getPacketSender().sendLoginInterfaces();
			}
			break;
		case 105:
			ExchangeHandler.action(player, buttonId);
			return true;
		case 449:
			if (buttonId == 1) {
				player.getPacketSender().sendInventoryInterface(621);
				player.getIOSession().write(new CS2Script(player, 149, "IviiiIsssss", new Object[] {
						"Sell 50", "Sell 10", "Sell 5", "Sell 1", "Value", -1, 1, 7, 4, 93, 40697856
				}));
				player.getIOSession().write(new AccessMask(player, 0, 27, 621, 0, 36, 1086));
				player.getIOSession().write(new ContainerPacket(player, 93, player.getInventory(), false));
			}
			break;
		case 107:
			if (packet.getOpcode() == 85)
				ExchangeHandler.sendSellItem(player, itemId, slot, buttonId);
			else
				player.getPacketSender().sendMessage(ItemDefinition.forId(itemId).getExamine());
			return true;
		case 182:
			if (player.getAttribute("tick:combat", -1) > MajorUpdateWorker.getTicks()) {
				player.getPacketSender().sendMessage("You need to be 10 seconds out of combat before logging out.");
				return true;
			}
			player.getIOSession().write(new LogoutPacket(player, buttonId));
			return true;
		case 261:
			switch (buttonId) {
			case 6: // Mouse Button config
				player.setAttribute("mouseButtons", player.getAttribute("mouseButtons", 0) == 0 ? 1 : 0);
				player.getPacketSender().sendConfig(170, (Integer) player.getAttribute("mouseButtons"));
				break;
			case 5:
				if (player.getIOSession().getDisplayMode() < 2) {
					player.getIOSession().write(
							new InterfacePacket(player, 548, 215, 982, true));
				} else {
					player.getIOSession().write(
							new InterfacePacket(player, 746, 101, 982, true));
				}
				return true;
			case 14:
				player.getPacketSender().sendInterface(742);
				return true;
			case 16:
				player.getPacketSender().sendInterface(743);
				return true;
			}
			return true;
		case 982:
			if (buttonId == 5) {
				int winId = player.getIOSession().getDisplayMode() < 2 ? 548 : 746;
				int slotId = player.getIOSession().getDisplayMode() < 2 ? 215 : 101;
				player.getIOSession().write(new InterfacePacket(player, winId, slotId, 261, true));
			}
			return true;
		}
		if (!EventManager.handleButtonEvent(player, packet.getOpcode(),
				interfaceId, componentId, itemId, slot) && Main.DEBUG) {
			Main.getLogger().info("Unhandled action button - [opcode="
					+ packet.getOpcode() + ", interfaceId=" + interfaceId
					+ ", buttonId=" + componentId + ", itemId=" + itemId
					+ ", slot=" + slot + "].");
		}
		return true;
	}

}