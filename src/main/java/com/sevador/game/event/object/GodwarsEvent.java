package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.Constants;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class GodwarsEvent implements ObjectEvent {

	private static final int[] GWD_OBJS = new int[]{26341, 26342, 26293, 26384, 26425, 26439};


	private static final Location GOD_DUNGEON = Location.locate(2881, 5308, 2);

	private static final Location GOD_DUNGEON_ENTRANCE = Location.locate(2917, 3745, 0);

	@Override
	public boolean init() {
		for (int i : GWD_OBJS)
			EventManager.register(i, this);
		return true;
	}

	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		switch(obj.getId()) {
		case 26342:
			if (player.getInventory().contains(954, 1)) {
				player.getInventory().remove(954, 1);
				player.teleport(GOD_DUNGEON);
			} else 
				player.getPacketSender().sendMessage("You need a rope in order to enter god wars.");
			break;
		case 26293:
			player.teleport(GOD_DUNGEON_ENTRANCE);
			return true;
		case 26425:
			if (player.getKillCount()[Constants.BANDOS_KILLS] > Constants.GODWAR_KILL_REQUIREMENT) {
				player.getKillCount()[Constants.BANDOS_KILLS] -= Constants.GODWAR_KILL_REQUIREMENT;
				player.getPacketSender().sendMessage("The door devours the life-force of " + Constants.GODWAR_KILL_REQUIREMENT + " followers of Bandos you have slain.");
			}
            player.getProperties().setTeleportLocation(Location.locate(player.getLocation().getX() == 2864 ? 2863 : 2864, 5354, 2));
			break;
        case 26384: //put what door this is so i can know ILL DO ITTT, ur doing nex :p ygh ok
        	if (player.getKillCount()[Constants.BANDOS_KILLS] < Constants.GODWAR_KILL_REQUIREMENT) {
        		player.getPacketSender().sendMessage("You need " + Constants.GODWAR_KILL_REQUIREMENT + " bandos godwars kills to enter this room.");
        		return true;
        	}
            player.getProperties().setTeleportLocation(Location.locate(player.getLocation().getX() == 2851 ? 2850 : 2851, 5333, 2));
            break;
		default:
			System.err.println("case " + obj.getId()+ ": // UNHANDLED GODWARS OBJECT.");
		}
		return true;
	}

	@Override
	public void setDestination(Player player, GameObject obj) {

	}

}
