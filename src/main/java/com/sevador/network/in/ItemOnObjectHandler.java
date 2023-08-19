package com.sevador.network.in;

import net.burtleburtle.cache.format.ObjectDefinition;

import com.sevador.game.event.item.BoneEvent.Bone;
import com.sevador.game.misc.InputHandler;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.skills.cooking.Cooking;
import com.sevador.game.node.model.skills.cooking.CookingData.Cookables;
import com.sevador.game.node.model.skills.smithing.Smithing.ForgingInterface;
import com.sevador.game.node.model.skills.smithing.SmithingData.ForgingBar;
import com.sevador.game.node.player.Player;
import com.sevador.network.IncomingPacket;
import com.sevador.network.PacketSkeleton;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Identified applicable streams.</code>
 */
public class ItemOnObjectHandler implements PacketSkeleton {

	@SuppressWarnings("unused")
	@Override
	public boolean execute(Player player, IncomingPacket packet) {
		final int objY = packet.readLEShortA();
		final int objX = packet.readShort();
		int itemSlot = packet.readByte();
		packet.readByte();
		int fromInterface = packet.readLEShort();
		packet.readShort();
		final int itemUsed = packet.readShort();
		packet.readByte();
		int objectId = packet.readLEShortA();
		final Location location = new Location(objX, objY, player.getLocation().getZ());
//		player.getSkillAction().forceStop();
//		player.getDialogueManager().finishDialogue();
		Item item = new Item(itemUsed);
		GameObject object = location.getGameObject(objectId);
		ObjectDefinition objectDef = ObjectDefinition.forId(objectId);
//		player.getActionManager().register(new MovementAction(player, objX, objY, player.getSettings().isRunToggled()));
		if (objectDef.name.toLowerCase().contains("range")
				|| objectDef.name.toLowerCase().contains("stove")
				|| objectId == 2732) {
			Cookables cook = Cooking.isCookingSkill(item);
			if (cook != null) {
				player.getDialogueManager().startDialogue("CookingD", cook,
						object);
			}
		}
		if (fromInterface == 679) {
			if (objectDef.name.equalsIgnoreCase("Anvil")) {
				player.getTemporaryAttributtes().put("itemUsed", item.getId());
				ForgingBar bar = ForgingBar.forId(itemUsed);
				if (bar != null) ForgingInterface.sendSmithingInterface(player, bar); else player.getPacketSender().sendMessage("Invalid bar.");
			}
		}
		for (Bone bone : Bone.values()) {
			if (item.getId() == bone.getBoneId() && objectId == 37985) {
				InputHandler.requestInput(player, 5, "How many bones would you like to use?");
				player.setAttribute("id:item", item.getId());
				player.setAttribute("id:object", object);
			}
		}
		return true;
	}

}
