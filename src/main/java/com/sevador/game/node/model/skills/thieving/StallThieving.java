package com.sevador.game.node.model.skills.thieving;

import com.sevador.game.action.impl.combat.HitAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.ForceTextUpdate;
import com.sevador.game.node.model.skills.thieving.HomeStalls.BasicStalls;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.utility.Misc;
import com.sevador.utility.Priority;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class StallThieving extends SkillAction {

	private GameObject stall;

	public StallThieving(GameObject stall) {
		this.stall = stall;
	}

	@Override
	public boolean start(Player player) {
		if (!onCheck(player)) return false;
		player.getUpdateMasks().register(new Animation(832, 0, false, Priority.HIGHEST));
		return true;
	}

	public boolean onCheck(Player player) {
		if (player.getInventory().freeSlots() == 0) {
			player.getPacketSender().sendMessage("You need some free inventory space to do this!");
			return false;
		}
		for (BasicStalls stalls : BasicStalls.values()) {
			if (stalls.getId() == stall.getId()) {
				if (player.getSkills().getLevel(Skills.THIEVING) < stalls.getReq()) {
					player.getPacketSender().sendMessage("You need a thieving level of "+ stalls.getReq()+ " to steal from this stall.");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (!onCheck(player)) return false;
		int id = stall.getId();
		GameObject obj = stall;
		for (final BasicStalls stalls : BasicStalls.values()) {
			if (stalls.getId() == id) {
				if (player.getRandom().nextInt(100) < 95) {
					Item helm = null;
					switch(stalls.ordinal()) {
					case 0:
						helm = new Item(1139);
						break;
					case 1:
						helm = new Item(1137);
						break;
					case 2:
						helm = new Item(1141);
						break;
					case 3:
						helm = new Item(1143);
						break;
					case 4:
						helm = new Item(1145);
						break;
					case 5:
						helm = new Item(1147);
						break;
					}
					player.getInventory().add(helm);
					player.getInventory().add(new Item(995, stalls.getReward() / 3));
					((Skills) player.getSkills()).addExperience(Skills.THIEVING, stalls.getXp());
					player.getPacketSender().sendMessage("You <col=FF0000>successfully</col> steal " + stalls.getReward() / 3 + " coins from the " + obj.getDefinition().name + ".");
					return false;
				} else {
					player.getPacketSender().sendMessage("You didn't successfully steal, and you were damaged in the process.");
					player.getUpdateMasks().register(new ForceTextUpdate("Ouch!", false));
					player.getActionManager().register(new HitAction(player, player, new Damage(10 + Misc.random(40)).setType(DamageType.RED_DAMAGE), 0));
				}
			}
		}
		return false;
	}

	@Override
	public int processWithDelay(Player player) {
		return 5;
	}

	@Override
	public void stop(Player player) {

	}

}
