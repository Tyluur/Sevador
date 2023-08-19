package com.sevador.game.event.object;

import net.burtleburtle.cache.format.NPCDefinition;
import net.burtleburtle.thread.NodeWorker;

import com.sevador.Main;
import com.sevador.content.misc.IconManager;
import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.minigames.Barrows;
import com.sevador.game.minigames.Barrows.BarrowsBrother;
import com.sevador.game.node.control.impl.Barrows.Brothers;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.ForceTextUpdate;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.impl.BarrowBrother;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class BarrowsObjects implements ObjectEvent{

	@Override
	public boolean init() {
		for (Brothers bro : Brothers.values()) {
			EventManager.register(bro.getCoffinId(), this);
			EventManager.register(bro.getStairsId(), this);
		}
		return true;
	}

	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		switch(obj.getId()) {
		case 6703: // Barrows
		case 6704:
		case 6702:
		case 6705:
		case 6707:
		case 6706:
			for (Brothers bro : Brothers.values()) {
				if (obj.getId() == bro.getStairsId()) {
					player.getProperties().setTeleportLocation(new Location(bro.getExitLocation().getX(), bro.getExitLocation().getY(), 0));
				}
			}
			return true;
		default:
			if (type != OptionType.FIRST) {
				return false;
			}

			BarrowsBrother coffinBrother = null;
			for (BarrowsBrother brother : BarrowsBrother.values()) {
				if (brother.getCoffinId() == obj.getId()) {
					coffinBrother = brother;
					break;
				}
			}

			if (coffinBrother == null) {
				return true;
			}
			
			if (player.getSettings().getKilledBrothers()[coffinBrother.ordinal()] || player.getAttribute("currentlyFightingBrother") != null) {
				player.getPacketSender().sendMessage("You don't find anything.");
				return true;
			}
			if (player.getAttribute("barrows_tunnel") == coffinBrother) {
				player.getPacketSender().sendMessage("Claim your rewards from the Wise Old Man if you've finished every brother.");
				//TODO: DialogueManager.sendDisplayBox(player, 10, "You've found a hidden tunnel, do you want to enter?");
				return true;
			}
			if (NPCDefinition.forId(coffinBrother.getNpcId()).name.toLowerCase().contains("karil")) {
				NPC npc = new NPC(coffinBrother.getNpcId());
				player.getPlayer().getSettings().getKilledBrothers()[Barrows.getIndexForBrother(npc)] = true;
				player.getPlayer().getSettings().getKillCount()[0]++;
				if (player.getPlayer().getSettings().getKillCount()[0] > 9994) {
					player.getPlayer().getSettings().getKillCount()[0] = 9994;
				}
				player.getPlayer().getPlayerAreaTick().updateBarrowsInterface();
				return true;
			}
			BarrowBrother brother = new BarrowBrother(coffinBrother, player.getLocation().transform(0, 1, 0));
			brother.getUpdateMasks().register(new ForceTextUpdate("You dare disturb my rest!", true));
			brother.getCombatAction().setVictim(player);
			brother.getCombatAction().execute();
			brother.getActionManager().register(brother.getCombatAction());
			NodeWorker.getNPCs().add(brother);
			Main.getNodeWorker().offer(brother);
			IconManager.iconOnMob(player, brother, 1, -1);
			player.setAttribute("currentlyFightingBrother", brother);
			break;
		}
		return true;
	}

	@Override
	public void setDestination(Player player, GameObject obj) {
		// TODO Auto-generated method stub

	}

}
