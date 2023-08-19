package com.sevador.game.node.model.skills.prayer;

import com.sevador.game.event.item.BoneEvent.Bone;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class BoneAltar extends SkillAction {

	private int itemId, value;

	public BoneAltar(int boneId, int value) {
		this.itemId = boneId;
		this.value = value;
	}

	@Override
	public boolean start(final Player player) {
		if (!checkAll(player)) {
			return false;
		}
		player.getPacketSender().sendMessage("The gods bless you and you receive extra experience for your bone.");
		player.bonesToMake = value;
		return true;
	}

	@Override
	public boolean process(final Player player) {
		return true;
	}

	public boolean checkAll(Player player) {
		if (player.getCredentials().getVotePoints() == 0) {
			player.getPacketSender().sendMessage("You need 1 voting point to use this altar, you only have " + player.getCredentials().getVotePoints() + " right now.");
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (itemId != -1) {
			if (value > player.getInventory().getAmount(itemId)) 
				value = player.getInventory().getAmount(itemId);
			if (player.bonesToMake < 1) { player.getSkillAction().forceStop(); return 0; }
			if (!player.getInventory().contains(itemId, 1)) {
				return 0;
			}
			player.getInventory().remove(itemId, 1);
			for (Bone bone : Bone.values()) {
				if (bone.getBoneId() == itemId) {
					player.setAnimation(new Animation(713));
					((Skills) player.getSkills()).addExperience(Skills.PRAYER, bone.getExp() * 2	);
				}
			}
			player.bonesToMake--;
			return 0;
		}
		return 4;
	}

	@Override
	public void stop(Player player) {
	}

}
