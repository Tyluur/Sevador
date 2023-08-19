package com.sevador.game.event.object;

import net.burtleburtle.cache.format.ObjectDefinition;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class PrayerAlterEvent implements ObjectEvent {

	@Override
	public boolean init() {
		EventManager.register(47120, this);
		EventManager.register(17010, this);
		return EventManager.register(37985, this);
	}

	@Override
	public boolean handle(Player player, GameObject obj, OptionType type) {
		ObjectDefinition def = obj.getDefinition();
		if (def.name.contains("Decayed")) {
			player.getDialogueManager().startDialogue("AncientAltar");
		} else if (def.name.contains("Zaros")) {
			player.getDialogueManager().startDialogue("ZarosAltar");
		} else {
			switch (type) {
			case SECOND:
				player.getDialogueManager().startDialogue("LunarAltar");
				break;
			case FIRST:
				break;
			case FOURTH:
				break;
			case THIRD:
				break;
			default:
				break;
			}
		}
		return true;
	}

	@Override
	public void setDestination(Player player, GameObject obj) {

	}

}
