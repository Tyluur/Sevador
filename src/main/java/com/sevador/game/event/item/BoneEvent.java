package com.sevador.game.event.item;

import java.util.HashMap;
import java.util.Map;

import net.burtleburtle.tick.Tick;

import com.sevador.game.event.EventManager;
import com.sevador.game.event.ItemActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.game.world.World;
import com.sevador.utility.OptionType;

/**
 * Handles the teletab item action events.
 * 
 * @author Emperor
 * 
 */
public final class BoneEvent implements ItemActionEvent {
	
		public static enum Bone {
			NORMAL(526, 5), NORMAL2(2530, 5), BURNT(528, 5), BAT(530, 5), WOLF(
					2859, 5), MONKEY1(3179, 5), MONKEY2(3180, 5), MONKEY3(3181, 5), MONKEY4(
					3182, 5), MONKEY5(3183, 5), MONKEY7(3185, 5), MONKEY8(3186, 5), MONKEY9(
					3187, 5), JOGRE(3125, 15), ZOGRE(4812, 23), BURNTJOGRE(3127, 25), SHAIKAHAN(
					3123, 25), BIG(532, 15), BABYDRAGON(534, 30), WYVERN(6812, 50), DRAGON(
					536, 72), FAYGR(4830, 84), RAURG(4832, 96), OURG(4834, 140), DAGANNOTH(
					6729, 125), FROSTDRAGON(18830, 180), FROSTDRAGON1(18832, 180), ANCIENT(15410, 200);
	
			private int boneId, xp;
	
			Bone(int boneId, int xp) {
				this.boneId = boneId;
				this.setXp(xp);
			}
	
			public int getBoneId() {
				return boneId;
			}
	
			public int getExp() {
				return getXp();
			}
	
			private static Map<Integer, Bone> boneMap = new HashMap<Integer, Bone>();
	
			public static Bone forId(int boneId) {
				return boneMap.get(boneId);
			}
	
			/**
			 * @return the xp
			 */
			public int getXp() {
				return xp;
			}
	
			/**
			 * @param xp the xp to set
			 */
			public void setXp(int xp) {
				this.xp = xp;
			}
	
			static {
				for (Bone bones : Bone.values())
					boneMap.put(bones.boneId, bones);
			}
		}

	@Override
	public boolean init() {
		for (Bone t : Bone.values()) {
			EventManager.register(t.getBoneId(), this);
		}
		return true;
	}

	@Override
	public boolean handle(final Player player, final Item item, int interfaceId, int slot, OptionType type) {
		final Bone bone = Bone.forId(item.getId());
		if (player.getAttribute("canBury") != null && !player.getAttribute("canBury").equals(true)) return true;
		if (bone != null && interfaceId == 679) {
			player.setAttribute("canBury", false);
			player.getPacketSender().sendMessage("You dig a hole in the ground...");
			player.getUpdateMasks().register(new Animation(827, 0, false));
			player.getWalkingQueue().reset();
			World.getWorld().submit(new Tick(2) {
				@Override
				public boolean run() {
					((Skills) player.getSkills()).addExperience(Skills.PRAYER, bone.getXp());
					player.getInventory().remove(new Item(bone.getBoneId()));
					player.getPacketSender().sendMessage("You bury the bones.");
					player.setAttribute("canBury", true);
					return true;
				}

			});
			return true;
		}
		return true;
	}

}