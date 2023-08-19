package com.sevador.game.action.impl;

import net.burtleburtle.tick.Tick;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.event.item.TeletabActionEvent;
import com.sevador.game.node.Item;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.mask.Animation;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.model.mask.UpdateFlag;
import com.sevador.game.region.GroundItem;
import com.sevador.game.region.GroundItemManager;
import com.sevador.game.world.World;
import com.sevador.utility.Priority;

/**
 * Handles the teleporting action.
 * 
 * @author Emperor
 * 
 */
public final class TeleportAction extends Action {

	/**
	 * The modern teleport start animation.
	 */
	public static final Animation MODERN_ANIM = new Animation(8939, 0, false,
			Priority.HIGHEST);

	/**
	 * The modern teleport start graphics.
	 */
	public static final Graphic MODERN_GRAPHIC = new Graphic(1576, 0, 0, false);

	/**
	 * The modern teleport end animation.
	 */
	public static final Animation MODERN_END_ANIM = new Animation(8941, 0,
			false, Priority.HIGHEST);

	/**
	 * The modern teleport end graphic.
	 */
	public static final Graphic MODERN_END_GRAPHIC = new Graphic(1577, 0, 0,
			false);

	/**
	 * The ancient teleport start animation.
	 */
	public static final Animation ANCIENT_ANIM = new Animation(9599, 0, false,
			Priority.HIGHEST);

	/**
	 * The ancient teleport start graphic.
	 */
	public static final Graphic ANCIENT_GRAPHIC = new Graphic(1681, 0, 0, false);

	/**
	 * The lunar teleport animation.
	 */
	public static final Animation LUNAR_ANIM = new Animation(9606, 0, false,
			Priority.HIGHEST);

	/**
	 * The lunar teleport graphic.
	 */
	public static final Graphic LUNAR_GRAPHIC = new Graphic(1685, 0, 0, false);

	/**
	 * The teletab start animation.
	 */
	public static final Animation TELETAB_START = new Animation(9597, 16,
			false, Priority.HIGHEST);

	/**
	 * The teletab second animation.
	 */
	public static final Animation TELETAB_SECOND = new Animation(4071, 0,
			false, Priority.HIGHEST);

	/**
	 * The teletab graphics.
	 */
	public static final Graphic TELETAB_GFX = new Graphic(1680, 0, 0, false);

	/**
	 * The teletab end animation.
	 */
	public static final Animation TELETAB_END = new Animation(9598, 0, false,
			Priority.HIGHEST);

	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();

	/**
	 * Add the flag to the default reset flag.
	 */
	static {
		DEFAULT_RESET |= FLAG;
	}

	/**
	 * The destination.
	 */
	private final Location destination;

	/**
	 * The animations.
	 */
	private final Animation start, end;

	/**
	 * The graphics.
	 */
	private final UpdateFlag startGfx, endGfx;

	/**
	 * The amount of ticks.
	 */
	private final int startTick, teleportTick, endTick;

	/**
	 * If we should check for wilderness level.
	 */
	private final boolean wildernessCheck;

	/**
	 * The items to remove.
	 */
	private final Item[] items;

	/**
	 * The current amount of cycles.
	 */
	private int cycles = 0;

	/**
	 * Constructs a new {@code TeleportAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity teleporting.
	 * @param location
	 *            The location to teleport to.
	 * @param start
	 *            The start animation.
	 * @param startGfx
	 *            The start graphics.
	 * @param end
	 *            The end animation.
	 * @param endGfx
	 *            The end graphics.
	 * @param startTick
	 *            The start animation tick.
	 * @param teleportTick
	 *            the teleport tick.
	 * @param endTick
	 *            The end animation tick.
	 * @param items
	 *            The items to remove.
	 */
	public TeleportAction(Entity entity, Location location, Animation start,
			UpdateFlag startGfx, Animation end, UpdateFlag endGfx,
			int startTick, int teleportTick, int endTick, Item... items) {
		this(entity, location, start, startGfx, end, endGfx, startTick,
				teleportTick, endTick, true, items);
	}

	/**
	 * Constructs a new {@code TeleportAction} {@code Object}.
	 * 
	 * @param entity
	 *            The entity teleporting.
	 * @param location
	 *            The location to teleport to.
	 * @param start
	 *            The start animation.
	 * @param startGfx
	 *            The start graphics.
	 * @param end
	 *            The end animation.
	 * @param endGfx
	 *            The end graphics.
	 * @param startTick
	 *            The start animation tick.
	 * @param teleportTick
	 *            the teleport tick.
	 * @param endTick
	 *            The end animation tick.
	 * @param wildernessCheck
	 *            If we should check for wilderness level.
	 * @param items
	 *            The items to remove.
	 */
	public TeleportAction(Entity entity, Location location, Animation start,
			UpdateFlag startGfx, Animation end, UpdateFlag endGfx,
			int startTick, int teleportTick, int endTick,
			boolean wildernessCheck, Item... items) {
		super(entity);
		this.destination = location;
		this.start = start;
		this.startGfx = startGfx;
		this.end = end;
		this.endGfx = endGfx;
		this.startTick = startTick;
		this.teleportTick = teleportTick;
		this.endTick = endTick;
		this.wildernessCheck = wildernessCheck;
		this.items = items;
	}

	@Override
	public boolean execute() {
		if (!canTeleport(entity, wildernessCheck)) {
			return true;
		}
		if (cycles++ == startTick) {
			if (entity.isPlayer()) {
				entity.getPlayer().getPacketSender().sendCloseInterface().sendCloseInventoryInterface().sendChatBoxInterface(137);
				if (items.length > 0) {
					if (TeletabActionEvent.TELETABS.get((int) items[0].getId()) != null) {
						World.getWorld().submit(new Tick(1) {
							@Override
							public boolean run() {
								entity.getUpdateMasks()
										.register(TELETAB_SECOND);
								return true;
							}
						});
					} else if (items[0].getId() == 17489) {
						GroundItem item = entity.getAttribute("gatestone");
						if (item != null) {
							entity.removeAttribute("gatestone");
							entity.getPlayer()
									.getPacketSender()
									.sendMessage(
											"Your gatestone disappears as you use its magic.");
							GroundItemManager.removeGroundItem(item);
						} else {
							return true;
						}
					}
				}
				entity.getPlayer().getInventory().remove(items);
			}
			entity.visual(start, startGfx);
		}
		if (cycles == teleportTick) {
			entity.getProperties().setTeleportLocation(destination);
		}
		if (cycles == endTick) {
			entity.visual(end, endGfx);
			return true;
		}
		return false;
	}

	/**
	 * Checks if an entity can teleport.
	 * 
	 * @param e
	 *            The entity.
	 * @param wildernessCheck
	 *            If we should check the wilderness level.
	 * @return {@code True} if so.
	 */
	public static boolean canTeleport(Entity e, boolean wildernessCheck) {
		if (wildernessCheck) {
			if (e.getAttribute("area:risk", false)
					&& getWildernessLevel(e.getLocation()) > 20) {
				if (e.isPlayer()) {
					e.getPlayer()
							.getPacketSender()
							.sendMessage(
									"You're too deep in the wilderness to teleport.");
				}
				return false;
			}
		}
		// TODO: Teleblock.
		return true;
	}

	public static int getWildernessLevel(Location l) {
		if (l.getY() > 3520 && l.getY() < 4000) {
			return (((int) (Math.ceil((l.getY()) - 3520D) / 8D) + 1));
		}
		return 0;
	}

	@Override
	public boolean dispose(Action a) {
		return false;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}