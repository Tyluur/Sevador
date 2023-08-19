package net.burtleburtle.tick.impl;

import com.sevador.game.node.model.Entity;
import com.sevador.game.world.World;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.PlayerOptionPacket;
import com.sevador.utility.world.WorldArea;

/**
 * Handles the area update tick.
 * @author Emperor
 *
 */
public class AreaUpdateTick implements Runnable {

	/**
	 * The entity to update.
	 */
	private final Entity entity;
	
	/**
	 * Constructs a new {@code AreaUpdateTick} {@code Object}.
	 * @param entity The entity.
	 */
	public AreaUpdateTick(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void run() {
		checkGodwarsZones(entity);
		boolean safe = entity.getAttribute("area:safe", false);
		boolean currentSafe = WorldArea.isSafe(entity);
		if (!safe && currentSafe) {
			entity.setAttribute("area:safe", safe = true);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 3, false));
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 6, true));
			}
		} else if (safe && !currentSafe) {
			entity.setAttribute("area:safe", safe = false);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 3, true));
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 6, true));
			}
		}
		boolean risk = entity.getAttribute("area:risk", false);
		boolean currentRisk = WorldArea.isRisk(entity);
		if (!risk && currentRisk) {
			entity.setAttribute("area:risk", risk = true);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 3, true));
			//	entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 6, false));
			}
		} else if (risk && !currentRisk) {
			entity.setAttribute("area:risk", risk = false);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 3, true));
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 6, true));
			}
		}
		boolean isWilderness = entity.getAttribute("area:wilderness", false);
		boolean currentWilderness = safe || risk;
		if (!isWilderness && currentWilderness) {
			entity.setAttribute("area:wilderness", isWilderness = true);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new PlayerOptionPacket(entity.getPlayer(), "Attack", true, 1));
				entity.getPlayer().getPacketSender().sendOverlay(381);
				//entity.getPlayer().getIOSession().write(new Interface(entity.getPlayer(), 0, 381, 0, currentWilderness));
			}
		} else if (isWilderness && !currentWilderness) {
			entity.setAttribute("area:wilderness", isWilderness = false);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new PlayerOptionPacket(entity.getPlayer(), "null", true, 1));
				entity.getPlayer().getPacketSender().closeOverlay();
			}			
		}
		boolean multi = entity.getAttribute("area:multi", false);
		boolean currentMulti = WorldArea.isMulti(entity);
		if (!multi && currentMulti) {
			entity.setAttribute("area:multi", true);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 1, false));
			}
		} else if (multi && !currentMulti) {
			entity.setAttribute("area:multi", false);
			if (entity.isPlayer()) {
				entity.getPlayer().getIOSession().write(new InterfaceConfig(entity.getPlayer(), 745, 1, true));
			}
		}
	}
	
	/**
	 * Checks the godwars zones the player could be standing in.
	 */
	private static void checkGodwarsZones(Entity entity) {
		boolean bandos = entity.getAttribute("area:bandos", false);
		boolean currentBandos = World.getWorld().getAreaManager().getAreaByName("bandos").contains(entity.getLocation());
		if (!bandos && currentBandos) {
			entity.setAttribute("area:bandos", true);
			entity.setAttribute("area:boss", true);
			return;
		} else if (bandos && !currentBandos) {
			entity.removeAttribute("area:bandos");
			entity.removeAttribute("area:boss");
			return;
		}
		boolean armadyl = entity.getAttribute("area:armadyl", false);
		boolean currentArmadyl = World.getWorld().getAreaManager().getAreaByName("armadyl").contains(entity.getLocation());
		if (!armadyl && currentArmadyl) {
			entity.setAttribute("area:armadyl", true);
			entity.setAttribute("area:boss", true);
			return;
		} else if (armadyl && !currentArmadyl) {
			entity.removeAttribute("area:armadyl");
			entity.removeAttribute("area:boss");
			return;
		}
	}
}