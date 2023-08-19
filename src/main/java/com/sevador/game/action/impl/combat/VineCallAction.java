package com.sevador.game.action.impl.combat;

import com.sevador.game.action.Action;
import com.sevador.game.action.ActionFlag;
import com.sevador.game.node.model.Entity;
import com.sevador.game.node.model.Location;
import com.sevador.game.node.model.LocationGraphic;
import com.sevador.game.node.model.combat.CombatType;
import com.sevador.game.node.model.combat.Damage;
import com.sevador.game.node.model.combat.TypeHandler;
import com.sevador.game.node.model.combat.form.GaussianGen;
import com.sevador.game.node.npc.NPC;
import com.sevador.game.node.player.PacketSender;
import com.sevador.game.node.player.Player;
import com.sevador.game.region.RegionManager;

/**
 * Handles the Vine call special attack's action.
 * @author Emperor
 *
 */
public class VineCallAction extends Action {

	/**
	 * The action type flag.
	 */
	public static final int FLAG = ActionFlag.nextFlag();
	
	/**
	 * The location graphic.
	 */
	private final LocationGraphic graphic;
	
	/**
	 * The location.
	 */
	private final Location location;
	
	/**
	 * The amount of attacks left.
	 */
	private int count = 11;
	
	/**
	 * The delay left before the next attack.
	 */
	private int delay = 0;
	
	/**
	 * The maximum accuracy.
	 */
	private final double accuracy;
	
	/**
	 * The maximum hit.
	 */
	private final double maximum;
	
	/**
	 * Constructs a new {@code VineCallAction} {@code Object}.
	 * @param entity The entity who's summoned the vine.
	 * @param location The location to hit on.
	 * @param handler The type handler.
	 */
	public VineCallAction(Entity entity, Location location, TypeHandler handler) {
		super(entity);
		addFlag(FLAG);
		this.location = location;
		this.graphic = new LocationGraphic(478, location);
		this.accuracy = handler.getAccuracy(entity);
		this.maximum = handler.getMaximum(entity);
	}

	@Override
	public boolean execute() {
		if (delay-- > 0) {
			return false;
		}
		PacketSender.sendGraphics(graphic);
		int victims = entity.getAttribute("area:multi", false) ? 3 : 1;
		for (Player p : RegionManager.getLocalPlayers(location, 1)) {
			if (p != entity && p.isAttackable(entity) && victims-- > 0) {
				int hit = GaussianGen.getDamage(null, entity, p, accuracy, maximum, CombatType.MELEE.getHandler().getDefence(p, 2));
				p.getActionManager().register(new HitAction(p, entity, Damage.getDamage(entity, p, CombatType.MELEE, hit), 0));
			}
		}
		if (victims > 0) {
			for (NPC npc : RegionManager.getLocalNPCs(location, 1)) {
				if (npc.isAttackable(entity) && victims-- > 0) {
					int hit = GaussianGen.getDamage(null, entity, npc, accuracy, maximum, CombatType.MELEE.getHandler().getDefence(npc, 2));
					npc.getActionManager().register(new HitAction(npc, entity, Damage.getDamage(entity, npc, CombatType.MELEE, hit), 1));
				} else {
					break;
				}
			}
		}
		delay = 3;
		return --count < 1;
	}

	@Override
	public int getActionType() {
		return FLAG;
	}

}