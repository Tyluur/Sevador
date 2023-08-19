package com.sevador.game.node.npc.impl;

import net.burtleburtle.cache.format.NPCDefinition;

import com.sevador.game.node.NodeType;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.mask.Graphic;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.npc.impl.sum.BurdenBeast;
import com.sevador.game.node.npc.impl.sum.CombatFamiliar;
import com.sevador.game.node.npc.impl.sum.NullFamiliar;
import com.sevador.game.node.player.Player;
import com.sevador.game.node.player.Skills;
import com.sevador.network.out.AccessMask;
import com.sevador.network.out.CS2Config;
import com.sevador.network.out.CS2Script;
import com.sevador.network.out.CS2String;
import com.sevador.network.out.CloseInterface;
import com.sevador.network.out.ConfigPacket;
import com.sevador.network.out.InterfaceConfig;
import com.sevador.network.out.InterfacePacket;

/**
 * Represents a summoning familiar.
 * 
 * @author Emperor
 * 
 */
public class Familiar extends NPC {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = 514237489042688931L;

	/**
	 * The small familiar's summoned gfx (blue flames).
	 */
	protected static final Graphic SMALL_SUMMON_GRAPHIC = new Graphic(1314, 0,
			0, true);

	/**
	 * The spawn graphics for a large familiar (blue flames).
	 */
	protected static final Graphic LARGE_SUMMON_GRAPHIC = new Graphic(1315, 0,
			0, true);

	/**
	 * The owner of this familiar.
	 */
	protected Player owner;

	/**
	 * The amount of ticks left.
	 */
	protected int ticks;

	/**
	 * The initial amount of ticks.
	 */
	private int maximumTicks;

	/**
	 * The amount of special points left.
	 */
	private int specialPoints = 60;

	/**
	 * Constructs a new {@code Familiar} {@code Object}.
	 * 
	 * @param id
	 *            The npc id.
	 * @param ticks
	 *            The amount of ticks.
	 * @param specialPoints
	 *            The amount of special points.
	 */
	public Familiar(int id, int ticks) {
		super(id, NodeType.HUMAN, false);
		this.maximumTicks = ticks;
		this.ticks = ticks;
	}

	@Override
	public void tick() {
		if (owner == null || getAttribute("state:dead", false)) {
			return;
		}
		if (ticks % 50 == 0) {
			updateSpecialPoints(-15);
			owner.getSkills().updateLevel(Skills.SUMMONING, -1, 0);
		}
		ticks--;
		owner.getIOSession().write(new InterfaceConfig(owner, 747, 8, false));
		int minutes = Math.round(ticks / 100);
		int hash = minutes << 7 | ((ticks - (minutes * 100)) > 49 ? 1 : 0) << 6;
		if (getAttribute("timeHash", -1) != hash) {
			setAttribute("timeHash", hash);
			owner.getIOSession().write(new ConfigPacket(owner, 1176, hash));
		}
		switch (ticks) {
		case 100:
			owner.getPacketSender().sendMessage(
					"<col=ff0000>Your familiar has 1 minute left.");
			break;
		case 50:
			owner.getPacketSender().sendMessage(
					"<col=ff0000>Your familiar has 30 seconds left.");
			break;
		case 0:
			owner.getPacketSender().sendMessage(
					"<col=ff0000>Your familiar has run out of time.");
			dismiss();
			return;
		}
		if (!getAttribute("switched", false)
				&& getAttribute("area:wilderness", false)) {
			setAttribute("switched", true);
			if (NPCDefinition.forId(getId() + 1).getName()
					.equals(getDefinition().getName())) {
				// TODO: getMask().setSwitchId(getId() + 1);
			}
		} else if (getAttribute("switched", false)
				&& !getAttribute("area:wilderness", false)) {
			setAttribute("switched", false);
			// TODO: getUpdateMasks().register(new Switch).setSwitchId(getId() -
			// 1);
		}
		if (getCombatAction().getVictim() == null) {
			if (owner.getCombatAction().getVictim() != null
					&& getAttribute("area:multi", false)
					&& owner.getAttribute("area:multi", false)) {
				getCombatAction()
				.setVictim(owner.getCombatAction().getVictim());
			} else if (getLocation().getDistance(owner.getLocation()) > 12) {
				call();
			} else {
				turnTo(owner);
				getCombatAction().getMovement().go(owner, CombatType.MELEE);
			}
		}
	}

	public void unlockOrb() {
		owner.getIOSession().write(new InterfaceConfig(owner, 747, 8, false));
		sendLeftClickOption();
	}
	
	public void selectLeftOption() {
		boolean res = owner.getIOSession().getScreenSizeMode() > 1;
		owner.getIOSession()
				.write(new InterfacePacket(owner, res ? 746 : 548, res ? 98 : 218,
						880, true));
		sendLeftClickOption();
		owner.getIOSession().write(new ConfigPacket(owner, 168, 8));
	}

	public void sendLeftClickOption() {
		owner.getIOSession().write(
				new ConfigPacket(owner, 1493, owner.getSummoningLeftClickOption()));
		owner.getIOSession().write(
				new ConfigPacket(owner, 1494, owner.getSummoningLeftClickOption()));
	}

	public void setLeftclickOption(int summoningLeftClickOption) {
		if (summoningLeftClickOption == owner.getSummoningLeftClickOption())
			return;
		owner.setSummoningLeftClickOption(summoningLeftClickOption);
		sendLeftClickOption();
	}

	/**
	 * Requests this familiar to attack a victim.
	 * 
	 * @param victim
	 *            The mob to be attacked.
	 */
	public void requestCombat(Entity victim) {
	/*	if (!getAttribute("area:multi", false)
				|| !owner.getAttribute("area:multi", false)) {
			owner.getPacketSender()
			.sendMessage(
					"Your familiar can't assist you in a non-multi combat zone.");
			return;
		} else if (victim.isPlayer()) {
			if (!owner.getAttribute("area:wilderness", false)
					|| !victim.getAttribute("area:wilderness", false)) {
				owner.getPacketSender()
				.sendMessage(
						"Your familiar can only attack players in a player-vs-player area!");
				return;
			}
		}
		if (owner.getCombatAction().getLastAttacker() == null
				&& owner.getCombatAction().getVictim() == null) {
			owner.getPacketSender()
			.sendMessage(
					"Your familiar cannot fight unless you have attacked or been attacked recently.");
			return;
		}*/
		getCombatAction().setVictim(victim);
		getCombatAction().execute();
	}

	/**
	 * Executes the special move.
	 */
	public void specialMove(Entity victim) {
		/*
		 * empty.
		 */
	}

	/**
	 * Calls the familiar to its owner.
	 */
	public void call() {
		getProperties().setTeleportLocation(owner.getLocation());
		getCombatAction().reset();
		turnTo(owner);
		this.getUpdateMasks().register(LARGE_SUMMON_GRAPHIC);
	}
	
	public void sendFollowerDetails() {
		boolean res = owner.getIOSession().getScreenSizeMode() > 1;
		owner.getIOSession().write(new AccessMask(owner, 0, 29, 271, 42, 0, 2));
		owner.getIOSession().write(new InterfacePacket(owner, res ? 746 : 548, res ? 104 : 218, 662, true));
		owner.getIOSession().write(new InterfaceConfig(owner, 662, 44, true));
		owner.getIOSession().write(new InterfaceConfig(owner, 662, 45, true));
		owner.getIOSession().write(new InterfaceConfig(owner, 662, 46, true));
		owner.getIOSession().write(new InterfaceConfig(owner, 662, 47, true));
		owner.getIOSession().write(new InterfaceConfig(owner, 662, 48, true));
		owner.getIOSession().write(new InterfaceConfig(owner, 662, 71, false));
		owner.getIOSession().write(new InterfaceConfig(owner, 662, 72, false));
		owner.getIOSession().write(new ConfigPacket(owner, 168, 218));
	}

	/**
	 * Initializes the owner's interfaces etc.
	 */
	public NPC init() {
		super.init();
		owner.getIOSession().write(new ConfigPacket(owner, 448, getPouchId()));
		owner.getIOSession().write(new ConfigPacket(owner, 1174, getId()));
		owner.getIOSession().write(new ConfigPacket(owner, 1175, getSpecialCost() << 23));
		owner.getIOSession().write(new ConfigPacket(owner, 1175, getSpecialCost() << 23));
		owner.getIOSession().write(new ConfigPacket(owner, 1171, 262144)); // ?
		owner.getIOSession().write(new ConfigPacket(owner, 1171, 262144)); //?
		owner.getIOSession().write(new ConfigPacket(owner, 1176, 768)); // ?
		owner.getIOSession().write(new ConfigPacket(owner, 2044, 0)); // ?
		owner.getIOSession().write(new ConfigPacket(owner, 1801, 5990631)); // ?
		owner.getIOSession().write(new ConfigPacket(owner, 1878, 0)); // ?
		owner.getIOSession().write(new ConfigPacket(owner, 1231, 595968)); // ?
		owner.getIOSession().write(new ConfigPacket(owner, 1160, 8388608)); // ?
		owner.getIOSession().write(new ConfigPacket(owner, 1175, getSpecialCost() << 23));
		owner.getIOSession().write(new ConfigPacket(owner, 1175, getSpecialCost() << 23));
		owner.getIOSession().write(new ConfigPacket(owner, 1160, 243269632)); // ?
		boolean res = owner.getIOSession().getScreenSizeMode() > 1;
		owner.getIOSession().write(new InterfacePacket(owner, res ? 746 : 548, res ? 104 : 219, 662,true));
		owner.getIOSession().write(new AccessMask(owner, 0, 0, 747, 17, 0, 20480));
		owner.getIOSession().write(new AccessMask(owner, 0, 0, 662, 74, 0, 20480));
		owner.getIOSession().write(new CS2Config(owner, 1436, 0));
		owner.getIOSession().write(new CS2Config(owner, 1000, 66));
		owner.getIOSession().write(new ConfigPacket(owner, 1160, 8388608)); //?
		owner.getIOSession().write(new ConfigPacket(owner, 1175, getSpecialCost()
				<< 23));
		owner.getIOSession().write(new ConfigPacket(owner, 1175, getSpecialCost()
				<< 23));
		owner.getIOSession().write(new ConfigPacket(owner, 1160, 243269632)); // ?
		owner.getIOSession().write(new InterfacePacket(owner, res ? 746 : 548, res
				? 104 : 219, 662, true));
		owner.getIOSession().write(new AccessMask(owner, 0, 1, 747, 17, 0,
				20480));
		owner.getIOSession().write(new AccessMask(owner, 0, 1, 662, 74, 0,
				20480));
		owner.getIOSession().write(new CS2Config(owner, 1436, 0));
		owner.getIOSession().write(new CS2String(owner, 204, getSpecialName()));
		owner.getIOSession().write(new CS2String(owner, 205, getSpecialDescription()));
		return this;
	}

	/**
	 * Dismisses the familiar.
	 */
	public void dismiss() {
		instantDeath();
		if (owner == null || owner.getIOSession() == null) {
			return;
		}
		boolean res = owner.getIOSession().getScreenSizeMode() > 1;
		owner.getIOSession().write(
				new CloseInterface(owner, res ? 746 : 548, res ? 104 : 219));
		owner.setFamiliar(new NullFamiliar());
		owner.getFamiliar().setOwner(owner);
		owner.getIOSession().write(new ConfigPacket(owner, 448, -1));
		owner.getIOSession().write(new ConfigPacket(owner, 1176, 64));
		owner.getIOSession().write(new ConfigPacket(owner, 1176, 0));
		owner.getIOSession().write(new ConfigPacket(owner, 1175, 182986));
		owner.getIOSession().write(
				new AccessMask(owner, 0, 0, 747, 17, 0, 20480));
		owner.getIOSession().write(new CS2Config(owner, 1000, 66));
		owner.getIOSession().write(new ConfigPacket(owner, 448, -1));
		owner.getIOSession().write(new ConfigPacket(owner, 1174, -1));
		owner.getIOSession().write(new CS2Script(owner, 2471, ""));
		owner.getIOSession().write(new CS2Script(owner, 655, ""));
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	/**
	 * @return the owner
	 */
	public final Player getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public final void setOwner(Player owner) {
		this.owner = owner;
	}

	/**
	 * If the familiar is a combat familiar.
	 * 
	 * @return {@code true} if so.
	 */
	public boolean isCombatFamiliar() {
		return false;
	}

	/**
	 * Gets the {@code CombatFamiliar} {@code Instance} of this familiar.
	 * 
	 * @return The combat familliar instance.
	 */
	public CombatFamiliar getCombatFamiliar() {
		return null;
	}

	/**
	 * If the familiar is a beast of burden.
	 * 
	 * @return {@code true} if so.
	 */
	public boolean isBurdenBeast() {
		return false;
	}

	/**
	 * Gets the {@code BurdenBeast} {@code Instance} of this familiar.
	 * 
	 * @return The burden beast instance.
	 */
	public BurdenBeast getBurdenBeast() {
		return null;
	}

	/**
	 * Gets the pouch id.
	 * 
	 * @return The pouch item id.
	 */
	public int getPouchId() {
		return 12047;
	}

	/**
	 * Gets the amount of special move points left.
	 * 
	 * @return The amount of special move points.
	 */
	public int getSpecialPoints() {
		return specialPoints;
	}

	/**
	 * Updates the special move points.
	 * 
	 * @param diff
	 *            The difference to decrease with.
	 */
	public void updateSpecialPoints(int diff) {
		specialPoints -= diff;
		if (specialPoints > 60) {
			specialPoints = 60;
		}
		owner.getIOSession().write(new ConfigPacket(owner, 1177, specialPoints));
	}

	/**
	 * Sets the amount of special move points.
	 * 
	 * @param amount
	 *            The amount of special move points left.
	 */
	public void setSpecialPoints(int amount) {
		this.specialPoints = amount;
	}

	/**
	 * Gets the amount of special move cost.
	 * 
	 * @return The amount.
	 */
	public int getSpecialCost() {
		return 3;
	}

	/**
	 * Gets the name of the special move.
	 * 
	 * @return The name.
	 */
	public String getSpecialName() {
		return "Howl";
	}

	/**
	 * Gets a description of the special attack.
	 * 
	 * @return The special attack description.
	 */
	public String getSpecialDescription() {
		return "Causes NPC foes to flee";
	}

	/**
	 * @return the ticks
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * @param ticks
	 *            the ticks to set
	 */
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	/**
	 * @return the maximumTicks
	 */
	public int getMaximumTicks() {
		return maximumTicks;
	}

	/**
	 * @param maximumTicks
	 *            the maximumTicks to set
	 */
	public void setMaximumTicks(int maximumTicks) {
		this.maximumTicks = maximumTicks;
	}

	public void confirmLeftOption() {
		owner.getIOSession().write(new ConfigPacket(owner, 168, 4));
		boolean res = owner.getIOSession().getScreenSizeMode() > 1;
		owner.getIOSession().write(
				new CloseInterface(owner, res ? 746 : 548, 218));
	}

}