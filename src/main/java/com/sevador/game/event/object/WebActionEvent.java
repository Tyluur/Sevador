package com.sevador.game.event.object;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ObjectEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.combat.form.RangeWeapon;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.gameobject.ObjectBuilder;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.utility.OptionType;

/**
 * Handles the cutting of a web.
 * @author Emperor
 *
 */
public final class WebActionEvent implements ObjectEvent {

	/**
	 * The animation to use to cut the web.
	 */
	private static final Animation ANIMATION = new Animation(12029, 0, false);
	
	@Override
	public boolean init() {
		return EventManager.register(733, this);
	}

	@Override
	public boolean handle(Player p, GameObject obj, OptionType type) {
		Item weapon = p.getEquipment().get(3);
		if (weapon == null || RangeWeapon.get(weapon.getId()) != null) {
			p.getPacketSender().sendMessage("You're not wielding any weapon to cut the web.");
			return true;
		}
		p.getUpdateMasks().register(ANIMATION);
		if (p.getRandom().nextInt(10) < 5) {
			p.getPacketSender().sendMessage("You fail to cut the web.");
			return true;
		}
		ObjectBuilder.replace(obj, obj.transform(734), 500);
		p.getPacketSender().sendMessage("You cut to through the web.");
		return true;
	}

	@Override
	public void setDestination(Player p, GameObject obj) {
		switch (obj.getRotation()) {
		case 0:
			if (p.getLocation().getX() >= obj.getLocation().getX()) {
				p.setAttribute("m_o_d", obj.getLocation());
			} else {
				p.setAttribute("m_o_d", obj.getLocation().transform(-1, 0, 0));
			}
			break;
		case 2:
			if (p.getLocation().getX() <= obj.getLocation().getX()) {
				p.setAttribute("m_o_d", obj.getLocation());
			} else {
				p.setAttribute("m_o_d", obj.getLocation().transform(1, 0, 0));
			}
			break;
		}
	}

}
