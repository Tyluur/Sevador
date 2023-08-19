package com.sevador.game.node.model.skills.thieving;

import net.burtleburtle.cache.format.ItemDefinition;
import net.burtleburtle.thread.MajorUpdateWorker;

import com.sevador.game.action.impl.combat.HitAction;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.Damage.DamageType;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Equipment;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.node.player.action.SkillAction;
import com.sevador.utility.Misc;
import com.sevador.utility.Priority;

/**
 * Handels the pick pocketing.
 * 
 * @author Raghav/Own4g3
 * 
 */
public class PickPocketAction extends SkillAction {

	/**
	 * Pick pocketing npc.
	 */
	private NPC npc;

	/**
	 * Data of an npc.
	 */
	private PickPocketableNPC npcData;

	/**
	 * The pick pocketing animation.
	 */
	private static final Animation PICKPOCKETING_ANIMATION = new Animation(881),


			/**
			 * The double loot animation.
			 */
			DOUBLE_LOOT_ANIMATION = new Animation(5074),

			/**
			 * The triple loot animation.
			 */
			TRIPLE_LOOT_ANIMATION = new Animation(5075),

			/**
			 * The quadruple loot animation.
			 */
			QUADRUPLE_LOOT_ANIMATION = new Animation(5078);

	/**
	 * The index to use in the levels required arrays.
	 */ 
	private int index = 0;

	/**
	 * Constructs a new {@code PickpocketAction} {@code Object}.
	 * 
	 * @param npc
	 *            The npc to whom the player is pickpocketing.
	 * @param npcData
	 *            Data of an npc.
	 */
	public PickPocketAction(NPC npc, PickPocketableNPC npcData) {
		this.npc = npc;
		this.npcData = npcData;
	}

	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			try {
				int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
				int agilityLevel = player.getSkills().getLevel(Skills.AGILITY);
				if (Misc.random(50) < 5) {
					for (int i = 0; i < 4; i++) {
						if (npcData.getThievingLevels()[i] <= thievingLevel
								&& npcData.getAgilityLevels()[i] <= agilityLevel)
							index = i;
					}
				}
				player.turnTo(npc);
				player.setAnimation(getAnimation());
				player.getPacketSender().sendMessage("You attempt to pick the " + npc.getDefinition().name.toLowerCase()	+ "'s pocket...");
				setActionDelay(player, 3);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		npc.turnTo(player);
		if (!isSuccesfull(player)) {
			npc.getUpdateMasks().register(new Animation(422, 0, true, Priority.HIGHEST));
			player.visual(new Animation(424, 0, false), new Graphic(80, 0, 0, false));
			player.getPacketSender().sendMessage("You've been stuned.");
			player.getActionManager().register(new HitAction(player, player, new Damage(npcData.getStunDamage()).setType(DamageType.DEFLECT), 1));
			npc.getWalkingQueue().reset();
			player.setAttribute("freezeImmunity", MajorUpdateWorker.getTicks() + 7); 
			if (npcData.equals(PickPocketableNPC.MASTER_FARMER)
					|| npcData.equals(PickPocketableNPC.FARMER))
				npc.setNextForceTalk("Cor blimey mate, what are ye doing in me pockets?");
			else
				npc.setNextForceTalk("What do you think you're doing?");
			/*npc.setAnimation(STUN_ANIMATION);
			stop(player);*/
		} else {
			player.getPacketSender().sendMessage("" + getMessage(player));
			((Skills) player.getSkills()).addExperience(Skills.THIEVING, npcData.getExperience());
			for (int i = 0; i <= index; i++) {
				Item item = npcData.getLoot()[Misc.random(npcData.getLoot().length - 1)];
				player.getInventory().addItem(item.getId(), item.getAmount());
			}
		}
		return -1;
	}

	@Override
	public void stop(Player player) {
	}

	/**
	 * Checks if the player is succesfull to thiev or not.
	 * 
	 * @param player
	 *            The player.
	 * @return {@code True} if succesfull, {@code false} if not.
	 */
	private boolean isSuccesfull(Player player) {
		int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
		int increasedChance = getIncreasedChance(player);
		int level = Misc.random(thievingLevel + increasedChance) + 1;
		double ratio = level
				/ (Misc.random(npcData.getThievingLevels()[0] + 5) + 1);
		if (Math.round(ratio * thievingLevel) < npcData.getThievingLevels()[0]
				/ player.getAuraManager().getThievingAccurayMultiplier())
			return false;
		return true;
	}

	/**
	 * Gets the increased chance for succesfully pickpocketing.
	 * 
	 * @param player
	 *            The player.
	 * @return The amount of increased chance.
	 */
	private int getIncreasedChance(Player player) {
		int chance = 0;
		if (Equipment.getItemSlot(Equipment.SLOT_HANDS) == 10075)
			chance += 12;
		player.getEquipment();
		if (Equipment.getItemSlot(Equipment.SLOT_CAPE) == 15349)
			chance += 15;
		if (npc.getDefinition().name.contains("H.A.M")) {
			for (Item item : player.getEquipment().toArray()) {
				if (item == null) continue;
				if (item.getDefinition() == null) item.setDefinition(ItemDefinition.forId(item.getId()));
				if (item.getDefinition().getName().contains("H.A.M")) {
					chance += 3;
				}
			}
		}
		return chance;
	}

	/**
	 * Gets the message to send when finishing.
	 * 
	 * @param player
	 *            The player.
	 * @return The message.
	 */
	private String getMessage(Player player) {
		switch (index) {
		case 0:
			return "You succesfully pick the "
			+ npc.getDefinition().name.toLowerCase() + "'s pocket.";
		case 1:
			return "Your lighting-fast reactions allow you to steal double loot.";
		case 2:
			return "Your lighting-fast reactions allow you to steal triple loot.";
		case 3:
			return "Your lighting-fast reactions allow you to steal quadruple loot.";
		}
		return null;
	}

	/**
	 * Checks everything before starting.
	 * 
	 * @param player
	 *            The player.
	 * @return
	 */
	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(Skills.THIEVING) < npcData
				.getThievingLevels()[0]) {
			player.getDialogueManager().startDialogue(
					"SimpleMessage",
					"You need a thieving level of "
							+ npcData.getThievingLevels()[0]
									+ "to steal from this npc.");
			return false;
		}
		if (player.getInventory().freeSlots() < 1) {
			player.getPacketSender().sendMessage(
					"You don't have enough space in your inventory.");
			return false;
		}
		if (player.getAttackedBy() != null || npc.getAttribute("tick:combat", -1) > MajorUpdateWorker.getTicks()) {
			player.getPacketSender().sendMessage("You can't do this while you're under combat.");
			return false;
		}
		if (npc.getAttackedBy() != null || npc.getAttribute("tick:combat", -1) > MajorUpdateWorker.getTicks()) {
			player.getPacketSender().sendMessage("The npc is under combat.");
			return false;
		}
		if (npc.isDead()) {
			player.getPacketSender().sendMessage("Too late, the npc is dead.");
			return false;
		}
		return true;

	}

	/**
	 * Gets the animation to perform.
	 * 
	 * @param player
	 *            The player.
	 * @return The animation.
	 */
	private Animation getAnimation() {
		switch (index) {
		case 0:
			return PICKPOCKETING_ANIMATION;
		case 1:
			return DOUBLE_LOOT_ANIMATION;
		case 2:
			return TRIPLE_LOOT_ANIMATION;
		case 3:
			return QUADRUPLE_LOOT_ANIMATION;
		}
		return null;
	}

}
