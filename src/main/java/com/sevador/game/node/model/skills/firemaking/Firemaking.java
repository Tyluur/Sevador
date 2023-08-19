package com.sevador.game.node.model.skills.firemaking;

import net.burtleburtle.tick.Tick;

import com.sevador.game.action.impl.packetactions.MovementAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.gameobject.GameObject;
import com.sevador.game.node.model.gameobject.ObjectBuilder;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.skills.firemaking.FiremakingData.Fire;
import com.sevador.game.node.npc.impl.Familiar;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;
import com.sevador.game.world.World;
import com.sevador.utility.Misc;

/**
 * 
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class Firemaking extends SkillAction {
	private Fire fire;

	public Firemaking(Fire fire) {
		this.fire = fire;
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player))
			return false;
		player.getPacketSender().sendMessage(
				"You attempt to light the logs.", true);
		player.getInventory().remove(fire.getLogId(), 1);
		Long time = (Long) player.getTemporaryAttributtes().remove("Fire");
		boolean quickFire = time != null && time > Misc.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : Misc.random(5) + 4);
		if (!quickFire)
			player.setAnimation(new Animation(733));
		return true;
	}

	public static boolean isFiremaking(Player player, Item item1, Item item2) {
		Item log = (item1.getId() == 590 ? item2 : item1);
		if (log == null) return false;
		if (item1.getId() != 590 && item2.getId() != 590) return false;
		return isFiremaking(player, log.getId());
	}

	public static boolean isFiremaking(Player player, int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				player.getSkillAction().setSkill(new Firemaking(fire));
				return true;
			}
		}
		return false;

	}

	public static void startFamiliarFire(Player player, Familiar familiar,
			Fire fire) {
		if (player.getFamiliar().getId() == 7378
				|| player.getFamiliar().getId() == 7377) {
		}
	}

	public boolean checkAll(Player player) {
		if (!player.getInventory().contains(590, 1)) {
			player.getPacketSender().sendMessage(
					"You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevel(Skills.FIREMAKING) < fire.getLevel()) {
			player.getPacketSender().sendMessage(
					"You do not have the required level to light this.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(final Player player) {
		player.getPacketSender().sendMessage("The fire catches and the logs begin to burn.", true);
		final int x = player.getLocation().getX() - 1;
		final int y = player.getLocation().getY();
		final GameObject fireObject = new GameObject(2732, player.getLocation());
		World.getWorld().submit(new Tick(1) {
			@Override
			public boolean run() {
				player.getActionManager().register(
						new MovementAction(player, x, y, true));
				ObjectBuilder.add(fireObject);
				((Skills) player.getSkills()).addExperience(Skills.FIREMAKING,
						fire.getExperience());
				World.getWorld().submit(new Tick(50) {

					@Override
					public boolean run() {
						ObjectBuilder.replace(fireObject, new GameObject(-1,
								fireObject.getLocation()));
						ObjectBuilder.remove(fireObject);
						GroundItemManager.createGroundItem(new GroundItem(
								player, new Item(592, 1), fireObject
										.getLocation(), false));
						player.getPacketSender().sendMessage("The fire burns out and ashes fall in its place.");
						return true;
					}

				});
				return true;
			}
		});
		player.getTemporaryAttributtes().put("Fire",
				Misc.currentTimeMillis() + 1800);
		return -1;
	}

	@Override
	public void stop(Player player) {

	}
}
