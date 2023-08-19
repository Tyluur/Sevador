package com.sevador.game.action.impl.packetactions;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionManager;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.ConfigPacket;
import com.sevador.utility.Priority;

/**
 * Handles resting.
 * @author Emperor
 *
 */
public class RestAction extends Action {

	/**
	 * The aciton type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * Add to the default reset flag.
	 */
	static {
		DEFAULT_RESET |= FLAG;
	}
	
	/**
	 * The first possible animation.
	 */
	private static final Animation FIRST_ANIMATION = new Animation(5713, 0, false);
	
	/**
	 * The second possible animation.
	 */
	private static final Animation SECOND_ANIMATION = new Animation(11786, 0, false);

	/**
	 * The reset animation.
	 */
	private static final Animation RESET_ANIMATION = new Animation(5748, 0, false, Priority.HIGH);
	
	/**
	 * The second reset animation.
	 */
	private static final Animation RESET_ANIMATION_2 = new Animation(5748, 0, false, Priority.HIGH);
	
	/**
	 * The animation to use.
	 */
	private final Animation animation;
	
	/**
	 * If we're resting at a musician.
	 */
	private boolean musician;
	
	/**
	 * Constructs a new {@code RestAction} {@code Object}.
	 * @param entity The player resting.
	 * @param musician If the player is resting at a musician.
	 */
	public RestAction(Entity entity) {
		super(entity);
		addFlag(DEFAULT_RESET);
		if (entity.getRandom().nextBoolean()) {
			animation = FIRST_ANIMATION;
		} else {
			animation = SECOND_ANIMATION;
		}
	}

	@Override
	public boolean execute() {
		Player p = entity.getPlayer();
		p.getWalkingQueue().reset();
		if (!entity.getAttribute("resting", false)) {
			for (NPC npc : RegionManager.getLocalNPCs(entity.getLocation(), 3)) {
				if (npc.getDefinition().name.contains("usician")) {
					musician = true;
					entity.turnTo(npc);
					break;
				}
			}
			entity.setAttribute("resting", true);
			p.getIOSession().write(new ConfigPacket(p, 1433, 1));
			p.getIOSession().write(new ConfigPacket(p, 1189, 3833973));
		}
		p.getIOSession().write(new CS2Config(p, 119, musician ? 4 : 3));
		entity.getUpdateMasks().register(animation);
		entity.getPlayer().getSettings().increaseRunEnergy(musician ? 2.874 : 1.416);
		return false;
	}

	@Override
	public boolean dispose(Action action) {
		if (action.getActionType() == EmoteAction.FLAG) {
			entity.getPlayer().getPacketSender().sendMessage("You can't do an emote while resting.");
			return false;
		} else if (action.getActionType() == FLAG) {
			entity.getPlayer().getPacketSender().sendMessage("You are already resting.");
			return false;
		}
		if (entity.getAttribute("resting", false)) {
			entity.turnTo(null);
			entity.removeAttribute("resting");
			if (animation == FIRST_ANIMATION) {
				entity.getUpdateMasks().register(RESET_ANIMATION);
			} else {
				entity.getUpdateMasks().register(RESET_ANIMATION_2);
			}
			entity.getPlayer().getIOSession().write(new CS2Config(entity.getPlayer(), 119, 0));
			entity.getPlayer().getSettings().refresh();
		}
		return true;
	}
	
	@Override
	public int getActionType() {
		return FLAG;
	}

}